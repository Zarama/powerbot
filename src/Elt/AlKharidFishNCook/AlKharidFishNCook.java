package Elt.AlKharidFishNCook;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Skills;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import Elt.AlKharidFishNCook.Task;
import Elt.AlKharidFishNCook.tasks.*;

@Script.Manifest(name="AlKharidFishNCook", description="This bot fishes, cooks, and banks or drops in Al Kharid.", properties="author=Elt; topic=1337953; client=4;")

public class AlKharidFishNCook extends PollingScript<ClientContext> implements PaintListener, MessageListener {

    private int baseFishingXP=0;
    private int baseCookingXP=0;
    private int numFishCaught=0;
    private int numFishCooked=0;
    private Skills skills;
    private List<Task> taskList = new ArrayList<Task>();
    private Task handleCombatTask;
    private Task fishTask;
    private Task cookTask;
    private Task bankTask;
    private Task dropTask;

    @Override
    public void start() {
        System.out.println("AlKharidFishNCook starting.");
        String userOptions[] = {"Bank", "Drop"};
        String userChoice = ""+(String)JOptionPane.showInputDialog(null, "Bank or Drop cooked fish?", "Fishing", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        this.handleCombatTask = new handleCombat(ctx);
        this.fishTask = new Fish(ctx);
        this.cookTask = new Cook(ctx);
        this.skills=new Skills(ctx);
        this.baseFishingXP=this.skills.experience(10);
        this.baseCookingXP=this.skills.experience(7);

        taskList.add(handleCombatTask);
        taskList.add(fishTask);
        taskList.add(cookTask);
        if (userChoice.equals("Bank")) {
            this.bankTask = new Banking(ctx, true);
            taskList.add(bankTask);
        } else {
            this.dropTask = new Drop(ctx);
            taskList.add(dropTask);
        }
    }

    @Override
    public void stop() {
        System.out.println("AlKharidFishNCook stopping.");
    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60) % 60);
        long hours = (milliseconds / (1000*60*60)) % 24;

        Graphics2D g = (Graphics2D)graphics;
        g.setColor(new Color(255, 107, 107, 180));
        g.fillRect(200, 0, 170, 130);
        g.setColor(new Color(255, 45, 45));
        g.drawRect(200, 0, 170, 130);
        g.setColor(new Color(255, 255, 255));
        g.drawString("AlKharidFishNCook", 210, 20);
        g.drawString("Run time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 210, 40);
        g.drawString("Fish",210, 60);
        g.drawString("Cooked/Caught: " + numFishCooked + "/" + numFishCaught, 210, 80);
        while(skills==null);
        g.drawString("Fishing xp gained: " + (skills.experience(10) - this.baseFishingXP), 210, 100);
        g.drawString("Cooking xp gained: " + (skills.experience(7) - this.baseCookingXP), 210, 120);
    }

    @Override
    public void poll() {
        if (ctx.players.local().animation() == -1) {
            for (Task task : taskList) {
                if (task.activate()) {
                    Condition.sleep(Random.nextInt(100,1500));
                    task.execute();
                    break;
                }
            }
        }
    }
    @Override
    public void messaged(MessageEvent e) {
        if (e.text().contains("You catch some")) {
            ++numFishCaught;
        }
        else if (e.text().contains("You successfully cook some")) {
            ++numFishCooked;
        }
    }
}
