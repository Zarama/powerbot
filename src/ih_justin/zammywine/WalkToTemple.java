package ih_justin.zammywine;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;

public class WalkToTemple extends Task<ClientContext> {

    private TilePath templePath;
    private final Tile finalTile = new Tile(2931, 3515);
    private int lawRune = 563;

    public WalkToTemple(ClientContext ctx, TilePath templePath) {
        super(ctx);
        this.templePath = templePath;
    }

    @Override
    public boolean activate() {
        return !ctx.inventory.select().id(lawRune).isEmpty() &&
                finalTile.distanceTo(ctx.players.local()) >= 1;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Walking to temple");

        if (finalTile.distanceTo(ctx.players.local()) > 5) {
            this.templePath.traverse();
        } else {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    return ctx.movement.step(finalTile);
                }
            }, 1000, 1);

        }
    }
}
