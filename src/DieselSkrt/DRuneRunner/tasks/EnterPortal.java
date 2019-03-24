package DieselSkrt.DRuneRunner.tasks;

import DieselSkrt.DRuneRunner.DRuneRunner;
import DieselSkrt.DRuneRunner.Task;
import DieselSkrt.DRuneRunner.Trade;
import DieselSkrt.DRuneRunner.Walker;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

/**
 * Created by Shane on 15-1-2018.
 */
public class EnterPortal extends Task<ClientContext> {

    public EnterPortal(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate() {
        Trade t = new Trade(ctx);
        if (!ctx.objects.select().id(PORTALID).isEmpty())
            if (ctx.inventory.select().id(ESSENCE).isEmpty()) if (!t.opened()) return true;
        return false;
    }

    @Override
    public void execute() {
        DRuneRunner.STATUS = "Entering portal";
        GameObject portalObject = ctx.objects.select().id(PORTALID).poll();

        if(portalObject.inViewport()){
            portalObject.interact("Use");
        }else{
            ctx.movement.step(portalObject);
            ctx.camera.turnTo(portalObject);
        }


    }
}
