package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import org.powerbot.script.rt4.ClientContext;

public class ParticularItemUsingAction extends UsingAction
{
    public ParticularItemUsingAction(int id, String action)
    {
        super(id, action);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.inventory.selectedItem().id() == this.id;
    }
}
