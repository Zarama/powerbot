package Trikkstr.scripts.tasks;

import Trikkstr.scripts.goblin_killer.Constants;
import Trikkstr.scripts.goblin_killer.GoblinKiller;
import Trikkstr.scripts.goblin_killer.Task;


import org.powerbot.script.Tile;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;


import java.util.concurrent.Callable;

public class Fight extends Task
{
    private final Walker walker = new Walker(ctx);

    //inventoryOpen VS inventoryClosed
    private final Component inventory = ctx.widgets.widget(161).component(61);
    private final Component unselectedInventory = ctx.widgets.widget(161).component(54);

    private Npc goblin;
    private Tile targetTile;

    private int startingHealth;
    private int currentHealth;
    private int startAmountInventory;

    public Fight(ClientContext ctx) 
    {
        super(ctx);
    }

    @Override
    public boolean activate() 
    {
        return true;
    }

    @Override
    public void execute()
    {
        GoblinKiller.setStatus("Fighting");
        System.out.printf("Executing Fight.\n");

        if (GoblinKiller.getBanked())
        {
            GoblinKiller.setBanked(false);
        }

        if (ctx.players.local().tile().x() > 3267)
        {
            walkToGoblins();
        }
        else
        {
            if(!ctx.players.local().inCombat())
            {
                if(targetTile != null)
                {
                    lootItemPile(targetTile);
                }

                lootCoins();

                handleBones();
            }

            if (hasFood() || ctx.combat.health() >= 10)
            {
                if (needsHeal())
                {
                    heal();
                }
                else if (shouldAttack())
                {
                    attack();
                }

                if(goblin != null && goblin.valid())
                {
                    targetTile = goblin.tile();
                }
            }
        }
    }

    private boolean needsHeal() 
    {
        return ctx.combat.health() < 6;
    }

    private boolean shouldAttack() 
    {
        return !ctx.players.local().inCombat();
    }

    private boolean hasFood() 
    {
        return ctx.inventory.select().id(Constants.FOOD).count() > 0;
    }

    private void lootItemPile(Tile targetTile)
    {
        GoblinKiller.setSubstatus("Looting");

        if(GoblinKiller.getBones())
        {
            while(ctx.groundItems.select().at(targetTile).id(Constants.BONES).poll().valid() && ctx.inventory.count() < 28)
            {
                System.out.printf("Item pile contains bones.\n");

                final GroundItem item = ctx.groundItems.select().at(targetTile).id(Constants.BONES).poll();

                item.interact(false, "Take", item.name());

                Condition.wait(new Callable<Boolean>() 
                {
                    @Override
                    public Boolean call() throws Exception
                    {
                        return !item.valid();
                    }
                }, 250, 8);
            }
        }

        while(ctx.groundItems.select().id(Constants.DROPS).at(targetTile).poll().valid() && ctx.inventory.count() < 28)
        {
            System.out.printf("Item pile contains loot.\n");

            final GroundItem item = ctx.groundItems.select().id(Constants.DROPS).at(targetTile).poll();

            item.interact(false, "Take", item.name());

            Condition.wait(new Callable<Boolean>() 
            {
                @Override
                public Boolean call() throws Exception
                {
                    return !item.valid();
                }
            }, 250, 8);
        }

        targetTile = null;
    }

    private void lootCoins()
    {
        final GroundItem coins = ctx.groundItems.select().id(Constants.COINS).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem groundItem)
            {
                if(ctx.inventory.select().id(Constants.FOOD).count() > 1)
                {
                    return groundItem.tile().distanceTo(ctx.players.local().tile()) < 4;
                }
                else
                {
                    return groundItem.tile().distanceTo(ctx.players.local().tile()) < 30;
                }

            }
        }).select(new Filter<GroundItem>() 
        {
            @Override
            public boolean accept(GroundItem groundItem) 
            {
                return !Constants.ABANDONED_HOUSE.contains(groundItem);
            }
        }).poll();

        if(coins.valid())
        {
            ctx.movement.step(coins);

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return !ctx.players.local().inMotion();
                }
            }, 250, 40);

            coins.interact(false, "Take", coins.name());

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return coins.valid();
                }
            }, 250, 8);
        }
    }

    private void attack()
    {
        GoblinKiller.setSubstatus("Attacking");
        System.out.printf("Selecting A Goblin To Attack\n");

        goblin = exclusiveLocateNPC(Constants.GOBLIN, Constants.ABANDONED_HOUSE);

        if (!goblin.inViewport())
        {
            ctx.camera.turnTo(goblin);
            ctx.movement.step(goblin);
        }

        goblin.interact(true, "Attack", "Goblin");

        Condition.wait(new Callable<Boolean>() 
        {
            @Override
            public Boolean call() throws Exception 
            {
                return goblin.inCombat();
            }
        }, 500, 10);

        Condition.wait(new Callable<Boolean>() 
        {
            @Override
            public Boolean call() throws Exception 
            {
                return ctx.players.local().inCombat();
            }
        }, 500, 10);
    }

    private void heal()
    {
        GoblinKiller.setSubstatus("Healing");

        if (unselectedInventory.textureId() == -1)
        {
            inventory.click();
        }

        Item food = ctx.inventory.select().id(Constants.FOOD).poll();

        startingHealth = ctx.combat.health();

        food.interact("Eat");

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                currentHealth = ctx.combat.health();
                return currentHealth != startingHealth;
            }
        }, 150, 20);
    }

    private void handleBones()
    {

        if (unselectedInventory.textureId() == -1)
        {
            inventory.click();
        }

        for (final Item bones : ctx.inventory.select().id(Constants.BONES))
        {
            startAmountInventory = ctx.inventory.select().count();

            if (GoblinKiller.getBones())
            {
                GoblinKiller.setSubstatus("Burying bones");

                bones.interact("Bury", "Bones");
            }
            else
            {
                GoblinKiller.setSubstatus("Dropping bones");

                bones.interact("Drop", "Bones");
            }

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return !bones.valid() && ctx.players.local().animation() == -1;
                }
            }, 150, 20);
        }
    }

    private Npc exclusiveLocateNPC(int ids[], final Area area)
    {
        return ctx.npcs.select().id(ids).select(new Filter<Npc>()
        {
            @Override
            public boolean accept(Npc npc)
            {
                return(!area.contains(npc.tile()) && !npc.inCombat());
            }
        }).nearest().poll();
    }

    private void walkToGoblins()
    {
        GoblinKiller.setSubstatus("Walking to goblins");

        walker.walkPath(Constants.AK_BANK_TO_GOBLINS);

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return ctx.players.local().inCombat();
            }
        }, Random.nextInt(850, 1420), 1);

        //if close to the gate and not already on the other side, then pay the toll
        if(ctx.objects.select().id(Constants.AL_KHARID_GATE).poll().tile().distanceTo(ctx.players.local()) < 8
                && ctx.players.local().tile().x() > Constants.GATE_SOUTH_SIDE)
        {
            System.out.printf("Opening Al-Kharid Gate\n");

            if (!ctx.objects.select().id(Constants.AL_KHARID_GATE).poll().inViewport())
            {
                ctx.camera.turnTo(ctx.objects.select().id(Constants.AL_KHARID_GATE).poll());
            }

            ctx.objects.select().id(Constants.AL_KHARID_GATE).poll().interact("Pay-toll(10gp)", "Gate");

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return ctx.players.local().tile().x() < Constants.GATE_NORTH_SIDE;
                }
            }, 500, 6);
        }
    }
}
