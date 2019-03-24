package manbearpigcat.scripts.potatofarmer.tasks;

import manbearpigcat.scripts.potatofarmer.PotatoPicker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import java.util.concurrent.Callable;

/**
 * Created by Shan on 2016-08-17.
 */
public class Bank extends Task<ClientContext> {

    private static final Tile VARROCK_BANK_TILE = new Tile(3185, 3435, 0);
    private static final Tile ALKHARID_BANK_TILE = new Tile(3270, 3168, 0);
    private int bank = 0; //1 = varrock, 2 = al-kharid

    public Bank(ClientContext ctx, int bank){
        super(ctx);
        this.bank = bank;
    }

    public boolean activate(){ return (VARROCK_BANK_TILE.distanceTo(ctx.players.local()) < 20 ||
            ALKHARID_BANK_TILE.distanceTo(ctx.players.local()) < 20) && ctx.backpack.select().count() == 28;}

    public void execute(){
        PotatoPicker.sPots.setState("Banking.");

        final Tile bankTile = bank == 1 ? VARROCK_BANK_TILE : ALKHARID_BANK_TILE;
        if(!bankTile.matrix(ctx).inViewport()){
            ctx.movement.step(bankTile);
            ctx.camera.turnTo(bankTile);
        }
        Condition.wait(new Condition.Check() {
            public boolean poll() {
                return bankTile.distanceTo(ctx.players.local()) < 5;
            }
        });
/*
        if(!ctx.bank.inViewport()){
            if(bank == 1) {
                ctx.movement.step(VARROCK_BANK_TILE);
                ctx.camera.turnTo(VARROCK_BANK_TILE);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return VARROCK_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                    }
                }, 1000, 3);
            }
            else{
                ctx.movement.step(ALKHARID_BANK_TILE);
                ctx.camera.turnTo(ALKHARID_BANK_TILE);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ALKHARID_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                    }
                }, 1000, 3);
            }
        }
*/
        if(ctx.bank.open()){
            if(ctx.bank.depositInventory()){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 1000, 3);
            }
        }
    }
}