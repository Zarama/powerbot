package sheel.OSRSTreeBot;

import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;

public abstract class TreeBot extends PollingScript<ClientContext>
{

    public abstract TreeTask createNewRoot();

    @Override
    public void poll() {
        createNewRoot().execute();
    }

}
