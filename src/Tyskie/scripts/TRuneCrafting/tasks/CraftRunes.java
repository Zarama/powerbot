package Tyskie.scripts.TRuneCrafting.tasks;

import Tyskie.scripts.TRuneCrafting.resources.Antiban;
import Tyskie.scripts.TRuneCrafting.resources.Task;
import Tyskie.scripts.TRuneCrafting.resources.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import Tyskie.scripts.TRuneCrafting.resources.MyConstants;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 18-6-2017.
 */
public class CraftRunes extends Task {

    private int essenceId, runeId, ruinsId, altarId, portalId;
    private Tile[] pathToAltar;
    private final Walker walker = new Walker(ctx);
    private Antiban antiban;

    public CraftRunes(ClientContext ctx, int essenceId, int runeId, int ruinsId, int altarId, int portalId, Tile[] pathToAltar) {
        super(ctx);
        this.essenceId = essenceId;
        this.runeId = runeId;
        this.ruinsId = ruinsId;
        this.altarId = altarId;
        this.portalId = portalId;
        this.pathToAltar = pathToAltar;
        antiban = new Antiban();
    }

    @Override
    public boolean activate() {
        return ctx.inventory.count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(essenceId).count() == MyConstants.INVENTORY_FULL
                && ctx.objects.select().id(ruinsId).nearest().poll().inViewport();
    }

    @Override
    public void execute() {
        if (Random.nextDouble() > 0.75){
            antiban.doAntibanAction(Random.nextInt(1, 10));
        }
        enterRuins();
        walkToAltar();
        craftRunes();
        walkToPortal();
        leaveRuins();
    }

    private void enterRuins(){
        final GameObject ruins = ctx.objects.select().id(ruinsId).nearest().poll();
        if (ruins.inViewport() && ruins.valid()){
            ruins.hover();
            Condition.sleep(Random.nextInt(250, 500));
            ruins.interact("Enter", "Mysterious ruins");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() == MyConstants.ANIMATION_IDLE
                            && !ruins.inViewport();
                }
            }, 250, 20);
        } else {
            ctx.camera.turnTo(ruins);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ruins.inViewport();
                }
            }, 250, 10);
        }
    }

    private void walkToAltar(){
        final GameObject altar = ctx.objects.select().id(altarId).nearest().poll();
        if (!altar.inViewport()) {
            if (!ctx.movement.running()
                    && ctx.movement.energyLevel() > Random.nextInt(20, 35)) {
                ctx.movement.running(true);
            }
            if (!ctx.players.local().inMotion()
                    || ctx.movement.destination().equals(Tile.NIL)
                    || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                if (ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                        && ctx.inventory.select().id(essenceId).count() == 28) {
                    walker.walkPath(pathToAltar);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return altar.valid() && altar.inViewport() && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
                        }
                    }, 250, 20);
                    Condition.sleep(Random.nextInt(2000, 2500));
                }
            }
        }
    }

    private void craftRunes(){
        final GameObject altar = ctx.objects.select().id(altarId).nearest().poll();
        if (altar.inViewport() && altar.valid()){
            altar.hover();
            Condition.sleep(Random.nextInt(250, 500));
            altar.interact("Craft-rune", "Altar");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.count() == MyConstants.INVENTORY_ONLY_RUNES
                            && ctx.inventory.select().id(runeId).count() == 1
                            && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
                }
            }, 250, 10);
            Condition.sleep(Random.nextInt(2000, 2500));
        } else {
            ctx.camera.turnTo(altar);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return altar.inViewport();
                }
            }, 250, 10);
        }
    }

    private void walkToPortal(){
        final GameObject portal = ctx.objects.select().id(portalId).nearest().poll();
            if (!ctx.movement.running()
                    && ctx.movement.energyLevel() > Random.nextInt(20, 35)) {
                ctx.movement.running(true);
            }
            if (!ctx.players.local().inMotion()
                    || ctx.movement.destination().equals(Tile.NIL)
                    || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                if (ctx.inventory.select().count() == MyConstants.INVENTORY_ONLY_RUNES
                        && ctx.inventory.select().id(runeId).count() == 1) {
                    if (runeId != MyConstants.BODY_RUNE) {
                        walker.walkPathReverse(pathToAltar);
                    } else {
                        walker.walkPath(MyConstants.BODY_PATH_PORTAL);
                    }
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return portal.valid() && portal.inViewport() && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
                        }
                    }, 250, 20);
                    Condition.sleep(Random.nextInt(2000, 2500));
                }
            }
    }

    private void leaveRuins(){
        final GameObject portal = ctx.objects.select().id(portalId).nearest().poll();
        portal.hover();
        Condition.sleep(Random.nextInt(500, 1000));
        portal.interact("Use", "Portal");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !portal.inViewport();
            }
        }, 250, 20);
    }
}
