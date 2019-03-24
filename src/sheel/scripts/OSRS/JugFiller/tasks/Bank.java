package sheel.scripts.OSRS.JugFiller.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Player;
import sheel.scripts.OSRS.JugFiller.Constants;
import sheel.scripts.OSRS.JugFiller.Task;
import org.powerbot.script.rt4.ClientContext;

public class Bank extends Task<ClientContext> {

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.inventory.select().count() == 0 || ctx.inventory.select().id(Constants.JUG_ID).count() == 0;
    }

    @Override
    public void execute()
    {
        Player player = ctx.players.local();
        if(player.tile().distanceTo(ctx.bank.nearest()) < 3)
        {
                if(ctx.bank.opened())
                {
                    if(ctx.inventory.select().count() != 0)
                    {
                        Condition.wait(() -> ctx.bank.depositInventory(), 250, 10);
                    }

                    if(ctx.bank.select().id(Constants.JUG_ID).count() > 0)
                        Condition.wait(() -> ctx.bank.withdraw(Constants.JUG_ID, 28));
                    else ctx.controller.stop();
                }
                else
                {
                    Condition.wait(() -> ctx.bank.open(), 250, 10);
                }
        }
        else
        {
            ctx.movement.step(ctx.bank.nearest());
        }

    }
}
