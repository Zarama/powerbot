package sheel.scripts.RS3.BarbarianFisherAndCooker.leaves;

import org.powerbot.script.rt6.ClientContext;
import sheel.RS3TreeBot.LeafTask;

public class StopBot extends LeafTask
{
    public StopBot(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        ctx.controller.stop();
    }
}
