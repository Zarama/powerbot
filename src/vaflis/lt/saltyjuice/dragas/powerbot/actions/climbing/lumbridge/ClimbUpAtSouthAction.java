package vaflis.lt.saltyjuice.dragas.powerbot.actions.climbing.lumbridge;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.climbing.ClimbStairsAction;
import org.powerbot.script.rt4.GameObject;

public class ClimbUpAtSouthAction extends ClimbStairsAction
{

    @Override
    protected void unclimb(GameObject stairs)
    {
        climbDown(stairs);
    }

    @Override
    protected int getReturningStairsId()
    {
        return Constant.Objects.Stairs.LUMBRIDGE_THIRD_FLOOR_SOUTH;
    }

    @Override
    protected void climb(GameObject stairs)
    {
        climbUp(stairs);
    }

    @Override
    protected int getRequiredFloor()
    {
        return 1;
    }

    @Override
    protected int getStairId()
    {
        return Constant.Objects.Stairs.LUMBRIDGE_SECOND_FLOOR_SOUTH;
    }

    @Override
    protected int getTargetFloor()
    {
        return 2;
    }
}
