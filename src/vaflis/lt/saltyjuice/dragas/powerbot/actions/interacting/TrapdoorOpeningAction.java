package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class TrapdoorOpeningAction extends ObjectInteractingAction
{
    @Override
    protected int getObjectID()
    {
        return Constant.Objects.Trapdoor.ECTOFUNTUS_SLIME_TRAPDOOR_CLOSED;
    }

    @Override
    protected int getSearchRadius()
    {
        return 5;
    }

    @Override
    protected void interact(GameObject obj)
    {
        obj.interact("Open");
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return !getObject(ctx).valid();
    }
}
