package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

public class IsInventoryFull extends BranchTask
{
    private ContainsFeathers containsFeathersBranch = new ContainsFeathers(ctx);
    private ContainsRawFood containsRawFoodBranch = new ContainsRawFood(ctx);

    public IsInventoryFull(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.backpack.select().count() == 28 || (!(contains("Raw trout") || contains("Raw salmon")) && (contains("Trout") || contains("Salmon"))) || ((contains("Raw trout") || contains("Raw salmon")) && (contains("Trout") || contains("Salmon")));
    }

    @Override
    public TreeTask successTask() {
        return containsRawFoodBranch;
    }

    @Override
    public TreeTask failureTask() {
        return containsFeathersBranch;
    }

    private boolean contains (String name)
    {
        Item[] items = ctx.backpack.items();
        for (Item item : items)
        {
            if(item.name().equals(name))
                return true;
        }
        return false;
    }



}
