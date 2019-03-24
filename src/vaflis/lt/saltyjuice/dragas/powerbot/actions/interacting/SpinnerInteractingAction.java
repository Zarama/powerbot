package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import vaflis.lt.saltyjuice.dragas.powerbot.Utility;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class SpinnerInteractingAction extends ObjectInteractingAction
{
    @Override
    protected int getObjectID()
    {
        return Constant.Objects.Spinner.LUMBRIDGE_SECOND_FLOOR;
    }

    @Override
    protected int getSearchRadius()
    {
        return 12;
    }

    @Override
    protected void interact(GameObject obj)
    {
        Utility.turnTo(obj);
        obj.interact("Spin");
        int delay = (int) (Math.random() * 600) + 300; //from 300 to 900 ms
        Condition.sleep(delay);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.widgets.select().id(Constant.Widget.CHOICES).poll().valid();
    }
}
