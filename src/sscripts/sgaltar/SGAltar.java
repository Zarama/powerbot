package sscripts.sgaltar;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import sscripts.sgaltar.data.Data;
import sscripts.sgaltar.gui.Gui;
import sscripts.sgaltar.tasks.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Script.Manifest(name = "SGAltar", description = "[Rimmington] [Yanille] Gilded Altar Bot - Fast Prayer EXP - Supports all Bones - Refreshes your run energy - Up to 900 Bones/hr [200k+ exp/h] - Version:2.04 [05/08/2018]", properties = "author=sscripts; topic=1338021; client=4")
public class SGAltar extends PollingScript<ClientContext> implements PaintListener, MessageListener, MouseListener {

    public static int startEXP, startLVL, expGain, failSaves, prayerLvl, bone_ID, bonesUsed;
    public final List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());

    public static String status = "Waiting for Input";
    public static String playername;
    public static boolean failSave = false;
    public static boolean useFountain = false;
    public static boolean stop = false;
    public static boolean nameEntered = false;
    public static boolean yanille = false;
    public static boolean rimm = false;
    private long startTime;
    public static Data data;
    public static String boneName;

    @Override
    public void start() {
        startLVL = ctx.skills.realLevel(Constants.SKILLS_PRAYER);
        startEXP = ctx.skills.experience(Constants.SKILLS_PRAYER);
        startTime = System.currentTimeMillis();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui(ctx, tasks);
            }
        });
        ctx.input.speed(Random.nextInt(65,100));
    }

    @Override
    public void stop() {

    }

    @Override
    public void messaged(MessageEvent msg) {
        if (msg.text().contains("The gods")) {
            bonesUsed++;
            failSave = false;
        }
        if (msg.text().contains("Nothing interesting")) {
            failSave = true;
            failSaves++;
            ctx.camera.angle(org.powerbot.script.Random.nextInt(100, 250));
            ctx.camera.pitch(org.powerbot.script.Random.nextInt(60, 100));
        }
        if (msg.text().contains("That player is offline")) {
            stop = true;
        }
        if (msg.text().contains("Their house is")) {
            stop = true;
        }
        if (msg.text().contains("They have locked")) {
            stop = true;
        }
        if (msg.text().contains("They do not seem")) {
            stop = true;
        }
        if (ctx.widgets.widget(229).component(0).visible()) {
            stop = true;
        }
    }

    @Override
    public void poll() {
        synchronized (tasks) {
            if (tasks.size() == 0) {
                try {
                    tasks.wait();
                } catch (InterruptedException ignored) {
                }
            }
        }
        for (Task task : tasks) {
            if (task.activate()) {
                task.execute();
            }
        }
    }

    private String formatTime(final long time) {
        final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }

    private int perHour(int value) {
        return (int) ((value) * 3600000D / (System.currentTimeMillis() - startTime));
    }

    private boolean hide = false;
    private Rectangle close = new Rectangle(70,183,70,16);
    private Rectangle open = new Rectangle(10,10,60,16);


    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();
        if (close.contains(p) && !hide) {
            hide = true;
        } else if (open.contains(p) && hide) {
            hide = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void repaint (Graphics g) {
        expGain =  ctx.skills.experience(Constants.SKILLS_PRAYER) - startEXP;
        prayerLvl = ctx.skills.realLevel(Constants.SKILLS_PRAYER);
        if (!hide){
        g.setColor(Color.RED);
        g.fillRect(5,10,190,20);
        g.setColor(Color.BLUE);
        g.fillRect(10,30,180,150);
        g.setColor(Color.CYAN);
        g.fillRect(15,37,113,16);
        g.fillRect(15,57,165,16);
        g.fillRect(15,77,165,16);
        g.fillRect(15,97,165,16);
        g.fillRect(15,117,165,16);
        g.fillRect(15,137,108,16);
        g.fillRect(15,157,165,16);
        g.fillRect(70,183,70,16);


        g.setColor(Color.BLUE);
        g.drawString("SGAltar - v2.04",60,25);
        g.setColor(Color.BLACK);
        g.drawString("RunTime: " + formatTime(getTotalRuntime()),20,50);
        g.drawString("Status: " + status, 20, 70);
        g.drawString("Bones: " + boneName, 20, 90);
        g.drawString("Host-Name: " + playername, 20, 110);
        g.drawString("Bones used (H): " + bonesUsed+" ("+perHour(bonesUsed)+")", 20, 130);
        g.drawString("Prayer Lvl: " + prayerLvl + " (+" + (ctx.skills.realLevel(Constants.SKILLS_PRAYER)-startLVL) + ")",20,150);
        g.drawString("Prayer EXP (H): "+ expGain + " ("+perHour(expGain)+")",20,170);
        g.drawString("Hide paint", 75, 195);}
        else {
            g.setColor(Color.CYAN);
            g.fillRect(10,10,60,16);
            g.setColor(Color.BLACK);
            g.drawString("Show paint", 10, 20);
    }
        //g.drawString("Fail-Save = " + failSave + " ("+failSaves+")",50,190);// g.drawString("Debug" +test,20,220);
        g.setColor(Color.WHITE);
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y - 10, ctx.input.getLocation().x, ctx.input.getLocation().y + 10);
        g.drawLine(ctx.input.getLocation().x - 10, ctx.input.getLocation().y, ctx.input.getLocation().x + 10, ctx.input.getLocation().y);
    }
}
