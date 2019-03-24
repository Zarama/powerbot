package vaflis.lt.saltyjuice.dragas.powerbot;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Widget;

public class Utility
{

    public static boolean isAtFloor(ClientContext ctx, int floor)
    {
        Tile location = getCurrentTile(ctx);
        return location.floor() == floor;
    }

    public static boolean isAtLocation(ClientContext ctx, int x, int y)
    {
        Tile location = getCurrentTile(ctx);
        return location.x() == x && location.y() == y;
    }

    public static boolean isAtLocation(ClientContext ctx, int x, int y, int floor)
    {
        return isAtFloor(ctx, floor) && isAtLocation(ctx, x, y);
    }

    public static boolean hasLeveledUp(ClientContext ctx)
    {
        return ctx.widgets.select().id(Constant.Widget.LEVEL_UP).poll().valid();
    }

    public static boolean areChoicesVisible(ClientContext ctx)
    {
        return ctx.widgets.select().id(Constant.Widget.CHOICES).poll().valid();
    }

    /**
     * Checks whether or not the character is currently moving.
     * @param ctx
     * @return
     */
    public static boolean isMoving(ClientContext ctx)
    {
        return !ctx.movement.destination().equals(Tile.NIL);
    }

    public static boolean isNotMoving(ClientContext ctx)
    {
        return !isMoving(ctx);
    }

    private static Tile getCurrentTile(ClientContext ctx)
    {
        return ctx.players.local().tile();
    }


    public static boolean inventoryContains(ClientContext ctx, int itemId) {
        return ctx.inventory.select().id(itemId).count() != 0;
    }

    public static void turnTo(GameObject obj)
    {
        obj.ctx.camera.turnTo(obj);
    }

    public static Widget getLevelUpWidget(ClientContext ctx)
    {
        return ctx.widgets.select().id(Constant.Widget.LEVEL_UP).poll();
    }
}
