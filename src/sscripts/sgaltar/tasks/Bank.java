package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class Bank extends Task {
    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().isEmpty() && ctx.bank.inViewport() && !ctx.bank.opened();
    }

    @Override
    public void execute() {
        SGAltar.status ="Opening Bank";
        if (ctx.bank.open()) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            },500,3);
        }
    }
}
