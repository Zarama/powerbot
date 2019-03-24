package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.LoadBankPreset;
import sheel.scripts.RS3.BarbarianFisherAndCooker.leaves.OpenBank;
import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class IsBankOpen extends BranchTask {

    private OpenBank openBankLeaf = new OpenBank(ctx);
    private LoadBankPreset loadBankPreset = new LoadBankPreset(ctx);

    public IsBankOpen(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.bank.opened();
    }

    @Override
    public TreeTask successTask() {
        return loadBankPreset;
    }

    @Override
    public TreeTask failureTask() {
        return openBankLeaf;
    }
}
