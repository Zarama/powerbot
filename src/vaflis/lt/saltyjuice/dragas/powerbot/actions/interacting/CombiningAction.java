package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.ArrayList;
import java.util.List;

public class CombiningAction extends InteractingAction<Item>
{
    private final int second;
    private final int first;
    private final List<Item> cache = new ArrayList<>();

    public CombiningAction(int first, int second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    protected Item getObject(ClientContext ctx)
    {
        return getObject(ctx, first);
    }

    protected Item getObject(ClientContext ctx, int id)
    {
        return cache.stream().filter((it) -> it.id() == id).findFirst().orElse(null);
    }

    @Override
    protected void interact(Item obj)
    {

    }

    @Override
    public boolean isUsable(ClientContext ctx)
    {
        bustCache(ctx);
        return (getItemCount(ctx, first) + getItemCount(ctx, second)) != 0;
    }

    private void bustCache(ClientContext ctx)
    {
        cache.clear();
        ctx.inventory.select().addTo(cache);
    }

    @Override
    public boolean isFinished(ClientContext ctx)
    {
        bustCache(ctx);
        return (getItemCount(ctx, first) + getItemCount(ctx, second)) == 0;
    }

    protected int getItemCount(ClientContext ctx, int id)
    {
        return ((int) cache.stream().filter((it) -> it.id() == id).count());
    }

    @Override
    public void undo(ClientContext ctx)
    {

    }

    @Override
    public void execute(ClientContext ctx)
    {
        Item firstItem = getObject(ctx, first);
        Item secondItem = getObject(ctx, second);
        if(firstItem == null || secondItem == null)
            return;
        firstItem.interact("Use");
        secondItem.interact("Use", firstItem.name());
    }
}
