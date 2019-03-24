package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Utility;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.widgeting.ChoiceInteractingAction;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Widget;

public class ParticularChoiceInteractingAction extends ChoiceInteractingAction
{
    private final int choice;
    private final String interaction;

    public ParticularChoiceInteractingAction(int choice, String interaction)
    {
        super();
        this.choice = choice;
        this.interaction = interaction;
    }
    @Override
    protected int getComponentIndex()
    {
        return choice;
    }

    @Override
    protected void interact(Widget w, Component c)
    {
        c.interact(interaction);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return !getWidget(ctx).valid() && !Utility.getLevelUpWidget(ctx).valid();
    }
}
