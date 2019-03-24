package drusepth.scripts.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class DepositItemsTask extends Task {
    int item_id_to_deposit;
    Bank.Amount amount_to_deposit;

    public DepositItemsTask(ClientContext ctx, int item_id) {
        super(ctx);

        item_id_to_deposit = item_id;
        amount_to_deposit = Bank.Amount.ALL;
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
            ctx.bank.deposit(item_id_to_deposit, amount_to_deposit);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    int amount_in_inventory = ctx.inventory.select().id(item_id_to_deposit).count();
                    return amount_in_inventory == 0;
                }
            }, Random.nextInt(500, 1000), 15);
        }
    }
}
