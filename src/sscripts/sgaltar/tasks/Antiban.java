package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;
import sscripts.sgaltar.SGAltar;

import java.awt.*;
import java.util.concurrent.Callable;

public class Antiban extends Task {

    public Antiban(ClientContext arg0) {
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
        Random r = new Random();
        int ra = r.nextInt(1,250);
        return inHouse() &&  (ra == 25 || ra == 50 || ra == 75);
    }


    @Override
    public void execute() {
        int r = Random.nextInt(0,10);
        switch (r){
            case 0:
                SGAltar.status = "Antiban#1";
                Condition.sleep(Random.nextInt(1500,4500));
                System.out.println("Antipattern-Case0:Sleeping");
                break;
            case 1:
                SGAltar.status = "Antiban#2";
                ctx.input.move(-100,Random.nextInt(100,350));
                Condition.sleep(Random.nextInt(2500,5500));
                System.out.println("Antipattern-Case1:Moving mouse offscreen");

                break;
            case 2:
                SGAltar.status = "Antiban#3";
                System.out.println("Antipattern-Case2:Hovering a other Player");

                Player pl = ctx.players.select().nearest().poll();
                pl.hover();
                Condition.sleep(Random.nextInt(500,2500));
                break;
            case 3:
                SGAltar.status = "Antiban#4";
                System.out.println("Antipattern-Case3:Moving mouse offscreen");

                int x = Random.nextInt(-500, -100);
                int y = Random.nextInt(-500, -100);
                ctx.input.move(x, y);
                ctx.input.defocus();

                Condition.sleep(Random.nextInt(5000, 13000));

                ctx.input.focus();
                x = Random.nextInt(16, 512);
                y = Random.nextInt(45, 334);
                ctx.input.move(x, y);
                break;
            case 4:
                SGAltar.status = "Antiban#5";
                ctx.camera.turnTo(ctx.players.select().nearest().poll());

                break;
            case 5:
                SGAltar.status = "Antiban#6";
                System.out.println("Antipattern-Case5:Random mouse moving");

                int maxDistance = 0;
                int minDistance = 0;
                double xvec = Math.random();
                if (Random.nextInt(0, 2) == 1) {
                    xvec = -xvec;
                }
                double yvec = Math.sqrt(1 - xvec * xvec);
                if (Random.nextInt(0, 2) == 1) {
                    yvec = -yvec;
                }
                double distance = maxDistance;
                Point p = ctx.input.getLocation();
                int maxX = (int) Math.round(xvec * distance + p.x);
                distance -= Math.abs((maxX - Math.max(0,
                        Math.min(ctx.game.dimensions().getWidth(), maxX)))
                        / xvec);
                int maxY = (int) Math.round(yvec * distance + p.y);
                distance -= Math.abs((maxY - Math.max(0,
                        Math.min(ctx.game.dimensions().getHeight(), maxY)))
                        / yvec);
                if (distance < minDistance) {
                    return;
                }
                distance = Random.nextInt(minDistance, (int) distance);
                ctx.input.move((int) (xvec * distance) + p.x, (int) (yvec * distance) + p.y);
                break;
            case 6:
                SGAltar.status = "Antiban#7";
                System.out.println("Antipattern-Case6:Hovering Prayer EXP");
                if (inHouse() && !ctx.players.local().inMotion()){
                    ctx.widgets.widget(548).component(49).click();
                    Condition.sleep(Random.nextInt(300, 500));
                    if (ctx.widgets.widget(320).component(5).valid()) {
                        ctx.widgets.widget(320).component(5 ).hover();
                        Condition.sleep(Random.nextInt(2500, 5000));
                        ctx.widgets.widget(548).component(51).click();
                    }
                }
                break;
            case 7:
                SGAltar.status = "Antiban#8";
                System.out.println("Antipattern-Case7:Activating quickprayer");
                if (ctx.widgets.widget(160).component(19).valid() && ctx.prayer.prayerPoints() < 20){
                    ctx.widgets.widget(160).component(19).click();
                }
                break;
            case 8:
                SGAltar.status = "Antiban#9";
                System.out.println("Antipattern-Case8:Refreshing Prayer");
                if (ctx.prayer.prayerPoints() < 20) {
                    final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();
                    if (altar.inViewport()){
                        if (altar.interact("Pray")){
                            Condition.wait(new Callable<Boolean>() {
                                @Override
                                public Boolean call() throws Exception {
                                    return ctx.prayer.prayerPoints() > 20;
                                }
                            },250,1000);
                        }
                    }
                }

                break;
            case 9:
                SGAltar.status = "Antiban#10";
                System.out.println("Antipattern-Case9:Changing mouse speed");
                ctx.input.speed(Random.nextInt(65,100));

                break;
            case 10:
                SGAltar.status = "Antiban#11";
                System.out.println("Antipattern-Case10:Hovering Host");
                final Player host = ctx.players.select().nearest().name(SGAltar.playername).poll();
                if (host.inViewport()){
                    host.hover();
                    Condition.sleep(Random.nextInt(500,2500));
                }
                break;

                default:
                    SGAltar.status = "Antiban-fefault";
                    System.out.println("Antipattern-Case-default:Changing mouse speed");
                    ctx.input.speed(Random.nextInt(65,100));

        }

    }
}
