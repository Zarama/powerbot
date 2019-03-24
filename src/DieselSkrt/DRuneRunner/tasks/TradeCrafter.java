package DieselSkrt.DRuneRunner.tasks;

import DieselSkrt.DRuneRunner.DRuneRunner;
import DieselSkrt.DRuneRunner.Task;
import DieselSkrt.DRuneRunner.Trade;
import DieselSkrt.DRuneRunner.Walker;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Shane on 15-1-2018.
 */
public class TradeCrafter extends Task<ClientContext> {


    public TradeCrafter(ClientContext ctx){
        super(ctx);
    }

    /**
     * - Near crafter
     * - Essence in invent
     * */


    @Override
    public boolean activate(){
        return !ctx.inventory.select().id(ESSENCE).isEmpty() && !ctx.objects.select().id(craftAltar).isEmpty();

    }

    /**
     * - Trade crafter
     * - Offer essence
     * */

    @Override
    public void execute(){
        DRuneRunner.STATUS = "Trading crafter";

        final Trade trade = new Trade(ctx);

        if(!ctx.players.select().name(DRuneRunner.CRAFTER_USERNAME).poll().inViewport()){
            ctx.movement.step(ctx.players.select().name(DRuneRunner.CRAFTER_USERNAME).poll());
            ctx.camera.turnTo(ctx.players.select().name(DRuneRunner.CRAFTER_USERNAME).poll());
        }

        trade.tradeWith(ctx.players.select().name(DRuneRunner.CRAFTER_USERNAME).poll());

        if(!Condition.wait(new Condition.Check(){
            public boolean poll(){
                return trade.opened();
            }
        }, 100, 100))
            return;

        trade.offerAll(ctx.inventory.select().id(ESSENCE).poll());
        trade.accept();

        if(trade.secondOpened()){
            trade.accept();
        }

//        if(!Condition.wait(new Condition.Check(){
//            public boolean poll(){
//                return trade.opened() && trade.hasPlayerAccepted();
//            }
//        }, 50, 100))
//            trade.decline();
//        trade.accept();
    }
}
