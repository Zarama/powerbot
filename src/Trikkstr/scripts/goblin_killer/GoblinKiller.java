package Trikkstr.scripts.goblin_killer;

import Trikkstr.scripts.tasks.Bank;
import Trikkstr.scripts.tasks.BuyKebabs;
import Trikkstr.scripts.tasks.Fight;
import Trikkstr.scripts.utils.InitializeOptions;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt4.*;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(

        name="Goblin Killer",
        description = "Kills Goblins and Loots them",
        properties = "client=4; author=Trikkstr; topic=1343746;"
)

public class GoblinKiller extends PollingScript<ClientContext> implements PaintListener
{
    public static boolean bones;
    private static boolean banked = false;

    private int pollCount = 0;

    private long startTime;
    private long endTime;
    private long runTime;

    private int attack = ctx.skills.experience(Constants.SKILLS_ATTACK);
    private int hitpoints = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
    private int strength = ctx.skills.experience(Constants.SKILLS_STRENGTH);
    private int defense = ctx.skills.experience(Constants.SKILLS_DEFENSE);
    private int range = ctx.skills.experience(Constants.SKILLS_RANGE);
    private int prayer = ctx.skills.experience(Constants.SKILLS_PRAYER);
    private int magic = ctx.skills.experience(Constants.SKILLS_MAGIC);
    private int initialEXP;
    private int exp;

    private Font font = new Font("Tahoma", Font.PLAIN, 12);
    private static String status = "-";
    private static String substatus = "-";

    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start()
    {
        startTime = System.currentTimeMillis();
        System.out.println("Starting.");

        initialEXP = attack + hitpoints + strength + defense + range + prayer + magic;

        new InitializeOptions();

        taskList.add(new Bank(ctx));
        taskList.add(new BuyKebabs(ctx));
        taskList.add(new Fight(ctx));

    }

    @Override
    public void stop()
    {
        endTime = System.currentTimeMillis();
        runTime = (endTime - startTime) / 1000;
        System.out.printf("Total Runtime: %ds\n", runTime);
        System.out.printf("Polls: %d\n", pollCount);
        System.out.println("Stopped.");
    }

    @Override
    public void poll()
    {
        System.out.printf("Polling...\n");

        updateEXP();

        for (Task task : taskList)
        {
            if (ctx.controller.isStopping())
            {
                break;
            }

            if (task.activate())
            {
                task.execute();
                break;
            }
        }
        pollCount += 1;
    }

    public static boolean getBones()
    {
        return bones;
    }

    public static void setBanked(boolean bool)
    {
        banked = bool;
    }

    public static boolean getBanked()
    {
        return  banked;
    }

    public static void setStatus(String string)
    {
        status = string;
    }

    public static void setSubstatus(String string)
    {
        substatus = string;
    }

    private void updateEXP()
    {
        attack = ctx.skills.experience(Constants.SKILLS_ATTACK);
        hitpoints = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        strength = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        defense = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        range = ctx.skills.experience(Constants.SKILLS_RANGE);
        prayer = ctx.skills.experience(Constants.SKILLS_PRAYER);
        magic = ctx.skills.experience(Constants.SKILLS_MAGIC);

        exp = attack + hitpoints + strength + defense + range + prayer + magic - initialEXP;
    }

    @Override
    public void repaint(Graphics graphics)
    {
        long runtime = System.currentTimeMillis() - startTime;

        long hours = runtime/(1000*60*60) % 60;
        long minutes = runtime/(1000*60) % 60;
        long seconds = runtime/1000 % 60;

        Graphics2D g = (Graphics2D)graphics;

        g.setFont(font);

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0,20, 240, 50);

        g.setColor(new Color(255,255,255));
        g.drawRect(0, 20,240,50);

        g.drawString("Run Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, 35 );
        g.drawString("Exp Gain: " + String.format("%d", exp), 10, 50);
        g.drawString(String.format("Status: %s - %s", status, substatus), 10, 65);
    }
}
