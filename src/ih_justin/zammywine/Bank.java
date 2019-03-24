package ih_justin.zammywine;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Bank extends Task<ClientContext> {
    private final int bankBoothID = 24101;
    private int wineID = 245;
    private int waterRune = 555;
    private int lawRune = 563;

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 5 &&
                ctx.inventory.select().id(lawRune).isEmpty();
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Banking");

        if (ctx.bank.opened()) {
            ctx.bank.depositInventory();

            if (ctx.bank.select().id(lawRune).isEmpty()) {
                ctx.controller.stop();
            } else {
                ctx.bank.withdraw(lawRune, 27);
                ctx.bank.withdraw(waterRune, 1);
            }

            ctx.bank.close();
        } else {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    return ctx.objects.select().id(bankBoothID).nearest().poll().interact("Bank");
                }
            }, 1000, 1);

        }
    }
}
