package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.Constants;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.WalkToBank;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class InLodestoneRegion extends BranchTask
{
    private WalkToBank walkToBankLeaf = new WalkToBank(ctx);

    public InLodestoneRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.players.local().tile().distanceTo(Constants.EDGEVILLE_LODESTONE_TILE) < 5;
    }

    @Override
    public TreeTask successTask() {
        return walkToBankLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return null;
    }
}
