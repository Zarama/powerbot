package VisionEyes.scripts.iSmithing.tasks;

import VisionEyes.scripts.iSmithing.Task;
import VisionEyes.scripts.iSmithing.resources.Bar;
import VisionEyes.scripts.iSmithing.resources.Ore;
import VisionEyes.scripts.iSmithing.resources.Util;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import java.util.LinkedHashMap;


public class Bank extends Task {

    private Util util = new Util(ctx);
    private Bar bar;
    private Ore ore;
    private LinkedHashMap<Integer, Integer> ores;

    public Bank(ClientContext ctx, Bar selectedBar) {
        super(ctx);
        this.bar = selectedBar;
        this.ore = new Ore();
        this.ores = ore.getQuantity(bar);
    }

    @Override
    public boolean activate() {
        return util.getItemsCount(ores) != ore.getMaxItemsByBar(bar) && !util.hasOres(ores);
    }

    @Override
    public void execute() {
        if (ctx.bank.opened()) {
            if((!util.hasBar(bar) && !util.hasOres(ores)) && util.inventoryCount() > 0){
                this.deposit();
            }else if(util.hasBar(bar)){
                this.deposit();
            }else{
                this.withdraw();
            }
        } else {
            if (ctx.bank.inViewport()) {
                if (ctx.bank.open()) {
                    Condition.wait(ctx.bank::opened, 250, 20);
                }
            } else {
                ctx.camera.turnTo(ctx.bank.nearest());
                ctx.movement.step(ctx.bank.nearest());
            }
        }

    }

    @Override
    public String getName() {
        return "Bank";
    }

    private void deposit() {
        if (ctx.bank.depositInventory()) {
            final int inventCount = ctx.inventory.select().count();
            Condition.wait(() -> ctx.inventory.select().count() != inventCount, 150, 20);
        }
    }

    private void withdraw(){
        ores.forEach(ctx.bank::withdraw);
    }
}
