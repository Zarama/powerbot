package nagi.sorceress.script;

import java.util.Random;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Player;

import nagi.sorceress.SorceressGarden;
import nagi.sorceress.type.Guard;
import nagi.sorceress.type.Vector;

public abstract class Season {

    protected SorceressGarden instance;

    protected Class<? extends SeasonScript> scriptHerbs;
    protected Class<? extends SeasonScript> scriptFruit;

    protected int xp_rate;
    protected int fruit_count;

    protected int gate_id;
    protected int[] gate_bounds;
    protected Tile gate_location;

    protected int tree_id;
    protected int herb_id;

    protected Guard[] guards;

    protected int run_disable;
    protected int run_enable;

    public Season(SorceressGarden instance) {
        this.instance = instance;
        this.resetEnergies(new Random());
    }

    public abstract boolean inGarden(Tile tile);

    public abstract Tile getRandGateTile(Random r);

    public int getXPRate() {
        return this.xp_rate;
    }

    public int getFruitCount() {
        return this.fruit_count;
    }

    public SorceressGarden getInstance() {
        return this.instance;
    }

    public Class<? extends SeasonScript> getHerbsScript() {
        return this.scriptHerbs;
    }

    public Class<? extends SeasonScript> getFruitsScript() {
        return this.scriptFruit;
    }

    public int getGateID() {
        return this.gate_id;
    }

    public int[] getGateBounds() {
        return this.gate_bounds;
    }

    public Tile getGateLocation() {
        return this.gate_location;
    }

    public int getTreeID() {
        return this.tree_id;
    }

    public int getHerbID() {
        return this.herb_id;
    }

    public void resetEnergies(Random random) {
        this.run_disable = random.nextInt(20) + 10;
        this.run_enable = random.nextInt(20) + 70;
    }

    public int getRunDisable() {
        return this.run_disable;
    }

    public int getRunEnable() {
        return this.run_enable;
    }

    public Guard[] getGuards() {
        return this.guards;
    }

    public boolean inLobby(Tile tile) {
        if (tile.x() >= 2903 && tile.x() <= 2920 && tile.y() >= 5463 && tile.y() <= 5480)
            return true;
        return false;
    }

    public Player getPlayer() {
        return this.instance.ctx().players.local();
    }

    public Vector getEntityPosition(int id) {
        Npc npc = this.instance.ctx().npcs.select().id(id).poll();
        Tile tile = npc.tile();
        return new Vector(tile.x(), tile.y(), npc.orientation());
    }
}