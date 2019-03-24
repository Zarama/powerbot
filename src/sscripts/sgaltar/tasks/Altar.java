package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Item;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.data.Data;

import java.util.concurrent.Callable;

public class Altar extends Task {

    private Data data;

    public Altar(ClientContext ctx, Data data) {
        super(ctx);
        this.data = data;
    }


    @Override
    public boolean activate() {
        final GameObject portalIn = ctx.objects.select().id(4525).nearest().poll();
        final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();
        return (portalIn.inViewport() || altar.inViewport() || ctx.client().getFloor() == 1) && !ctx.inventory.select().id(data.getBone_ID()).isEmpty() && (ctx.movement.energyLevel() > 50 || !SGAltar.useFountain);
    }

    @Override
    public void execute() {
        SGAltar.status = "Interacting with Altar";
        final Item b = ctx.inventory.select().id(data.getBone_ID()).shuffle().poll();
        final int[] bounds = {-44, 20, -100, 4, -112, 112};
        final GameObject altar = ctx.objects.select().name("Altar").each(Interactive.doSetBounds(bounds)).nearest().poll();
        if (SGAltar.yanille) {
            if (altar.inViewport()) {
                if (!ctx.players.local().inMotion() && ctx.players.local().animation() == -1 || SGAltar.failSave) {
                    if (b.interact("Use")) {
                        if (altar.interact("Use")) {
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return ctx.inventory.select().isEmpty() || ctx.widgets.component(233, 2).visible() || SGAltar.failSave;
                                }
                            }, 2250, 28);
                        }
                    }
                }
            } else {
                SGAltar.status = "Looking for Altar";
                ctx.camera.turnTo(altar);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return altar.inViewport();
                    }
                }, 500, 3);

                ctx.movement.step(altar);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return altar.inViewport();
                    }
                }, 500, 3);
            }
        } else if (SGAltar.rimm){
            if (altar.inViewport()) {

                if (!ctx.players.local().inMotion() && ctx.players.local().animation() == -1 || SGAltar.failSave) {
                    if (b.interact("Use")) {
                        if (altar.interact("Use")) {
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return ctx.inventory.select().count() == 2 || ctx.widgets.component(233, 2).visible() || SGAltar.failSave;
                                }
                            }, 2250, 28);
                        }
                    }
                }
            } else {
                SGAltar.status = "Looking for Altar";
                ctx.camera.turnTo(altar);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return altar.inViewport();
                    }
                }, 500, 3);

                ctx.movement.step(altar);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return altar.inViewport();
                    }
                }, 500, 3);
            }
        }
    }
}
