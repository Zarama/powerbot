package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.ParticularWalkingAction;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class WaypointWalkingAction extends ParticularWalkingAction
{
    public WaypointWalkingAction(Tile to, Tile from)
    {
        super(to, from);
    }

    @Override
    public void execute(ClientContext ctx)
    {
        super.execute(ctx);
    }
}
