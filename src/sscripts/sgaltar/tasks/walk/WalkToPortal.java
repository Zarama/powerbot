package sscripts.sgaltar.tasks.walk;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.Task;


public class WalkToPortal extends Task {

    private final  Tile[] path = {new Tile(2545, 3096, 0), new Tile(2548, 3093, 0), new Tile(2552, 3093, 0), new Tile(2556, 3092, 0), new Tile(2560, 3090, 0), new Tile(2564, 3090, 0), new Tile(2568, 3090, 0), new Tile(2572, 3090, 0), new Tile(2576, 3090, 0), new Tile(2580, 3093, 0), new Tile(2583, 3096, 0), new Tile(2587, 3097, 0), new Tile(2591, 3097, 0), new Tile(2595, 3097, 0), new Tile(2599, 3097, 0), new Tile(2603, 3097, 0), new Tile(2606, 3093, 0), new Tile(2610, 3093, 0)};

    private final Walker walker = new Walker(ctx);

    public WalkToPortal(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final GameObject portal = ctx.objects.select().id(30172).poll();
        final GameObject portalIn = ctx.objects.select().id(4525).nearest().poll();
        final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();
        return ctx.inventory.select().count() == 28 && !portal.inViewport() && !portalIn.inViewport() && !altar.inViewport() && ctx.client().getFloor() == 0;
    }

    @Override
    public void execute() {
        SGAltar.status="Walking to Portal";
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,55)){
            ctx.movement.running(true);
        }
        if (ctx.movement.running()) {
            walker.walkPathReverse(path);
        } else {
             ctx.movement.newTilePath(path).reverse().randomize(0,2).traverse();
        }

    }
}
