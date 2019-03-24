package nagi.sorceress.script;

import java.util.HashSet;
import java.util.Random;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Camera;
import org.powerbot.script.rt4.ClientContext;

import nagi.sorceress.misc.Cooldown;
import nagi.sorceress.type.Vector;

public abstract class SeasonScript {

    protected Season season;
    protected HashSet<String> settings = new HashSet<String>();

    public SeasonScript(Season season) {
        this.season = season;
    }

    public abstract void poll();

    public Season getSeason() {
        return this.season;
    }

    public void completeLap() {
        this.resetSettings();
    }

    public void failLap() {
        this.resetSettings();
    }

    public void resetSettings() {

    }

    public boolean verifyUpdateCam(Camera camera, int deviation, int... angles) {
        if (!this.verifyCamera(camera.yaw(), deviation, angles)) {
            this.setCamera(camera, angles);
            return false;
        }

        return true;
    }

    public boolean verifyCamera(int current, int deviation, int angle) {
        int difference = Math.abs(current - angle);
        return difference >= 0 && difference <= deviation;
    }

    public boolean verifyCamera(int current, int deviation, int... angles) {
        for (int angle : angles) {
            if (this.verifyCamera(current, deviation, angle)) {
                return true;
            }
        }
        return false;
    }

    public void setCamera(Camera camera, int... angles) {
        int angle = angles[new Random().nextInt(angles.length)];
        if (!this.verifyCamera(camera.yaw(), 15, angle)) {
            camera.angle(angle);
        }
    }

    public void travelTile(ClientContext ctx, Tile tile) {
        if (tile.matrix(ctx).inViewport()) {
            if (ctx.players.local().tile().distanceTo(tile) <= 6)
                tile.matrix(ctx).click(true);
            else
                tile.matrix(ctx).interact("Walk here");
        } else {
            ctx.movement.step(tile);
        }

        Random rand = new Random();
        Cooldown.setCooldown("movement_in_motion", 500L + Math.round(Math.abs(rand.nextGaussian()) * 250d));
        Cooldown.setCooldown("movement_no_motion", 1000L + Math.round(Math.abs(rand.nextGaussian()) * 250d));
    }

    public boolean isWithin(Vector find, Vector[] search, int... bounds) {
        for (int i = 0; i < (bounds.length / 2); i++) {
            int start = bounds[i * 2];
            int end = bounds[(i * 2) + 1];
            for (int x = start; x <= end; x++) {
                if (search[x].equals(find))
                    return true;
            }
        }

        return false;
    }
}