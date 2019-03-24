package scripts.TSuperHeat.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import scripts.TSuperHeat.resources.MyConstants;
import scripts.TSuperHeat.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 17-6-2017.
 */
public class Bank extends Task {

    private int oreId;
    private int oreNeeded;
    private int coalNeeded;

    public Bank(ClientContext ctx, int oreId, int coalNeeded) {
        super(ctx);
        this.oreId = oreId;
        this.coalNeeded = coalNeeded;
        oreNeeded = (int) Math.floor((27 / (coalNeeded + 1)));
        this.coalNeeded = 27 - oreNeeded;
    }

    @Override
    public boolean activate() {
        return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                && ctx.inventory.select().id(oreId).count() == 0
                && ctx.inventory.select().id(MyConstants.COAL_ID).count() == 0;
    }

    @Override
    public void execute() {
        if (ctx.bank.opened()){
            if (!ctx.bank.currentTab(0)){
                ctx.bank.currentTab(0);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.currentTab(0);
                    }
                }, 250, 10);
            }
            if (ctx.bank.depositAllExcept(MyConstants.NATURE_ID)){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.count() == 1;
                    }
                }, 250, 10);

                if (ctx.bank.contains(ctx.bank.select().id(oreId).peek())) {
                    ctx.bank.withdraw(oreId, oreNeeded);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().id(oreId).count() == oreNeeded;
                        }
                    }, 250, 10);
                } else {
                    ctx.controller.stop();
                }

                if (coalNeeded >= 1) {
                    if (ctx.bank.contains(ctx.bank.select().id(MyConstants.COAL_ID).peek())) {
                        ctx.bank.withdraw(MyConstants.COAL_ID, coalNeeded);
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.inventory.select().id(MyConstants.COAL_ID).count() == coalNeeded;
                            }
                        }, 250, 10);
                    } else {
                        ctx.controller.stop();
                    }
                }

                ctx.bank.close();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 250, 10);
            }
        } else {
            if (ctx.bank.inViewport()){
                if (ctx.bank.open()){
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
    }
}
