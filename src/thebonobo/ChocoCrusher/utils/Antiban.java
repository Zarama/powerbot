package thebonobo.ChocoCrusher.utils;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import thebonobo.ChocoCrusher.tasks.Crusher;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 16/09/17
 */

public class Antiban extends ClientContext {
    public Antiban(ClientContext ctx) {
        super(ctx);
    }

    //source: https://www.powerbot.org/community/topic/895370-mouse-help-please-move-mouse-off-of-client/
    public static void MoveMouseOffScreen(ClientContext ctx) {
        ctx.input.hop((int) (Random.nextInt(10, 100) + ctx.game.dimensions().getWidth()),
                Random.nextInt(-10, (int) (ctx.game.dimensions().getHeight() + 10)));
    }

    public static void Wait(int fastest, int slowest) {
        try {
            Thread.sleep((long) Random.nextInt(fastest, slowest));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void MissClick(ClientContext ctx, int itemID) {
        try {
            ctx.inventory.select().id(itemID).poll().click();
            Thread.sleep((long) Random.nextInt(150, 250));
            ctx.inventory.select().id(itemID).poll().click();
            Thread.sleep((long) Random.nextInt(500, 1000));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void WaitAfterBankInteraction() {
        try {
            Thread.sleep((long) Random.nextInt(500, 1500));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void dismissRandomEvent(ClientContext ctx) {
        /* Credit to @laputa.  URL: https://www.powerbot.org/community/topic/1292825-random-event-dismisser/  */
        Npc randomNpc = ctx.npcs.select().within(2.0).select(new Filter<Npc>() {

            @Override
            public boolean accept(Npc npc) {
                return npc.overheadMessage().contains(ctx.players.local().name());
            }

        }).poll();

        if (randomNpc.valid()) {
            String action = randomNpc.name().equalsIgnoreCase("genie") ? "Talk-to" : "Dismiss";
            if (randomNpc.interact(action)) {
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (Random.nextDouble(3, 3.5) * 1000));
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            }

        }
    }

    public static void degradationSleep(){
        // degredation
        long sessionTime = (System.currentTimeMillis() / 1000) - Info.getInstance().getStartTime();
        // After 1 hour, every poll (300 ms) wait 100ms, after 3 hours slowest at -50% efficiency
        Condition.sleep(((int) sessionTime / 36) % 300);
    }
}

