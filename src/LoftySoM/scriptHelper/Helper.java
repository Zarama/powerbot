package LoftySoM.scriptHelper;

import LoftySoM.scriptHelper.utilities.CustomConstants;
import org.powerbot.script.Random;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class Helper extends ClientAccessor {
    // todo create timer class
//    public SkillTracker skillTracker;
    private static String status = "Idle";
    private List<String> paintList = new ArrayList<>();
    private List<String> lootList = new ArrayList<>();
    private boolean paintAboveChat = false;
    private GroundItem targetItem;
    private int autoLootValue = 1000;
    private Walker walker = new Walker(ctx);
    public Timer timer = new Timer(this);

    public Helper(ClientContext ctx) {
        super(ctx);
        targetItem = ctx.groundItems.nil();
        ctx.input.keyboard(true);
//        skillTracker = new SkillTracker(ctx);
    }

    public Item grabRandomFromInventory(String item, boolean exactMatch) {
        openTab(Game.Tab.INVENTORY);
        if (exactMatch)
            ctx.inventory.select().name(item).shuffle();
        else
            ctx.inventory.select(x -> x.name().contains(item)).shuffle();
        return ctx.inventory.peek().valid() ? ctx.inventory.peek() : ctx.inventory.nil();
    }

    public Item grabFromInventory(String item, boolean exactMatch) {
        if (exactMatch)
            ctx.inventory.select().name(item);
        else
            ctx.inventory.select(x -> x.name().contains(item));
        return ctx.inventory.peek().valid() ? ctx.inventory.peek() : ctx.inventory.nil();
    }

    public Item grabFromInventory(String item) {
        ctx.inventory.select(i -> i.name().contains(item));
        boolean valid = ctx.inventory.peek().valid();
        return ctx.inventory.peek().valid() ? ctx.inventory.peek() : ctx.inventory.nil();
    }

    public boolean interactWithInInventory(String item, String action) {
        Item invItem = grabFromInventory(item);
        return invItem.valid() && invItem.interact(action);
    }

    public Player getPlayer() {
        return ctx.players.local();
    }

    public Item grabUnnoted(String item) {
        ctx.inventory.select(i -> i.noted()).name(item);
        return ctx.inventory.peek().valid() ? ctx.inventory.peek() : ctx.inventory.nil();
    }

    public boolean inventoryContains(String itemName) {
        return !ctx.inventory.select(i->i.name().contains(itemName)).isEmpty();
    }

    public List<Item> getInventory() {
        return ctx.inventory.select().stream().collect(Collectors.toList());
    }

    public List<GameObject> getGameObjects() {
        return ctx.objects.select().stream().collect(Collectors.toList());
    }

    public List<GroundItem> getGroundItems() {
        return ctx.groundItems.select().stream().collect(Collectors.toList());
    }

    public List<Item> grabAllUnnoted(String item) {
        return ctx.inventory.select().name(item).stream().filter(x -> !x.noted()).collect(Collectors.toList());
    }

    public GameObject getObjectWithAction(String gameObjectName, String action) {
        ctx.objects.select().action(action).name(gameObjectName);
        if (ctx.objects.peek().valid())
            return ctx.objects.peek();
        else
            return ctx.objects.nil();
    }

    public GameObject getObjectThatDoesNotHaveSpecificAction(String gameObjectName, String action) {
        ctx.objects.select(i-> Arrays.stream(i.actions()).noneMatch(a->a.contains(action))).name(gameObjectName);
        return ctx.objects.peek().valid() ? ctx.objects.peek() : ctx.objects.nil();
    }

    public boolean doAction(Callable<Boolean> action, boolean checkMotion, boolean checkAnimation) {
        if (checkAnimation && ctx.players.local().animation() != -1)
            return false;
        if (checkMotion && ctx.players.local().inMotion())
            return false;
        boolean success;
        try {
            success = Condition.wait(action);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Condition.sleep(Random.getDelay());
        return success;
    }

    public boolean doAction(Callable<Boolean> action, boolean checkMotion, boolean checkAnimation, String message) {
        if (checkAnimation && ctx.players.local().animation() != -1)
            return false;
        if (checkMotion && ctx.players.local().inMotion())
            return false;
        boolean success;
        try {
            log(message);
            success = Condition.wait(action);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Condition.sleep(Random.getDelay());
        return success;
    }

    public boolean doAction(Callable<Boolean> action, boolean checkMotion) {
        if (checkMotion && ctx.players.local().inMotion())
            return false;
        boolean success;
        try {
            success = Condition.wait(action);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Condition.sleep(Random.getDelay());
        return success;
    }

    public boolean doAction(Callable<Boolean> action) {
        boolean success;
        try {
            success = Condition.wait(action);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Condition.sleep(Random.getDelay());
        return success;
    }

//    public int getFatigue() {
//        if (ctx == null) {
//            log("You must set the ctx before using this method.");
//            ctx.controller.suspend();
//        }
//        return 100 - (int) (ctx.controller.script().getRuntime() * 0.0025);
//    }

    public String getStatus() {
        return "Status: " + Helper.status;
    }

    public void setStatus(String s) {
        if (!Helper.status.equals(s)) {
            Helper.status = s;
            log(s);
        }

    }

    public void log(String msg) {
        ctx.controller.script().log.info(msg);
    }

    public boolean drawNumber(int i) {
        return Random.nextInt(0, i) == 1;
    }

    public boolean drag(Item item, Point slot) {
        log(String.format("Dragging %s to slot %s", item.name(), slot));

////        log(String.format("Inventory position: %s", ctx.inventoryList.indexCenterPoint(slot)));
///*        log(String.format("Inventory component ID: %d", ctx.inventoryList.component().id()));
//        log(String.format("Inventory widget ID: %d", ctx.inventoryList.component().widget().id()));*/
        openTab(Game.Tab.INVENTORY);
        Condition.sleep(Random.getDelay());

        ctx.inventory.select().name(item).poll().hover();
        ctx.input.drag(slot, true);
        return true;
    }

    public double second() {
        return 3600000d / ctx.controller.script().getRuntime();
    }


    public void face(Interactive interactive) {
        if (!interactive.inViewport())
            ctx.camera.turnTo((Locatable) interactive);
    }

    public int randomInt(int min, int max) {
        return Random.nextInt(min, max);
    }

    public int randomInt(int max) {
        return Random.nextInt(0, max);
    }

    public void goNear(Locatable locatable) {
        if (getDistanceFromPlayer(locatable) < 5)
            return;
        if (locatable.tile().matrix(ctx).inViewport()) {
            locatable.tile().derive(randomInt(3), randomInt(3)).matrix(ctx).interact("Walk here");
        } else if (getDistanceFromPlayer(locatable.tile().matrix(ctx)) < 7) {
            face(locatable.tile().matrix(ctx));
            sleep();
            locatable.tile().derive(randomInt(3), randomInt(3)).matrix(ctx).interact("Walk here");
        } else
            ctx.movement.step(locatable.tile().derive(randomInt(5), randomInt(5)));
    }

    public void target(Interactive locatable) {
        if (!locatable.inViewport()) {
            ctx.camera.turnTo((Locatable) locatable);
            sleep();
        }
        if (!locatable.inViewport())
            goNear((Locatable) locatable);
    }

    public boolean select(String item) {
        return doAction(() -> grabFromInventory(item).interact("Use"));
    }

    public boolean selectUnnoted(String item) {
        return doAction(() -> grabUnnoted(item).interact(true, "Use"));
    }

    public int getCount(String item, boolean stackable) {
        return ctx.inventory.select().name(item).count(stackable);
    }

    public int getCount(String item) {
        return ctx.inventory.select().name(item).count();
    }

    public GameObject getObjectFromArea(String gameObject, Area area) {
        for (GameObject g : ctx.objects.select().name(gameObject).sort(Comparator.comparingInt(ctx.movement::distance)).reverse()) {
            if (area.contains(g))
                return g;
        }
        return ctx.objects.nil();
    }

    public GameObject getObjectFromArea(String gameObject, Area area, Locatable nearestTo) {
        for (GameObject g : ctx.objects.select().name(gameObject).nearest(nearestTo)) {
            if (area.contains(g))
                return g;
        }
        return ctx.objects.nil();
    }

    public <T> T getRandom(List<T> list) {
        return list.get(Random.nextInt(0, list.size()));
    }

    public boolean wield(String name) {
        openTab(Game.Tab.INVENTORY);
        return grabFromInventory(name).interact("Wield");
    }

    public boolean dropAll(List<Item> items) {

        if (drawNumber(5))
            Collections.reverse(items);
        if ((ctx.varpbits.varpbit(1055) & 131072) > 0) {
            ctx.input.send("{VK_SHIFT down}");
            for (Item i : items) {
                if (!i.click(true)) {
                    ctx.input.send("{VK_SHIFT up}");
                    return false;
                }
            }
            ctx.input.send("{VK_SHIFT up}");
            return true;
        }
        for (Item i : items)
            if (!i.interact("Drop"))
                return false;
        return true;
    }

    public Area makeAreaFromTile(Tile tile, int size) {
        return new Area(
                new Tile(tile.x() + size, tile.y() + size),
                new Tile(tile.x() + size, tile.y() - size),
                new Tile(tile.x() - size, tile.y() - size),
                new Tile(tile.x() - size, tile.y() + size));
    }

    public Area makeAreaFromCenter(Locatable locatable, int size) {
        Tile tile = locatable.tile();
        return new Area(
                new Tile(tile.x() + size, tile.y() + size),
                new Tile(tile.x() + size, tile.y() - size),
                new Tile(tile.x() - size, tile.y() - size),
                new Tile(tile.x() - size, tile.y() + size));
    }

    public void drawArea(Graphics2D g, Area area) {
        Rectangle rect = area.getPolygon().getBounds();
        for (int x = 0; x < rect.width; x++) {
            for (int y = 0; y < rect.height; y++) {
                Tile tile = new Tile(rect.x + x, rect.y + y);
                if (!tile.matrix(ctx).inViewport() || ctx.bank.opened())
                    continue;
                TileMatrix matrix = tile.matrix(this.ctx);
                g.draw(matrix.bounds());
            }
        }
    }

    public boolean continueWhileLoop() {
        return !(ctx.controller.isSuspended() || ctx.controller.isStopping());
    }

    public void dropAll(String item) {
        openTab(Game.Tab.INVENTORY);
        ctx.inventory.select().name(item).each((i)->{
            setStatus(String.format("Dropping %s", i.name()));
            ctx.input.send("{VK_SHIFT down}");
            Condition.wait(i::click);
            ctx.input.send("{VK_SHIFT up}");
            sleep();
            return true;
        });
    }

    public long timePassed(long startTime) {
        return (new Date()).getTime() - startTime;
    }

    public void sleep() {
        Condition.sleep(Random.getDelay());
    }

//    public List<String> skillInfo() {
//
//        return new ArrayList<>(Arrays.asList(
//                String.format("Exp gained: %s", skillTracker.getExpGained()),
//                String.format("Exp until next level: %s", skillTracker.getExpLeft()),
//                String.format("Exp per hour: %s", skillTracker.getExpPh()),
//                String.format("%s", skillTracker.getTimeToNextLevel()),
//                String.format("Elapsed time: %s", skillTracker.time(ctx.controller.script().getTotalRuntime()))
//        ));
//    }


    public int getDistanceFromPlayer(Locatable locatable) {
        return ctx.movement.distance(getPlayer(), locatable);
    }

    public boolean openTab(Game.Tab tab) {
        if (ctx.game.tab() != tab)
            ctx.game.tab(tab);
        Condition.sleep(Random.getDelay());
        return true;
    }

    public boolean step(Locatable locatable) {
        if (getPlayer().tile().distanceTo(locatable) > 1 && (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL)
                || ctx.movement.destination().distanceTo(ctx.players.local()) < 5))
            if (locatable.tile().matrix(ctx).inViewport())
                locatable.tile().matrix(ctx).interact("Walk here");
            else
                ctx.movement.step(locatable);
        Condition.sleep(Random.getDelay());
        return true;
    }

    public boolean traverse(TilePath tilePath) {
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL)
                || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            return Condition.wait(() -> walker.walkPath(tilePath.toArray()));

        }
        return false;
    }

    public List<GroundItem> getLootableList() {
        ctx.groundItems.select(item->lootList.contains(item.name())
                || lootList.stream().anyMatch(i -> item.name().contains(i))
                || new GeItem(item.id()).price >= autoLootValue);
        return ctx.groundItems.get();
    }

    public GroundItem grabNearestLootable() {
        ctx.groundItems.select(item->lootList.contains(item.name())
                || lootList.stream().anyMatch(i -> item.name().contains(i))
                || new GeItem(item.id()).price >= autoLootValue).nearest();
        return ctx.groundItems.peek();
    }

    public boolean anyLootableItems() {
        return !getLootableList().isEmpty();
    }

    public void addLootable(String itemName) {
        lootList.add(itemName);
    }

    public void addLootables(List<String> itemNames) {
        lootList.addAll(itemNames);
    }

    public void autoLootMin(int value) {
        autoLootValue = value;
    }

    public boolean takeItem(GroundItem groundItem) {
        if (groundItem.valid() && !ctx.inventory.isFull()) {
            groundItem.interact("Take", groundItem.name());
            targetItem = groundItem;
            Condition.sleep(Random.nextInt(1000, 3000));
        }

        return true;
    }

    public List<String> getLootList() {
        return lootList;
    }

    public void suspend() {
        ctx.controller.suspend();
    }

    public void resume() {
        ctx.controller.resume();
    }


    public boolean interact(Interactive interactive, String action) {
        if (!interactive.valid())
            return false;
        target(interactive);
        sleep();
        return interactive.interact(action);
    }

    public boolean interact(Interactive interactive, String action, String entityName) {
        if (!interactive.valid())
            return false;
        target(interactive);
        sleep();
        return interactive.interact(action, entityName);
    }

    public void suspendAlert(String s) {
        JOptionPane.showMessageDialog(null,
                s, "Suspending.",
                JOptionPane.ERROR_MESSAGE, null);
    }

    public void alert(String message, String title) {
        JOptionPane.showMessageDialog(null,
                message, title,
                JOptionPane.ERROR_MESSAGE, null);
    }

    public void stopAlert(String s) {
        JOptionPane.showMessageDialog(null,
                s, "Stopping.",
                JOptionPane.ERROR_MESSAGE, null);
    }


    public void statHover(CustomConstants.SkillComponent skill) {
        openTab(Game.Tab.STATS);
        CustomConstants.getStatHover(skill.componentId).hover();
    }

    public void randomHover() {
        ctx.objects.select().shuffle().peek().tile().derive(randomInt(5), randomInt(5)).matrix(ctx).hover();
    }
}
