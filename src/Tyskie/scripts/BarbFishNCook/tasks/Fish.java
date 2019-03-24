package scripts.BarbFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import scripts.BarbFishNCook.resources.Antiban;
import scripts.BarbFishNCook.resources.MyConstants;
import scripts.BarbFishNCook.resources.Task;

import java.util.concurrent.Callable;

public class Fish extends Task {

    private int[] fishIds;
    private Tile fishingSpotLocation = Tile.NIL;
    private Antiban antiban;

    public Fish(ClientContext ctx, int[] fishIds) {
        super(ctx);
        this.fishIds = fishIds;
        antiban = new Antiban();
    }

    @Override
    public boolean activate() {
        return ctx.inventory.count() != MyConstants.INVENTORY_FULL
                && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
    }

    @Override
    public void execute() {
        Npc fishingSpotToFish = ctx.npcs.select().id(fishIds).nearest().poll();

        fishingSpotLocation = fishingSpotToFish.tile();

        if(!fishingSpotToFish.inViewport()){
            ctx.camera.turnTo(fishingSpotToFish);
        }

        fishingSpotToFish.doSetBounds(MyConstants.FISHING_BOUNDS);
        fishingSpotToFish.hover();
        Condition.sleep(Random.nextInt(50, 150));
        fishingSpotToFish.interact("Lure", "Fishing spot");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == MyConstants.ANIMATION_FISHING;
            }
        }, 250, 20);

        boolean isFishing = true;

        while (isFishing){
            if (ctx.players.local().animation() == MyConstants.ANIMATION_IDLE){
                isFishing = !isFishing;
            }
            if (Random.nextDouble() > 0.75){
                antiban.doAntibanAction(Random.nextInt(1, 10));
            }
            Condition.sleep(Random.nextInt(5000, 10000));
        }
    }
}
