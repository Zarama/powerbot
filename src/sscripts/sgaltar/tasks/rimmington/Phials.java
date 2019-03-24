package sscripts.sgaltar.tasks.rimmington;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.Task;

import java.util.concurrent.Callable;

public class Phials extends Task {

    public Phials(ClientContext arg0) {
        super(arg0);
    }

    final GameObject portal = ctx.objects.select().id(4525).nearest().poll();
    final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();
    public boolean inHouse() {
        if (portal.inViewport() || altar.inViewport() || ctx.client().getFloor() == 1){
            return true;
        }else {return false;}
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() != 28 && ctx.client().getFloor() == 0 && !ctx.players.local().inMotion() && !inHouse();
    }

    @Override
    public void execute() {
        SGAltar.status = "Interacting with Phials";
        final Tile tileNPC = new Tile(2950, 3214, 0);
        final int[] bounds = {-28, 24, -192, -136, -20, 16};
        final Npc phials = ctx.npcs.select().id(1614).each(Interactive.doSetBounds(bounds)).nearest().poll();
        final int bonenid = SGAltar.data.getBone_NID();
        final Item notedBones = ctx.inventory.select().id(bonenid).first().poll();
        final Item gold = ctx.inventory.select().id(995).first().poll();

        if (notedBones.stackSize() < 28){
            ctx.controller.stop();
            System.out.println("Out of Bones - Script ended");
        }
        if (gold.stackSize() < 100) {
            ctx.controller.stop();
            System.out.println("Out of Gold - Script ended");
        }

        if (phials.inViewport()){
            if (ctx.inventory.select().count() <= 21){
               if (notedBones.interact("Use")) {
                 SGAltar.status = "Using noted bones";
                    if (phials.interact("Use")) {
                       Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                              return ctx.widgets.widget(219).component(0).component(3).visible();
                         }
                    }, 750, 5);
                     if (ctx.widgets.widget(219).component(0).component(3).visible()) {
                           ctx.input.sendln("3");
                             Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                   return !ctx.widgets.component(219, 3).visible();
                              }
                        }, 500, 3);
                      }
                 }
               }
            }else if (ctx.inventory.select().count() == 22
                    || ctx.inventory.select().count() == 23
                    || ctx.inventory.select().count() == 24
                    || ctx.inventory.select().count() == 25
                    || ctx.inventory.select().count() == 26
                    || ctx.inventory.select().count() == 27){
                if (notedBones.interact("Use")) {
                    SGAltar.status = "Using noted bones";
                        if (phials.interact("Use")) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.widgets.widget(219).component(0).component(3).visible();
                            }
                        }, 750, 5);
                        if (ctx.widgets.widget(219).component(0).component(3).visible()) {
                            ctx.input.sendln("2");
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return !ctx.widgets.component(219, 3).visible();
                                }
                            }, 500, 3);
                        }
                    }
                }
            }
        } else {
            SGAltar.status = "Looking for Phials";

            ctx.camera.turnTo(tileNPC);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return phials.inViewport();
                }
            },1000, 3);
            if (!phials.inViewport() && tileNPC.distanceTo(ctx.players.local()) > 3) {
                ctx.movement.step(tileNPC);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return phials.inViewport();
                    }
                }, 1000, 3);
            }
        }
    }
}
