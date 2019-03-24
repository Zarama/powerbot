package thebonobo.listeners;

import org.powerbot.script.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: Phantomist96
 * Date: 08/24/17
 */

public class InventoryEventSource implements Runnable {

    private final EventDispatcher dispatcher;
    private final ClientContext ctx;
    private final int inventorySlots;
    private final Map<Integer, Item> inventoryCache;

    public InventoryEventSource(EventDispatcher dispatcher, ClientContext ctx) {
        this.dispatcher = dispatcher;
        this.ctx = ctx;
        this.inventorySlots = 28;
        this.inventoryCache = new HashMap<Integer, Item>();

        for (int i = 0; i < inventorySlots; i++) {
            inventoryCache.put(i, getInventoryItem(i));
        }

    }

    @Override
    public void run() {
        while (dispatcher.isRunning()) {
            for (int i = 0; i < inventorySlots; i++) {

                Item oldItem = inventoryCache.get(i);
                Item newItem = getInventoryItem(i);

                if (oldItem != null && newItem != null) {
                    if (oldItem.id() != newItem.id()) {
                        dispatcher.fireEvent(new InventoryEvent(i, oldItem, newItem));
                        inventoryCache.put(i, newItem);
                    }
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private Item getInventoryItem(int inventoryIndex) {
        return ((org.powerbot.script.rt4.ClientContext) ctx).inventory.itemAt(inventoryIndex);
    }

}