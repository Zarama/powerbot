package sheel.scripts.RS3.BarbarianFisherAndCooker;

import org.powerbot.script.PaintListener;
import sheel.scripts.RS3.BarbarianFisherAndCooker.branches.IsInventoryFull;
import sheel.RS3TreeBot.*;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.Constants;

import java.awt.*;

@Script.Manifest(
        name = "Barbarian Fisher and Cooker",
        description = "Fishes and cooks at the barbarian village and banks after!",
        properties = "author=sheel;topic=1340719;client=6"
)
public class BarbarianFisherAndCooker extends TreeBot implements PaintListener
{

    private IsInventoryFull isInventoryFullBranch = new IsInventoryFull(ctx);
    private int fishingStartExp = 0;
    private int cookingStartExp = 0;
    private int fishingStartLevel = 0;
    private int cookingStartLevel = 0;


    @Override
    public void start() {
        super.start();

        fishingStartExp = ctx.skills.experience(Constants.SKILLS_FISHING);
        cookingStartExp = ctx.skills.experience(Constants.SKILLS_COOKING);
        fishingStartLevel = ctx.skills.level(Constants.SKILLS_FISHING);
        cookingStartLevel = ctx.skills.level(Constants.SKILLS_COOKING);
    }

    @Override
    public TreeTask createNewRoot() {
        return isInventoryFullBranch;
    }

    @Override
    public void repaint(Graphics graphics)
    {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int fishingExpRate = (ctx.skills.experience(Constants.SKILLS_FISHING) - fishingStartExp);
        int cookingExpRate = (ctx.skills.experience(Constants.SKILLS_COOKING) - cookingStartExp);
        int fishingLevelsGained = ctx.skills.level(Constants.SKILLS_FISHING) - fishingStartLevel;
        int cookingLevelsGained = ctx.skills.level(Constants.SKILLS_COOKING) - cookingStartLevel;

        int x = 5;
        int y = 400;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(0, 76, 153, 180));
        g.fillRect(x, y, 240, 130);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(x, y, 240, 130);

        g.drawString("Sheel's BarbarianFisherAndCooker", x + 5, y + 26);
        g.drawString("Running for: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), x + 5, y + 46);
        g.drawString("Fishing exp gained: " + fishingExpRate, x + 5, y + 66);
        g.drawString("Cooking exp gained: " + cookingExpRate, x + 5, y + 86);
        g.drawString("Fishing levels gained: " + fishingLevelsGained, x + 5, y + 106);
        g.drawString("Cooking levels gained: " + cookingLevelsGained, x + 5, y + 126);
    }

}
