package slicedtoast.KebabBuyer;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "Kebab Buyer", description = "Buys kebabs from kebab man", properties= "author=slicedtoast; client=4; topic=1341248;")

public class KebabBuyer extends PollingScript<ClientContext> implements PaintListener
{
    List<Task> taskList = new ArrayList<Task>();

    GeItem kebab = new GeItem(1971);
    int priceKebab = kebab.price;
    int startCoins = ctx.inventory.select().id(995).count(true); //get amount of coins you start with

    @Override
    public void start()
    {
        taskList.add(new Bank(ctx));
        taskList.add(new Kebab(ctx));
    }

    @Override
    public void poll()
    {
        for(Task Task : taskList)
        {
            if(Task.activate())
            {
                Task.execute();
                break;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics)
    {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60))% 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int currentCoins = ctx.inventory.select().id(995).count(true);
        int totalProfit = (startCoins - currentCoins)*(priceKebab-1); //amount of kebabs bought * profit per kebab

        Graphics2D g = (Graphics2D)graphics;
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0,0, 150, 90);
        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, 150, 90);

        g.drawString("Kebab Buyer", 20, 20);
        g.drawString("Runtime: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);
        g.drawString("Total Profit: " + totalProfit, 20, 60);
        g.drawString("Profit/Hr: " + (int)(totalProfit * (3600000D / milliseconds)), 20, 80);
    }

}
