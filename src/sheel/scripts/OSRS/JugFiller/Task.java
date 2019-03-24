package sheel.scripts.OSRS.JugFiller;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

public abstract class Task<C extends ClientContext> extends ClientAccessor<C>
{
    public Task(C ctx) {
        super(ctx);
    }

    public abstract boolean validate();

    public abstract void execute();
}
