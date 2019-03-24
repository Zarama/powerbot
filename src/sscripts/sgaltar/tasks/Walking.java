package sscripts.sgaltar.tasks;


import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import sscripts.sgaltar.SGAltar;

public class Walking extends Task {

    public Walking(ClientContext ctx) {
        super(ctx);
    }
    private static final Tile[] path = {new Tile(2545, 3096, 0), new Tile(2548, 3093, 0), new Tile(2552, 3093, 0), new Tile(2556, 3092, 0), new Tile(2560, 3090, 0), new Tile(2564, 3090, 0), new Tile(2568, 3090, 0), new Tile(2572, 3090, 0), new Tile(2576, 3090, 0), new Tile(2580, 3093, 0), new Tile(2583, 3096, 0), new Tile(2587, 3097, 0), new Tile(2591, 3097, 0), new Tile(2595, 3097, 0), new Tile(2599, 3097, 0), new Tile(2603, 3097, 0), new Tile(2606, 3093, 0), new Tile(2610, 3093, 0)};
    private static final Tile bank = new Tile(2613, 3092,0);
    @Override
    public boolean activate() {
        return ctx.inventory.select().isEmpty() && !ctx.bank.inViewport() && ctx.players.local().tile().distanceTo(bank) > 4;
    }

    @Override
    public void execute() {
        SGAltar.status="Walking to Bank";
        ctx.movement.newTilePath(path).randomize(0,2).traverse();

    }
}
