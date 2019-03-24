package Vaynex.tasks;

import Vaynex.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

/**
 * Created by Me on 19/06/2017.
 */
public class Drop extends Task {
    public Drop(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()==28 && ctx.inventory.select().id(331).count()==0 && ctx.inventory.select().id(335).count()==0; // ctx.inventory.select().id(331).count()==0 && ctx.inventory.select().id(343).count()>=1; //or when salmon/burnt fish >1 and 331 < 1
    }

    @Override
    public void execute() {
        Dropfish();
}



private void Dropfish() {

    for (Item cookResult : ctx.inventory.select().name(Pattern.compile("(Salmon)|(Burnt fish)|(Trout)"))) {
        final int startAmtInventory = ctx.inventory.select().count();
        cookResult.interact("Drop");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().count() != startAmtInventory; //bascially checked drop b4 next drop
            }
        }, 25, 20);

    }
}

}