package vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.lumbridge;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.WalkingAction;
import org.powerbot.script.rt4.ClientContext;

public class GotoSouthStairsAction extends WalkingAction
{
    private int targetX = 3206;
    private int targetY = 3210;
    private int originalX = 3206;
    private int originalY = 3216;
    private int uses = 0;

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return uses != 2 && super.isUsable(ctx);
    }

    @Override
    public void execute(ClientContext ctx)
    {
        uses++;
        super.execute(ctx);
    }

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
        boolean isFinished = ctx.objects.select(3).id(Constant.Objects.Stairs.LUMBRIDGE_THIRD_FLOOR_SOUTH, Constant.Objects.Stairs.LUMBRIDGE_SECOND_FLOOR_SOUTH).poll().valid();
        if(isFinished)
            uses = 0;
        return isFinished;
    }
}
