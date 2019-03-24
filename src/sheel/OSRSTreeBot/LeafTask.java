package sheel.OSRSTreeBot;


import org.powerbot.script.rt4.ClientContext;

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
