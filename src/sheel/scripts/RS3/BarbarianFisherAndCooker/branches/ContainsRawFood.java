package sheel.scripts.RS3.BarbarianFisherAndCooker.branches;

import sheel.RS3TreeBot.BranchTask;
import sheel.RS3TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

public class ContainsRawFood extends BranchTask
{
    private InCookingRegion inCookingRegionBranch = new InCookingRegion(ctx);
    private InBankRegion inBankRegionBranch = new InBankRegion(ctx);

    public ContainsRawFood(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return contains("Raw trout") || contains("Raw salmon");
    }

    @Override
    public TreeTask successTask() {
        return inCookingRegionBranch;
    }

    @Override
    public TreeTask failureTask() {
        return inBankRegionBranch;
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
