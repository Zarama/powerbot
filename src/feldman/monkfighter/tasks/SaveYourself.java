package feldman.monkfighter.tasks;

import feldman.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class SaveYourself extends Task {
    public static final Tile RUN_SOUTH[] = {new Tile(3051, 3485, 0), new Tile(3052, 3467, 0), new Tile(3049, 3449, 0)};

    public SaveYourself(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate(){
        return ctx.players.local().inCombat() && (ctx.players.local().healthPercent() <= 50);
    }

    @Override
    public void execute(){
        if(!ctx.movement.running() && ctx.movement.energyLevel()> 10){
            ctx.movement.running(true);
        }
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            walker.walkPath(RUN_SOUTH);
        }
    }
}