package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.Constants;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.WalkToBank;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class InBankRegion extends BranchTask
{
    private WalkToBank walkToBankLeaf = new WalkToBank(ctx);
    private IsBankOpen isBankOpenBranch = new IsBankOpen(ctx);

    public InBankRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.players.local().tile().distanceTo(Constants.BANK_TILE) < 5;
    }

    @Override
    public TreeTask successTask() {
        return isBankOpenBranch;
    }

    @Override
    public TreeTask failureTask() {
        return walkToBankLeaf;
    }
}
