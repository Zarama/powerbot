package sheel.OSRSTreeBot;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

public abstract class TreeTask<C extends ClientContext> extends ClientAccessor<C> {

    public TreeTask(C ctx) {
        super(ctx);
    }

    public abstract boolean validate();

    public abstract void execute();

}
