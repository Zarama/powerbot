package nagi.sorceress.script.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Camera;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.TileMatrix;

import nagi.sorceress.SorceressGarden;
import nagi.sorceress.misc.Cooldown;
import nagi.sorceress.type.ClientPhase;
import nagi.sorceress.type.PsuedoTile;

public class BankScript {

    public SorceressGarden instance;
    public ClientContext ctx;

    public List<PsuedoTile> path = new ArrayList<PsuedoTile>();

    public BankScript(SorceressGarden instance, ClientContext ctx) {
        this.instance = instance;
        this.ctx = ctx;
    }

    public void poll() {
        Random rand = new Random();
        this.adjustCamera();

        Tile tile = this.getPlayerPosition();

        if (this.instance.getClientPhase() == ClientPhase.BANKING) {
            if (this.instance.getSeason().inLobby(tile)) {
                if (!Cooldown.onCooldown("fountain_drink")) {
                    Cooldown.setCooldown("fountain_drink", (9 + rand.nextInt(4)) * 1000L);

                    GameObject fountain = this.ctx.objects.select().id(12941).poll();
                    if (!fountain.inViewport()) {
                        this.ctx.camera.turnTo(fountain);
                    }

                    fountain.interact("Drink-from");
                    this.generateBankPath();
                }
            } else if (this.inHouse()) {
                if (this.ctx.movement.energyLevel() > 50 && !this.ctx.movement.running()) {
                    this.ctx.movement.running(true);
                    return;
                }

                final int[] doorBounds = { 4, 124, -240, 0, 0, 16 };
                GameObject door = this.ctx.objects.select().id(1535).each(Interactive.doSetBounds(doorBounds)).poll();

                if (door != null && door.valid()) {
                    if (!Cooldown.onCooldown("open_door")) {
                        Cooldown.setCooldown("open_door", (4 + rand.nextInt(2)) * 1000L);
                        if (!door.inViewport()) {
                            this.ctx.camera.turnTo(door);
                        }

                        door.interact("Open");
                    }
                } else {
                    this.pathToBank();
                }
            } else if (this.inShanty()) {
                this.pathToBank();
            }
        } else if (this.instance.getClientPhase() == ClientPhase.RETURNING) {
            if (this.instance.getSeason().inLobby(tile)) {
                this.instance.setClientPhase(ClientPhase.COLLECTING);
            } else if (this.inShanty()) {
                this.pathToGarden();
            }
        }
    }

    public void adjustCamera() {
        Random rand = new Random();
        Camera cam = this.ctx.camera;

        if (this.instance.getClientPhase() == ClientPhase.BANKING) {
            if (!Cooldown.onCooldown("camera_adjust_game_banking")) {
                Cooldown.setCooldown("camera_adjust_game_banking", (45 + rand.nextInt(30)) * 1000L);

                int i = rand.nextInt(3);

                if (i == 0) {
                    cam.angle(180 + rand.nextInt(20) - 10);
                    cam.pitch(80 + rand.nextInt(20) - 10);
                } else if (i == 1) {
                    cam.angle(130 + rand.nextInt(20) - 10);
                    cam.pitch(80 + rand.nextInt(20) - 10);
                } else {
                    cam.angle(300 + rand.nextInt(20) - 10);
                    cam.pitch(80 + rand.nextInt(20) - 10);
                }
            }
        } else if (this.instance.getClientPhase() == ClientPhase.RETURNING) {
            if (!Cooldown.onCooldown("camera_adjust_game_return")) {
                Cooldown.setCooldown("camera_adjust_game_return", (45 + rand.nextInt(30)) * 1000L);

                if (cam.pitch() != 99) {
                    cam.pitch(true);
                }

                if (rand.nextBoolean()) {
                    cam.angle(320 + rand.nextInt(20) - 10);
                } else {
                    cam.angle(30 + rand.nextInt(20) - 10);
                }
            }
        }
    }

    public void pathToGarden() {
        Tile pos = this.getPlayerPosition();
        Random rand = new Random();

        if (pos.x() >= 3315) {
            final int[] doorBounds = { 4, 124, -240, 0, 0, 16 };
            GameObject door = this.ctx.objects.select().id(1535).each(Interactive.doSetBounds(doorBounds)).poll();

            if (door != null && door.valid()) {
                if (!Cooldown.onCooldown("open_door_back")) {
                    Cooldown.setCooldown("open_door_back", (3 + rand.nextInt(2)) * 1000L);
                    if (!door.inViewport()) {
                        this.ctx.camera.turnTo(door);
                    }

                    door.interact("Open");
                }
            } else {
                Npc apprentice = this.ctx.npcs.select().id(1808).poll();
                if (apprentice != null && apprentice.inViewport() && !Cooldown.onCooldown("interact_apprentice") && !Cooldown.onCooldown("enter_house")) {
                    Cooldown.setCooldown("interact_apprentice", (5 + new Random().nextInt(2)) * 1000L);
                    this.ctx.npcs.select().id(1808).poll().interact("Teleport");
                } else {
                    if (!Cooldown.onCooldown("interact_apprentice") && !Cooldown.onCooldown("enter_house")) {
                        Cooldown.setCooldown("enter_house", (1 + new Random().nextInt(2)) * 1000L);
                        Random r = new Random();
                        int x = r.nextInt(3) - 1;
                        int y = r.nextInt(3) - 1;
                        Tile tile = new Tile(3321 + x, 3139 + y);
                        tile.matrix(ctx).interact("Walk here");
                        this.ctx.movement.step(tile);
                    }
                }
                // if (!this.inHouse()) {
                // if (!Cooldown.onCooldown("enter_house")) {
                // Cooldown.setCooldown("enter_house", (1 + new Random().nextInt(2)) * 1000L);
                // Random r = new Random();
                // int x = r.nextInt(3) - 1;
                // int y = r.nextInt(3) - 1;
                // Tile tile = new Tile(3321 + x, 3139 + y);
                // tile.matrix(ctx).interact("Walk here");
                // this.ctx.movement.step(tile);
                // }
                // } else {
                // if (!Cooldown.onCooldown("interact_apprentice")) {
                // Cooldown.setCooldown("interact_apprentice", (5 + new Random().nextInt(2)) * 1000L);
                // this.ctx.npcs.select().id(1808).poll().interact("Teleport");
                // }
                // }
            }
        } else {
            int found = -1;
            for (int i = this.path.size() - 1; i >= 0; i--) {
                Tile checkTile = this.path.get(i);
                TileMatrix matrix = checkTile.matrix(this.ctx);
                if (matrix.onMap()) {
                    found = i;
                    break;
                }
            }

            if (found == -1) {
                this.generateReturnPath();
            } else {
                if (!Cooldown.onCooldown("movement_click")) {
                    Cooldown.setCooldown("movement_click", 1 + rand.nextInt(5) * 500L);
                    Tile tile = this.path.get(found);
                    this.ctx.movement.step(new Tile(tile.x() + (int) Math.round(rand.nextGaussian()), tile.y() + (int) Math.round(rand.nextGaussian())));
                }

                for (int i = 0; i < found; i++)
                    this.path.remove(0);
            }
        }
    }

    public void pathToBank() {
        if (this.ctx.movement.energyLevel() >= 50 && !this.ctx.movement.running()) {
            this.ctx.movement.running(true);
            return;
        }

        if (this.ctx.bank.open()) {
            if (this.ctx.inventory.select().count() > 0) {
                if (!Cooldown.onCooldown("bank_deposit")) {
                    this.ctx.bank.depositInventory();
                }
            } else {
                this.generateReturnPath();
                Cooldown.setCooldown("camera_adjust_game_return", new Random().nextInt(4) * 1000L);
                this.instance.setClientPhase(ClientPhase.RETURNING);
            }
        } else {
            Tile pos = this.getPlayerPosition();
            Random rand = new Random();

            if (pos.y() <= 3131) {
                if (!this.ctx.bank.inViewport()) {
                    if (!Cooldown.onCooldown("bank_move_closer")) {
                        Cooldown.setCooldown("bank_move_closer", (5 + new Random().nextInt(2)) * 1000L);
                        Random r = new Random();
                        int x = r.nextInt(3) - 1;
                        int y = r.nextInt(3) - 1;
                        Tile tile = new Tile(3307 + x, 3120 + y);
                        this.ctx.movement.step(tile);
                    }

                    this.ctx.camera.turnTo(ctx.bank.nearest());
                }

                if (!Cooldown.onCooldown("bank_open")) {
                    Cooldown.setCooldown("bank_open", (5 + rand.nextInt(3)) * 1000L);
                    ctx.bank.open();
                }
            } else {
                int found = -1;
                for (int i = this.path.size() - 1; i >= 0; i--) {
                    Tile checkTile = this.path.get(i);
                    TileMatrix matrix = checkTile.matrix(this.ctx);
                    if (matrix.onMap()) {
                        found = i;
                        break;
                    }
                }

                if (found == -1) {
                    this.generateBankPath();
                } else {
                    if (!Cooldown.onCooldown("movement_click")) {
                        Cooldown.setCooldown("movement_click", 1 + rand.nextInt(5) * 500L);
                        Tile tile = this.path.get(found);
                        this.ctx.movement.step(new Tile(tile.x() + (int) Math.round(rand.nextGaussian()), tile.y() + (int) Math.round(rand.nextGaussian())));
                    }

                    for (int i = 0; i < found; i++)
                        this.path.remove(0);
                }
            }
        }
    }

    public void generateBankPath() {
        this.path.clear();
        this.path.addAll(this.generatePath(new Tile(3321, 3139), new Tile(3304, 3135)));
        this.path.addAll(this.generatePath(new Tile(3304, 3135), new Tile(3308, 3120)));

    }

    public void generateReturnPath() {
        this.generateBankPath();
        Collections.reverse(this.path);
    }

    public List<PsuedoTile> generatePath(final Tile from, final Tile to) {
        List<PsuedoTile> path = new ArrayList<PsuedoTile>();

        if (to.x() == from.x()) {
            int yDiff = Math.abs(from.y() - to.y());
            if (to.y() > from.y()) {
                for (int i = 1; i <= yDiff; i++)
                    path.add(new PsuedoTile(from.x(), from.y() + i));
            } else {
                for (int i = 1; i < yDiff; i++)
                    path.add(new PsuedoTile(from.x(), from.y() - i));
            }
        } else {
            double numerator = to.y() - from.y();
            double denominator = to.x() - from.x();

            double slope = numerator / denominator;
            double sum = from.y() - (slope * from.x());

            if (to.x() > from.x()) {
                for (int x = from.x() + 1; x <= to.x(); x++) {
                    int y = (int) Math.round((slope * x) + sum);
                    path.add(new PsuedoTile(x, y));
                }
            } else {
                for (int x = from.x() - 1; x >= to.x(); x--) {
                    int y = (int) Math.round((slope * x) + sum);
                    path.add(new PsuedoTile(x, y));
                }
            }

            if (to.y() > from.y()) {
                for (int y = from.y() + 1; y <= to.y(); y++) {
                    int x = (int) Math.round((y - sum) / slope);
                    PsuedoTile tile = new PsuedoTile(x, y);
                    if (!path.contains(tile))
                        path.add(tile);
                }
            } else {
                for (int y = from.y() - 1; y >= to.y(); y--) {
                    int x = (int) Math.round((y - sum) / slope);
                    PsuedoTile tile = new PsuedoTile(x, y);
                    if (!path.contains(tile))
                        path.add(tile);
                }
            }

            Collections.sort(path, new Comparator<Tile>() {
                @Override
                public int compare(Tile a, Tile b) {
                    if (a.x() != b.x())
                        return Integer.compare(a.x(), b.x());
                    else if (to.y() > from.y())
                        return Integer.compare(a.y(), b.y());
                    else
                        return -1 * Integer.compare(a.y(), b.y());
                }
            });

            if (to.x() < from.x())
                Collections.reverse(path);
        }
        return path;
    }

    public Tile getPlayerPosition() {
        return this.ctx.players.local().tile();
    }

    public boolean inHouse() {
        Tile pos = this.getPlayerPosition();
        return pos.x() >= 3318 && pos.x() <= 3324 && pos.y() >= 3137 && pos.y() <= 3141;
    }

    public boolean inShanty() {
        Tile pos = this.getPlayerPosition();
        return pos.x() >= 3298 && pos.x() <= 3331 && pos.y() >= 3118 && pos.y() <= 3148;
    }
}
