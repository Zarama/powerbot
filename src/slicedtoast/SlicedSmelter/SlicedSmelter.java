package slicedtoast.SlicedSmelter;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Script;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Script.Manifest(name = "SlicedSmelter", description = "Smelts ores in Al Kharid and Edgeville", properties= "author=slicedtoast; client=4; topic=1341521;")

public class SlicedSmelter extends PollingScript<ClientContext> implements PaintListener
{
    List<Task> taskList = new ArrayList<Task>();

    private int startExp = ctx.skills.experience(Constants.SKILLS_SMITHING);
    int totalProfit; double costOfMaterials, priceOfResult, experiencePerSmelt, barsMade;

    private GeItem ringOfForging = new GeItem(2568);
    private GeItem cannonball = new GeItem(2);

    private GeItem copperOre = new GeItem(436);
    private GeItem tinOre = new GeItem(438);
    private GeItem ironOre = new GeItem(440);
    private GeItem silverOre = new GeItem(442);
    private GeItem goldOre = new GeItem(444);
    private GeItem coalOre = new GeItem(453);
    private GeItem mithrilOre = new GeItem(447);
    private GeItem adamantOre = new GeItem(449);
    private GeItem runeOre = new GeItem(451);

    private GeItem bronzeBar = new GeItem(2349);
    private GeItem ironBar = new GeItem(2351);
    private GeItem silverBar = new GeItem(2355);
    private GeItem steelBar = new GeItem(2353);
    private GeItem goldBar = new GeItem(2357);
    private GeItem mithrilBar = new GeItem(2359);
    private GeItem adamantBar = new GeItem(2361);
    private GeItem runeBar = new GeItem(2363);

    private int ringOfForgingPrice = ringOfForging.price;
    int priceCannonball = cannonball.price;

    private int copperOrePrice = copperOre.price;
    private int tinOrePrice = tinOre.price;
    private int ironOrePrice = ironOre.price;
    private int silverOrePrice = silverOre.price;
    private int goldOrePrice = goldOre.price;
    private int coalOrePrice = coalOre.price;
    private int mithrilOrePrice = mithrilOre.price;
    private int adamantOrePrice = adamantOre.price;
    private int runeOrePrice = runeOre.price;

    private int bronzeBarPrice = bronzeBar.price;
    private int ironBarPrice = ironBar.price;
    private int silverBarPrice = silverBar.price;
    private int steelBarPrice = steelBar.price;
    private int goldBarPrice = goldBar.price;
    private int mithrilBarPrice = mithrilBar.price;
    private int adamantBarPrice = adamantBar.price;
    private int runeBarPrice = runeBar.price;

    @Override
    public void start()
    {
        String userOptionsArea[] = {"Edgeville", "Al Kharid"};
        String userChoiceArea = ""+JOptionPane.showInputDialog(null, "Location?", "AIOSmelter", JOptionPane.PLAIN_MESSAGE, null, userOptionsArea, "Edgeville");
        String userOptionsType[] = {"Bronze", "Iron", "Iron w/Ring of Forging", "Silver", "Steel", "Gold", "Gold w/Gauntlets", "Mithril", "Adamant", "Rune", "Cannonballs"};
        String userChoiceType = ""+JOptionPane.showInputDialog(null, "Type?", "AIOSmelter", JOptionPane.PLAIN_MESSAGE, null, userOptionsType, "Bronze");
        taskList.add(new Smith(ctx, userChoiceArea, userChoiceType));
        taskList.add(new Bank(ctx, userChoiceType));
        if(userChoiceType.equals("Bronze"))
        {
            costOfMaterials = copperOrePrice + tinOrePrice;
            priceOfResult = bronzeBarPrice;
            experiencePerSmelt = 6.25;
        }
        else if(userChoiceType.equals("Iron w/Ring of Forging"))
        {
            costOfMaterials = ironOrePrice + 0.00714285714*ringOfForgingPrice; //1/140 of ring of forging price
            priceOfResult = ironBarPrice;
            experiencePerSmelt = 12.5;
        }
        else if(userChoiceType.equals("Iron"))
        {
            costOfMaterials = ironOrePrice;
            priceOfResult = .5*ironBarPrice; //just an estimate, nobody smelts iron for profit, so an estimate is fine
            experiencePerSmelt = 12.5;
        }
        else if(userChoiceType.equals("Silver"))
        {
            costOfMaterials = silverOrePrice;
            priceOfResult = silverBarPrice;
            experiencePerSmelt = 13.7;
        }
        else if(userChoiceType.equals("Gold"))
        {
            costOfMaterials = goldOrePrice;
            priceOfResult = goldBarPrice;
            experiencePerSmelt = 22.5;

        }
        else if(userChoiceType.equals("Gold w/Gauntlets"))
        {
            costOfMaterials = goldOrePrice;
            priceOfResult = goldBarPrice;
            experiencePerSmelt = 56.2;
        }
        else if(userChoiceType.equals("Steel"))
        {
            costOfMaterials = 2*coalOrePrice + ironOrePrice;
            priceOfResult = steelBarPrice;
            experiencePerSmelt = 17.5;
        }
        else if(userChoiceType.equals("Mithril"))
        {
            costOfMaterials = 4*coalOrePrice + mithrilOrePrice;
            priceOfResult = mithrilBarPrice;
            experiencePerSmelt = 30;
        }
        else if(userChoiceType.equals("Adamant"))
        {
            costOfMaterials = 6*coalOrePrice + adamantOrePrice;
            priceOfResult = adamantBarPrice;
            experiencePerSmelt = 37.5;
        }
        else if(userChoiceType.equals("Rune"))
        {
            costOfMaterials = 8*coalOrePrice + runeOrePrice;
            priceOfResult = runeBarPrice;
            experiencePerSmelt = 50;
        }
        else if(userChoiceType.equals("Cannonballs"))
        {
            costOfMaterials = steelBarPrice;
            priceOfResult = 4*priceCannonball;
            experiencePerSmelt = 25.6;
        }
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

        double expGained = ctx.skills.experience(Constants.SKILLS_SMITHING) - startExp;
        barsMade = expGained/experiencePerSmelt;
        totalProfit = (int)((priceOfResult - costOfMaterials)*barsMade);

        Graphics2D g = (Graphics2D)graphics;
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0,0, 150, 110);
        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, 150, 110);

        g.drawString("Sliced's Smelter", 20, 20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);
        g.drawString("Exp/Hour: " + (int)(expGained * (3600000D / milliseconds)) , 20, 60);
        g.drawString("Total Profit: " + totalProfit, 20, 80);
        g.drawString("Profit/Hr: " + (int)(totalProfit * (3600000D / milliseconds)), 20, 100);
    }

}
