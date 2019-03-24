package vaflis.lt.saltyjuice.dragas.powerbot.actions.walking;

import vaflis.lt.saltyjuice.dragas.powerbot.Utility;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public abstract class WalkingAction implements Action
{
    /**
     * Returns required X coordinate for this action
     * @return an integer representing X coordinate for this action.
     */
    protected abstract int getTargetX();

    /**
     * Returns required Y coordinate for this action.
     * @return an integer representing Y coordinate for this action.
     */
    protected abstract int getTargetY();

    protected abstract int getOriginalX();

    protected abstract int getOriginalY();

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return !Utility.isAtLocation(ctx, getTargetX(), getTargetY());
    }

    @Override
    public void execute(ClientContext ctx)
    {
        move(ctx, getTargetX(), getTargetY());
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return Utility.isAtLocation(ctx, getTargetX(), getTargetY()) && Utility.isNotMoving(ctx);
    }

    @Override
    public void undo(ClientContext ctx)
    {
        move(ctx, getOriginalX(), getOriginalY());
    }

    protected void move(ClientContext ctx, int x, int y)
    {
        ctx
                .movement
                .findPath(new Tile(x, y))
                .traverse();
    }
}
