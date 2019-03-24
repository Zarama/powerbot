package vaflis.lt.saltyjuice.dragas.powerbot.actions;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Magic;

public abstract class SpellcastingAction implements Action
{
    private final Magic.MagicSpell spell;

    public SpellcastingAction(Magic.MagicSpell spell)
    {
        this.spell = spell;
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return true;
    }

    @Override
    public void execute(ClientContext ctx)
    {
        ctx.magic.cast(this.spell);
        Condition.sleep(2000);
    }

    @Override
    public void undo(ClientContext ctx)
    {

    }
}
