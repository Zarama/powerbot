package vaflis.lt.saltyjuice.dragas.powerbot;

import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;

public abstract class AbstractPollingScript extends PollingScript<ClientContext>
{
    @Override
    public final void poll()
    {
        if(ctx.controller.isStopping())
            return;
        onPoll();
    }

    abstract void onPoll();
}
