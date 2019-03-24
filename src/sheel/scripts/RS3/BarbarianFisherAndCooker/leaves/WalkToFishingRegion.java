package sheel.scripts.RS3.BarbarianFisherAndCooker.leaves;

import sheel.scripts.RS3.BarbarianFisherAndCooker.Constants;
import sheel.RS3TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class WalkToFishingRegion extends LeafTask {

    public WalkToFishingRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        Player player = ctx.players.local();
        Tile fishingRegion = Constants.FISHING_TILE;

        ctx.camera.turnTo(fishingRegion);

        ctx.movement.step(fishingRegion);

        Condition.wait(() -> fishingRegion.distanceTo(player) < 3, 250, 10);



    }
}
