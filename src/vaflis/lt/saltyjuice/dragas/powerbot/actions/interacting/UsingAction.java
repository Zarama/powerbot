package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public abstract class UsingAction extends InteractingAction<Item>
{
    private final String action;
    protected final int id;

    public UsingAction(int id, String action)
    {
        this.id = id;
        this.action = action;
    }
    @Override
    protected Item getObject(ClientContext ctx)
    {
        return ctx.inventory.select().id(this.id).poll();
    }

    @Override
    protected void interact(Item obj)
    {
        obj.interact(this.action);
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return getObject(ctx).valid();
    }


    @Override
    public void undo(ClientContext ctx)
    {

    }
}
