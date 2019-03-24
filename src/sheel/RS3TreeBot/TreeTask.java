package sheel.RS3TreeBot;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class TreeTask<C extends ClientContext> extends ClientAccessor<C> {

    public TreeTask(C ctx) {
        super(ctx);
    }

    public abstract boolean validate();

    public abstract void execute();

}
