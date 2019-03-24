package drusepth.scripts.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class SeekObjectTask extends Task {
    int target_id;

    public SeekObjectTask(ClientContext ctx, int _target_id) {
        super(ctx);

        target_id = _target_id;
    }

    @Override
    public void execute() {
        final GameObject nearest_target = nearest_object();

        if (nearest_target.valid()) {
            final Tile target_tile = nearest_target.tile();
            while (target_tile.distanceTo(ctx.players.local().tile()) > 2) {
                ctx.camera.turnTo(nearest_target);
                ctx.movement.step(nearest_target);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return target_tile.distanceTo(ctx.players.local().tile()) == 1;
                    }
                }, Random.nextInt(500, 1000), 15);

            }
        }
    }

    public GameObject nearest_object() {
        GameObject nearest_target = ctx
                .objects
                .select()
                .within(30.0)
                .id(target_id)
                .nearest()
                .peek();

        return nearest_target;
    }
}
