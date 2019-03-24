package sheel.scripts.RS3.BarbarianFisherAndCooker.leaves;

import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import sheel.RS3TreeBot.LeafTask;
import org.powerbot.script.rt6.ClientContext;

public class OpenBank extends LeafTask
{

    public OpenBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        Locatable nearestBank = ctx.bank.nearest();

        if(!ctx.bank.inViewport())
            ctx.camera.turnTo(nearestBank);

        Condition.wait(() -> ctx.bank.inViewport(), 250, 10);

        ctx.bank.open();
    }
}
