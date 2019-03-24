package nagi.sorceress.script.summer;

import java.util.Random;

import org.powerbot.script.Tile;

import nagi.sorceress.SorceressGarden;
import nagi.sorceress.script.Season;
import nagi.sorceress.script.summer.guard.SummerGuardA;
import nagi.sorceress.script.summer.guard.SummerGuardB;
import nagi.sorceress.script.summer.guard.SummerGuardC;
import nagi.sorceress.script.summer.guard.SummerGuardD;
import nagi.sorceress.script.summer.guard.SummerGuardE;
import nagi.sorceress.script.summer.guard.SummerGuardF;
import nagi.sorceress.type.Guard;

public class Summer extends Season {

    public Summer(SorceressGarden instance) {
        super(instance);

        // this.scriptHerbs = SummerHerbs.class;
        // this.scriptFruit = SummerFruits.class;

        /**
         * Class referencing doesn't seem to work on SDN. Check out src/stormneo7/sorceress/SoreressGarden.java for better explaination.
         */

        this.xp_rate = 3000;
        this.fruit_count = 2;

        this.gate_id = 11987;
        this.gate_bounds = new int[] { 4, 128, -240, -20, 116, 128 };
        this.gate_location = new Tile(2910, 5480);

        this.tree_id = 12943;
        this.herb_id = 4980;

        this.guards = new Guard[] { new SummerGuardA(), new SummerGuardB(), new SummerGuardC(), new SummerGuardD(), new SummerGuardE(), new SummerGuardF() };
    }

    @Override
    public boolean inGarden(Tile tile) {
        return tile.x() >= 2905 && tile.x() <= 2926 && tile.y() >= 5481 && tile.y() <= 5496;
    }

    @Override
    public Tile getRandGateTile(Random r) {
        int randX = (int) Math.round(r.nextGaussian()) * 2;
        int randY = (int) Math.round(r.nextGaussian());
        return new Tile(2910 + randX, 5485 + randY);
    }
}