package Elt.AlKharidFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import Elt.AlKharidFishNCook.Task;

import java.util.concurrent.Callable;

public class handleCombat extends Task {

    public handleCombat(ClientContext ctx) {
        super(ctx);
    }

    private final int fightingAnimationId = 390;

    @Override
    public boolean activate() {
        return (ctx.players.local().inCombat());
    }

    @Override
    public void execute() {
        if ((ctx.players.local().interacting().combatLevel() == 0) || (ctx.players.local().interacting().combatLevel() == -1)) {
            Npc scorpion = ctx.npcs.select().name("Scorpion").nearest().poll();
            if (scorpion != null && scorpion.interacting().equals(ctx.players.local())) {
                scorpion.interact("Attack");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == fightingAnimationId;
                    }
                }, 150, 20);
            }
        }
    }

}
