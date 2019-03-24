package sheel.RS3TreeBot;

import org.powerbot.script.PollingScript;
import org.powerbot.script.rt6.ClientContext;

public abstract class TreeBot extends PollingScript<ClientContext>
{

    public abstract TreeTask createNewRoot();

    @Override
    public void poll() {
        createNewRoot().execute();
    }

}
