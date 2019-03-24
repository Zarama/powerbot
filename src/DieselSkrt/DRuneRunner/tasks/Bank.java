package DieselSkrt.DRuneRunner.tasks;

import DieselSkrt.DRuneRunner.DRuneRunner;
import DieselSkrt.DRuneRunner.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Drawable;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Shane on 15-1-2018.
 */
public class Bank extends Task<ClientContext> {

    public Bank(ClientContext ctx){
        super(ctx);
    }

    /**
     *  - No essence in invent
     *  - Bank in viewport
     * */

    @Override
    public boolean activate(){
        return ctx.inventory.select().id(ESSENCE).isEmpty() && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) <= 2;
    }

    /**
     * - Open bank
     * - Check invent spaces
     * - Deposit invent when needed
     * - Withdraw essence, it will check for pure ess first, if those are nor available it will take rune ess
     * - Close bank
     * */
    @Override
    public void execute() {
        DRuneRunner.STATUS = "Banking";
        if (ctx.bank.opened()) {
            if(!ctx.inventory.select().isEmpty()){
                ctx.bank.depositInventory();
            }
            if (!ctx.bank.select().id(7936).isEmpty()) {
                ctx.bank.withdraw(7936, DRuneRunner.AMOUNT_ESSENCE);
            } else if (!ctx.bank.select().id(1436).isEmpty()) {
                ctx.bank.withdraw(1426, DRuneRunner.AMOUNT_ESSENCE);
            }
        } else {
            if (ctx.bank.inViewport()) {
                ctx.bank.open();
            }else{
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }
        if(ctx.inventory.select().count() > 0){
            ctx.bank.close();
        }
    }
}
