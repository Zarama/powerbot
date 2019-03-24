package thebonobo.listeners;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA
 * User: Phantomist96
 * Date: 08/24/17
 */
public interface InventoryListener extends EventListener {

    void onInventoryChange(InventoryEvent inventoryEvent);
}