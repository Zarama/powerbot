package manbearpigcat.scripts.potatofarmer.tasks;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;


/**
 * Created by Shan on 2016-08-17.
 */
public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    public Task(C ctx) {
        super(ctx);
    }
    public abstract boolean activate();

    public abstract void execute();
}
