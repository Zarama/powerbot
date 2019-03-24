package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.Cook;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.UseFire;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class IsCookingInterfaceOpen extends BranchTask
{
    private Cook cookLeaf = new Cook(ctx);
    private UseFire useFireLeaf = new UseFire(ctx);

    public IsCookingInterfaceOpen(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.productionInterface.opened();
    }

    @Override
    public TreeTask successTask() {
        return cookLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return useFireLeaf;
    }
}
