package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;


import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Skills;

public class FoodEatingAction extends ParticularItemUsingAction
{
    private final int restoredHealth;

    public FoodEatingAction(int id, int restoredHealth)
    {
        super(id, "Eat");
        this.restoredHealth = restoredHealth;
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        final int actualHealth = ctx.skills.level(Constants.SKILLS_HITPOINTS);
        final int realHealth = ctx.skills.realLevel(Constants.SKILLS_HITPOINTS);
        return super.isUsable(ctx) && Math.min(
                actualHealth + this.restoredHealth,
                realHealth) <= realHealth;
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        final int actualHealth = ctx.skills.level(Constants.SKILLS_HITPOINTS);
        final int realHealth = ctx.skills.realLevel(Constants.SKILLS_HITPOINTS);
        return !getObject(ctx).valid() || actualHealth == realHealth;
    }
}
