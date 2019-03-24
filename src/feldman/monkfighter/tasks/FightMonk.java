package feldman.monkfighter.tasks;

import feldman.Task;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Npcs;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FightMonk extends Task {
    private int monkId = 2579;
    private int healAnimation = 709;
    Npc monk;

    public FightMonk(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        return !ctx.players.local().inCombat() && ctx.combat.healthPercent() > 50;
    }

    @Override
    public void execute(){
        monk = ctx.npcs.select().select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return (npc.id() == monkId)
                        && (npc.interacting().equals(ctx.players.local()) ||
                                (!npc.inCombat() &&
                        npc.animation() != 709 &&
                        !npc.interacting().inCombat()));
            }
        }).nearest().poll();

        if(monk.valid()){
            if(!monk.inViewport() && !ctx.players.local().inMotion()){
                Tile[] findMonk = {monk.tile()};
                walker.walkPath(findMonk);
                ctx.camera.turnTo(monk);
            }
            if(!monk.interacting().equals(ctx.players.local())){
                monk.interact("Attack");
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return monk.animation() == 836 ||
                            !monk.valid() ||
                            ctx.combat.healthPercent() <50 ||
                            (!monk.interacting().equals(ctx.npcs.nil()) && !monk.interacting().equals(ctx.players.local()));
                }
            });
            try{
                TimeUnit.MILLISECONDS.sleep(Random.nextInt(800,1300));
            }catch(InterruptedException e){
            }
        } else if(ctx.players.local().tile().y() < 3480){
            //maybe we healed >50 while de-aggro
            if(!ctx.movement.running() && ctx.movement.energyLevel()> 10){
                ctx.movement.running(true);
            }
            if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                walker.walkPathReverse(SaveYourself.RUN_SOUTH);
            }
        }
    }
}
