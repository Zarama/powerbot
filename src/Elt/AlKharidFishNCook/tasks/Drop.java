package Elt.AlKharidFishNCook.tasks;

import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import Elt.AlKharidFishNCook.Task;

public class Drop extends Task {

    public Drop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() == 28 && !hasRawFish());
    }

    @Override
    public void execute() {
        ctx.inventory.select().id(cookedFishIds).each(new Filter<Item>() {
           @Override
           public boolean accept(Item item) {
               return item.interact("Drop");
           }
        });
    }

}
