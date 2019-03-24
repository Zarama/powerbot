package VisionEyes.scripts.iSmithing.tasks;

import VisionEyes.scripts.iSmithing.resources.Bar;
import VisionEyes.scripts.iSmithing.resources.Consts;
import VisionEyes.scripts.iSmithing.resources.Ore;
import VisionEyes.scripts.iSmithing.resources.Util;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Component;
import VisionEyes.scripts.iSmithing.Task;

import java.util.Date;


public class Smith extends Task {

    private Util util = new Util(ctx);
    private Consts consts = new Consts();
    private Date last_animation = new Date();
    private Bar bar;
    private Ore ore;

    public Smith(ClientContext ctx, Bar bar) {
        super(ctx);
        this.bar = bar;
        this.ore = new Ore();
    }

    @Override
    public boolean activate() {
        return util.getItemsCount(ore.getQuantity(bar)) > 0;
    }

    @Override
    public void execute() {
        final GameObject furnace = ctx.objects.select().id(consts.FURNACE_ID).poll();
        final Component component = ctx.widgets.component(consts.SMELT_WIDGET, bar.getParentComponent()).component(consts.SMELT_CHILD_COMPONENT);
        if (furnace.inViewport()) {
            if(ctx.players.local().animation() == consts.SMELT_ANIMATION){
                last_animation = new Date();
            }else{
                if(util.checkTime(last_animation)){
                    furnace.interact("Smelt");
                    Condition.wait(component::visible, 150 , 10);
                    component.click();
                }
            }
        } else {
            ctx.movement.step(furnace);
            ctx.camera.turnTo(furnace);
        }
    }

    @Override
    public String getName() {
        return "Smith";
    }


}
