package nagi.sorceress.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Widget;

import nagi.sorceress.SorceressGarden;
import nagi.sorceress.type.ClientPhase;

public class Statistics implements PaintListener, MessageListener {

    public SorceressGarden instance;
    public ClientContext ctx;

    public long startTime = 0L;

    public int totalAttempts = 0;
    public int failedAttempts = 0;

    private BufferedImage image = null;

    public Statistics(SorceressGarden instance, ClientContext ctx) {
        this.instance = instance;
        this.ctx = ctx;

        try {
            this.image = instance.downloadImage("http://image.ibb.co/eSnCe6/a9db1aab71089d97781da7dd1c89.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void messaged(MessageEvent arg0) {
        String msg = arg0.text();
        String from = arg0.source();

        if (from.length() == 0) {
            if (msg.equals("An elemental force emanating from the garden teleports you away.")) {
                totalAttempts++;
                this.instance.getSeasonScript().completeLap();
            } else if (msg.equalsIgnoreCase("You've been spotted by an elemental and teleported out of its garden.")) {
                totalAttempts++;
                failedAttempts++;
                this.instance.getSeasonScript().failLap();
            } else if (msg.equalsIgnoreCase("I can't reach that!")) {
                if (this.instance.getSeasonScript().getSeason().inGarden(this.ctx.players.local().tile())) {
                    Cooldown.setCooldown("interact_herb", 0L);
                }
            }
        }
    }

    @Override
    public void repaint(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;

        Widget chat_widget = this.ctx.widgets.widget(162);
        Component chat_comp = chat_widget.component(0);
        Point chat_point = chat_comp.screenPoint();

        if (this.image != null) {
            g.drawImage(this.image, chat_point.x, chat_point.y, chat_comp.width(), chat_comp.height(), null);
        }

        g.setColor(Color.WHITE);
        Font regular = g.getFont();
        g.setFont(new Font(regular.getName(), Font.BOLD, regular.getSize() + 3));

        if (this.instance.getClientPhase() == ClientPhase.SET_UP) {
            g.drawString("Status: Initilizing Setup...", chat_point.x + 20, chat_point.y + 75);
            g.drawString("Script: N/A", chat_point.x + 20, chat_point.y + 100);
            g.drawString("Success Rate: 0 / 0 (100.00%)", chat_point.x + 20, chat_point.y + 125);
            g.drawString("Elapsed Time: " + this.formatInterval(0, true) + " (0.00/hr)", chat_point.x + 20, chat_point.y + 150);
        } else {
            long time = System.currentTimeMillis() - this.startTime;
            String timeElapsed = this.formatInterval(time, true);

            double successAttempts = this.totalAttempts - this.failedAttempts;
            double failRate = this.totalAttempts != 0 ? ((double) this.failedAttempts) / ((double) this.totalAttempts) : 0;
            double successRate = 100 * (1 - failRate);
            String success = String.format("%.2f", successRate);

            double rateHour = 3600 * (successAttempts / (time / 1000));
            String rate = String.format("%.2f", rateHour);

            g.drawString("Status: " + this.instance.getClientPhase().name(), chat_point.x + 20, chat_point.y + 75);
            g.drawString("Script: " + this.instance.getSeasonScript().getClass().getSimpleName(), chat_point.x + 20, chat_point.y + 100);
            g.drawString("Success Rate: " + ((int) successAttempts) + " / " + this.totalAttempts + " (" + success + "%)", chat_point.x + 20, chat_point.y + 125);
            g.drawString("Elapsed Time: " + timeElapsed + " (" + rate + "/hr)", chat_point.x + 20, chat_point.y + 150);
        }
    }

    public String formatInterval(final long interval, boolean millisecs) {
        final long hr = TimeUnit.MILLISECONDS.toHours(interval);
        final long min = TimeUnit.MILLISECONDS.toMinutes(interval) % 60;
        final long sec = TimeUnit.MILLISECONDS.toSeconds(interval) % 60;
        final long ms = TimeUnit.MILLISECONDS.toMillis(interval) % 1000;
        if (millisecs) {
            return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
        } else {
            return String.format("%02d:%02d:%02d", hr, min, sec);
        }
    }
}