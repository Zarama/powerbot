package Vaynex.tasks;

import Vaynex.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

/**
 * Created by Me on 19/06/2017.
 */
public class Cook extends Task {

    GameObject fire = ctx.objects.select().id(26185).nearest().poll();
    long lastChecked;
    public Cook(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return
        //if we have raw fish in invent.
        ctx.inventory.select().count()==28 && ctx.inventory.select().id(331,335).count()>=1;
    } //or when our invent is full. but we have =>14 331

    @Override
    public void execute() {
        cookfish();
    }


    private void cookfish() {

        if (ctx.players.local().animation() != -1) {
            lastChecked = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - lastChecked > Random.nextInt(1000, 1700)) {
            final GameObject fire = ctx.objects.select().id(26185).nearest().poll();
            if(!fire.inViewport())
                gotoTile(fire.tile());

            final Item raw = ctx.inventory.select().id(335, 331).poll();
            final Component cookingInterface = ctx.widgets.component(307, 5);

            openInventory();

            if (!cookingInterface.visible() && raw.interact("Use", raw.name())
                    && fire.interact("Use", fire.name())) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return cookingInterface.visible();
                    }
                }, 250, 10);
            }
            if (cookingInterface.visible()) {
                cookingInterface.interact("Cook All");
                Condition.sleep(30000);
            }
        }
    }

    public void gotoTile(Tile spot){
        ctx.movement.step(spot);
        ctx.camera.turnTo(spot);
        Condition.sleep(Random.nextInt(300,500));
    }




           /* raw.interact("Use");
            //fire.click();
            fire.interact("Fire");
            fire.interact("Use", fire.name());

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return cookingInterface.visible();
                }
            }, 200, 20);

            if (cookingInterface.visible()) {
                cookingInterface.interact("Cook All");

               // Condition.sleep(50000);

            }




        } else {
            ctx.camera.turnTo(fire);
        }

    }
*/
    public void openInventory(){
        if(!ctx.inventory.component().visible()){
            ctx.widgets.widget(548).component(50).click();

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.component().visible();
                }
            }, 250, 10);
        }
    }


}

