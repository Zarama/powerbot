package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import org.powerbot.script.Actionable;
import org.powerbot.script.rt4.ClientContext;

public abstract class InteractingAction<T extends Actionable> implements Action
{
    @Override
    public void execute(ClientContext ctx)
    {
        T obj = getObject(ctx);
        interact(obj);
    }

    protected abstract T getObject(ClientContext ctx);

    protected abstract void interact(T obj);
}
