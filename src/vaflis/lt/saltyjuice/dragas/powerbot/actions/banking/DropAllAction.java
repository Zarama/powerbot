package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class DropAllAction implements Action
{
    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return ctx.inventory.select().count() != 0;
    }

    @Override
    public void execute(ClientContext ctx)
    {
        ctx.inventory.select().forEach(this::dropItem);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.inventory.select().count() == 0;
    }

    @Override
    public void undo(ClientContext ctx)
    {

    }

    private void dropItem(Item it)
    {
        it.interact("Drop");
    }
}
