package Elt.AlKharidFishNCook.tasks;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Npc;
import Elt.AlKharidFishNCook.Task;

public class Fish extends Task {

    private final int shrimpSpotId = 1528;
    private final Tile[] bankToSpotPath = {new Tile(3269, 3166, 0), new Tile(3273, 3166, 0), new Tile(3273, 3162, 0), new Tile(3273, 3158, 0), new Tile(3273, 3154, 0), new Tile(3273, 3150, 0), new Tile(3273, 3145, 0)};

    public Fish(ClientContext ctx) {
        super(ctx);
    }

    private String getFishingMode() {
        if (
            ctx.skills.level(Constants.SKILLS_FISHING) >= 5 &&
            ctx.inventory.select().id(rodId).count() > 0 &&
            ctx.inventory.select().id(baitId).count() > 0
        ) {
            return "Bait";
        } else if (ctx.inventory.select().id(netId).count() > 0) {
            return "Net";
        } else {
            return null;
        }
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() < 28);
    }

    @Override
    public void execute() {
        // They should be fishing, but they aren't.
        if (!ctx.npcs.select().id(shrimpSpotId).isEmpty()) {
            // A fishing spot exists around the player.
            final Npc fishSpot = ctx.npcs.select().id(shrimpSpotId).nearest().poll();
            if (fishSpot.inViewport()) {
                String fishingMode = getFishingMode();
                if (fishingMode != null) {
                    Condition.sleep(Random.nextInt(100, 1500));
                    fishSpot.interact(fishingMode);
                    Condition.sleep(Random.nextInt(5000, 10000));
                } else {
                    System.out.println("Fishing supplies not in inventory. Stopping script.");
                    ctx.controller.stop();
                }
            } else {
                ctx.movement.step(fishSpot);
                ctx.camera.turnTo(fishSpot);
            }
        } else {
            // There isn't a fish spot nearby. Walk to the fish spot.
            ctx.movement.newTilePath(bankToSpotPath).traverse();
        }
    }
}
