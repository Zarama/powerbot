package sheel.scripts.RS3.BarbarianFisherAndCooker.leaves;

import sheel.RS3TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class UseFire extends LeafTask
{
    public UseFire(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {

        GameObject fire = ctx.objects.select().name("Fire").nearest().poll();
        System.out.println(fire.id());
        if(!fire.inViewport())
        {
            ctx.camera.turnTo(fire);
        }

        fire.interact("Cook-at");

        Condition.wait(() -> ctx.productionInterface.opened(), 250, 10);
    }
}
