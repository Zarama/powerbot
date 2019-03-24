package vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.lumbridge;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.WalkingAction;
import org.powerbot.script.rt4.ClientContext;

public class GotoBankAction extends WalkingAction
{
    private int targetX = 3208;
    private int targetY = 3218;
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

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.objects.select(6).id(18491).poll().valid();
    }
}
