package vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.lumbridge;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.WalkingAction;

public class GotoSpinnerRoomAction extends WalkingAction
{
    private int targetX = 3207;
    private int targetY = 3214;
    private int originalX = 3206;
    private int originalY = 3210;

    @Override
    protected int getTargetX()
    {
        return targetX;
    }

    @Override
    protected int getTargetY()
    {
        return targetY;
    }

    @Override
    protected int getOriginalX()
    {
        return originalX;
    }

    @Override
    protected int getOriginalY()
    {
        return originalY;
    }
}
