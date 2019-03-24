package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.Constants;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.WalkToCookingRegion;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class InCookingRegion extends BranchTask
{
    private IsCooking isCookingBranch = new IsCooking(ctx);
    private WalkToCookingRegion walkToCookingRegionLeaf = new WalkToCookingRegion(ctx);

    public InCookingRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.players.local().tile().distanceTo(Constants.FIRE_TILE) < 3;
    }

    @Override
    public TreeTask successTask() {
        return isCookingBranch;
    }

    @Override
    public TreeTask failureTask() {
        return walkToCookingRegionLeaf;
    }
}
