package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import org.powerbot.script.rt4.ClientContext;

public class TreeChoppingAction extends ParticularObjectInteractingAction
{
    public TreeChoppingAction(int id)
    {
        super(id, "Chop down");
    }

    @Override
    protected int getSearchRadius()
    {
        return 3;
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return !getObject(ctx).valid() || (ctx.players.local().animation() == -1 && ctx.inventory.select().id(Constant.Item.YEW_LOGS).count() != 0);
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return super.isUsable(ctx) || ctx.players.local().animation() == -1;
    }
}
