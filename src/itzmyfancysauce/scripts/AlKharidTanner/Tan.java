package itzmyfancysauce;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class Tan extends Task {
    private WidgetManager widgetManager;
    private Area tannerArea = new Area(new Tile(3278, 3194), new Tile(3270, 3189));
    int hideID = AlKharidTanner.hideID;
    int leatherID = AlKharidTanner.leatherID;
    final int ELLIS_ID = 3231;

    public Tan(ClientContext ctx) {
        super(ctx);
        widgetManager = new WidgetManager(ctx);
    }

    @Override
    public boolean activate(String storageDirectory) {
        Tile playerTile = ctx.players.local().tile();

        //If player is within bank area
        if(tannerArea.getCentralTile().distanceTo(playerTile) <= 10 && ctx.inventory.select().id(leatherID).isEmpty() && !widgetManager.isTradeVisible()) {
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        System.out.println("Tanning!");
        Npc ellis = ctx.npcs.select().id(ELLIS_ID).nearest().poll();

        //if(!ellis.inViewport()) {
            ctx.camera.turnTo(ellis);
        //}

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ellis.interact("Trade");
            }
        });

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        });

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return widgetManager.isTradeVisible();
            }
        });

        Condition.sleep(Random.nextInt(400, 800));

        try {
            widgetManager.tanHide();
        } catch(ArrayIndexOutOfBoundsException e) {
            Condition.sleep(Random.nextInt(700, 1200));
            //widgetManager.tanHide();
        }
        //Condition.sleep(Random.nextInt(700, 1200));
    }
}
