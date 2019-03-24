package vaflis.lt.saltyjuice.dragas.powerbot.actions.opening;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;

public class TabOpeningAction implements Action
{
    private final Game.Tab tab;

    public TabOpeningAction(Game.Tab tab)
    {
        this.tab = tab;
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return true;
    }

    @Override
    public void execute(ClientContext ctx)
    {
        ctx.game.tab(this.tab, true);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.game.tab(this.tab, true);
    }

    @Override
    public void undo(ClientContext ctx)
    {

    }
}
