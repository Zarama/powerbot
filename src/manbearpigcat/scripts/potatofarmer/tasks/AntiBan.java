package manbearpigcat.scripts.potatofarmer.tasks;

import manbearpigcat.scripts.potatofarmer.PotatoPicker;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Shan on 2016-08-17.
 */
public class AntiBan extends Task<ClientContext> {

    int seconds = 0;

    public AntiBan(ClientContext ctx){
        super(ctx);
    }

    public boolean activate(){

        Random rnd = new Random();
        int rand = rnd.nextInt(1,250);
        return rand == 25 || rand == 175;
    }

    public void execute(){
        Random rnd = new Random();
        int rand = rnd.nextInt(1,3);
        switch (rand){
            case 1:
                PotatoPicker.sPots.setState("Anti-Ban (Idle)");
                Condition.sleep(rnd.nextInt(5125, 8015));
                break;
            case 3:
                PotatoPicker.sPots.setState("Anti-Ban (Examine)");
                GameObject ex = ctx.objects.select(10).nearest().poll();
                ex.interact("Examine", ex.name());
                break;
            default:
                PotatoPicker.sPots.setState("Anti-Ban (Camera)");
                GameObject obj = ctx.objects.select().nearest().poll();
                ctx.camera.turnTo(obj);
        }
    }
}
