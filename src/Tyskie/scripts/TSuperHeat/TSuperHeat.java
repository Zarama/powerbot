package scripts.TSuperHeat;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import scripts.TSuperHeat.resources.MyConstants;
import scripts.TSuperHeat.resources.Task;
import scripts.TSuperHeat.tasks.Bank;
import scripts.TSuperHeat.tasks.Superheat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyskie on 17-6-2017.
 */
@Script.Manifest(name="Tyskie's TSuperHeat", description = "Tyskie's TSuperHeat superheats ores into bars.", properties = "author=Tyskie; topic=1334360; client=4")
public class TSuperHeat extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();
    private int magicStartExp, smithingStartExp, magicExpGained, smithingExpGained = 0;
    private int oreId, coalNeeded;

    @Override
    public void start(){
        String userOptions[] = {"Iron bar", "Silver bar", "Steel bar", "Gold bar", "Mithril bar", "Adamantite bar", "Runite bar"};
        String userChoice = "" + JOptionPane.showInputDialog(null, "Select the bar you want to make.", "TSuperHeat", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        if (userChoice.equals("Iron bar")){
            oreId = MyConstants.IRON_ORE;
            coalNeeded = MyConstants.COAL_IRON;
        } else if (userChoice.equals("Silver bar")){
            oreId = MyConstants.SILVER_ORE;
            coalNeeded = MyConstants.COAL_SILVER;
        } else if (userChoice.equals("Steel bar")){
            oreId = MyConstants.IRON_ORE;
            coalNeeded = MyConstants.COAL_STEEL;
        } else if (userChoice.equals("Gold bar")){
            oreId = MyConstants.GOLD_ORE;
            coalNeeded = MyConstants.COAL_GOLD;
        } else if (userChoice.equals("Mithril bar")){
            oreId = MyConstants.MITHRIL_ORE;
            coalNeeded = MyConstants.COAL_MITHRIL;
        } else if (userChoice.equals("Adamantite bar")){
            oreId = MyConstants.ADAMANTITE_ORE;
            coalNeeded = MyConstants.COAL_ADAMANTITE;
        } else if (userChoice.equals("Runite bar")){
            oreId = MyConstants.RUNITE_ORE;
            coalNeeded = MyConstants.COAL_RUNITE;
        } else {
            ctx.controller.stop();
        }

        taskList.add(new Superheat(ctx, oreId, coalNeeded));
        taskList.add(new Bank(ctx, oreId, coalNeeded));

        magicStartExp = ctx.skills.experience(Constants.SKILLS_MAGIC);
        smithingStartExp = ctx.skills.experience(Constants.SKILLS_SMITHING);
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (ctx.controller.isStopping()){
                break;
            }

            if (task.activate()){
                task.execute();
                break;
            }
        }
    }

    @Override
    public void stop(){
        System.out.println("Thanks for using Tyskie's TSuperHeat!");
        System.out.println("Runned for: " + getRunningTime());
        magicExpGained = ctx.skills.experience(Constants.SKILLS_MAGIC) - magicStartExp;
        smithingExpGained = ctx.skills.experience(Constants.SKILLS_SMITHING) - smithingStartExp;
        System.out.println("Magic Exp gained: " + magicExpGained);
        System.out.println("Smithing Exp gained: " + smithingExpGained);
    }

    @Override
    public void repaint(Graphics graphics) {
        magicExpGained = ctx.skills.experience(Constants.SKILLS_MAGIC) - magicStartExp;
        smithingExpGained = ctx.skills.experience(Constants.SKILLS_SMITHING) - smithingStartExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 175, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 175, 90);

        g.drawString("Tyskie's TSuperHeat", 10, 266);
        g.drawString("Running For: " + getRunningTime(), 10, 286);
        g.drawString("Magic Exp Gained: " + magicExpGained, 10, 306);
        g.drawString("Smithing Exp Gained: " + smithingExpGained, 10, 326);
    }

    private String getRunningTime(){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
