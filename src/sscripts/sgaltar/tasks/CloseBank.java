package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class CloseBank extends Task {

    public CloseBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.bank.opened() && ctx.inventory.select().count() == 28;
    }

    @Override
    public void execute() {
        SGAltar.status="Closing Bank";
        if (ctx.bank.close()) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.bank.opened();
                }
            },100,5);
        }
    }
}
