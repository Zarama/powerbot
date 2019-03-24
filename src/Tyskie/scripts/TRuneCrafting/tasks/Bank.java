package Tyskie.scripts.TRuneCrafting.tasks;

import Tyskie.scripts.TRuneCrafting.resources.Antiban;
import Tyskie.scripts.TRuneCrafting.resources.MyConstants;
import Tyskie.scripts.TRuneCrafting.resources.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 18-6-2017.
 */
public class Bank extends Task {

    private int essenceId;
    private Antiban antiban;

    public Bank(ClientContext ctx, int essenceId) {
        super(ctx);
        this.essenceId = essenceId;
        antiban = new Antiban();
    }

    @Override
    public boolean activate() {
        return (ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.select().id(MyConstants.AIR_RUNE).count() == 1
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) < 3
                && ctx.inventory.count() == MyConstants.INVENTORY_ONLY_RUNES
                && ctx.inventory.select().id(essenceId).count() == 0)
                ||
                (ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.count() == MyConstants.INVENTORY_EMPTY
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local().tile()) < 3);
    }

    @Override
    public void execute() {
        if (Random.nextDouble() > 0.75){
            antiban.doAntibanAction(Random.nextInt(1, 10));
        }
        if (ctx.bank.opened()) {
            withdrawEssence(essenceId);
        } else {
            openBank();
        }
    }

    private void openBank() {
        if (ctx.bank.inViewport()) {
            if (ctx.bank.open()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 250, 10);
            }
        } else {
            ctx.camera.turnTo(ctx.bank.nearest());
        }
    }

    private void withdrawEssence(final int essenceId){
            if (ctx.bank.currentTab() != 0) {
                ctx.bank.currentTab(0);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.currentTab(0);
                    }
                }, 250, 10);
            }
            if (ctx.bank.depositInventory()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.count() == MyConstants.INVENTORY_EMPTY;
                    }
                }, 250, 10);

                if (ctx.bank.contains(ctx.bank.select().id(essenceId).peek())) {
                    ctx.bank.withdraw(essenceId, Random.nextInt(28, 99));
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().id(essenceId).count() == MyConstants.INVENTORY_FULL;
                        }
                    }, 250, 10);
                } else {
                    ctx.controller.stop();
                }

                ctx.bank.close();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 250, 10);
            }
        }

}
