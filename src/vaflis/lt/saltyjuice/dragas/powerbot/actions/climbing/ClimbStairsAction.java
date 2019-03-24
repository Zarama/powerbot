package vaflis.lt.saltyjuice.dragas.powerbot.actions.climbing;

import vaflis.lt.saltyjuice.dragas.powerbot.Utility;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.ObjectInteractingAction;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public abstract class ClimbStairsAction extends ObjectInteractingAction
{
    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return super.isUsable(ctx) && Utility.isAtFloor(ctx, getRequiredFloor());
    }

    @Override
    protected void interact(GameObject obj)
    {
        Utility.turnTo(obj);
        climb(obj);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return Utility.isAtFloor(ctx, getTargetFloor());
    }

    @Override
    public void undo(ClientContext ctx)
    {
        GameObject stairs = ctx.objects.id(getReturningStairsId()).poll();
        unclimb(stairs);
    }

    @Override
    protected int getObjectID()
    {
        return getStairId();
    }

    @Override
    protected int getSearchRadius()
    {
        return 4;
    }

    /**
     * Helper utility to climb up the provided stairs object.
     * @param stairs
     */
    protected void climbUp(GameObject stairs)
    {
        stairs.interact("Climb-up");
    }

    protected void climbDown(GameObject stairs)
    {
        stairs.interact("Climb-down");
    }

    protected abstract void unclimb(GameObject stairs);

    protected abstract int getReturningStairsId();

    /**
     * Climbs the provided stairs.
     * @param stairs
     */
    protected abstract void climb(GameObject stairs);

    /**
     * Returns required floor for this action to be considered valid.
     * @return
     */
    protected abstract int getRequiredFloor();

    /**
     * Returns required stair id.
     * @return
     */
    protected abstract int getStairId();

    /**
     * Returns floor, which is necessary for this action to be considered finished.
     * @return an integer of target floor
     */
    protected abstract int getTargetFloor();
}
