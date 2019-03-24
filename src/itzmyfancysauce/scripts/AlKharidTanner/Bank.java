package itzmyfancysauce;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Bank extends Task {
    private Npc banker;
    private int[] bankerIDs = new int[]{396, 397};
    int tanningHide = AlKharidTanner.hideID;
    int tanningLeather = AlKharidTanner.leatherID;
    private Area bankArea = new Area(new Tile(3272, 3170), new Tile(3269, 3163));

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate(String storageDirectory) {
        Tile playerTile = ctx.players.local().tile();

        //If player is within bank area
        if(bankArea.getCentralTile().distanceTo(playerTile) <= 10 && (!ctx.inventory.select().id(tanningLeather).isEmpty() || ctx.inventory.count() != 28)) {
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        System.out.println("Banking!");
        getBanker();

        if(!banker.inViewport()) {
            ctx.camera.turnTo(banker);
        }

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.open();
            }
        });

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.opened();
            }
        });

        //If no hide in bank
        if(ctx.bank.select().id(tanningHide).count() <= 0) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.depositInventory();
                }
            });

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.close();
                }
            });
            ctx.controller.stop();
        }

        //Check if invalid items are in inventory
        System.out.println("Count: " + ctx.inventory.id(tanningLeather).count());
        if(ctx.inventory.id(tanningLeather).count() != 27 || ctx.inventory.select().id(995).count(true) <= 0) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.depositInventory();
                }
            });

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.withdraw(995, 0);
                }
            });
        }

        //Check if leather is in inventory
        if(ctx.inventory.select().id(tanningLeather).count() > 0) {
            System.out.println("Deposit!");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.deposit(tanningLeather, org.powerbot.script.rt4.Bank.Amount.ALL);
                }
            });
        }

        //Withdraw hide
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.withdraw(tanningHide, 27);
            }
        });
    }

    public void getBanker() {
        banker = ctx.npcs.select().id(bankerIDs).nearest().poll();
    }
}
