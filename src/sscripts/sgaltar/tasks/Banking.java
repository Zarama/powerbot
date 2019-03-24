package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.data.Data;

import java.util.concurrent.Callable;


public class Banking extends Task {
    Data data;
    public Banking(ClientContext ctx, Data data) {
        super(ctx);
        this.data = data;
    }

    @Override
    public boolean activate() {
        return ctx.bank.opened() && ctx.inventory.select().count() != 28;
    }

    @Override
    public void execute() {
        SGAltar.status="Withdrawing Bones";
        int boneId = SGAltar.data.getBone_ID();
        final Item i = ctx.bank.select().id(boneId).first().poll();
        if (i.stackSize() < 27) {
            ctx.controller.stop();
            System.out.println("Stop - Out of bones ");
        }
        if(ctx.bank.withdraw(i, 28)){
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().contains(i);
                }
            }, 300, 5);
        }
    }
}
