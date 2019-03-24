package slicedtoast.SlicedSmelter;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class Smith extends Task
{
    private String barType;
    private String bankLocation;

    private int copperID = 436;
    private int tinID = 438;
    private int ironID = 440;
    private int silverID = 442;
    private int goldID = 444;
    private int coalID = 453;
    private int mithrilID = 447;
    private int adamantID = 449;
    private int runeID = 451;
    private int steelBarID = 2353;
    private int mouldID = 4;

    private int furnaceID;

    public Smith(ClientContext ctx, String bankLocation, String barType) {
        super(ctx);
        this.bankLocation = bankLocation;
        this.barType = barType;
    }

    @Override
    public boolean activate() //make sure proper shit is in the inventory
    {
        if(barType.equals("Bronze"))
        {
            return ctx.inventory.select().id(tinID).count() >= 1 && ctx.inventory.select().id(copperID).count() >= 1;
        }
        else if(barType.equals("Iron w/Ring of Forging") || barType.equals("Iron"))
        {
            return ctx.inventory.select().id(ironID).count() >= 1;
        }
        else if(barType.equals("Silver"))
        {
            return ctx.inventory.select().id(silverID).count() >= 1;
        }
        else if(barType.equals("Gold"))
        {
            return ctx.inventory.select().id(goldID).count() >= 1;
        }
        else if(barType.equals("Steel"))
        {
            return ctx.inventory.select().id(coalID).count() >= 2 && ctx.inventory.select().id(ironID).count() >= 1;
        }
        else if(barType.equals("Mithril"))
        {
            return ctx.inventory.select().id(coalID).count() >= 4 && ctx.inventory.select().id(mithrilID).count() >= 1;
        }
        else if(barType.equals("Adamant"))
        {
            return ctx.inventory.select().id(coalID).count() >= 6 && ctx.inventory.select().id(adamantID).count() >= 1;
        }
        else if(barType.equals("Rune"))
        {
            return ctx.inventory.select().id(coalID).count() >= 7 && ctx.inventory.select().id(runeID).count() >= 1;
        }
        else
        {
            return ctx.inventory.select().id(steelBarID).count() >= 1 && ctx.inventory.select().id(mouldID).count() == 1;
        }
    }

    @Override
    public void execute() {
        checkRun();
        if (bankLocation.equals("Edgeville")) {
            furnaceID = 16469;
            final GameObject furnace = ctx.objects.select().id(furnaceID).nearest().poll(); //edgeville furnace
            ctx.camera.turnTo(furnace); //turn towards it
            ctx.movement.step(furnace);
        } else {
            furnaceID = 24009;
            final GameObject furnace = ctx.objects.select().id(furnaceID).nearest().poll(); //al kharid furnace
            ctx.camera.turnTo(furnace); //turn towards it
            ctx.movement.step(furnace);
        }
        final GameObject furnace = ctx.objects.select().id(furnaceID).nearest().poll();
        Condition.wait(new Callable<Boolean>(){ // wait until 40 seconds up or full inventory of bars
            @Override
            public Boolean call() throws Exception //when out of all ore or the level up widget pops up
            {
                return furnace.inViewport();
            }
        }, 500, 16); //wait till furnace is in view
        if(barType.equals("Cannonballs"))
        {
            Item steelBar = ctx.inventory.select().id(steelBarID).poll();
            steelBar.interact("Use");
            furnace.interact(false, "Use", "Furnace");
        }
        else
        {
            furnace.interact("Smelt"); //interact with it
        }
        while (!ctx.widgets.widget(270).component(0).visible()) {
            sleep(1000); //give a bit of time to start out
            if (!ctx.players.local().inMotion() && !ctx.widgets.widget(270).component(0).visible()) //if the player hasn't started moving, try again until you start moving
            {
                ctx.camera.turnTo(furnace);
                ctx.movement.step(furnace);
                if(barType.equals("Cannonballs"))
                {
                    Item steelBar = ctx.inventory.select().id(steelBarID).poll();
                    steelBar.interact("Use");
                    furnace.interact(false, "Use", "Furnace");
                }
                else
                {
                    furnace.interact("Smelt"); //interact with it
                }
            }
        }
        smelt(barType, bankLocation);
        Condition.wait(new Callable<Boolean>(){ // wait until 40 seconds up or full inventory of bars
            @Override
            public Boolean call() throws Exception //when out of all ore or the level up widget pops up
            {
                    return (ctx.inventory.select().id(copperID).isEmpty()
                            && ctx.inventory.select().id(tinID).isEmpty()
                            && ctx.inventory.select().id(ironID).isEmpty()
                            && ctx.inventory.select().id(silverID).isEmpty()
                            && ctx.inventory.select().id(goldID).isEmpty()
                            && ctx.inventory.select().id(coalID).isEmpty()
                            && ctx.inventory.select().id(mithrilID).isEmpty()
                            && ctx.inventory.select().id(adamantID).isEmpty()
                            && ctx.inventory.select().id(runeID).isEmpty())
                            || ctx.widgets.widget(233).component(0).visible()
                            ||(barType.equals("Cannonballs") && !ctx.inventory.select().id(steelBarID).isEmpty());
            }
        }, 250, 400); //wait till out of ore or level-up pops up
        if (ctx.widgets.widget(233).component(0).visible()  //if it's the new level popup AND you haven't already depleted all your ore, then repeat again
                && !ctx.inventory.select().id(copperID).isEmpty()
                && !ctx.inventory.select().id(tinID).isEmpty()
                && !ctx.inventory.select().id(ironID).isEmpty()
                && !ctx.inventory.select().id(silverID).isEmpty()
                && !ctx.inventory.select().id(goldID).isEmpty()
                && !ctx.inventory.select().id(coalID).isEmpty()
                && !ctx.inventory.select().id(mithrilID).isEmpty()
                && !ctx.inventory.select().id(adamantID).isEmpty()
                && !ctx.inventory.select().id(runeID).isEmpty()
                ||(barType.equals("Cannonballs") && !ctx.inventory.select().id(steelBarID).isEmpty()))
        {
            if(barType.equals("Cannonballs"))
            {
                Item steelBar = ctx.inventory.select().id(steelBarID).poll();
                steelBar.interact("Use");
                furnace.interact(false, "Use", "Furnace");
            }
            else
            {
                furnace.interact("Smelt"); //interact with it
            }
            Condition.wait(new Callable<Boolean>(){ // wait until 40 seconds up or full inventory of bars
                @Override
                public Boolean call() throws Exception //when you are able to click smelt
                {
                    return  ctx.widgets.widget(270).component(0).visible();
                }
            }, 250, 12);
            smelt(barType, bankLocation);
            Condition.wait(new Callable<Boolean>(){ // wait until 40 seconds up or full inventory of bars
                @Override
                public Boolean call() throws Exception //when out of all ore or the level up widget pops up
                {
                    return     ctx.inventory.select().id(copperID).isEmpty()
                            && ctx.inventory.select().id(tinID).isEmpty()
                            && ctx.inventory.select().id(ironID).isEmpty()
                            && ctx.inventory.select().id(silverID).isEmpty()
                            && ctx.inventory.select().id(goldID).isEmpty()
                            && ctx.inventory.select().id(coalID).isEmpty()
                            && ctx.inventory.select().id(mithrilID).isEmpty()
                            && ctx.inventory.select().id(adamantID).isEmpty()
                            && ctx.inventory.select().id(runeID).isEmpty()
                            || (barType.equals("Cannonballs") && !ctx.inventory.select().id(steelBarID).isEmpty());
                }
            }, 250, 400); //wait till done smelting ore
        }
    }

    private void smelt(String barType, String Area) {
        if (Area.equals("Al Kharid")) {
            if (barType.equals("Bronze")) {
                ctx.widgets.widget(270).component(14).interact("Smelt");
            } else if (barType.equals("Iron") || barType.equals("Iron w/Ring of Forging")) {
                ctx.widgets.widget(270).component(15).interact("Smelt");
            } else if (barType.equals("Silver")) {
                ctx.widgets.widget(270).component(16).interact("Smelt");
            } else if (barType.equals("Gold")) {
                ctx.widgets.widget(270).component(18).interact("Smelt");
            } else if (barType.equals("Steel")) {
                ctx.widgets.widget(270).component(17).interact("Smelt");
            } else if (barType.equals("Mithril")) {
                ctx.widgets.widget(270).component(19).interact("Smelt");
            } else if (barType.equals("Adamant")) {
                ctx.widgets.widget(270).component(20).interact("Smelt");
            } else if (barType.equals("Rune")) {
                ctx.widgets.widget(270).component(21).interact("Smelt");
            } else if (barType.equals("Cannonballs")) {
                ctx.widgets.widget(270).component(14).interact("Make Sets:");
            }
        }
            else if (Area.equals("Edgeville")) {
                if (barType.equals("Bronze")) {
                    ctx.widgets.widget(270).component(14).interact("Smelt");
                } else if (barType.equals("Iron") || barType.equals("Iron w/Ring of Forging")) {
                    ctx.widgets.widget(270).component(16).interact("Smelt");
                } else if (barType.equals("Silver")) {
                    ctx.widgets.widget(270).component(17).interact("Smelt");
                } else if (barType.equals("Gold")) {
                    ctx.widgets.widget(270).component(19).interact("Smelt");
                } else if (barType.equals("Steel")) {
                    ctx.widgets.widget(270).component(18).interact("Smelt");
                } else if (barType.equals("Mithril")) {
                    ctx.widgets.widget(270).component(20).interact("Smelt");
                } else if (barType.equals("Adamant")) {
                    ctx.widgets.widget(270).component(21).interact("Smelt");
                } else if (barType.equals("Rune")) {
                    ctx.widgets.widget(270).component(22).interact("Smelt");
                } else if (barType.equals("Cannonballs")) {
                    ctx.widgets.widget(270).component(14).interact("Make Sets:");
                }
            }
    }
}
