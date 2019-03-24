package sheel.RS3TreeBot;

import org.powerbot.script.rt6.ClientContext;

public abstract class BranchTask extends TreeTask<ClientContext> {


    public BranchTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public abstract boolean validate();


    public abstract TreeTask successTask();

    public abstract TreeTask failureTask();


    @Override
    public void execute()
    {
        if(validate())
        {
            successTask().execute();
        }
        else failureTask().execute();
    }
}
