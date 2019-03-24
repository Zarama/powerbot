package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import org.powerbot.script.rt4.ClientContext;

public class ChoicesCombiningAction extends CombiningAction
{
    public ChoicesCombiningAction(int first, int second)
    {
        super(first, second);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.widgets.select().id(Constant.Widget.CHOICES).poll().valid();
    }
}
