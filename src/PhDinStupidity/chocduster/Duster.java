package PhDinStupidity.chocduster;

import PhDinStupidity.chocduster.interfaces.Task;
import PhDinStupidity.chocduster.tasks.BankChocolate;
import PhDinStupidity.chocduster.tasks.KnifeDustChocolate;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

@Script.Manifest(name = "Dr. Duster", description = "Simple no requirement money making script. Turns chocolate bars into chocolate dust.", properties = "author=PhD in Stupidity; topic=1335285; client=4;")
public class Duster extends PollingScript<ClientContext> implements PaintListener
{
    private java.util.List<Task> taskList = new ArrayList<Task>();
    private int chocolateDustPrice = -1, chocolateBarPrice = -1;
    public static int chocolateDusted = 0;


    private final Font titleFont = new Font("default", Font.BOLD, 16), paintFont = new Font("default", Font.PLAIN, 12);
    private final int xPos = 309, yStart = 300;
    private final Color chocolateColor = new Color(102, 48, 10, 255);
    @Override
    public void repaint(Graphics graphics)
    {
        Graphics2D g = (Graphics2D) graphics;

        g.setColor(chocolateColor);

        g.fillRect(xPos - 1, yStart - 14, 208, 337 - yStart + 15);

        int l = 0;
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString("Dr. Duster - Alpha v0.0.1", xPos, (yStart + l++ * 12));

        g.setFont(paintFont);
        long totalRuntime = this.getTotalRuntime(), seconds = (totalRuntime / 1000) % 60, minutes = (totalRuntime / 60000) % 60, hours = (totalRuntime / 3600000) % 24;
        if (hours == 0 && minutes == 0)
            g.drawString("Time: " + seconds + " second" + ((seconds != 1) ? "s" : ""), xPos, (yStart + l++ * 12));
        else if (hours == 0)
            g.drawString("Time: " + minutes + " min" + ((minutes != 1) ? "s" : "") + ((seconds == 0) ? "" : " & " + seconds + " sec" + ((seconds != 1) ? "s" : "")), xPos, (yStart + l++ * 12));
        else
            g.drawString("Time: " + hours + ":" + minutes + ":" + seconds, xPos, (yStart + l++ * 12));

        int profit = (chocolateDusted * (chocolateDustPrice - chocolateBarPrice));
        g.drawString("Dusted: " + chocolateDusted + " (" + (int) (chocolateDusted * (3600000D / totalRuntime)) + "/HR)", xPos, (yStart + l++ * 12));
        g.drawString("Profit: " + profit + " (" + (int) (profit * (3600000D / totalRuntime)) + "/HR)", xPos, (yStart + l * 12));
    }

    @Override
    public void start()
    {
        if (!ctx.bank.inViewport())
        {
            log.log(Level.SEVERE, "No bank in viewport. Script will not run.");
            ctx.controller.stop();
        }

        chocolateBarPrice = new GeItem(DConstants.CHOCOLATE_BAR).price;
        chocolateDustPrice = new GeItem(DConstants.CHOCOLATE_DUST).price;

        taskList.addAll(Arrays.asList(
                new BankChocolate(ctx),
                new KnifeDustChocolate(ctx)
        ));

        this.log.log(Level.INFO, "Dr. Duster Started [B = " + chocolateBarPrice + "] [D = " + chocolateDustPrice + "]");
    }

    public void stop()
    {
        this.log.log(Level.INFO, "Dr. Duster Stopped");
    }

    @Override
    public void poll()
    {
        for (Task task : taskList)
        {
            if (ctx.controller.isStopping())
                break;

            if (task.activate())
            {
                task.execute();
                break;
            }
        }
    }
}
