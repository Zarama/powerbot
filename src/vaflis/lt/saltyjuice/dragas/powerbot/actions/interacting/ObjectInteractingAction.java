package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public abstract class ObjectInteractingAction extends InteractingAction<GameObject>
{
    protected abstract int getObjectID();

    protected abstract int getSearchRadius();

    @Override
    protected GameObject getObject(ClientContext ctx)
    {
        return ctx.objects.select(getSearchRadius()).id(getObjectID()).poll();
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
