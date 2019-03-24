package scripts.BarbFishNCook;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import scripts.BarbFishNCook.resources.MyConstants;
import scripts.BarbFishNCook.resources.Task;
import scripts.BarbFishNCook.tasks.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="Tyskie's BarbFishNCook", description = "Tyskie's BarbFishNCook fishes trout/salmon at Barbarian Village and cooks them on the infinite fire. It supports banking/dropping.", properties = "author=Tyskie; topic=1334294; client=4;")
public class BarbFishNCook extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();
    int fishingStartExp = 0;
    int cookingStartExp = 0;

    @Override
    public void start(){
        String userOptions[] = {"Bank after cooking", "Drop after cooking"};
        String userChoice = "" + JOptionPane.showInputDialog(null, "Bank or Drop?", "BarbFishNCook", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        if (userChoice.equals("Bank after cooking")){
            taskList.add(new Bank(ctx, MyConstants.FISHING_SUPPLIES_IDS));
            taskList.add(new WalkToBank(ctx, MyConstants.FISHING_TO_BANK));
        } else if (userChoice.equals("Drop after cooking")){
            taskList.add(new Drop(ctx));
        } else {
            ctx.controller.stop();
        }

        taskList.add(new WalkToFire(ctx, MyConstants.FISHING_TO_FIRE));
        taskList.add(new Cook(ctx, MyConstants.RAW_FISH_IDS));
        taskList.add(new Fish(ctx, MyConstants.FISHING_SPOT_IDS));

        fishingStartExp = ctx.skills.experience(Constants.SKILLS_FISHING);
        cookingStartExp = ctx.skills.experience(Constants.SKILLS_COOKING);
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if(ctx.controller.isStopping()){
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }
    }

    @Override
    public void stop(){
        System.out.println("Thanks for using BarbFishNCook!");
    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int fishingExpGained = ctx.skills.experience(Constants.SKILLS_FISHING) - fishingStartExp;
        int cookingExpGained = ctx.skills.experience(Constants.SKILLS_COOKING) - cookingStartExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 175, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 175, 90);

        g.drawString("Tyskie's BarbFishNCook", 10, 266);
        g.drawString("Running For: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, 286);
        g.drawString("Fishing Exp Gained: " + fishingExpGained, 10, 306);
        g.drawString("Cooking Exp Gained: " + cookingExpGained, 10, 326);
    }
}
