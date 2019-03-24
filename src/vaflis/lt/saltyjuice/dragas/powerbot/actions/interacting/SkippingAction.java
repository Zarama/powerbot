package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Utility;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class SkippingAction extends UsingAction
{
    public SkippingAction(int id, String action)
    {
        super(id, action);
    }

    @Override
    protected void interact(Item obj)
    {

    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return super.isUsable(ctx) && !Utility.getLevelUpWidget(ctx).valid();
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return !getObject(ctx).valid();
    }
}
