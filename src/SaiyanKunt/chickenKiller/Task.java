package SaiyanKunt.chickenKiller;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

/**
 * Created by SaiyanStables on 3/27/2017.
 */
public abstract class Task<C extends ClientContext> extends ClientAccessor<C>
{
    public Task(C ctx)
    {
        super(ctx);
    }
    protected boolean failed = false;
    public abstract boolean activate();

    public abstract void execute();

    public boolean failed()
    {
        return failed;
    }

    public abstract String getName();


}
