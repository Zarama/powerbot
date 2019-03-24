package drusepth.scripts.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class WithdrawItemsTask extends Task {
    int item_id_to_withdraw;
    int amount_to_withdraw;

    public WithdrawItemsTask(ClientContext ctx, int item_id, int amount) {
        super(ctx);

        item_id_to_withdraw = item_id;
        amount_to_withdraw  = amount;
    }

    @Override
    public void execute() {
        if (!ctx.bank.opened()) {
            ctx.camera.turnTo(ctx.bank.nearest());
            ctx.bank.open();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    return ctx.bank.opened();
                }
            }, Random.nextInt(500, 1000), 15);
        }

        if (ctx.bank.opened()) {
            final int starting_bank_amount = ctx.bank.select().id(item_id_to_withdraw).count();
            ctx.bank.withdraw(item_id_to_withdraw, amount_to_withdraw);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    int amount_now_in_bank = ctx.bank.select().id(item_id_to_withdraw).count();
                    return amount_now_in_bank == 0
                            || amount_now_in_bank == starting_bank_amount - amount_to_withdraw;
                }
            }, Random.nextInt(250, 500), 15);
        }
    }
}
