package manbearpigcat.scripts.potatofarmer.tasks;

import manbearpigcat.scripts.potatofarmer.PotatoPicker;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.util.concurrent.Callable;

/**
 * Created by Shan on 2016-08-17.
 */
public class WalkToBank extends Task<ClientContext> {


    private static final Tile VARROCK_BANK_TILE = new Tile(3185, 3435, 0);
    private static final Tile ALKHARID_BANK_TILE = new Tile(3270, 3168, 0);
    private static final Tile ALKHARID_SUB_TILE = new Tile(3277, 3183, 0); //to avoid entering the palace

    private static final int homeWidget = 1465, homeComponent = 56;
    private static final int teleWidget = 1092, varrockComponent = 21, alkharidComponent = 10;


    private static final Tile vLodestone = new Tile(3214, 3376, 0);
    private static final Tile aLodestone = new Tile(3297, 3184, 0);
    private static final Tile T1 = new Tile(3265, 3321, 0);
    private static final Tile T2 = new Tile(3241, 3299, 0);
    private static Area area = new Area(T1, T2);
    private static int bank = 0;

    public WalkToBank(ClientContext ctx, int bank){
        super(ctx);
        this.bank = bank;
    }

    public boolean activate(){
        return ctx.backpack.select().count() == 28;
    }

    public void execute(){
        PotatoPicker.sPots.setState("Walking to bank.");
        if(ctx.movement.energyLevel() > 80){
            ctx.movement.running(true);
        }

        final Component home = ctx.widgets.component(homeWidget, homeComponent);
        final Component tele = bank == 1 ? ctx.widgets.component(teleWidget, varrockComponent) :
                ctx.widgets.component(teleWidget, alkharidComponent);
        final Tile lodestone = bank == 1 ? vLodestone : aLodestone;

        if(area.contains(ctx.players.local())) {
            if (home.visible() && home.click()) {

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return tele.visible();
                    }
                }, 1000, 4);
                tele.click();

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return lodestone.distanceTo(ctx.players.local()) == 0;
                    }
                }, 1000, 15);
            }
        }

        if(bank == 1) {
            ctx.movement.step(VARROCK_BANK_TILE);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return VARROCK_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                }
            }, 1000, 8);
        }
        else{

            ctx.movement.step(ALKHARID_SUB_TILE);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ALKHARID_SUB_TILE.distanceTo(ctx.players.local()) < 5;
                }
            }, 1000, 8);

            ctx.movement.step(ALKHARID_BANK_TILE);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ALKHARID_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                }
            }, 1000, 4);
        }
    }
}