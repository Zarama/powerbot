package LoftySoM.scriptHelper;

import org.powerbot.script.rt4.ClientContext;

import java.text.DecimalFormat;
import java.util.*;

public class SkillTracker {
    private ClientContext ctx;
    private int startExp;
    private int expLeft;
    private int expPh;
    private int expGained;
    private int startLevel;
    private int skill;
    private Map<Integer, Integer> runesUsed = new HashMap<>();
    private int goal;

    public SkillTracker(ClientContext ctx) {
        this.ctx = ctx;
    }
    public void initiate(){
        ctx = HelperSingleton.getContext();
    }
    public void update() {
        if (ctx == null)
            return;
        expLeft();
        expGained();
        expPerHour();
    }

    public List<String> skillInfo() {
        return new ArrayList<>(Arrays.asList(
                String.format("Exp gained: %s", getExpGained()),
                String.format("Exp until next level: %s", getExpLeft()),
                String.format("Exp per hour: %s", getExpPh()),
                String.format("%s", getTimeToNextLevel()),
                String.format("Elapsed time: %s", time(ctx.controller.script().getTotalRuntime()))
        ));
    }

    public void expLeft() {
        //Example using Fishing
        int currentExp = ctx.skills.experience(skill);
        int currLevel = ctx.skills.realLevel(skill);
        expLeft = ctx.skills.experienceAt(goal < currLevel ? currLevel + 1 : goal) - currentExp;
    }

    public void expPerHour() {
        long runTime = ctx.controller.script().getRuntime();
        expPh = (int) (3600000d / runTime * (double) (expGained));
    }

    public void expGained() {
        int currentExp = ctx.skills.experience(skill);
        expGained = currentExp - startExp;
    }

    public void addRune(int runeId, int count) {
        runesUsed.put(runeId, count);
    }

    public String time(long i) {
        DecimalFormat nf = new DecimalFormat("00");
        long millis = i;
        long hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        long minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        long seconds = millis / 1000;
        return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
    }

    //Method which gets time till next level.
    public String getTimeToNextLevel() {
        //If not earning exp then return
        if (expPh < 1) {
            return "No EXP gained yet.";
        }

        //If gaining exp then measure approximately it will take to level.
        return String.format("Time left: %s", time((long) (expLeft * 3600000D / expPh)));
    }

    public void setSkill(int skill_) {
        skill = skill_;
        startExp = ctx.skills.experience(skill);
        startLevel = ctx.skills.realLevel(skill);
    }

    public void setGoal(int level) {
        goal = level;
    }

    public int getExpPh() {
        return expPh;
    }

    public int getExpGained() {
        return expGained;
    }

    public int getExpLeft() {
        return expLeft;
    }

    public int getCastsLeft() {
        final int[] minCasts = {0};
        runesUsed.forEach((id, count) -> minCasts[0] = ctx.inventory.select().id(id).count(true) / count);
        return minCasts[0];
    }

    public int getLevelsGained() {
        return ctx.skills.realLevel(skill) - startLevel;
    }

    public int getGoal() {
        return goal;
    }
}