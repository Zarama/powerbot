package SaiyanKunt.chickenKiller;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;

/**
 * Created by SaiyanKunt on 3/27/2017.
 */
public class KillChicken extends GenericOSRSTask
{
    private int[] chickenIds = {2692, 2693};
    private int chickensKilled = 0;
    private boolean active = true;

    public KillChicken(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        if(!active || !chickensAround() || !playerIsIdle())
        {
            return false;
        }
        return true;
    }

    @Override
    public void execute()
    {
        int hitpointsXp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        final Npc chicken = ctx.npcs.select().id(chickenIds).select(new Filter<Npc>()
        {
            @Override
            public boolean accept(Npc npc)
            {
                return npc.interacting().equals(ctx.players.local()) || (!npc.interacting().valid() && npc.healthPercent() > 0);
            }
        }).nearest().poll();

        if(!isReachable(chicken))
        {
            openGate(chicken);
            if(!isReachable(chicken))
            {
                failed = true;
                return;
            }
        }
        if(!chicken.inViewport())
        {
            ctx.movement.step(chicken);
            ctx.camera.turnTo(chicken);
            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return chicken.inViewport();
                }
            }, 200, 20);
        }

        chicken.interact("Attack",  "Chicken");
        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return (chicken.interacting().valid() && !chicken.interacting().equals(ctx.players.local()) || chicken.healthPercent() == 0 || playerIsIdle() || !isReachable(chicken));
            }
        }, 1000, 35);

        if(ctx.skills.experience(Constants.SKILLS_HITPOINTS) > hitpointsXp)
        {
            chickensKilled++;
            failed = false;
            return;
        }
        failed = true;
    }

    @Override
    public String getName()
    {
        return "KillChicken";
    }

    private boolean chickensAround()
    {
        if(ctx.npcs.select().id(chickenIds).select(new Filter<Npc>()
        {
            @Override
            public boolean accept(Npc npc)
            {
                return !npc.interacting().valid() && npc.healthPercent() > 0;
            }
        }).isEmpty())
        {
            return false;
        }
        return true;
    }

    public int getChickensKilled()
    {
        return chickensKilled;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isActive()
    {
        return active;
    }

}
