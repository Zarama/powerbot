package slicedtoast.SlicedSmelter;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class Bank extends Task
{
    private String barType;

    private int mouldID = 4;
    private int steelBarID = 2353;
    private int copperID = 436;
    private int tinID = 438;
    private int ironID = 440;
    private int silverID = 442;
    private int goldID = 444;
    private int coalID = 453;
    private int mithrilID = 447;
    private int adamantID = 449;
    private int runeID = 451;
    private int ringOfForgingID = 2568;
    private int inventories = 1;

    public Bank(ClientContext ctx, String barType) {
        super(ctx);
        this.barType = barType;
    }


    @Override
    public boolean activate()
    { //activates when no ore is in the inventory, regardless of selection
            return ctx.inventory.select().id(copperID).isEmpty()
                && ctx.inventory.select().id(tinID).isEmpty()
                && ctx.inventory.select().id(ironID).isEmpty()
                && ctx.inventory.select().id(silverID).isEmpty()
                && ctx.inventory.select().id(goldID).isEmpty()
                && ctx.inventory.select().id(coalID).isEmpty()
                && ctx.inventory.select().id(mithrilID).isEmpty()
                && ctx.inventory.select().id(adamantID).isEmpty()
                && ctx.inventory.select().id(runeID).isEmpty();
    }

    @Override
    public void execute()
    {
        checkRun();
        final GameObject bankBooth = ctx.objects.select().id(6943).nearest().poll(); //find nearest bank booth
        ctx.camera.turnTo(bankBooth); //turn towards it
        ctx.movement.step(bankBooth); //go towards it
        Condition.wait(new Callable<Boolean>(){ // wait until 40 seconds up or full inventory of bars
            @Override
            public Boolean call() throws Exception //when out of all ore or the level up widget pops up
            {
                return bankBooth.inViewport();
            }
        }, 500, 16); //wait till bank is in view
        bankBooth.interact("Bank"); //interact with it
        sleep(1000);
        while(!ctx.bank.opened())
        {
            sleep(1000); //give a bit of time to start out
            if(!ctx.players.local().inMotion() && !ctx.bank.opened()) //if the player hasn't started moving, try again until you start moving
            {
                ctx.camera.turnTo(bankBooth);
                ctx.movement.step(bankBooth);
                bankBooth.interact("Bank");
            }
        }
        ctx.bank.depositInventory();
        if(barType.equals("Bronze"))
        {
            ctx.bank.withdraw(tinID, 14);
            ctx.bank.withdraw(copperID, 14);
        }
        else if(barType.equals("Iron"))
        {
            ctx.bank.withdraw(ironID, 28);
        }
        else if(barType.equals("Iron w/Ring of Forging"))
        {
            if(inventories == 5) //then equip a ring of forging, then bank
            {
                inventories = 1;
                ctx.bank.withdraw(ringOfForgingID, 1);
                ctx.bank.close();
                Item ring = ctx.inventory.select().id(ringOfForgingID).poll();
                ring.interact("Wear");
                ctx.bank.open();
                Condition.wait(new Callable<Boolean>() { // wait until 40 seconds up or full inventory of bars
                    @Override
                    public Boolean call() throws Exception //when out of all ore or the level up widget pops up
                    {
                        return ctx.bank.opened();
                    }
                }, 100, 30);
            }
            else
            {
                inventories++;
            }
            ctx.bank.withdraw(ironID, 28); //withdraw the iron
        }
        else if(barType.equals("Silver"))
        {
            ctx.bank.withdraw(silverID, 28);
        }
        else if(barType.equals("Gold"))
        {
            ctx.bank.withdraw(goldID, 28);
        }
        else if(barType.equals("Steel"))
        {
            ctx.bank.withdraw(coalID, 18);
            ctx.bank.withdraw(ironID, 9);
        }
        else if(barType.equals("Mithril"))
        {
            ctx.bank.withdraw(coalID, 20);
            ctx.bank.withdraw(mithrilID, 5);
        }
        else if(barType.equals("Adamant"))
        {
            ctx.bank.withdraw(adamantID, 24);
            ctx.bank.withdraw(coalID, 4);
        }
        else if(barType.equals("Rune"))
        {
            ctx.bank.withdraw(runeID, 24);
            ctx.bank.withdraw(coalID, 3);
        }
        else if(barType.equals("Cannonballs"))
        {
            ctx.bank.withdraw(mouldID, 1);
            ctx.bank.withdraw(steelBarID, 27);
        }
        ctx.bank.close();
        if(ctx.inventory.isEmpty()) //logout if inven is empty after a bank
        {
            ctx.game.tab(Game.Tab.LOGOUT);
            sleep(getRand(50, 100));
            ctx.input.click(getRand(580, 702), getRand(412, 423), true);
            ctx.controller.stop();
        }
    }


}
