package feldman.monkfighter;

import feldman.Task;
import feldman.monkfighter.tasks.FightMonk;
import feldman.monkfighter.tasks.Heal;
import feldman.monkfighter.tasks.SaveYourself;
import org.powerbot.script.Filter;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Npc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Script.Manifest(name="Feldman MonkFighter", description="Fights monks, heals when low", properties="client=4; author=Feldman; topic=1346154;")
public class MonkFighter extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();
    int currentExp;
    int startingExp = getCurrentExp();
    String expGainedStr;
    Color transGrey = new Color(119, 136, 153, 175);
    String title = "Feldman MonkFighter";

    @Override
    public void start(){
        taskList.add(new SaveYourself(ctx));
        taskList.add(new Heal(ctx));
        taskList.add(new FightMonk(ctx));

        ctx.camera.pitch(99);
    }

    @Override
    public void poll(){
        dismissRandomEvent();
        for(Task task : taskList){
            if(ctx.controller.isStopping()){
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }

    }

    @Override
    public void repaint(Graphics graphics) {
        currentExp = getCurrentExp();
        expGainedStr = "Exp gained: " + (currentExp - startingExp);
        graphics.setFont(new Font("Arial",Font.BOLD,12));
        graphics.setColor(transGrey);
        graphics.drawRect(0, 0, 230, 55);
        graphics.fillRect(0, 0, 230, 55);
        graphics.setColor(Color.black);
        graphics.drawString(title, 15, 15);
        graphics.drawString(expGainedStr, 15, 35);


    }

    private int getCurrentExp() {
        return ctx.skills.experience(Constants.SKILLS_ATTACK) + ctx.skills.experience(Constants.SKILLS_DEFENSE) + ctx.skills.experience(Constants.SKILLS_STRENGTH) + ctx.skills.experience(Constants.SKILLS_HITPOINTS);
    }

    public void dismissRandomEvent() {
        /* Credit to @laputa.  URL: https://www.powerbot.org/community/topic/1292825-random-event-dismisser/  */
        Npc randomNpc = ctx.npcs.select().within(2.0).select(new Filter<Npc>() {

            @Override
            public boolean accept(Npc npc) {
                return npc.overheadMessage().contains(ctx.players.local().name());
            }

        }).poll();

        if (randomNpc.valid()) {
            String action = randomNpc.name().equalsIgnoreCase("genie") ? "Talk-to" : "Dismiss";
            if (randomNpc.interact(action)) {
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (org.powerbot.script.Random.nextDouble(3, 3.5) * 1000));
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            }

        }
    }
}
