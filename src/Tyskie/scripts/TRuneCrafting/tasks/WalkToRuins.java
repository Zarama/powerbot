package Tyskie.scripts.TRuneCrafting.tasks;

import Tyskie.scripts.TRuneCrafting.resources.Task;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import Tyskie.scripts.TRuneCrafting.resources.MyConstants;
import Tyskie.scripts.TRuneCrafting.resources.Walker;

/**
 * Created by Tyskie on 18-6-2017.
 */
public class WalkToRuins extends Task {

    private Tile[] pathToRuins;
    private final Walker walker = new Walker(ctx);
    private int runeId, essenceId;

    public WalkToRuins(ClientContext ctx, Tile[] pathToRuins, int runeId, int essenceId) {
        super(ctx);
        this.pathToRuins = pathToRuins;
        this.runeId = runeId;
        this.essenceId = essenceId;
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.count() == MyConstants.INVENTORY_FULL
                && ctx.inventory.select().id(essenceId).count() == MyConstants.INVENTORY_FULL
                && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE)
                || (ctx.inventory.count() == MyConstants.INVENTORY_ONLY_RUNES
                && ctx.inventory.select().id(runeId).count() == 1
                && pathToRuins[0].distanceTo(ctx.players.local()) > 5
                && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE);
    }

    @Override
    public void execute() {
        if(!ctx.movement.running()
                && ctx.movement.energyLevel() > Random.nextInt(20, 35)){
            ctx.movement.running(true);
        }
        if(!ctx.players.local().inMotion()
                || ctx.movement.destination().equals(Tile.NIL)
                || ctx.movement.destination().distanceTo(ctx.players.local()) < 5){
            if(ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                    && ctx.inventory.select().id(essenceId).count() == MyConstants.INVENTORY_FULL) {
                walker.walkPath(pathToRuins);
            } else {
                walker.walkPathReverse(pathToRuins);
            }
        }
    }
}
