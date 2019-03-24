package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class LeaveHouse extends Task {

    public LeaveHouse(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final GameObject portal = ctx.objects.select().id(4525).nearest().poll();
        final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();
        return ctx.inventory.select().id(SGAltar.data.getBone_ID()).isEmpty() && !ctx.players.local().inMotion() && (altar.inViewport() || portal.inViewport()) && ctx.client().getFloor() != 0;
    }

    @Override
    public void execute() {
        SGAltar.status="Leaving House";
        final GameObject portal = ctx.objects.select().id(4525).nearest().poll();
        if (portal.inViewport()){
            if (portal.interact("Enter", "Portal")) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().inMotion() && ctx.players.local().animation() != -1;
                    }
                }, 500, 2);
                SGAltar.failSave = false;
            }
        } else {
            ctx.camera.turnTo(portal);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return portal.inViewport();
                }
            }, 500, 3);
            if (!portal.inViewport()) {
                ctx.movement.step(portal);
                SGAltar.status = "Stepping to Portal";
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().inMotion() && portal.inViewport();
                    }
                }, 500, 3);
            }
        }
    }
}
