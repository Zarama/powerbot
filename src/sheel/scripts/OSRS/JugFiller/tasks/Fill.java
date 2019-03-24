package sheel.scripts.OSRS.JugFiller.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Player;
import sheel.scripts.OSRS.JugFiller.Constants;
import sheel.scripts.OSRS.JugFiller.Task;

public class Fill extends Task<ClientContext> {
    public Fill(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate()
    {
        return ctx.inventory.select().id(Constants.JUG_ID).count() > 0 && ctx.players.local().animation() == -1;
    }

    @Override
    public void execute()
    {
        Player player = ctx.players.local();
        if(Constants.FOUNTAIN_AREA.contains(player))
        {
            Item jug = ctx.inventory.select().id(Constants.JUG_ID).poll();
            GameObject fountain = ctx.objects.select(10).id(Constants.FOUNTAIN_ID).poll();
            Condition.wait(() -> jug.interact("Use"), 250, 10);
            Condition.wait(() -> fountain.click(), 250, 10);

        }
        else
        {
            ctx.movement.step(Constants.FOUNTAIN_AREA.getRandomTile());
        }
    }
}
