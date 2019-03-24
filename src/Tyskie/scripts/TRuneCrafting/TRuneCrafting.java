package Tyskie.scripts.TRuneCrafting;

import Tyskie.scripts.TRuneCrafting.resources.Task;
import Tyskie.scripts.TRuneCrafting.tasks.Bank;
import Tyskie.scripts.TRuneCrafting.tasks.CraftRunes;
import Tyskie.scripts.TRuneCrafting.tasks.WalkToRuins;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import Tyskie.scripts.TRuneCrafting.resources.MyConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyskie on 18-6-2017.
 */
@Script.Manifest(name="Tyskie's AIO TRuneCrafting", description = "Tyskie's TRuneCrafting crafts your selected runes.", properties = "author=Tyskie; topic=1334445; client=4")
public class TRuneCrafting extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList = new ArrayList<Task>();
    private int startExp, expGained, essenceId, runeId, ruinsId, altarId, portalId;
    private Tile[] pathToRuins, pathToAltar;

    @Override
    public void start(){
        String essenceOptions[] = {"Rune essence", "Pure essence"};
        String essenceChoice = "" + JOptionPane.showInputDialog(null, "Select the essence you want to use.", "AIO TRuneCrafting", JOptionPane.PLAIN_MESSAGE, null, essenceOptions, essenceOptions[0]);

        String runesOptions[] = {"Air runes", "Mind runes", "Water runes", "Earth runes", "Fire runes", "Body runes"};
        String runesChoice = "" + JOptionPane.showInputDialog(null, "Select the runes you want to make.", "AIO TRuneCrafting", JOptionPane.PLAIN_MESSAGE, null, runesOptions, runesOptions[0]);

        if (essenceOptions.equals("Rune essence")){
            essenceId = MyConstants.RUNE_ESSENCE;
        } else if (essenceChoice.equals("Pure essence")){
            essenceId = MyConstants.PURE_ESSENCE;
        } else {
            ctx.controller.stop();
        }

        if (runesChoice.equals("Air runes")){
            runeId = MyConstants.AIR_RUNE;
            ruinsId = MyConstants.AIR_MYSTERIOUS_RUINS_ID;
            altarId = MyConstants.AIR_ALTAR_ID;
            portalId = MyConstants.AIR_PORTAL_ID;
            pathToRuins = MyConstants.FALLY_BANK_AIR_ALTAR;
            pathToAltar = MyConstants.AIR_PORTAL_TO_ALTAR;
        } else if (runesChoice.equals("Mind runes")){
            runeId = MyConstants.MIND_RUNE;
            ruinsId = MyConstants.MIND_MYSTERIOUS_RUINS_ID;
            altarId = MyConstants.MIND_ALTAR_ID;
            portalId = MyConstants.MIND_PORTAL_ID;
            pathToRuins = MyConstants.FALLY_BANK_MIND_ALTAR;
            pathToAltar = MyConstants.MIND_PORTAL_TO_ALTAR;
        } else if (runesChoice.equals("Water runes")){
            runeId = MyConstants.WATER_RUNE;
            ruinsId = MyConstants.WATER_MYSTERIOUS_RUINS_ID;
            altarId = MyConstants.WATER_ALTAR_ID;
            portalId = MyConstants.WATER_PORTAL_ID;
            pathToRuins = MyConstants.LUMBRIDGE_BANK_WATER_ALTAR;
            pathToAltar = MyConstants.WATER_PORTAL_TO_ALTAR;
        } else if (runesChoice.equals("Earth runes")){
            runeId = MyConstants.EARTH_RUNE;
            ruinsId = MyConstants.EARTH_MYSTERIOUS_RUINS_ID;
            altarId = MyConstants.EARTH_ALTAR_ID;
            portalId = MyConstants.EARTH_PORTAL_ID;
            pathToRuins = MyConstants.VARROCK_BANK_EARTH_ALTAR;
            pathToAltar = MyConstants.EARTH_PORTAL_TO_ALTAR;
        } else if (runesChoice.equals("Fire runes")){
            runeId = MyConstants.FIRE_RUNE;
            ruinsId = MyConstants.FIRE_MYSTERIOUS_RUINS_ID;
            altarId = MyConstants.FIRE_ALTAR_ID;
            portalId = MyConstants.FIRE_PORTAL_ID;
            pathToRuins = MyConstants.ALKHARID_BANK_FIRE_ALTAR;
            pathToAltar = MyConstants.FIRE_PORTAL_TO_ALTAR;
        } else if (runesChoice.equals("Body runes")){
            runeId = MyConstants.BODY_RUNE;
            ruinsId = MyConstants.BODY_MYSTERIOUS_RUINS_ID;
            altarId = MyConstants.BODY_ALTAR_ID;
            portalId = MyConstants.BODY_PORTAL_ID;
            pathToRuins = MyConstants.EDGEVILLE_BANK_BODY_ALTAR;
            pathToAltar = MyConstants.BODY_PATH_ALTAR;
        } else {
            ctx.controller.stop();
        }

        taskList.add(new CraftRunes(ctx, essenceId, runeId, ruinsId, altarId, portalId, pathToAltar));
        taskList.add(new WalkToRuins(ctx, pathToRuins, runeId, essenceId));
        taskList.add(new Bank(ctx, essenceId));

        startExp = ctx.skills.experience(Constants.SKILLS_RUNECRAFTING);
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
        System.out.println("Thanks for using Tyskie's AIO TRuneCrafting!");
        System.out.println("Runned For: " + getRunningTime());
        expGained = ctx.skills.experience(Constants.SKILLS_RUNECRAFTING) - startExp;
        System.out.println("Runecrafting Exp Gained: " + expGained);
    }

    @Override
    public void repaint(Graphics graphics) {
        expGained = ctx.skills.experience(Constants.SKILLS_RUNECRAFTING) - startExp;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(128, 128, 128, 180));
        g.fillRect(5, 246, 200, 90);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(5, 246, 200, 90);

        g.drawString("Tyskie's AIO TRuneCrafting", 10, 266);
        g.drawString("Running For: " + getRunningTime(), 10, 296);
        g.drawString("Runecrafting Exp Gained: " + expGained, 10, 326);
    }

    private String getRunningTime(){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
