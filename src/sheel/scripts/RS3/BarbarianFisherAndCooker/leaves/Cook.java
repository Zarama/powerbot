package sheel.scripts.RS3.BarbarianFisherAndCooker.leaves;

import sheel.RS3TreeBot.LeafTask;
import org.powerbot.script.rt6.ClientContext;

public class Cook extends LeafTask {
    public Cook(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        ctx.productionInterface.makeItem();
    }
}

