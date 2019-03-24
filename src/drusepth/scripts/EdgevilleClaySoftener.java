package drusepth.scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import drusepth.scripts.tasks.DepositItemsTask;
import drusepth.scripts.tasks.SeekObjectTask;
import drusepth.scripts.tasks.WithdrawItemsTask;

import java.util.concurrent.Callable;

@Script.Manifest(
        name = "Clay Softener",
        properties = "author=drusepth; topic=1344654; client=4;",
        description = "Softens all banked clay in Edgeville."
)

public class EdgevilleClaySoftener extends PollingScript<ClientContext> {
    public static int EMPTY_BUCKET_ID = 1925;
    public static int FILLED_BUCKET_ID = 1929;
    public static int CLAY_ID = 434;
    public static int SOFT_CLAY_ID = 1761;
    public static int WELL_ID = 884;
    public static int BANK_ID = 6943;

    ClaySofteningState current_state;

    @Override
    public void start() {
        current_state = new ClaySofteningState();
    }

    @Override
    public void poll() {
        System.out.println("State is: " + current_state.action);

        if (ctx.controller.isStopping()) {
            return;
        }

        switch (current_state.action) {
            case WITHDRAW_BUCKETS:
                new WithdrawItemsTask(ctx, EMPTY_BUCKET_ID, 14).execute();
                break;

            case WITHDRAW_CLAYS:
                if (ctx.bank.opened() && ctx.bank.select().id(CLAY_ID).count() == 0) {
                    // Job's done!
                    return;
                }

                new WithdrawItemsTask(ctx, CLAY_ID, 14).execute();
                break;

            case RUN_TO_WELL:
                new SeekObjectTask(ctx, WELL_ID).execute();
                break;

            case FILL_BUCKETS:
                ctx.inventory.select().id(EMPTY_BUCKET_ID).poll().interact("Use");
                Condition.sleep(Random.nextInt(300, 1300));

                GameObject well = ctx.objects.select().id(WELL_ID).nearest().poll();
                well.interact("Use");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.inventory.select().id(EMPTY_BUCKET_ID).count() == 0;
                    }
                }, Random.nextInt(500, 1000), 15);
                break;

            case SOFTEN_CLAYS:
                ctx.inventory.select().id(CLAY_ID).poll().interact("Use");
                Condition.sleep(Random.nextInt(300, 1300));

                ctx.inventory.select().id(FILLED_BUCKET_ID).reverse().poll().interact("Use");
                Condition.sleep(Random.nextInt(1000, 2000));
                ctx.input.send("1");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.inventory.select().id(CLAY_ID).count() == 0
                                && ctx.inventory.select().id(FILLED_BUCKET_ID).count() == 0;
                    }
                }, Random.nextInt(500, 1000), 25);
                break;

            case RUN_TO_BANK:
                new SeekObjectTask(ctx, BANK_ID).execute();
                break;

            case DEPOSIT_CLAYS:
                new DepositItemsTask(ctx, SOFT_CLAY_ID).execute();
                break;
        }

        current_state.step_to_next_action();
    }
}
