package Elt.AlKharidFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import Elt.AlKharidFishNCook.Task;

import java.util.concurrent.Callable;

public class Banking extends Task {

    private final Tile[] rangeToBankPath = {new Tile(3272, 3180, 0), new Tile(3276, 3180, 0), new Tile(3277, 3176, 0), new Tile(3276, 3172, 0), new Tile(3274, 3168, 0), new Tile(3270, 3167, 0)};
    private boolean switchToFishing;

    public Banking(ClientContext ctx, boolean switchToFishing) {
        super(ctx);
        this.switchToFishing = switchToFishing;
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() == 28 && !hasRawFish());
    }

    @Override
    public void execute() {
        if (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 6) {
            if (ctx.bank.opened()) {
                ctx.bank.depositAllExcept(netId, rodId, baitId);

                // Check for whether we should switch from net to bait fishing,
                // and attempt to take rod and bait from bank if so.
                if (switchToFishing && ctx.skills.level(Constants.SKILLS_FISHING) >= 5) {
                    boolean needsRod = (ctx.inventory.select().id(rodId).count() == 0);
                    boolean needsBait = (ctx.inventory.select().id(baitId).count() == 0);

                    if (needsRod && needsBait) {
                        if (
                            (ctx.bank.select().id(rodId).count() > 0) &&
                            (ctx.bank.select().id(baitId).count() > 0)
                        ) {
                            ctx.bank.withdraw(rodId, 1);
                            ctx.bank.withdraw(baitId, Bank.Amount.ALL_BUT_ONE);
                            if (ctx.inventory.select().id(netId).count() == 1) {
                                ctx.bank.deposit(netId, 1);
                            }
                        }
                    }

                }
            } else {
                if (!ctx.bank.inViewport()) {
                    ctx.camera.turnTo(ctx.bank.nearest());
                }
                ctx.bank.open();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 150, 20);
            }
        } else {
            ctx.movement.newTilePath(rangeToBankPath).traverse();
        }
    }

}
