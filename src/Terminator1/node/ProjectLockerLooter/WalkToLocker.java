package Terminator1.node.ProjectLockerLooter;

/**
 * Created by Genoss on 12/26/2016:4:40 PM
 */
import Terminator1.api.Node;
import org.powerbot.script.rt4.ClientContext;
import Terminator1.ProjectLockerLooter;

public class WalkToLocker extends Node<ClientContext>{
    
    private ProjectLockerLooter pll = null;

    public WalkToLocker(ClientContext ctx, ProjectLockerLooter mc,String name) {
        super(ctx,name);
        pll = mc;
    }

    @Override
    public void executeBlock() {
        pll.setStatus("Walking to the locker...");
        ctx.movement.step(pll.getSpot());
    }

    @Override
    public boolean isReady() {
        return (!ctx.players.local().inMotion()) && (ctx.players.local().tile().distanceTo(pll.getSpot()) > 0) && (ctx.inventory.select().count() > 0)&& ((ctx.inventory.select().count()!=28)||(pll.getEatFood()&&ctx.inventory.select().count()==28));
    }
}
