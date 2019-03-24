package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class PortalChat extends Task {
    public PortalChat(ClientContext ctx) {
        super(ctx);
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
        final GameObject portal = ctx.objects.select().id(30172).nearest().poll();
        final GameObject portalrim = ctx.objects.select().id(15478).nearest().poll();
        return ctx.widgets.widget(162).component(36).visible() && ctx.client().getFloor() == 0 && ctx.inventory.select().count() == 28 &&  (portal.inViewport() || portalrim.inViewport()); //!inHouse() &&
    }

    @Override
    public void execute() {
        SGAltar.status = "?????";

        if (SGAltar.stop){
            ctx.controller.stop();
            System.out.println("Stop - Host offline - restart and enter new host name");
        }
        SGAltar.status = "Selecting hostname";
        if(ctx.widgets.widget(162).component( 33).component(0).visible() && SGAltar.nameEntered){
            ctx.widgets.widget(162).component(33).component(0).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.widget(162).component( 33).component(0).visible();
                }
            }, 500, 2);
        }
        else {
            if (!SGAltar.nameEntered) {
                SGAltar.status = "Input hostname";

                if (ctx.widgets.component(162, 33).visible() && !ctx.widgets.widget(162).component(33).component(0).toString().contains(SGAltar.playername)) {
                    if (ctx.input.sendln(SGAltar.playername)) {
                        SGAltar.nameEntered = true;
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return !ctx.widgets.component(162, 33).visible();
                            }
                        }, 500, 2);
                    }
                }
            }
        }
    }
}
