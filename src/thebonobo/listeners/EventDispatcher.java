package thebonobo.listeners;

import org.powerbot.script.ClientContext;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: Anthony
 * Date: 11/16/15
 */

public class EventDispatcher {

    private final List<EventListener> listeners;
    private final Object syncLock = new Object();
    private volatile boolean running;

    public EventDispatcher(final ClientContext ctx) {
        this.listeners = new ArrayList<EventListener>();
        this.running = true;

        new Thread(new InventoryEventSource(this, ctx)).start();
    }

    public void addListener(EventListener listener) {
        synchronized (syncLock) {
            listeners.add(listener);
        }
    }

    public void removeListener(EventListener listener) {
        synchronized (syncLock) {
            listeners.remove(listener);
        }
    }

    public void clearListeners() {
        synchronized (syncLock) {
            listeners.clear();
        }
    }

    protected void fireEvent(EventObject event) {
        synchronized (syncLock) {
            for (EventListener listener : listeners) {
                if (listener instanceof InventoryListener && event instanceof InventoryEvent) {
                    ((InventoryListener) listener).onInventoryChange((InventoryEvent) event);
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}