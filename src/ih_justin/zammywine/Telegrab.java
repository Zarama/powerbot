package ih_justin.zammywine;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Interactive;

import java.util.concurrent.Callable;

public class Telegrab extends Task<ClientContext> {

    private final Tile finalTile = new Tile(2931, 3515);
    private final Tile wineTile = new Tile(2930, 3515);
    private final TileMatrix wineTileMatrix = new TileMatrix(ctx, wineTile);
    private final int[] wineBounds = {-8, 8, -120, -100, -8, 8};

    private boolean telegrabReady = false;
    private GroundItem wine;
    private int wineID = 245;
    private int lawRune = 563;

    private int wineBeforeGrab = 0;
    private int lawsBeforeGrab = 0;

    public Telegrab(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return finalTile.distanceTo(ctx.players.local()) == 0 &&
                ctx.inventory.select().id(lawRune).poll().stackSize() > 1;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Telegrabbing");

        wineBeforeGrab = ctx.inventory.select().id(wineID).count();
        lawsBeforeGrab = ctx.inventory.select().id(lawRune).poll().stackSize();

        if (ctx.camera.pitch() < 99) {
            ctx.camera.pitch(true);
        }

        if (!telegrabReady) {
            ctx.magic.cast(Magic.Spell.TELEKINETIC_GRAB);
            telegrabReady = true;
        }

        wineTileMatrix.hover();
        wine = ctx.groundItems.select().id(wineID).each(Interactive.doSetBounds(wineBounds)).poll();

        if (wine.valid()) {
            telegrabReady = false;
            Info.getInstance().tryWorldHop = true;

            wine.click(Game.Crosshair.ACTION);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    if (ctx.inventory.select().id(lawRune).poll().stackSize() < lawsBeforeGrab) {
                        Info.getInstance().incrementLawRunes();
                        return true;
                    }

                    return false;
                }
            }, 1000, 3);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    if (ctx.inventory.select().id(wineID).count() > wineBeforeGrab) {
                        Info.getInstance().incrementWine();
                        return true;
                    }
                    return false;
                }
            }, 1000, 3);

        }
    }
}
