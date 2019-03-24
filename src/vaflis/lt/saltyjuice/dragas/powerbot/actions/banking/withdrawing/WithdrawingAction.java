package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.withdrawing;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.BankingAction;
import org.powerbot.script.rt4.ClientContext;

public abstract class WithdrawingAction extends BankingAction
{
    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return super.isUsable(ctx) && ctx.bank.select().id(getItemId()).poll().stackSize() > 0;
    }

    @Override
    public void execute(ClientContext ctx)
    {
        withdraw(ctx);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.inventory.select().id(getItemId()).count() > 0;
    }

    @Override
    public void undo(ClientContext ctx)
    {
        deposit(ctx);
    }
}
