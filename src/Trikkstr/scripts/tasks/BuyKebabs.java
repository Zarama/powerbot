package Trikkstr.scripts.tasks;

import Trikkstr.scripts.goblin_killer.Constants;
import Trikkstr.scripts.goblin_killer.GoblinKiller;
import Trikkstr.scripts.goblin_killer.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;


public class BuyKebabs extends Task
{
    private final Component clickToContinue = ctx.widgets.widget(231).component(2);
    private final Component clickToContinue2 = ctx.widgets.widget(217).component(2);
    private final Component yes = ctx.widgets.widget(219).component(0).component(2);

    private int trigger;

    private Npc karim;

    public BuyKebabs(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.inventory.select().id(Constants.COINS).count(true) > 10
                && ctx.players.local().tile().x() > Constants.GATE_SOUTH_SIDE
                && GoblinKiller.getBanked();
    }

    @Override
    public void execute()
    {
        GoblinKiller.setStatus("Buying Kebabs");
        karim = detectKarim();
        //if(not near karim)
        //  step to karim
        System.out.println("Executing BuyKebabs.");

        if(ctx.players.local().tile().distanceTo(karim) > 4)
        {
            System.out.println("Walking over to Karim.");
            stepToKarim();
        }

        //if(coins > 10)
        //  buy kebab
        if(ctx.inventory.select().id(Constants.COINS).count(true) > 10)
        {
            System.out.println("Needs to buy a Kebab.");
            makePurchase();
        }
    }

    private Npc detectKarim()
    {
        return ctx.npcs.select().id(Constants.KARIM).poll();
    }

    private void stepToKarim()
    {
        GoblinKiller.setSubstatus("Walking to Karim");

        karim = detectKarim();

        ctx.movement.step(Constants.karimsTile);

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return ctx.players.local().inMotion();
            }
        }, 1000, 3);

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return !ctx.players.local().inMotion();
            }
        }, 1000, 8);

        System.out.println("Turning camera to Karim.");
        ctx.camera.turnTo(karim);
    }

    private void makePurchase()
    {
        GoblinKiller.setSubstatus("Making purchase");

        karim = detectKarim();

        System.out.println("Talking to Karim.");
        karim.interact("Talk-to");

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return clickToContinue.visible();
            }
        }, 1000, 6);

        clickToContinue.click();

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return yes.visible();
            }
        }, 1000, 6);

        trigger = ctx.inventory.select().count();

        yes.click();

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return clickToContinue2.visible();
            }
        }, 1000, 6);

        clickToContinue2.click();

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return ctx.inventory.select().count() != trigger;
            }
        }, 1000, 6);

        if(ctx.inventory.select().id(Constants.COINS).count(true) == 10)
        {
            GoblinKiller.setBanked(false);
        }
    }
}
