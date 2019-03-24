package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.Constants;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.WalkToFishingRegion;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;


public class InFishingRegion extends BranchTask
{
    private IsFishing isFishingBranch = new IsFishing(ctx);
    private WalkToFishingRegion walkToFishingRegionLeaf = new WalkToFishingRegion(ctx);


    public InFishingRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        Player player = ctx.players.local();
        return player.tile().distanceTo(Constants.FISHING_TILE) < 10;
    }

    @Override
    public TreeTask successTask() {
        return isFishingBranch;
    }

    @Override
    public TreeTask failureTask() {
        return walkToFishingRegionLeaf;
    }
}
