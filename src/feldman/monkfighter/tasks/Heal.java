package feldman.monkfighter.tasks;

import feldman.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public class Heal extends Task {

    private int abbotId = 2577;
    Npc abbot = ctx.npcs.select().id(abbotId).nearest().poll();

    public Heal(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate(){
        return !ctx.players.local().inCombat()
                && (ctx.players.local().healthPercent() <= 50);
    }

    @Override
    public void execute(){
        abbot = ctx.npcs.select().id(abbotId).nearest().poll();
        if(abbot.valid() && abbot.tile().distanceTo(ctx.players.local().tile()) < 3){
            if(!abbot.inViewport() && !ctx.players.local().inMotion()){
                Tile[] findAbbot = {abbot.tile()};
                walker.walkPath(findAbbot);
                ctx.camera.turnTo(abbot);
            }
            if (ctx.chat.canContinue()) {
                Condition.sleep(Random.nextInt(350, 750));
                ctx.chat.continueChat(false,"heal");
            } else{
                if(!ctx.players.local().inMotion()){
                    abbot.interact("Talk");
                }
            }
        }else{
            if(!ctx.movement.running() && ctx.movement.energyLevel()> 10){
                ctx.movement.running(true);
            }
            if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                walker.walkPathReverse(SaveYourself.RUN_SOUTH);
            }
        }
    }

}
