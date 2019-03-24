package DieselSkrt.DRuneRunner;

import DieselSkrt.DRuneRunner.tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shane on 13-1-2018.
 */

@Script.Manifest(
        name = "DRuneRunner",
        properties = "author=DieselSkrt; topic=1341913; client=4;",
        description = "Runs essence from bank to altar and trades your main")

public class DRuneRunner extends PollingScript<ClientContext> implements PaintListener{

    private List<Task> taskList = new ArrayList<Task>();

    public static String CRAFTER_USERNAME;
    public static String ALTAR_TO_CRAFT;
    public static String STATUS;
    public static int AMOUNT_ESSENCE;

    @Override public void start() {

        String altarOptions[] = {"Air altar", "Mind altar(Not added)", "Water altar(not added)", "Earth altar", "Fire altar(Not added)", "Body altar"};
        ALTAR_TO_CRAFT = "" + JOptionPane.showInputDialog(null, "Choose altar to run to: (Make sure you wear the right tiara)", "DRuneRunner", JOptionPane.QUESTION_MESSAGE, null, altarOptions, altarOptions[0]);
        CRAFTER_USERNAME = "" + JOptionPane.showInputDialog(null, "Give the username of the crafter to trade. Please enter as it's shown in-game.");
        AMOUNT_ESSENCE = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the amount of essence you want to take out of the bank:"));

        if(AMOUNT_ESSENCE < 1 && AMOUNT_ESSENCE > 28){
            ctx.controller.stop();
        }

        if(CRAFTER_USERNAME == null){
            ctx.controller.stop();
        }else {
            taskList.add(new Bank(ctx));
            taskList.add(new EnterRuins(ctx));
            taskList.add(new EnterPortal(ctx));
            taskList.add(new GoToAltar(ctx));
            taskList.add(new GoToBank(ctx));
            taskList.add(new TradeCrafter(ctx));
        }
    }
    @Override public void poll() {
            for(Task task : taskList){
                if(ctx.controller.isStopping()){
                    break;
                }
                if(task.activate()){
                    task.execute();
                }
            }
    }

    @Override
    public void repaint(Graphics graphics){
        long milliseconds   = this.getTotalRuntime();
        long seconds        = (milliseconds / 1000) % 60;
        long minutes        = (milliseconds / (1000 * 60) % 60);
        long hours          = (milliseconds / (1000 * 60 * 60) % 24);

        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0,0,0,180));
        g.fillRect(0, 0, 150, 100);

        g.setColor(new Color(255,255,255));
        g.drawString("DRuneRunner", 20, 20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);
        g.drawString("Trading: " + CRAFTER_USERNAME, 20, 60);
        g.drawString("Status: " + STATUS, 20, 80);
    }
}
