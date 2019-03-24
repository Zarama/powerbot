package ih_justin.zammywine;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class WalkToBank extends Task<ClientContext> {

    private final Tile wineTile = new Tile(2930, 3515);

    private TilePath bankPath;
    private int lawRune = 563;

    public WalkToBank(ClientContext ctx, TilePath bankPath) {
        super(ctx);
        this.bankPath = bankPath;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(lawRune).poll().stackSize() == 1 ||
                ctx.inventory.select().id(lawRune).isEmpty();
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Walking to bank");

        if (wineTile.distanceTo(ctx.players.local()) < 5) {
            System.out.println("trying to teleport");
            ctx.magic.cast(Magic.Spell.FALADOR_TELEPORT);
        }

        Condition.sleep(3000);
        this.bankPath.traverse();
    }
}
