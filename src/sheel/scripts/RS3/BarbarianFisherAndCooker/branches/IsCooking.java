package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.WaitUntilNotCooking;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class IsCooking extends BranchTask {

    private WaitUntilNotCooking waitUntilNotCookingLeaf = new WaitUntilNotCooking(ctx);
    private IsCookingInterfaceOpen isCookingInterfaceOpenBranch = new IsCookingInterfaceOpen(ctx);

    public IsCooking(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.productionInterface.working();
    }

    @Override
    public TreeTask successTask() {
        return waitUntilNotCookingLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return isCookingInterfaceOpenBranch;
    }
}