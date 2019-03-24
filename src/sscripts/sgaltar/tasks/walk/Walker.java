package sscripts.sgaltar.tasks.walk;


import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Player;


/*
*
*
*  All credits to Chris for the Walker! Check out his tutorials on Powerbot forum!
*
*
* */

public class Walker {

    private ClientContext ctx;

    public Walker(ClientContext ctx) {
        this.ctx = ctx;
    }
        public Tile getNextTile(Tile[] t) {
        Tile nextTile = ctx.movement.newTilePath(t).next();
        int index = 0;
        final Player p = ctx.players.local();

        for (int i = t.length - 1; i >= 0; i--) {
            if (t[i].equals(nextTile)) {
                if (i + 1 <= t.length - 1 && nextTile.distanceTo(p) < 3) {
                    index = i + 1;
                    break;
                }
                index = i;
                break;
            } else if(t[i].distanceTo(p)<8){
                index = i;
                break;
            }
        }
        return t[index];
    }

    public boolean walkPath(Tile[] t) {
        Tile ti = getNextTile(t);
        Tile nt = ti.derive(Random.nextInt(-1,1), Random.nextInt(-1,1));

        if(nt.matrix(ctx).reachable()){
            return ctx.movement.step(nt);
        }
        return ctx.movement.step(ti);
    }



    public boolean walkPathReverse(Tile[] t){
        t = ctx.movement.newTilePath(t).reverse().toArray();

        return walkPath(t);
    }

}

