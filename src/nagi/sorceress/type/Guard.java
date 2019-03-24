package nagi.sorceress.type;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public abstract class Guard {

    public abstract int getTag();

    public abstract Vector[] getPath();

    public Npc getNpc(ClientContext ctx) {
        return ctx.npcs.select().id(this.getTag()).poll();
    }

    public Tile getTile(ClientContext ctx) {
        Npc npc = this.getNpc(ctx);
        return npc != null ? npc.tile() : null;
    }

    public Vector getVector(ClientContext ctx) {
        Npc npc = this.getNpc(ctx);
        if(npc == null)
            return null;
        
        Tile tile = npc.tile();
        return new Vector(tile.x(), tile.y(), npc.orientation());
    }
}