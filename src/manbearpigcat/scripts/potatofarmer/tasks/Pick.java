package manbearpigcat.scripts.potatofarmer.tasks;

import manbearpigcat.scripts.potatofarmer.PotatoPicker;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Shan on 2016-08-17.
 */
public class Pick extends Task<ClientContext> {
    private static final int POTATO = 312;
    private static final Tile T1 = new Tile(3273, 3329, 0);
    private static final Tile T2 = new Tile(3241, 3297, 0);
    private static Area area = new Area(T1, T2);
    private static final Tile T4 = new Tile(3265, 3304, 1);
    private static final Tile T5 = new Tile(3268, 3300, 1);
    private static final Area a2 = new Area(T4, T5);

    public Pick(ClientContext ctx){
        super(ctx);
    }

    public boolean activate(){
        return ctx.backpack.select().count() < 28 && area.contains(ctx.players.local()) ||
            a2.contains(ctx.players.local()); //A2 fix for running back to field bug
    }

    public void execute(){
        PotatoPicker.sPots.setState("Picking Potatoes.");
        GameObject pot = ctx.objects.select(20).id(POTATO).nearest().poll();
        if(pot.inViewport()){
            boolean check;
            if(pot.interact("Pick", pot.name())){
                check = Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() != -1;
                    }
                }, 500, 8);

                if(check){
                    PotatoPicker.sPots.addPot();
                }
            }
        }
        else{
            if(pot.valid()) {
                ctx.movement.step(pot);
                ctx.camera.turnTo(pot);
            }
        }
    }
}
