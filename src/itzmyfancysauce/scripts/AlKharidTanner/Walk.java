package itzmyfancysauce;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.util.Calendar;
import java.util.concurrent.Callable;

public class Walk extends Task {
    //Path from tanner to bank
    private static Tile[] path = new Tile[]{new Tile(3280, 3191), new Tile(3282, 3186), new Tile(3280, 3180), new Tile(3277, 3174), new Tile(3275, 3167)};
    private Area bankArea = new Area(new Tile(3272, 3170), new Tile(3269, 3163));
    private Area tannerArea = new Area(new Tile(3278, 3194), new Tile(3270, 3189));
    private static TilePath tannerToBank;
    private static TilePath bankToTanner;
    private final int TANNER_DOOR_ID = 1535;
    int hideID = AlKharidTanner.hideID;
    int leatherID = AlKharidTanner.leatherID;
    final int ELLIS_ID = 3231;
    final int[] closedDoorBounds = {128, 96, -204, -20, 92, 16};

    public Walk(ClientContext ctx) {
        super(ctx);
        bankToTanner = ctx.movement.newTilePath(path).reverse();
        tannerToBank = ctx.movement.newTilePath(path);
    }

    @Override
    public boolean activate(String storageDirectory) {
        if (bankArea.contains(ctx.players.local().tile()) && !ctx.inventory.select().id(hideID).isEmpty()) {
            return true;
        } else if (tannerArea.contains(ctx.players.local().tile()) && !ctx.inventory.select().id(leatherID).isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        System.out.println("Walking!");
        if (!ctx.inventory.select().id(hideID).isEmpty()) {
            bankToTanner();
        } else if (!ctx.inventory.select().id(leatherID).isEmpty()) {
            tannerToBank();
        }
    }

    public void bankToTanner() {
        bankToTanner.randomize(2, 2);
        while (ctx.players.local().tile().distanceTo(bankToTanner.end()) >= 4) {
            if(!ctx.movement.reachable(ctx.players.local(), bankToTanner.next())) {
                bankToTanner.randomize(2, 2);
            }
            bankToTanner.traverse();

        }
        handleDoor();
        ctx.movement.step(randomWithinNPC());
    }

    public void tannerToBank() {
        AlKharidTanner.totalTanned += ctx.inventory.select().id(leatherID).count();
        handleDoor();
        tannerToBank.randomize(2, 2);

        while (ctx.players.local().tile().distanceTo(tannerToBank.end()) >= 4) {
            if(!ctx.movement.reachable(ctx.players.local(), tannerToBank.next())) {
                tannerToBank.randomize(2, 2);
            }
            tannerToBank.traverse();
        }
        ctx.movement.step(bankArea.getRandomTile());

        randomAfk();
    }

    public void handleDoor() {
        GameObject door = ctx.objects.select().id(1535).each(Interactive.doSetBounds(closedDoorBounds)).nearest().poll();
        //GameObject door = ctx.objects.select().id(TANNER_DOOR_ID).nearest().poll();
        System.out.println("Door id: " + door.id());
        System.out.println("Door valid: " + door.valid());
        if (door.valid() && door.tile().x() == 3277 && door.tile().y() == 3191) {
            AlKharidTanner.currentTask = "Opening Door";
            ctx.camera.turnTo(door);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return door.interact("Open");
                }
            });


            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !door.valid();
                }
            });
        }

        AlKharidTanner.currentTask = "Walking";
    }

    public void randomAfk() {
        if ((Calendar.getInstance().getTimeInMillis() - AlKharidTanner.startTime.getTimeInMillis()) / 1000 / 60 >= 10) {
            if (Random.nextInt(1, 100) >= 98) {
                int sleepTime = Random.nextInt(20, 100);
                AlKharidTanner.currentTask = "AFK for " + sleepTime + " seconds";
                Condition.sleep(sleepTime * 1000);
            }
        }
    }

    public Tile randomWithinNPC() {
        Tile tile;
        Npc ellis = ctx.npcs.select().id(ELLIS_ID).nearest().poll();
        do {
            tile = tannerArea.getRandomTile();
        } while (tile.distanceTo(ellis) >= 2);

        return tile;
    }
}
