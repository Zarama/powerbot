package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.Constants;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.Fish;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.WaitUntilNotFishing;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class IsFishing extends BranchTask {

    private Fish fishLeaf = new Fish(ctx);
    private WaitUntilNotFishing waitUntilNotFishingLeaf = new WaitUntilNotFishing(ctx);


    public IsFishing(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.players.local().animation() == Constants.FISHING_ANIMATION;
    }

    @Override
    public TreeTask successTask() {
        return waitUntilNotFishingLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return fishLeaf;
    }
}
