package vaflis.lt.saltyjuice.dragas.powerbot.actions.widgeting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;

public abstract class ChoiceInteractingAction extends WidgetInteractingAction
{
    @Override
    protected int getWidgetIndex()
    {
        return Constant.Widget.CHOICES;
    }
}
