package vaflis.lt.saltyjuice.dragas.powerbot.actions.widgeting;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Widget;

public abstract class WidgetInteractingAction implements Action
{
    protected abstract int getWidgetIndex();

    protected abstract int getComponentIndex();

    protected  abstract void interact(Widget w, Component c);

    protected Widget getWidget(ClientContext ctx)
    {
        return ctx.widgets.select().id(getWidgetIndex()).poll();
    }

    protected Component getComponent(Widget ctx)
    {
        return ctx.component(getComponentIndex());
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        Widget w = getWidget(ctx);
        Component c = getComponent(w);
        return w.valid() && c.valid();
    }

    @Override
    public void execute(ClientContext ctx)
    {
        Widget w = getWidget(ctx);
        Component c = getComponent(w);
        interact(w, c);
    }

    @Override
    public void undo(ClientContext ctx)
    {

    }
}
