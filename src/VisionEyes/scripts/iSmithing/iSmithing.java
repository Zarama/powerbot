package VisionEyes.scripts.iSmithing;

import VisionEyes.scripts.iSmithing.resources.Bar;
import VisionEyes.scripts.iSmithing.resources.Util;
import VisionEyes.scripts.iSmithing.tasks.Bank;
import VisionEyes.scripts.iSmithing.tasks.Smith;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Script.Manifest(name = "iSmithing", description = "F2P Bar Smithing", properties = "Author=VisionEyes; topic=1341884; client=4;")

public class iSmithing extends PollingScript<ClientContext> implements PaintListener {
    private int startExp;
    private List<Task> tasks = new ArrayList<>();
    private int currentLvl;
    private String status;
    private Util util = new Util(ctx);
    private Bar bar = new Bar();


    @Override
    public void start() {
        String title = "iSmithing";
        this.status = "Starting...";
        String barOptions[] = util.toOptions(bar.getBars());
        String selectedBar = (String) JOptionPane.showInputDialog(null, "Select Bar", title, JOptionPane.PLAIN_MESSAGE, null, barOptions, barOptions[0]);
        bar.setSelectedBar(selectedBar);
        tasks.add(new Bank(ctx, bar));
        tasks.add(new Smith(ctx, bar));
        this.startExp = ctx.skills.experience(Constants.SKILLS_SMITHING);
        this.currentLvl = ctx.skills.level(Constants.SKILLS_SMITHING);
    }

    @Override
    public void poll() {
        util.run();
        for (Task task : tasks) {
            if (ctx.controller.isStopping()) {
                break;
            }
            if (task.activate()) {
                status = task.getName();
                task.execute();
                break;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        int expGained = ctx.skills.experience(Constants.SKILLS_SMITHING) - startExp;
        int lvl = ctx.skills.level(Constants.SKILLS_SMITHING) - currentLvl;

        Graphics2D g = (Graphics2D) graphics;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 150, 160);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, 150, 160);

        g.drawString("iSmithing", 20, 20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);
        g.drawString("Exp/Hour " + ((int) (expGained * (3600000D / milliseconds))), 20, 60);
        g.drawString("Lvl: " + (currentLvl) + "(+" + lvl + ")", 20, 80);
        g.drawString("Status: " + (status), 20, 100);
        g.drawString("Bar: " + (this.bar.getSelectedBar() != null ? this.bar.getSelectedBar() : "None"), 20, 120);

    }
}

