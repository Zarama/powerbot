package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.depositing;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.BankingAction;
import org.powerbot.script.rt4.ClientContext;

public abstract class DepositingAction extends BankingAction
{
    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return super.isUsable(ctx) && ctx.inventory.select(it -> it.id() == getItemId()).count() > 0;
    }

    @Override
    public void execute(ClientContext ctx)
    {
        deposit(ctx);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.inventory.select().id(getItemId()).count() == 0;
    }

    @Override
    public void undo(ClientContext ctx)
    {
        withdraw(ctx);
    }
}
