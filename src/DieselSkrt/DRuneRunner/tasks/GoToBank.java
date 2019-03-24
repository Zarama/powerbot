package DieselSkrt.DRuneRunner.tasks;

import DieselSkrt.DRuneRunner.DRuneRunner;
import DieselSkrt.DRuneRunner.Task;
import DieselSkrt.DRuneRunner.Walker;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Shane on 15-1-2018.
 */
public class GoToBank extends Task<ClientContext> {

    public static final Tile[] AIR_TO_BANK = {new Tile(2983, 3288, 0), new Tile(2987, 3288, 0), new Tile(2989, 3292, 0), new Tile(2991, 3296, 0), new Tile(2991, 3300, 0), new Tile(2996, 3302, 0), new Tile(3000, 3304, 0), new Tile(3004, 3306, 0), new Tile(3005, 3310, 0), new Tile(3005, 3314, 0), new Tile(3005, 3318, 0), new Tile(3006, 3322, 0), new Tile(3006, 3327, 0), new Tile(3006, 3331, 0), new Tile(3006, 3335, 0), new Tile(3006, 3339, 0), new Tile(3006, 3343, 0), new Tile(3006, 3347, 0), new Tile(3006, 3352, 0), new Tile(3006, 3356, 0), new Tile(3009, 3359, 0), new Tile(3012, 3356, 0)};
    public static final Tile[] EARTH_ALTAR_PATH = {new Tile(3302, 3477, 0), new Tile(3298, 3476, 0), new Tile(3297, 3472, 0), new Tile(3294, 3468, 0), new Tile(3291, 3465, 0), new Tile(3289, 3461, 0), new Tile(3289, 3457, 0), new Tile(3288, 3453, 0), new Tile(3288, 3449, 0), new Tile(3284, 3446, 0), new Tile(3281, 3443, 0), new Tile(3278, 3439, 0), new Tile(3275, 3436, 0), new Tile(3275, 3432, 0), new Tile(3271, 3431, 0), new Tile(3267, 3430, 0), new Tile(3263, 3430, 0), new Tile(3259, 3430, 0), new Tile(3255, 3427, 0), new Tile(3254, 3423, 0), new Tile(3253, 3420)};
    public static final Tile[] BODY_ALTAR_PATH = {new Tile(3050, 3442, 0), new Tile(3054, 3440, 0), new Tile(3058, 3438, 0), new Tile(3062, 3438, 0), new Tile(3066, 3440, 0), new Tile(3069, 3443, 0), new Tile(3071, 3447, 0), new Tile(3074, 3450, 0), new Tile(3076, 3454, 0), new Tile(3080, 3456, 0), new Tile(3083, 3459, 0), new Tile(3086, 3462, 0), new Tile(3082, 3465, 0), new Tile(3080, 3469, 0), new Tile(3080, 3473, 0), new Tile(3080, 3477, 0), new Tile(3080, 3481, 0), new Tile(3084, 3484, 0), new Tile(3087, 3487, 0), new Tile(3090, 3490, 0), new Tile(3094, 3491, 0)};

    private final Walker walker = new Walker(ctx);


    public GoToBank(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        return ctx.inventory.select().id(ESSENCE).isEmpty() && ctx.objects.select().id(PORTALID).isEmpty() && !ctx.bank.opened();
    }

    @Override
    public void execute(){
        DRuneRunner.STATUS = "Going to bank";
        if(!ctx.movement.running() && ctx.movement.energyLevel() > 45){
            ctx.movement.running(true);
        }

        switch(DRuneRunner.ALTAR_TO_CRAFT){
            case "Air altar":
                if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                    walker.walkPath(AIR_TO_BANK);
                }
                break;
            case "Mind altar":
                break;
            case "Water altar":
                break;
            case "Earth altar":
                if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                    walker.walkPath(EARTH_ALTAR_PATH);
                }
                break;
            case "Fire altar":
                break;
            case "Body altar":
                if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                    walker.walkPath(BODY_ALTAR_PATH);
                }
                break;
        }
    }
}
