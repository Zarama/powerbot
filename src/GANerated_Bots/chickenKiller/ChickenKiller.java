package GANerated_Bots.chickenKiller;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

@Script.Manifest(name = "GAN Chicken Killer", description = "SUPER basic chicken killer", properties = "author=GANerated_Bots; topic=1347230; client=4;")

public class ChickenKiller extends PollingScript<ClientContext> {

    final static int NPC_IDS[] = {2692, 2693};
    final static int PICKUP_IDS[] = {314};

    @Override
    public void start() {
    }

    @Override
    public void stop() {

    }

    @Override
    public void poll() {
        if (shouldRun()) {
            ctx.movement.running(true);
        } else if (notFighting()) {
            pickup();
            attack();
        }
    }

    public boolean notFighting() {
        return !ctx.players.local().inCombat();
    }

    public boolean shouldRun() {
        return !ctx.movement.running() && ctx.movement.energyLevel() > 20;
    }

    public void attack() {
        System.out.println("Trying to attack");
        // finds the nearest npc that is NOT in combat
        final Npc npcToAttack = ctx.npcs.select().id(NPC_IDS).select(new Filter<Npc>() {

            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat();
            }
        }).nearest().poll();

        npcToAttack.interact("Attack");

        // makes sure player isnt in combat before attacking
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        }, 250, 12);
    }

    public void pickup() {
        final int currentNumber = ctx.inventory.select().id(PICKUP_IDS).count(true);
        GroundItem ItemToPickup = ctx.groundItems.select().id(PICKUP_IDS).nearest().poll();
        ItemToPickup.interact("Take", "Feather");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().id(PICKUP_IDS).count(true) != currentNumber;
            }
        }, 250, 12);
    }
}