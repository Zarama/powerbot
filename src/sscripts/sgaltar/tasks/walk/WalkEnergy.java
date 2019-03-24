package sscripts.sgaltar.tasks.walk;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.Task;

import java.util.concurrent.Callable;

public class WalkEnergy extends Task{
    public WalkEnergy(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final GameObject fountain = ctx.objects.select().name("Ornate rejuvenation pool").nearest().poll();
        final GameObject fountain2 = ctx.objects.select().name("Pool of Rejuvenation").nearest().poll();
        final GameObject fountain3 = ctx.objects.select().name("Fancy rejuvenation pool").nearest().poll();
        return ctx.movement.energyLevel() < 50 && ctx.inventory.select().count() == 28 && (fountain.valid() || fountain2.valid() || fountain3.valid());
    }

    @Override
    public void execute() {
        final GameObject fountain = ctx.objects.select().name("Ornate rejuvenation pool").nearest().poll();
        final GameObject fountain2 = ctx.objects.select().name("Pool of Rejuvenation").nearest().poll();
        final GameObject fountain3 = ctx.objects.select().name("Fancy rejuvenation pool").nearest().poll();

        SGAltar.status = "Refreshing Energy";
        if (fountain.inViewport()) {
            if (fountain.interact("Drink")) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.energyLevel() == 100;
                    }
                }, 500, 4);
            }
        }else if (fountain2.inViewport()){
            if (fountain2.interact("Drink")) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.energyLevel() == 100;
                    }
                }, 500, 4);
            }
        }else if (fountain3.inViewport()) {
            if (fountain3.interact("Drink")) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.energyLevel() == 100;
                    }
                }, 500, 4);
            }
        }
        else {
            ctx.camera.turnTo(fountain);
            ctx.movement.step(fountain);
            ctx.camera.turnTo(fountain2);
            ctx.movement.step(fountain2);
            SGAltar.status = "searching for fountain";
        }
    }
}
