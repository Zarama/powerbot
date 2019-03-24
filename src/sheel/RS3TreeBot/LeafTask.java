package sheel.RS3TreeBot;


import org.powerbot.script.rt6.ClientContext;

public abstract class LeafTask extends TreeTask<ClientContext> {

    public LeafTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public abstract void execute();
}
