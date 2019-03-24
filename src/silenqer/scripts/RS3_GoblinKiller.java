package silenq3r.scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;

import java.awt.*;

import static org.powerbot.script.Condition.sleep;

@Script.Manifest(name = "Goblin Killer",
                description = "Kills goblins in Lumbridge",
                properties = "author=SILENQER; topic=1343910; client=6;")

public class RS3_GoblinKiller extends PollingScript<ClientContext> implements PaintListener
{
    Npc gameNPC;

    private static int NPC_ID[] = {11234, 12353, 12357, 11232, 11236, 12355, 12357, 11240};
    private static int[] FOOD_ID = {1965, 2140, 2142}; //Cabbage && Cooked Meat && Cooked Chicken
    private int killedGoblins = 0;
    private String botStatus = null;

    private final Area GoblinLocation = new Area(
            new Tile(3242, 3251, 0),
            new Tile(3266, 3228, 0));

    private final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    private final Font font1 = new Font("Tahoma", 0, 14);
    private final Font font2 = new Font("Segoe UI", 1, 14);

    //XP Stuff
    private long startEXP1;
    private long expGained1 = 0;

    private long startEXP2;
    private long expGained2 = 0;

    private long startEXP3;
    private long expGained3 = 0;

    private long startEXP4;
    private long expGained4 = 0;

    private long startEXP5;
    private long expGained5 = 0;

    private long startEXP6;
    private long expGained6 = 0;

    private long expGainedTotal = 0;

    //Time Stuff
    private long HourToMilliseconds = 3600000;

    @Override
    public void start()
    {
        botStatus = "Starting Script...";
        enoughHP();
        ctx.camera.pitch(360);
        startEXP1 = ctx.skills.experience(Constants.SKILLS_ATTACK);
        startEXP2 = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        startEXP3 = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        startEXP4 = ctx.skills.experience(Constants.SKILLS_CONSTITUTION);
        startEXP5 = ctx.skills.experience(Constants.SKILLS_MAGIC);
        startEXP6 = ctx.skills.experience(Constants.SKILLS_RANGE);
    }

    @Override
    public void stop()
    {
        botStatus = "Stopping Script...";
        ctx.controller.stop();
    }

    @Override
    public void poll() {
        expGained();

        if (enoughHP()) {
            openTheDoor();
            Condition.sleep(300);
            AttackNPC();
        } else {
            botStatus = "Eating";
            ctx.hud.open(Hud.Window.BACKPACK);
            Item eatFood = ctx.backpack.select().id(FOOD_ID).poll();
            eatFood.interact("Eat");
        }
    }

    public void repaint(Graphics g1)
    {
        long seconds = getRuntime() / 1000 %60;
        long minutes =getRuntime() /60000 %60;
        long hours =getRuntime() /3600000;

        Graphics2D g = (Graphics2D)g1;
        g.setRenderingHints(antialiasing);
        g.setFont(font1);
        g.setColor(Color.GREEN);

        Graphics2D menu = (Graphics2D)g1;
        menu.setRenderingHints(antialiasing);
        menu.setFont(font1);

        gameNPC = ctx.npcs.select().id(NPC_ID).nearest().poll();

        int npc_X = gameNPC.centerPoint().x;
        int npc_Y = gameNPC.centerPoint().y;
        int player_X = ctx.players.local().centerPoint().x;
        int player_Y = ctx.players.local().centerPoint().y;
        int enemyDistance = (int)Math.sqrt(
                        (player_X - npc_X) * (player_X - npc_X) +
                        (player_Y - npc_Y) * (player_Y - npc_Y));

        if (GoblinLocation.contains(ctx.players.local()))
        {
            if (!gameNPC.inCombat())
            {
                if (ctx.players.local().animation() != -1)
                {
                    g.setColor(Color.RED);
                }
                else
                {
                    g.setColor(Color.GREEN);
                }

                g.drawLine(player_X -12, player_Y, npc_X, npc_Y);
                g.drawRect(player_X -12, player_Y -35, 25,75);
            }
        }

        menu.setColor(new Color(255,255,255));
        menu.drawRect(26,26, 200, 160);
        menu.setColor(new Color(0,0,0, 185));
        menu.fillRect(26, 26, 201, 160);
        g.setColor(Color.WHITE);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 34,45);
        g.drawString("Status: " + botStatus,  34,65);
        if (enemyDistance <= 55)
        {
            g.drawString("Distance: 0m", 34, 85);
        }
        else
        {
            g.drawString("Distance: " + String.valueOf(enemyDistance) + "m", 34, 85);
        }
        g.drawLine(26, 95, 226,95);
        g.drawString(String.format("XP Gained: %,d", expGainedTotal),  34,115);
        g.drawString(String.format("XP/H: %,d", (expGainedTotal * HourToMilliseconds) / getRuntime()),  34,135);
        g.drawString(String.format("Goblins killed: %,d", killedGoblins),  34,155);
        g.drawLine(26, 163, 226,163);

        g.setColor(Color.RED);
        g.setFont(font2);
        g.drawString("GoblinKill0r by SILENQER", 42, 180);
    }

    public void AttackNPC()
    {
        gameNPC = ctx.npcs.select().id(NPC_ID).nearest().poll();

        if(GoblinLocation.contains(ctx.players.local()) && gameNPC.tile().matrix(ctx).reachable())
        {
            if (enoughHP() && !gameNPC.inCombat())
            {
                AntiBan();
                botStatus = "Moving to NPC";
                gameNPC.interact("Attack", "Goblin");
                ctx.camera.turnTo(gameNPC);
                botStatus = "Attacking NPC";
                Condition.sleep(Random.nextInt(200, 1000));
            }
        }
        else
        {
            ctx.movement.step(GoblinLocation.getCentralTile());
        }
    }

    public void AntiBan()
    {
        int randomChoice = Random.nextInt(0, 4);
        int randomMouseMoveX = Random.nextInt(0, 1920);
        int randomMouseMoveY = Random.nextInt(0, 1920);

        switch(randomChoice)
        {
            case 0:
                ctx.camera.pitch(360);
                break;
            case 1:
                ctx.hud.open(Hud.Window.SKILLS);
                Condition.sleep(Random.nextInt(1000, 3000));
                ctx.hud.open(Hud.Window.BACKPACK);
                break;
            case 2:
                ctx.input.send("{VK_DOWN}");
                Condition.sleep(500);
                ctx.input.send("{VK_UP}");
                ctx.input.send("{VK_UP}");
                break;
            case 3:
                ctx.input.move(randomMouseMoveX, randomMouseMoveY);
                Condition.sleep(Random.nextInt(100, 500));
                ctx.input.move(randomMouseMoveX, randomMouseMoveY);
                break;
        }
    }

    public void openTheDoor()
    {
        GameObject theDoor = ctx.objects.select().id(45476).nearest().limit(1).poll();
        theDoor.interact("Open");
    }

    public boolean enoughHP()
    {
        int currentHP = ctx.combatBar.health();
        int maxHP = ctx.combatBar.maximumHealth();

        int healthPercentage = (currentHP * 100) / maxHP;

        if (healthPercentage >= 30)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void expGained()
    {
        if (startEXP1 != ctx.skills.experience(Constants.SKILLS_ATTACK))
        {
            killedGoblins++;
            expGained1 += ctx.skills.experience(Constants.SKILLS_ATTACK) - startEXP1;
            startEXP1 = ctx.skills.experience(Constants.SKILLS_ATTACK);
        }

        if (startEXP2 != ctx.skills.experience(Constants.SKILLS_STRENGTH))
        {
            killedGoblins++;
            expGained2 += ctx.skills.experience(Constants.SKILLS_STRENGTH) - startEXP2;
            startEXP2 = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        }

        if (startEXP3 != ctx.skills.experience(Constants.SKILLS_DEFENSE))
        {
            killedGoblins++;
            expGained3 += ctx.skills.experience(Constants.SKILLS_DEFENSE) - startEXP3;
            startEXP3 = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        }

        if (startEXP4 != ctx.skills.experience(Constants.SKILLS_CONSTITUTION))
        {
            expGained4 += ctx.skills.experience(Constants.SKILLS_CONSTITUTION) - startEXP4;
            startEXP4 = ctx.skills.experience(Constants.SKILLS_CONSTITUTION);
        }

        if (startEXP5 != ctx.skills.experience(Constants.SKILLS_MAGIC))
        {
            killedGoblins++;
            expGained5 += ctx.skills.experience(Constants.SKILLS_MAGIC) - startEXP5;
            startEXP5 = ctx.skills.experience(Constants.SKILLS_MAGIC);
        }

        if (startEXP6 != ctx.skills.experience(Constants.SKILLS_RANGE))
        {
            killedGoblins++;
            expGained6 += ctx.skills.experience(Constants.SKILLS_RANGE) - startEXP6;
            startEXP6 = ctx.skills.experience(Constants.SKILLS_RANGE);
        }

        expGainedTotal = expGained1 + expGained2 + expGained3 + expGained4 + expGained5 + expGained6;
    }
}