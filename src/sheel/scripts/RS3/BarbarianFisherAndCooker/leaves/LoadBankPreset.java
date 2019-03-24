package sheel.scripts.RS3.BarbarianFisherAndCooker.leaves;

import sheel.RS3TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

public class LoadBankPreset extends LeafTask {
    public LoadBankPreset(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        ctx.bank.presetGear2();

        Condition.wait(() -> !ctx.bank.opened(), 250, 10);
    }
}
