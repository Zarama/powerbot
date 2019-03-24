package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class ParticularObjectInteractingAction extends ObjectInteractingAction
{
    private final String action;
    private final int id;

    public ParticularObjectInteractingAction(int id, String action)
    {
        this.id = id;
        this.action = action;
    }
    @Override
    protected int getObjectID()
    {
        return id;
    }

    @Override
    protected int getSearchRadius()
    {
        return 5;
    }

    @Override
    protected void interact(GameObject obj)
    {
        obj.interact(action);
        Condition.wait(() -> this.isFinished(obj.ctx));
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return !getObject(ctx).valid();
    }
}
