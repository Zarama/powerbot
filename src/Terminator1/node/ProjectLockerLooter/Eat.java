package Terminator1.node.ProjectLockerLooter;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Genoss on 12/25/2016:11:18 AM
 */
import Terminator1.api.Node;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import Terminator1.ProjectLockerLooter;

public class Eat extends Node<ClientContext>{

    private ProjectLockerLooter pll = null;
    public Eat(ClientContext ctx, ProjectLockerLooter mc, String name) {
        super(ctx,name);
        pll = mc;
    }

    @Override
    public void executeBlock() {
        long tmp,tmp1;
        final Item item = ctx.inventory.select().id(pll.getFoodID()).poll();
        pll.setStatus("Eating...");
        if(!ctx.game.tab().name().equals(Game.Tab.INVENTORY.name()))
            ctx.game.tab(Game.Tab.INVENTORY);
        item.click();
        tmp = pll.getRunTime();
        tmp1 = Random.nextInt(700,1200);
       do {
            Condition.sleep(Random.nextInt(75,150));
        } while(item.valid()&&!((pll.getRuntime()-tmp)>tmp1));
    }

    @Override
    public boolean isReady() {
        return ((!(ctx.skills.level(Constants.SKILLS_HITPOINTS) > pll.getHealthLimt()) && (ctx.inventory.select().id(pll.getFoodID()).count() > 0) && !(ctx.bank.opened())) || ((ctx.inventory.select().id(pll.getFoodID()).count()>0)&&(pll.getEatFood())&&(ctx.inventory.select().count()==28)));
    }
}
