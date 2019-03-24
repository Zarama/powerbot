package nagi.sorceress.script.summer;

import java.util.Iterator;
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

public class SummerHerbs extends SeasonScript {

    public ClientContext ctx;

    public SummerHerbs(Season season) {
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
                        Cooldown.setCooldown("gate_click", (2 + (int) Math.round(Math.abs(rand.nextGaussian()))) * 1000L);
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
                if ((destX == 2920 || destX == 2921) && destY == 5485) {
                    if (x >= 18) {
                        Vector e = guard[4].getVector(this.ctx);
                        if (this.isWithin(e, guard[4].getPath(), 10, 11)) {
                            this.pick(rand); // To Herbs (correct)
                        }
                    }
                } else if (x < 2919 && destX != -1 && destY != -1) {
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
                        this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2920, 5485)); // To Zone 18A (incorrect)
                    }
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
                if (this.isWithin(c, guard[2].getPath(), 3, 4, 9, 11)) {
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
            } else if (x == 2921 && y == 5485) { // At Zone 19
                Vector e = guard[4].getVector(this.ctx);
                if (this.isWithin(e, guard[4].getPath(), 10, 13)) {
                    this.pick(rand); // To Herbs (correct)
                }
            } else if ((x >= 2923 && x <= 2925) && (y >= 5483 && y <= 5485)) { // At Zone 20
                this.pick(rand); // To Herbs (correct)
            }
        }
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
                        this.travelTile(this.ctx, new Tile(2921, 5485)); // To Zone 19 (correct)
                    } else {
                        this.travelTile(this.ctx, new Tile(2920, 5485)); // To Zone 18A (incorrect)
                    }
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
            } else if (x == 2921 && y == 5485) { // At Zone 19
                Vector e = guard[4].getVector(this.ctx);
                if (this.isWithin(e, guard[4].getPath(), 6, 9)) {
                    this.pick(rand); // To Herbs (correct)
                }
            } else if ((x >= 2923 && x <= 2925) && (y >= 5483 && y <= 5485)) { // At Zone 20
                this.pick(rand); // To Herbs (correct)
            }
        }
    }

    public void pick(Random rand) {
        Iterator<GameObject> herbs = ctx.objects.select().id(this.season.getHerbID()).iterator();
        GameObject a = null;
        GameObject b = null;
        while (herbs.hasNext()) {
            GameObject herb = herbs.next();
            Tile hTile = herb.tile();
            if (hTile.x() == 2923 && hTile.y() == 5483) {
                a = herb;
            } else if (hTile.x() == 2924 && hTile.y() == 5482) {
                b = herb;
            }
        }

        if (!Cooldown.onCooldown("interact_herb")) {
            Cooldown.setCooldown("interact_herb", (9 + new Random().nextInt(3)) * 1000L);

            if (rand.nextDouble() <= 0.85) {
                a.interact("Pick");
            } else {
                b.interact("Pick");
            }
        }
    }
}