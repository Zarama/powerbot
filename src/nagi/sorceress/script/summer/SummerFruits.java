package nagi.sorceress.script.summer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Camera;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Player;
import org.powerbot.script.rt4.TileMatrix;

import nagi.sorceress.misc.Cooldown;
import nagi.sorceress.script.Season;
import nagi.sorceress.script.SeasonScript;
import nagi.sorceress.type.ClientPhase;
import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public class SummerFruits extends SeasonScript {

    public ClientContext ctx;

    public SummerFruits(Season season) {
        super(season);
        this.ctx = this.season.getInstance().ctx();
    }

    @Override
    public void poll() {
        if (Cooldown.onCooldown("script_cooldown"))
            return;

        if (this.ctx.inventory.select().count() >= 28) {
            this.season.getInstance().setClientPhase(ClientPhase.BANKING);
            return;
        }

        final Random rand = new Random();
        final Player p = this.season.getPlayer();
        final Tile pos = p.tile();

        int energy = this.ctx.movement.energyLevel();
        if (!p.inMotion()) {
            if (this.ctx.movement.running() && energy <= this.season.getRunDisable()) {
                this.ctx.movement.running(false);
                this.season.resetEnergies(rand);
                return;
            } else if (!this.ctx.movement.running() && energy >= this.season.getRunEnable()) {
                this.ctx.movement.running(true);
                this.season.resetEnergies(rand);
                return;
            }
        }

        if (this.season.inLobby(pos)) {
            if (this.ctx.players.local().inMotion()) {
                if (!this.verifyUpdateCam(this.ctx.camera, 20, 320, 340, 360, 0, 20, 40)) {
                    return;
                }
            }

            if (this.season.getGateLocation().distanceTo(pos) <= 3) {
                GameObject gate = this.ctx.objects.select().id(this.season.getGateID()).each(Interactive.doSetBounds(this.season.getGateBounds())).poll();
                if (gate != null) {
                    if (!gate.inViewport()) {
                        this.ctx.camera.turnTo(gate);
                        return;
                    }

                    if (!Cooldown.onCooldown("gate_click")) {
                        Cooldown.setCooldown("gate_click", (3 + (int) Math.round(Math.abs(rand.nextGaussian()))) * 1000L);
                        gate.interact("Open");
                        return;
                    }
                }
            } else {
                if (!Cooldown.onCooldown("move_gate")) {
                    Cooldown.setCooldown("move_gate", (5 + (int) (Math.round(rand.nextGaussian()) * 2)) * 1000L);
                    Tile randTile = this.season.getRandGateTile(rand);
                    this.ctx.movement.step(randTile);
                    return;
                }
            }
        } else if (this.season.inGarden(pos)) {
            this.buffer(pos);
        } else {
            this.ctx.controller.stop();
            System.out.println("Currently at an unknown position (" + pos.x() + ", " + pos.y() + ").");
            return;
        }
    }

    public void buffer(Tile pos) {
        Guard[] guard = this.season.getGuards();
        Camera camera = this.ctx.camera;

        if (camera.pitch() != 99) {
            camera.pitch(99);
            return;
        }

        if (this.ctx.movement.running()) {
            // System.out.println("[Debug] Character Spriting -> Position (" + pos.x() + ", " + pos.y() + ")");
            this.sprint(pos, guard, camera);
        } else {
            // System.out.println("[Debug] Character Walking -> Position (" + pos.x() + ", " + pos.y() + ")");
            this.walk(pos, guard, camera);
        }
    }

    public void sprint(Tile pos, Guard[] guard, Camera camera) {
        final Random rand = new Random();
        final Player p = this.season.getPlayer();

        final int x = pos.x();
        final int y = pos.y();

        if (p.inMotion() && !Cooldown.onCooldown("movement_in_motion")) {
            final Tile dest = this.ctx.movement.destination();
            final int destX = dest.x();
            final int destY = dest.y();

            if ((x >= 2912 && x <= 2920) && y == 5485) {
                if (x < 2919) {
                    Tile tile = new Tile(2921, 5485);
                    for (int i = 0; i < 10; i++) {
                        Tile temp = new Tile(2921 - i, 5485);
                        TileMatrix tempMat = temp.matrix(this.ctx);
                        if (tempMat.inViewport()) {
                            tile = temp;
                            break;
                        }
                    }

                    this.travelTile(this.ctx, tile); // To Zone 19 (correct)
                }
            } else if ((x >= 2914 && x <= 2918) && y == 5483) {
                if ((destX != 2920 && destX != 2921) || destY != 5485) {
                    if (rand.nextDouble() < 0.83) {
                        Tile travel = new Tile(2921, 5485);
                        if (travel.matrix(this.ctx).inViewport()) {
                            this.travelTile(this.ctx, travel); // To Zone 19 (correct)
                        } else {
                            this.travelTile(this.ctx, new Tile(2919, 5485)); // To Zone 18B (incorrect)
                        }
                    } else {
                        Tile travel = new Tile(2920, 5485);
                        if (travel.matrix(this.ctx).inViewport()) {
                            this.travelTile(this.ctx, travel); // To Zone 18A (incorrect)
                        } else {
                            this.travelTile(this.ctx, new Tile(2920, 5484)); // To Zone 18B (incorrect)
                        }
                    }
                }
            } else if ((x >= 2921 && x <= 2923) && (y >= 5490 && y <= 5495)) {
                if ((destX == 2922 || destX == 2923) && (destY >= 5491)) {
                    if (rand.nextDouble() < 0.56) {
                        this.travelTile(this.ctx, new Tile(2921, 5495)); // To Zone 38D (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2921, 5494)); // To Zone 38E (incorrect)
                    }
                } else if (y >= 5493 && destX == 2921 && (destY == 5495 || destY == 5494)) {
                    GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                    if (tree == null || !tree.inViewport()) {
                        List<Tile> tiles = this.getTreeTiles();
                        this.travelTile(this.ctx, tiles.get(rand.nextInt(tiles.size()))); // To Tree Zone (incorrect)
                        this.verifyUpdateCam(camera, 15, 60, 80, 100, 120, 140);
                    } else {
                        this.pick(rand); // To Tree (correct)
                        this.verifyUpdateCam(camera, 15, 60, 80, 100, 120, 140);
                    }
                }
            } else if ((x >= 2912 && x <= 2921) && (y >= 5487 && y <= 5495)) {
                GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                if (tree.inViewport()) {
                    this.pick(rand);
                }
            }

        } else if (!p.inMotion() && !Cooldown.onCooldown("movement_no_motion")) {
            if (x == 2910 && y == 5481) { // At Zone 1
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 340, 100, 120))
                    return;

                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 3, 5)) {
                    if (rand.nextDouble() < 0.85) {
                        this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5485)); // To Zone 5 (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 60, 80, 100, 120);
                } else {
                    if (rand.nextDouble() < 0.93) {
                        this.travelTile(this.ctx, new Tile(2908, 5482)); // To Zone 3 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2909, 5482)); // To Zone 2 (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 340, 80, 100, 120, 140);
                }
            } else if ((x == 2909 || x == 2910) && y == 5482) { // At Zone 2
                this.travelTile(this.ctx, new Tile(2908, 5482)); // To Zone 3 (correct)
            } else if (x == 2908 && y == 5482) { // At Zone 3
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 340, 80, 100, 120, 140))
                    return;

                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 6, 8)) {
                    if (rand.nextDouble() < 0.95) {
                        this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5485)); // To Zone 5 (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 60, 80, 100, 120);
                }
            } else if (x == 2906 && y == 5483) { // At Zone 4
                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 8, 9)) {
                    if (rand.nextDouble() < 0.95) {
                        this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5485)); // To Zone 5 (incorrect)
                    }
                }
            } else if (x == 2906 && y == 5485) { // At Zone 5
                this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
            } else if (x == 2906 && y == 5486) { // At Zone 6
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 60, 80, 100, 120))
                    return;

                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 1, 3)) {
                    if (rand.nextDouble() < 0.88) {
                        this.travelTile(this.ctx, new Tile(2906, 5492)); // To Zone 9 (correct)
                    } else if (rand.nextDouble() < 0.94) {
                        this.travelTile(this.ctx, new Tile(2906, 5491)); // To Zone 8A (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5490)); // To Zone 8B (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 340, 60, 80, 100, 120, 140);
                }
            } else if (x == 2906 && (y >= 5488 && y <= 5491)) { // At Zone 7 and 8
                this.travelTile(this.ctx, new Tile(2906, 5492)); // To Zone 9 (correct)
            } else if (x == 2906 && y == 5492) { // At Zone 9
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 340, 60, 80, 100, 120, 140))
                    return;

                Vector b = guard[1].getVector(this.ctx);
                if (this.isWithin(b, guard[1].getPath(), 8, 9)) {
                    if (rand.nextDouble() < 0.87) {
                        this.travelTile(this.ctx, new Tile(2909, 5490)); // To Zone 13 (correct)
                    } else if (rand.nextDouble() < 0.94) {
                        this.travelTile(this.ctx, new Tile(2909, 5491)); // To Zone 12A (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2909, 5492)); // To Zone 12B (incorrect)
                    }

                    this.setCamera(camera, 280, 300, 320);
                }
            } else if (((x >= 2908 && x <= 2910) && y == 5495) // At Zone 11
                    || (x == 2909 && (y >= 5491 && y <= 5494))) { // At Zone 12
                this.travelTile(this.ctx, new Tile(2909, 5490)); // To Zone 13 (correct)
            } else if (x == 2909 && y == 5490) { // At Zone 13
                if (!this.verifyUpdateCam(camera, 15, 280, 300, 320))
                    return;

                Vector c = guard[2].getVector(this.ctx);
                if (this.isWithin(c, guard[2].getPath(), 3, 4, 9, 10)) {
                    if (rand.nextDouble() < 0.92) {
                        this.travelTile(this.ctx, new Tile(2911, 5485)); // To Zone 17 (correct)
                    } else if (rand.nextDouble() < 0.55) {
                        this.travelTile(this.ctx, new Tile(2911, 5484)); // To Zone 16 (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2910, 5485)); // To Zone 15A (incorrect)
                    }

                    this.setCamera(camera, 300, 320);
                }
            } else if ((x == 2909 && (y >= 5484 && y <= 5488)) // At Zone 14
                    || (x == 2910 && (y == 5484 || y == 5485)) // At Zone 15
                    || (x == 2911 && y == 5484)) { // At Zone 16
                this.travelTile(this.ctx, new Tile(2911, 5485)); // To Zone 17 (correct)
            } else if (x == 2911 && y == 5485) { // At Zone 17
                if (!this.verifyUpdateCam(camera, 15, 300, 320))
                    return;

                Vector d = guard[3].getVector(this.ctx);
                if (this.isWithin(d, guard[3].getPath(), 2, 6, 18, 22)) {
                    if (rand.nextDouble() < 0.68) { // Click Movement
                        Tile tile = new Tile(2921, 5485); // Generate Tiles To Zone 19
                        for (int i = 0; i < 10; i++) {
                            Tile temp = new Tile(2921 - i, 5485);
                            TileMatrix tempMat = temp.matrix(this.ctx);
                            if (tempMat.inViewport()) {
                                tile = temp;
                                break;
                            }
                        }

                        this.travelTile(this.ctx, tile); // To Zone 19 Path (incorrect)
                    } else { // Map Movement
                        if (rand.nextDouble() < 0.62) {
                            this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
                            this.setCamera(camera, 300, 320, 20, 40, 60);
                        } else if (rand.nextDouble() < 0.54) {
                            this.travelTile(this.ctx, new Tile(2920, 5485)); // To Zone 18A (incorrect)
                        } else if (rand.nextBoolean()) {
                            this.travelTile(this.ctx, new Tile(2919, 5485)); // To Zone 18B (incorrect)
                        } else {
                            this.travelTile(this.ctx, new Tile(2920, 5484)); // To Zone 18C (incorrect)
                        }
                    }

                    this.setCamera(camera, 300, 320, 340, 360, 0, 20);
                } else if (this.isWithin(d, guard[3].getPath(), 7, 12)) {
                    if (rand.nextDouble() < 0.78) {
                        this.travelTile(this.ctx, new Tile(2918, 5483)); // To Zone 31A (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2917, 5483)); // To Zone 31B (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 340);
                }
            } else if ((x >= 2912 && x <= 2918) && (y >= 5483 && y <= 5485)) { // At Zone 30
                if (rand.nextDouble() < 0.83) {
                    this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
                    this.setCamera(camera, 300, 320, 20, 40, 60);
                } else {
                    this.travelTile(this.ctx, new Tile(2920, 5485)); // To Zone 18A (incorrect)
                }
            } else if ((x == 2919 || x == 2920) && (y == 5484 || y == 5485)) { // At Zone 18
                this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
            } else if (x == 2921 && y == 5485) { // At Zone 19
                if (!this.verifyUpdateCam(camera, 300, 320, 20, 40, 60))
                    return;

                Vector e = guard[4].getVector(this.ctx);
                if (this.isWithin(e, guard[4].getPath(), 9, 13)) {
                    if (rand.nextDouble() < 0.92) {
                        this.travelTile(this.ctx, new Tile(2924, 5487)); // To Zone 23 (correct)
                        this.setCamera(camera, 300, 320, 20, 40, 60);
                    } else {
                        this.travelTile(this.ctx, new Tile(2925, 5487)); // To Zone 24A (incorrect)
                        this.setCamera(camera, 300, 320, 20, 40, 60);
                    }
                }
            } else if (x == 2925 && y == 5487) { // At Zone 24
                this.travelTile(this.ctx, new Tile(2924, 5487)); // To Zone 23 (correct)
            } else if (x == 2924 && y == 5487) { // At Zone 23
                Vector e = guard[4].getVector(this.ctx);
                Vector f = guard[5].getVector(this.ctx);
                if (this.isWithin(e, guard[4].getPath(), 2, 4)) {
                    if (this.isWithin(f, guard[5].getPath(), 3, 9)) {
                        GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                        if (tree == null || !tree.inViewport()) {
                            List<Tile> tiles = this.getTreeTiles();
                            this.travelTile(this.ctx, tiles.get(rand.nextInt(tiles.size()))); // To Tree Zone (incorrect)
                        } else {
                            this.pick(rand); // To Tree (correct)
                        }
                    } else if (this.isWithin(f, guard[5].getPath(), 9, 15)) {
                        if (rand.nextDouble() < 0.68) {
                            this.travelTile(this.ctx, new Tile(2923, 5495)); // To Zone 38A (correct)
                        } else if (rand.nextBoolean()) {
                            this.travelTile(this.ctx, new Tile(2922, 5495)); // To Zone 38B (incorrect)
                        } else {
                            this.travelTile(this.ctx, new Tile(2923, 5494)); // To Zone 38C (incorrect)
                        }
                    }
                }
            } else if ((x >= 2912 && x <= 2920) && (y >= 5487 && y <= 5495)) { // At Tree Zone
                this.pick(rand); // To Tree (correct)
            } else if ((x >= 2921 && x <= 2923) && (y >= 5488 && y <= 5495)) { // At Zone 38
                GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                if (tree == null || !tree.inViewport()) {
                    List<Tile> tiles = this.getTreeTiles();
                    this.travelTile(this.ctx, tiles.get(rand.nextInt(tiles.size()))); // To Tree Zone (incorrect)
                } else {
                    this.pick(rand); // To Tree (correct)
                }
            }
        }
    }

    public List<Tile> getTreeTiles() {
        List<Tile> tiles = new ArrayList<Tile>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 9; y++) {
                Tile tile = new Tile(2912 + x, 5487 + y);
                if (tile.matrix(this.ctx).inViewport()) {
                    tiles.add(tile);
                }
            }
        }

        tiles.add(new Tile(2920, 5488));
        return tiles;
    }

    public void walk(Tile pos, Guard[] guard, Camera camera) {
        final Random rand = new Random();
        final Player p = this.season.getPlayer();

        final int x = pos.x();
        final int y = pos.y();

        if (p.inMotion() && !Cooldown.onCooldown("movement_in_motion")) {
            final Tile dest = this.ctx.movement.destination();
            final int destX = dest.x();
            final int destY = dest.y();

            if ((x >= 2912 && x <= 2920) && y == 5485) {
                if (!((destX == 2920 || destX == 2921) && destY == 5485) && !((destX >= 2923 && destX <= 2925) && (destY >= 5482 && destY <= 5485)) && !(destX == -1 && destY == -1)) {
                    Tile tile = new Tile(2921, 5485);
                    for (int i = 0; i < 10; i++) {
                        Tile temp = new Tile(2921 - i, 5485);
                        TileMatrix tempMat = temp.matrix(this.ctx);
                        if (tempMat.inViewport()) {
                            tile = temp;
                            break;
                        }
                    }

                    this.travelTile(this.ctx, tile); // To Zone 19 (correct)
                }
            } else if ((x >= 2914 && x <= 2918) && y == 5483) {
                if ((destX != 2920 && destX != 2921) || destY != 5485) {
                    if (rand.nextDouble() < 0.83) {
                        Tile travel = new Tile(2921, 5485);
                        if (travel.matrix(this.ctx).inViewport()) {
                            this.travelTile(this.ctx, travel); // To Zone 19 (correct)
                        } else {
                            this.travelTile(this.ctx, new Tile(2919, 5485)); // To Zone 18B (incorrect)
                        }
                    } else {
                        Tile travel = new Tile(2920, 5485);
                        if (travel.matrix(this.ctx).inViewport()) {
                            this.travelTile(this.ctx, travel); // To Zone 18A (incorrect)
                        } else {
                            this.travelTile(this.ctx, new Tile(2920, 5484)); // To Zone 18B (incorrect)
                        }
                    }
                }
            } else if ((x >= 2921 && x <= 2923) && (y >= 5490 && y <= 5495)) {
                if ((destX == 2922 || destX == 2923) && (destY >= 5492)) {
                    if (rand.nextDouble() < 0.56) {
                        this.travelTile(this.ctx, new Tile(2921, 5495)); // To Zone 38D (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2921, 5494)); // To Zone 38E (incorrect)
                    }
                } else if (y >= 5493 && destX == 2921 && (destY == 5495 || destY == 5494)) {
                    GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                    if (tree == null || !tree.inViewport()) {
                        List<Tile> tiles = this.getTreeTiles();
                        this.travelTile(this.ctx, tiles.get(rand.nextInt(tiles.size()))); // To Tree Zone (incorrect)
                        this.verifyUpdateCam(camera, 15, 60, 80, 100, 120, 140);
                    } else {
                        this.pick(rand); // To Tree (correct)
                        this.verifyUpdateCam(camera, 15, 60, 80, 100, 120, 140);
                    }
                }
            } else if ((x >= 2912 && x <= 2921) && (y >= 5487 && y <= 5495)) {
                GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                if (tree.inViewport()) {
                    this.pick(rand);
                }
            }

        } else if (!p.inMotion() && !Cooldown.onCooldown("movement_no_motion")) {
            if (x == 2910 && y == 5481) { // At Zone 1
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 340, 100, 120))
                    return;

                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 0, 1, 10, 11)) {
                    if (rand.nextDouble() < 0.85) {
                        this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5485)); // To Zone 5 (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 60, 80, 100, 120);
                } else {
                    if (rand.nextDouble() < 0.93) {
                        this.travelTile(this.ctx, new Tile(2908, 5482)); // To Zone 3 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2909, 5482)); // To Zone 2 (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 340, 80, 100, 120, 140);
                }
            } else if ((x == 2909 || x == 2910) && y == 5482) { // At Zone 2
                this.travelTile(this.ctx, new Tile(2908, 5482)); // To Zone 3 (correct)
            } else if (x == 2908 && y == 5482) { // At Zone 3
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 340, 80, 100, 120, 140))
                    return;

                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 4, 6)) {
                    if (rand.nextDouble() < 0.95) {
                        this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5485)); // To Zone 5 (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 60, 80, 100, 120);
                }
            } else if (x == 2906 && y == 5483) { // At Zone 4
                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 4, 7)) {
                    if (rand.nextDouble() < 0.95) {
                        this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5485)); // To Zone 5 (incorrect)
                    }
                }
            } else if (x == 2906 && y == 5485) { // At Zone 5
                this.travelTile(this.ctx, new Tile(2906, 5486)); // To Zone 6 (correct)
            } else if (x == 2906 && y == 5486) { // At Zone 6
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 60, 80, 100, 120))
                    return;

                Vector a = guard[0].getVector(this.ctx);
                if (this.isWithin(a, guard[0].getPath(), 1, 2)) {
                    if (rand.nextDouble() < 0.88) {
                        this.travelTile(this.ctx, new Tile(2906, 5492)); // To Zone 9 (correct)
                    } else if (rand.nextDouble() < 0.94) {
                        this.travelTile(this.ctx, new Tile(2906, 5491)); // To Zone 8A (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2906, 5490)); // To Zone 8B (incorrect)
                    }

                    this.setCamera(camera, 300, 320, 340, 60, 80, 100, 120, 140);
                }
            } else if (x == 2906 && (y >= 5488 && y <= 5491)) { // At Zone 7 and 8
                this.travelTile(this.ctx, new Tile(2906, 5492)); // To Zone 9 (correct)
            } else if (x == 2906 && y == 5492) { // At Zone 9
                if (!this.verifyUpdateCam(camera, 15, 300, 320, 340, 60, 80, 100, 120, 140))
                    return;

                Vector b = guard[1].getVector(this.ctx);
                if (this.isWithin(b, guard[1].getPath(), 1, 1)) {
                    if (rand.nextDouble() < 0.87) {
                        this.travelTile(this.ctx, new Tile(2909, 5490)); // To Zone 13 (correct)
                    } else if (rand.nextDouble() < 0.94) {
                        this.travelTile(this.ctx, new Tile(2909, 5491)); // To Zone 12A (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2909, 5492)); // To Zone 12B (incorrect)
                    }

                    this.setCamera(camera, 280, 300, 320);
                }
            } else if (((x >= 2908 && x <= 2910) && y == 5495) // At Zone 11
                    || (x == 2909 && (y >= 5491 && y <= 5494))) { // At Zone 12
                this.travelTile(this.ctx, new Tile(2909, 5490)); // To Zone 13 (correct)
            } else if (x == 2909 && y == 5490) { // At Zone 13
                if (!this.verifyUpdateCam(camera, 15, 280, 300, 320))
                    return;

                Vector c = guard[2].getVector(this.ctx);
                if (this.isWithin(c, guard[2].getPath(), 2, 2, 9, 9)) {
                    if (rand.nextDouble() < 0.92) {
                        this.travelTile(this.ctx, new Tile(2911, 5485)); // To Zone 17 (correct)
                    } else if (rand.nextDouble() < 0.55) {
                        this.travelTile(this.ctx, new Tile(2911, 5484)); // To Zone 16 (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2910, 5485)); // To Zone 15A (incorrect)
                    }

                    this.setCamera(camera, 300, 320);
                }
            } else if ((x == 2909 && (y >= 5484 && y <= 5488)) // At Zone 14
                    || (x == 2910 && (y == 5484 || y == 5485)) // At Zone 15
                    || (x == 2911 && y == 5484)) { // At Zone 16
                this.travelTile(this.ctx, new Tile(2911, 5485)); // To Zone 17 (correct)
            } else if (x == 2911 && y == 5485) { // At Zone 17
                if (!this.verifyUpdateCam(camera, 15, 300, 320))
                    return;

                Vector d = guard[3].getVector(this.ctx);
                if (this.isWithin(d, guard[3].getPath(), 26, 27)) {
                    if (rand.nextDouble() < 0.68) { // Click Movement
                        Tile tile = new Tile(2921, 5485); // Generate Tiles To Zone 19
                        for (int i = 0; i < 10; i++) {
                            Tile temp = new Tile(2921 - i, 5485);
                            TileMatrix tempMat = temp.matrix(this.ctx);
                            if (tempMat.inViewport()) {
                                tile = temp;
                                break;
                            }
                        }

                        this.travelTile(this.ctx, tile); // To Zone 19 Path (incorrect)
                    } else { // Map Movement
                        if (rand.nextDouble() < 0.62) {
                            this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
                        } else if (rand.nextDouble() < 0.54) {
                            this.travelTile(this.ctx, new Tile(2920, 5485)); // To Zone 18A (incorrect)
                        } else if (rand.nextBoolean()) {
                            this.travelTile(this.ctx, new Tile(2919, 5485)); // To Zone 18B (incorrect)
                        } else {
                            this.travelTile(this.ctx, new Tile(2920, 5484)); // To Zone 18C (incorrect)
                        }
                    }

                    this.setCamera(camera, 300, 320, 340, 360, 0, 20);
                } else if (this.isWithin(d, guard[3].getPath(), 7, 8)) {
                    if (rand.nextDouble() < 0.78) {
                        this.travelTile(this.ctx, new Tile(2918, 5483)); // To Zone 31A (incorrect)
                    } else {
                        this.travelTile(this.ctx, new Tile(2917, 5483)); // To Zone 31B (incorrect)
                    }

                    this.setCamera(camera, 300, 320);
                }
            } else if ((x >= 2912 && x <= 2918) && (y >= 5483 && y <= 5485)) { // At Zone 30
                if (rand.nextDouble() < 0.83) {
                    this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
                } else {
                    this.travelTile(this.ctx, new Tile(2920, 5485)); // To Zone 18A (incorrect)
                }
            } else if ((x == 2919 || x == 2920) && (y == 5484 || y == 5485)) { // At Zone 18
                this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
            } else if (x == 2921 && y == 5485) { // At Zone 19 #@#!#@!($@!$&)(*&@!(#*&@)!#*&()(!@*#&)@!)(*@&!)(*#@&!)(#*&)*@#&)(!*&!)@(*#&)
                if (!this.verifyUpdateCam(camera, 300, 320, 20, 40, 60))
                    return;

                Vector e = guard[4].getVector(this.ctx);
                if (this.isWithin(e, guard[4].getPath(), 6, 9)) {
                    if (rand.nextDouble() < 0.92) {
                        this.travelTile(this.ctx, new Tile(2924, 5487)); // To Zone 23 (correct)
                        this.setCamera(camera, 300, 320, 20, 40, 60);
                    } else {
                        this.travelTile(this.ctx, new Tile(2925, 5487)); // To Zone 24A (incorrect)
                        this.setCamera(camera, 300, 320, 20, 40, 60);
                    }
                }
            } else if (x == 2925 && y == 5487) { // At Zone 24
                this.travelTile(this.ctx, new Tile(2924, 5487)); // To Zone 23 (correct)
            } else if (x == 2924 && y == 5487) { // At Zone 23
                Vector e = guard[4].getVector(this.ctx);
                Vector f = guard[5].getVector(this.ctx);
                if (this.isWithin(e, guard[4].getPath(), 2, 3, 10, 11)) {
                    if (this.isWithin(f, guard[5].getPath(), 1, 5)) {
                        GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                        if (tree == null || !tree.inViewport()) {
                            List<Tile> tiles = this.getTreeTiles();
                            this.travelTile(this.ctx, tiles.get(rand.nextInt(tiles.size()))); // To Tree Zone (incorrect)
                        } else {
                            this.pick(rand); // To Tree (correct)
                        }
                    } else if (this.isWithin(f, guard[5].getPath(), 1, 10)) {
                        if (rand.nextDouble() < 0.68) {
                            this.travelTile(this.ctx, new Tile(2923, 5495)); // To Zone 38A (correct)
                        } else if (rand.nextBoolean()) {
                            this.travelTile(this.ctx, new Tile(2922, 5495)); // To Zone 38B (incorrect)
                        } else {
                            this.travelTile(this.ctx, new Tile(2923, 5494)); // To Zone 38C (incorrect)
                        }
                    }
                }
            } else if ((x >= 2912 && x <= 2920) && (y >= 5487 && y <= 5495)) { // At Tree Zone
                this.pick(rand); // To Tree (correct)
            } else if ((x >= 2921 && x <= 2923) && (y >= 5488 && y <= 5495)) { // At Zone 38
                GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
                if (tree == null || !tree.inViewport()) {
                    List<Tile> tiles = this.getTreeTiles();
                    this.travelTile(this.ctx, tiles.get(rand.nextInt(tiles.size()))); // To Tree Zone (incorrect)
                } else {
                    this.pick(rand); // To Tree (correct)
                }
            }
        }
    }

    public void pick(Random rand) {
        GameObject tree = this.ctx.objects.select().id(this.season.getTreeID()).poll();
        if (tree != null && tree.inViewport()) {
            if (!Cooldown.onCooldown("interact_herb")) {
                Cooldown.setCooldown("interact_herb", (9 + new Random().nextInt(3)) * 1000L);
                tree.interact("Pick-Fruit");
            }
        }
    }
}