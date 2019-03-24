package SaiyanKunt.chickenKiller;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GeItem;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created by SaiyanKunt on 3/27/2017.
 */

@Script.Manifest(
        name = "Chicken Killer", properties = "author=SaiyanKunt; topic=1330081; client=4;",
        description = "A basic script that kills chickens, collects feathers and bones after 5 kills and buries bones when the inventory is full.")

public class ChickenKiller extends PollingScript<ClientContext> implements PaintListener
{
    private KillChicken killChicken = new KillChicken(ctx);
    private PickupLoot pickupLoot = new PickupLoot(ctx);
    private BuryBones buryBones = new BuryBones(ctx);

    public void start()
    {
        final ChickenKillerGUI gui = new ChickenKillerGUI();
        gui.setVisible(true);
        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return gui.done();
            }
        }, 1000, 60);

        killChicken.setActive(gui.killChickens());
        pickupLoot.setActive(gui.pickupFeathers(), gui.buryBones());
        buryBones.setActive(gui.buryBones());
    }

    public void poll()
    {
        ctx.camera.pitch(true);
        ctx.game.tab(Game.Tab.INVENTORY);
        if(ctx.movement.energyLevel() > 25)
        {
            ctx.movement.running(true);
        }
        if(killChicken.activate())
        {
            killChicken.execute();
            if(killChicken.failed())
            {
                return;
            }
        }
        if(killChicken.getChickensKilled() % 5 == 0 || !killChicken.isActive()) //TODO possibly allow the user to configure this number?
        {
            for(int i = 0; i < 5; i++)
            {
                if (pickupLoot.activate())
                {
                    pickupLoot.execute();
                }
            }
        }
        if(buryBones.activate())
        {
            buryBones.execute();
        }
    }


    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);
    private final int FEATHER_COST = new GeItem(314).price;
    public void repaint(Graphics graphics)
    {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);

        int chickensKilled = killChicken.getChickensKilled();
        int bonesBuried = buryBones.getBonesBuried();
        int feathersLooted = pickupLoot.getFeatherCount();
        int chickensHr = (int) ((chickensKilled * 3600000D) / getRuntime());
        int feathersHr = (int) ((feathersLooted * 3600000D) / getRuntime());
        int bonesBuriedHr = (int) ((bonesBuried * 3600000D) / getRuntime());
        int profit = feathersLooted*FEATHER_COST;

        g.setColor(Color.BLACK);
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g.setComposite(alphaComposite);
        g.fillRect(5, 25, 185, 80);
        g.setColor(Color.WHITE);

        g.drawString(String.format("Chickens Killed: %,d (%,d /Hr)", chickensKilled, chickensHr), 10, 40);
        g.drawString(String.format("Feathers Looted: %,d (%,d /Hr)", feathersLooted, feathersHr), 10, 60);
        g.drawString(String.format("Bones Buried: %,d (%,d /Hr)", bonesBuried, bonesBuriedHr), 10, 80);
        g.drawString(String.format("Money Made: %,d gp", profit), 10, 100);
    }
}