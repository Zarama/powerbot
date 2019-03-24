package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class VeryParticularObjectInteractingAction extends ParticularObjectInteractingAction
{
    private final Locatable tile;

    public VeryParticularObjectInteractingAction(int id, String action, int x, int y)
    {
        super(id, action);
        this.tile = new Tile(x, y);
    }

    @Override
    protected GameObject getObject(ClientContext ctx)
    {
        return ctx.objects.select().at(tile).id(this.getObjectID()).poll();
    }
}
