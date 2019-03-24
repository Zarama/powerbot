package thebonobo.ChocoCrusher.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import thebonobo.ChocoCrusher.utils.Info;
import thebonobo.ChocoCrusher.utils.Items;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 16/09/17
 */

public class Banking extends Task<ClientContext> {
    private int[] bankBooths = {100063, 10059, 10061, 10060};

    public Banking(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.objects.select(5).id(bankBooths).poll().valid() &&
                ctx.inventory.select().id(Items.CHOCOLATE_BAR).count() == 0;
    }

    @Override
    public void execute() {
        System.out.println("bars in inventory is: " + ctx.inventory.select().id(Items.CHOCOLATE_BAR).count());
        Info.getInstance().setCurrentTask("Banking");

        if (ctx.inventory.selectedItem().valid())
            ctx.inventory.select().id(Items.KNIFE).poll().click();

        if (ctx.bank.opened()) {
            ctx.bank.deposit(Items.CHOCOLATE_DUST, Bank.Amount.ALL);
            if (ctx.bank.select().id(Items.CHOCOLATE_BAR).isEmpty()) {
                ctx.controller.stop();
            } else {
                ctx.bank.withdraw(Items.CHOCOLATE_BAR, Bank.Amount.ALL);
            }
            ctx.bank.close();
        } else {
            Condition.wait(() -> {ctx.bank.open(); return ctx.bank.opened();}, 300, 5);
        }
    }
}
