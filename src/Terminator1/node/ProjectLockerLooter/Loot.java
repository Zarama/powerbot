package Terminator1.node.ProjectLockerLooter;

/**
 * Created by Genoss on 12/25/2016:11:02 AM
 */
import Terminator1.api.Node;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import Terminator1.ProjectLockerLooter;

import java.util.concurrent.Callable;

public class Loot extends Node<ClientContext> {

    private final int[] bounds1 = {-24, 28, -180, -100, -80, -64},bounds0 = {-16, 28, -196, -112, 64, 84};
    private int[] bounds = new int[6];
    private final int lockerid = 7235,deadlocker = 7238;
    private long lastClick = 0;
    private GameObject locker = null;
    private ProjectLockerLooter pll = null;

    public Loot(ClientContext ctx, ProjectLockerLooter mc,String name)
    {
        super(ctx,name);
        pll = mc;
    }

    @Override
    public void executeBlock() {
        if(pll.getSpotPos() == 0 || pll.getSpotPos() == 1)
            bounds = bounds0;
        else
            bounds = bounds1;
        pll.setStatus("Cracking...");
        locker = ctx.objects.select(2).id(lockerid).each(Interactive.doSetBounds(bounds)).nearest().poll();
        if(ctx.inventory.selectedItemIndex()!=-1)
            ctx.inventory.select().shuffle().poll().click();
        if(ctx.objects.select(2).id(deadlocker).nearest().poll().id() != -1)
            pll.setClick(false);
        if (ctx.players.local().animation() == -1 && (!pll.getClick()||(pll.getRunTime()-lastClick)>10000) && ctx.objects.select(2).id(deadlocker).nearest().poll().id() == -1) {
            ctx.camera.turnTo(locker);
            locker.interact("Crack");
            lastClick = pll.getRunTime();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return pll.getClick();
                }
            },100,10);
        }
    }

    @Override
    public boolean isReady() {
        return (!ctx.bank.opened() && (ctx.inventory.select().id(pll.getStethID()).count() > 1 && !ctx.players.local().inMotion()) && (ctx.inventory.select().id(pll.getFoodID()).count() > 0)) || ((ctx.skills.level(Constants.SKILLS_HITPOINTS) > pll.getHealthLimt()) && (ctx.players.local().tile().distanceTo(pll.getSpot()) == 0)) && !(ctx.inventory.select().count() == 28);
    }

}
