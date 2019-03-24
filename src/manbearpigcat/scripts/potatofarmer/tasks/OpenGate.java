package manbearpigcat.scripts.potatofarmer.tasks;

import manbearpigcat.scripts.potatofarmer.PotatoPicker;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Shan on 2016-08-17.
 */
public class OpenGate extends Task<ClientContext> {
    private static final int POTATO = 312;
    private static final int[] GATE = {45208, 45206};
    private static final Tile GATE_AREA_T1 = new Tile(3270, 3329, 0);
    private static final Tile GATE_AREA_T2 = new Tile(3245, 3309, 0);
    private static final Area area = new Area(GATE_AREA_T1, GATE_AREA_T2);
    private static final Tile after = new Tile(3262, 3320,0);
   // private static final Tile after1 = new Tile(3262, 3319, 0);
   // private static final Tile after2 = new Tile(3263, 3320, 0);
    private Random r = new Random();

    public OpenGate(ClientContext ctx){
        super(ctx);
    }

    //if pathway is blocked, open gate
    public boolean activate(){
        return !ctx.movement.reachable(ctx.players.local().tile(), after) && area.contains(ctx.players.local());
    }

    public void execute(){
        PotatoPicker.sPots.setState("Opening Gate.");
        final GameObject closedGate = ctx.objects.select(20).id(GATE).nearest().poll();
        if(closedGate.valid()) {
            final GameObject pot = ctx.objects.select(20).id(POTATO).nearest().poll();
            ctx.camera.turnTo(pot);
            //use random so camera doesn't always set pitch to exactly 45 deg. every time
            int pitch = r.nextInt(40,50); //angle the camera to make sure the mouse doesn't miss the gate
            ctx.camera.pitch(pitch);
            boolean check;
            check = Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return closedGate.interact("Open", closedGate.name());
                }
            }, 1000, 5);

            //reset overhead view
            if(check){
                pitch = r.nextInt(80,90);
                ctx.camera.pitch(pitch);
                /*
                int x = r.nextInt(0,4);
                switch(x){
                    case 0:
                        ctx.movement.step(after);
                        break;
                    case 1:
                        ctx.movement.step(after1);
                        break;
                    case 2:
                        ctx.movement.step(after2);
                        break;
                    default:
                        ctx.movement.step(after);
                        break;
                }
                */
            }
        }
    }
}
