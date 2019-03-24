package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.SpellcastingAction;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Magic;

public class TeleportingAction extends SpellcastingAction
{
    private final Tile destination;

    public TeleportingAction(Magic.MagicSpell spell, Tile destination)
    {
        super(spell);
        this.destination = destination;
    }

    @Override
    public void execute(ClientContext ctx)
    {
        super.execute(ctx);
        Condition.wait(() -> this.isFinished(ctx));
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.players.local().tile().distanceTo(destination) <= 5;
    }
}
