package thebonobo.ChocoCrusher;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import thebonobo.listeners.EventDispatcher;
import thebonobo.listeners.InventoryEvent;
import thebonobo.listeners.InventoryListener;
import thebonobo.ChocoCrusher.tasks.Banking;
import thebonobo.ChocoCrusher.tasks.Crusher;
import thebonobo.ChocoCrusher.tasks.Task;
import thebonobo.ChocoCrusher.utils.Antiban;
import thebonobo.ChocoCrusher.utils.Info;
import thebonobo.ChocoCrusher.utils.Items;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 16/09/17
 */

@Script.Manifest(
        name = "Bonobo ChocoCrusher", properties = "author=thebonobo; topic=1338005; client=4;",
        description = "Takes Chocolate bars out of your bank and uses a knife to make chocolate dust. Start at grand exchange with the knife in the right bottom slot for best profitably"
)
public class ChocoCrusher extends PollingScript<ClientContext> implements PaintListener, InventoryListener {

    private static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);
    private List<Task> taskList = new ArrayList<Task>();
    private EventDispatcher eventDispatcher;
    private Item knife;

    private static String timeConversion(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds -= minutes * 60;
        minutes -= hours * 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void start() {
        eventDispatcher = new EventDispatcher(ctx);
        eventDispatcher.addListener(this);

        taskList.add(new Crusher(ctx));
        taskList.add(new Banking(ctx));
        Info.getInstance().setStartTime(System.currentTimeMillis() / 1000);
    }

    @Override
    public void poll() {
        for (Task t : taskList) {
            if (t.activate()) {
                t.execute();
            }
        }
        Antiban.degradationSleep();
        Antiban.dismissRandomEvent(ctx);
    }

    @Override
    public void stop() {
        log.info("finished");
    }

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(5, 5, 275, 90, 10, 10);
        g.setFont(TAHOMA);
        g.setPaint(Color.WHITE);
        g.draw(roundedRectangle);
        g.fill(roundedRectangle);
        g.setPaint(Color.BLACK);

        int gp = Info.getInstance().chocolateDone() * Info.getInstance().getChocolateDustPrice() - Info.getInstance().chocolateDone() * Info.getInstance().getChocolateBarPrice();
        int gpPerHour = (int) ((gp * 3600000D) / getRuntime());
        int barsPerHour = (int) ((Info.getInstance().chocolateDone() * 3600000D) / getRuntime());

        g.drawString("Runtime: " + timeConversion(getRuntime() / 1000), 10, 30);
        g.drawString("Current Task: " + Info.getInstance().getCurrentTask(), 10, 45);
        g.drawString(String.format("Chocolate bars done (bars/HR: %,d (%,d)", Info.getInstance().chocolateDone(), barsPerHour), 10, 60);
        g.drawString(String.format("GP (GP/HR): %,d (%,d)", gp, gpPerHour), 10, 75);
    }

    @Override
    public void onInventoryChange(InventoryEvent inventoryEvent) {
        int id = inventoryEvent.getNewItem().id();
        if (id == Items.CHOCOLATE_DUST)
            Info.getInstance().incrementChocolateDust(1);
    }

}