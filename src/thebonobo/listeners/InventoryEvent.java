package thebonobo.listeners;

import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Item;

import java.util.EventObject;

/**
 * Created with IntelliJ IDEA
 * User: Phantomist96
 * Date: 08/24/17
 */
public class InventoryEvent extends EventObject {

    private final int inventorySlot;
    private final Item oldItem;
    private final Item newItem;

    public InventoryEvent(int inventorySlot, Item oldItem, Item newItem) {
        super(inventorySlot);
        this.inventorySlot = inventorySlot;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public Item getOldItem() {
        return oldItem;
    }

    public Item getNewItem() {
        return newItem;
    }

    public int getValueChange() {
        GeItem oldItem = new GeItem(this.oldItem.id());
        GeItem newItem = new GeItem(this.newItem.id());

        return newItem.price - oldItem.price;
    }

}