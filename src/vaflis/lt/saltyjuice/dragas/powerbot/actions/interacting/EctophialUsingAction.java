package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import org.powerbot.script.rt4.ClientContext;

public class EctophialUsingAction extends UsingAction
{
    public EctophialUsingAction()
    {
        super(Constant.Item.ECTOPHIAL, "Empty");
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.objects.select(5).id(Constant.Objects.ECTOFUNTUS).poll().valid();
    }
}
