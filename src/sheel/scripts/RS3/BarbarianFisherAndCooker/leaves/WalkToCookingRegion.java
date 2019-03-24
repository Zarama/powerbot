package sheel.scripts.RS3.BarbarianFisherAndCooker.leaves;

import sheel.scripts.RS3.BarbarianFisherAndCooker.Constants;
import sheel.RS3TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class WalkToCookingRegion extends LeafTask
{
    public WalkToCookingRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        Player player = ctx.players.local();
        Tile cookingRegion = Constants.FIRE_TILE;

        ctx.camera.turnTo(cookingRegion);

        ctx.movement.step(cookingRegion);

        Condition.wait(() -> cookingRegion.distanceTo(player) < 3, 250, 10);
    }
}
