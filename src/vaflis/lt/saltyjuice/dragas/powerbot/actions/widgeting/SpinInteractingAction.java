package vaflis.lt.saltyjuice.dragas.powerbot.actions.widgeting;

import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Widget;

public abstract class SpinInteractingAction extends ChoiceInteractingAction
{
    @Override
    protected void interact(Widget w, Component c)
    {
        c.interact("Spin");
    }
}
