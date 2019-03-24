package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import org.powerbot.script.rt4.ClientContext;

public class BankOpeningAction implements Action
{
    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return ctx.bank.inViewport() && !ctx.bank.opened();
    }

    @Override
    public void execute(ClientContext ctx)
    {
        ctx.bank.open();
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.bank.opened();
    }

    @Override
    public void undo(ClientContext ctx)
    {
        ctx.bank.close();
    }
}
