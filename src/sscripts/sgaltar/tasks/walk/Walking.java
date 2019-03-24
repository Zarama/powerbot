package sscripts.sgaltar.tasks.walk;


import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.Task;

public class Walking extends Task {

    private  Tile[] path = {new Tile(2545, 3096, 0), new Tile(2548, 3093, 0), new Tile(2552, 3093, 0), new Tile(2556, 3092, 0), new Tile(2560, 3090, 0), new Tile(2564, 3090, 0), new Tile(2568, 3090, 0), new Tile(2572, 3090, 0), new Tile(2576, 3090, 0), new Tile(2580, 3093, 0), new Tile(2583, 3096, 0), new Tile(2587, 3097, 0), new Tile(2591, 3097, 0), new Tile(2595, 3097, 0), new Tile(2599, 3097, 0), new Tile(2603, 3097, 0), new Tile(2606, 3093, 0), new Tile(2612, 3093, 0)};

    private final Walker walker = new Walker(ctx);
    public Walking(ClientContext ctx) {
        super(ctx);
    }
    private static final Tile bank = new Tile(2613, 3092,0);

    @Override
    public boolean activate() {
        final GameObject portalIn = ctx.objects.select().id(4525).nearest().poll();
        final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();
        return ctx.inventory.select().isEmpty() && !ctx.bank.inViewport() && ctx.players.local().tile().distanceTo(bank) > 4 && !portalIn.inViewport() && !altar.inViewport() ;
    }

    @Override
    public void execute() {
        SGAltar.status="Walking to Bank";
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        if (ctx.movement.running()) {
            walker.walkPath(path);
        } else {
            ctx.movement.newTilePath(path).randomize(0,2).traverse();
        }
    }
}
