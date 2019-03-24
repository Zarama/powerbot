package vaflis.lt.saltyjuice.dragas.powerbot.actions.walking;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.camera.CameraTurningAction;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class ParticularWalkingAction extends WalkingAction
{
    private final Tile from;
    private final Tile to;
    private final CameraTurningAction camera;
    public ParticularWalkingAction(Tile to, Tile from)
    {
        this.to = to;
        this.from = from;
        this.camera = new CameraTurningAction();
    }

    @Override
    protected int getTargetX()
    {
        return to.x();
    }

    @Override
    protected int getTargetY()
    {
        return to.y();
    }

    @Override
    protected int getOriginalX()
    {
        return 0;
    }

    @Override
    protected int getOriginalY()
    {
        return 0;
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        return ctx.players.local().tile().distanceTo(to) <= 5;
    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        return ctx.players.local().tile().distanceTo(this.from) <= 50;
    }

    @Override
    public void execute(ClientContext ctx)
    {
        super.execute(ctx);
        camera.execute(ctx);
    }
}
