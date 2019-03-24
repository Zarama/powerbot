package DieselSkrt.DRuneRunner.tasks;

import DieselSkrt.DRuneRunner.DRuneRunner;
import DieselSkrt.DRuneRunner.Task;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

/**
 * Created by Shane on 15-1-2018.
 */
public class EnterRuins extends Task<ClientContext> {

    public EnterRuins(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.objects.select().id(RUINSID).isEmpty() && !ctx.inventory.select().id(ESSENCE).isEmpty();
    }

    @Override
    public void execute() {
        DRuneRunner.STATUS = "Entering ruins";
        GameObject altar = ctx.objects.select().id(RUINSID).poll();


        if(altar.inViewport()){
            altar.interact("Enter");
        }else{
            ctx.movement.step(altar);
            ctx.camera.turnTo(altar);
        }
    }
}
