package SaiyanKunt.chickenKiller;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

import java.util.concurrent.Callable;

/**
 * Created by SaiyanKunt on 3/27/2017.
 */
public class PickupLoot extends GenericOSRSTask
{
    private LootState state;
    private final int BONES_ID = 526;
    private final int FEATHER_ID = 314;
    private int featherCount = 0;
    private boolean bonesActive = true;
    private boolean featherActive = true;

    public PickupLoot(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        if((!bonesActive && !featherActive) || !lootAround() || !playerIsIdle() || !inventoryIsSuitable())
        {
            return false;
        }
        return true;
    }

    @Override
    public void execute()
    {
        switch (state)
        {
            case FEATHERSBONES: pickup(FEATHER_ID, BONES_ID);
                break;
            case FEATHERS: pickup(FEATHER_ID);
                break;
            case BONES: pickup(BONES_ID);
                break;
            default: return;
        }
    }

    private boolean lootAround()
    {
        GroundItem feathers = ctx.groundItems.select().id(FEATHER_ID).nearest().within(6.0).peek();
        if(feathers.valid() && featherActive)
        {
            if(bonesActive && ctx.groundItems.select().id(BONES_ID).at(feathers.tile()).peek().valid())
            {
                state = LootState.FEATHERSBONES;
                return true;
            }
            state = LootState.FEATHERS;
            return true;
        }

        if(bonesActive && ctx.groundItems.select().id(BONES_ID).nearest().within(6.0).peek().valid())
        {
            state = LootState.BONES;
            return true;
        }
        state = null;
        return false;
    }


    private boolean inventoryIsSuitable()
    {
        if(ctx.inventory.select().count()<27)
        {
            return true;
        }
        if(ctx.inventory.select().count()==27)
        {
            if(state == LootState.FEATHERSBONES && ctx.inventory.select().id(FEATHER_ID).count() < 1)
            {
                state = LootState.FEATHERS;
            }
            return true;
        }
        if((ctx.inventory.select().count() > 27) && (state == LootState.FEATHERS || state == LootState.FEATHERSBONES) && (ctx.inventory.select().id(FEATHER_ID).count() > 0))
        {
            state = LootState.FEATHERS;
            return true;
        }

        return false;
    }

    private void pickup(int... itemIds)
    {
        for(int itemId: itemIds)
        {
            final GroundItem groundItem = ctx.groundItems.select().id(itemId).nearest().poll();

            if(!isReachable(groundItem))
            {
                openGate(groundItem);
                if(!isReachable(groundItem))
                {
                    continue;
                }
            }
            if(groundItem.interact("Take", groundItem.name()) & groundItem.name().equals("Feather"))
            {
                featherCount = featherCount + groundItem.stackSize();
            }
            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return !groundItem.valid();
                }
            }, 200, 25);
        }
    }

    @Override
    public String getName()
    {
        return "PickupLoot";
    }

    public int getFeatherCount()
    {
        return featherCount;
    }

    public void setActive(boolean featherActive, boolean bonesActive)
    {
        this.featherActive = featherActive;
        this.bonesActive = bonesActive;
    }
}
