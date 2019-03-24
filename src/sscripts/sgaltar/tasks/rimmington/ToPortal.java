package sscripts.sgaltar.tasks.rimmington;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.Task;

import java.util.concurrent.Callable;

public class ToPortal extends Task {
    public ToPortal(ClientContext arg0) {
        super(arg0);
    }

    final GameObject portal = ctx.objects.select().id(4525).nearest().poll();
    final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();
    public boolean inHouse() {
        if (portal.inViewport() || altar.inViewport() || ctx.client().getFloor() == 1){
            return true;
        }else {return false;}
    }

    @Override
    public boolean activate() {
        final GameObject portal = ctx.objects.select().id(15478).nearest().poll();
        final Tile tilePortal = new Tile(2954,3223,0);

        return ctx.inventory.select().count() == 28 && ctx.client().getFloor() == 0 && !portal.inViewport() && !inHouse() && tilePortal.distanceTo(ctx.players.local()) > 3;
    }

    @Override
    public void execute() {
        SGAltar.status = "Walking to Portal";
        final GameObject portal = ctx.objects.select().id(15478).nearest().poll();
        final Tile tilePortal = new Tile(2954,3223,0);
        if (ctx.movement.energyLevel() > 50 && !ctx.movement.running()){
            ctx.movement.running(true);
        }
        if (!portal.inViewport()) {
            ctx.camera.turnTo(tilePortal);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return portal.inViewport();
                }
            }, 1000, 3);
            if (!portal.inViewport()) {
                if (ctx.movement.step(tilePortal)) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return portal.inViewport();
                        }
                    }, 1000, 3);
                }
            }
        }

    }
}
