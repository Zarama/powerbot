package SaiyanKunt.chickenKiller;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;

/**
 * Created by SaiyanKunt on 3/27/2017.
 */
public class BuryBones extends GenericOSRSTask
{
    private int bonesBuried = 0;
    private final int BONES_ID = 526;
    private boolean active = true;
//
    public BuryBones(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        if(active && playerIsIdle() && ctx.inventory.select().count() == 28 && ctx.inventory.select().id(BONES_ID).count() > 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public void execute()
    {
        for(Item bones : ctx.inventory.select().id(BONES_ID))
        {
            int prayerXp = ctx.skills.experience(Constants.SKILLS_PRAYER);
            bones.interact("Bury");
            Condition.sleep(Random.nextInt(930, 1010));
            if(ctx.skills.experience(Constants.SKILLS_PRAYER) > prayerXp)
            {
                bonesBuried++;
            }
        }
    }

    @Override
    public String getName()
    {
        return "BuryBones";
    }

    public int getBonesBuried()
    {
        return  bonesBuried;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
}
