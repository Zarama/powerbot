package vaflis.lt.saltyjuice.dragas.powerbot.actions.opening;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.ObjectInteractingAction;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Arrays;

public abstract class OpeningAction extends ObjectInteractingAction
{
    @Override
    protected int getObjectID()
    {
        return getClosedDoorId();
    }

    @Override
    protected int getSearchRadius()
    {
        return 6;
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return super.isUsable(ctx) && Arrays.asList(getObject(ctx).actions()).contains("Open");
    }

    @Override
    protected void interact(GameObject obj)
    {
        obj.ctx.camera.turnTo(obj);
        //obj.click();
        obj.interact("Open");
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.objects.select(getSearchRadius()).id(getOpenDoorId()).poll().valid();
    }

    @Override
    public void undo(ClientContext ctx)
    {

    }

    protected abstract int getClosedDoorId();

    protected abstract int getOpenDoorId();
}
