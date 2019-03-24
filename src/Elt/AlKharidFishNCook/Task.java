package Elt.AlKharidFishNCook;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

public abstract class Task extends ClientAccessor {

    protected final int netId = 303;
    protected final int rodId = 307;
    protected final int baitId = 313;
    protected final int[] rawFishIds = {317, 321, 327, 345};
    protected final int[] cookedFishIds = {315, 319, 325, 347};

    public Task(ClientContext ctx) {
        super(ctx);
    }

    protected boolean hasRawFish() {
        for (Integer rawFishId : rawFishIds) {
            if (ctx.inventory.select().id(rawFishId).count() > 0) {
                return true;
            }
        }
        return false;
    }

    public abstract boolean activate();
    public abstract void execute();

}
