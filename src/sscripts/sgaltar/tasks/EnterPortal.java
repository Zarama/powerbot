package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class EnterPortal extends Task {
    public EnterPortal(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final GameObject portal = ctx.objects.select().id(30172).poll();
        final GameObject portalrim = ctx.objects.select().id(15478).poll();
        return  ctx.inventory.select().count() == 28
                && (portalrim.tile().distanceTo(ctx.players.local()) < 3 || portal.tile().distanceTo(ctx.players.local()) <3 || portal.inViewport() || portalrim.inViewport())
                && !ctx.widgets.component(162, 33).visible();
    }

    @Override
    public void execute() {
        SGAltar.status = "Entering House";
        final GameObject portal = ctx.objects.select().name("Portal").nearest().poll();
            if (portal.interact("Friend's house", "Portal")){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !portal.inViewport() && ctx.widgets.component(162, 33).visible();
                    }
                }, 750, 5);
            }
        }
    }


