package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import org.powerbot.script.rt4.ClientContext;

public abstract class BankingAction implements Action
{
    protected abstract int getItemId();

    protected abstract int getItemCount();

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return ctx.bank.opened();
    }

    protected void withdraw(ClientContext ctx)
    {
        ctx.bank.select();
        ctx.bank.withdraw(getItemId(), getItemCount());
    }

    protected void deposit(ClientContext ctx)
    {
        ctx.inventory.select();
        ctx.bank.deposit(getItemId(), getItemCount());
    }
}
