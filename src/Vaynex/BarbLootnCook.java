package Vaynex;



import Vaynex.tasks.Cook;
import Vaynex.tasks.Drop;
import Vaynex.tasks.Loot;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Me on 19/06/2017. //test
 */

@Script.Manifest(name = "Vaynex Barb Vaynex.BarbLootnCook",
        description = "Picks up and Cooks Fish on fire",
        properties = "client=4; topic=1334448; author=Vaynex;")

public class BarbLootnCook extends PollingScript<ClientContext> implements PaintListener{

    List<Task> tasklist = new ArrayList<Task>();
    int  startExp = 0;
    final static Antiban antiban = new Antiban();

@Override
public void start() {

antiban.runAntiban();
    tasklist.add(new Drop(ctx));
    tasklist.add(new Loot(ctx));
tasklist.add(new Cook(ctx));
    startExp = ctx.skills.experience(Constants.SKILLS_COOKING);

    //tasklist.add(new Drop(ctx));
}


    @Override
    public void poll() {
        for (Task task : tasklist) {
            if (ctx.controller.isStopping()) {
                break;
            }
            if (task.activate()) {
                task.execute();
                break; //add break for task prioritising
            }


        }
    }

    @Override
    public void repaint(Graphics graphics) {
        int expGained = ctx.skills.experience(Constants.SKILLS_COOKING)-startExp;

        Graphics2D g = (Graphics2D)graphics; //get 2d graphics
        g.setColor(new Color(0,0,0,180)); //max is 255.
        g.fillRect(0,0,150,120); //draws rectangle


        g.setColor(new Color(255,255,255)); //draws white border
        g.drawRect(0,0,150,120); //same as first

        g.drawString("Vaynex Barb Fish Cooker", 20, 20); //add text into rectangle
        g.drawString("Time : " + getRunningTime(), 20, 40 );
       //g.drawString("Exp/Hour " + (int)(expGained * (3600000 / getTotalRuntime())), 20, 80); //divides milliseconds/hr by /milliseconds
        g.drawString("Exp Gained: " + expGained, 20, 60);

    }

    private String getRunningTime(){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }
}
