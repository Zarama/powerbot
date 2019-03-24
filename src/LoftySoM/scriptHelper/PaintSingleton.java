package LoftySoM.scriptHelper;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Widget;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PaintSingleton {
    private static Helper helper;
    private static Graphics graphics;
    private static Graphics2D graphics2D;
    private static ClientContext ctx;
    private static List<String> paintList = new ArrayList<>();
    private static boolean paintAboveChat;
    private static Rectangle stopAtLevelRect = new Rectangle(390, 355, 25, 25);

    public static void initiate() {
        helper = HelperSingleton.getHelper();
        ctx = HelperSingleton.getContext();
        graphics = null;
    }

    public static void update(Graphics graphics_) {
        paintList.clear();
        PaintSingleton.graphics = graphics_;
        graphics2D = (Graphics2D) graphics_;
    }


    public static void addPaintString(String string) {
        paintList.add(string);
    }

    public static void addPaintStrings(String[] strings) {
        for (String string : strings)
            addPaintString(string);
    }

    public static void paintAll(List<String> strings) {
        paintAll(strings.toArray(new String[0]));
    }

    public static void paintAll() {
        paintAll(paintList);
    }

    public static void paintAll(String status, List<String> strings) {
        strings.add(0, status);
        paintAll(strings.toArray(new String[0]));
    }

    public static void paintAll(String... strings) {
//        if (ctx.controller.isSuspended())
//            return;
        int increment = 15;
        int y;
        if (!paintAboveChat)
            y = 480 - 15 * strings.length;
        else
            y = 350 - 15 * strings.length;
        int x = 10;
        // hide username
        graphics2D.setColor(new Color(0, 0, 0, 255));
        graphics2D.fillRect(10, 460, 85, 10);
        // paint string rect
        graphics2D.setColor(new Color(255, 107, 107, 180));
        graphics2D.fillRect(x - 3, y - 15, 250, 15 * strings.length + 5);

        graphics2D.setColor(new Color(255, 255, 255));
        for (String string : strings) {
            graphics2D.drawString(string, x, y);
            y += increment;
        }
    }

    public static boolean ready() {
        return ctx != null;
    }

    public static void paintWidgets() {
        int prevArea = 384292;
        Component smallestVisibleComp = null;
        for (Widget w : ctx.widgets) {
            for (Component c : w.components()) {
                if (c.visible() &&
                        c.boundingRect().contains(ctx.input.getLocation()) &&
                        c.boundingRect().getWidth() * c.boundingRect().getWidth() < prevArea) {
                    prevArea = (int) (c.boundingRect().getWidth() * c.boundingRect().getWidth());
                    smallestVisibleComp = c;
                }
            }
        }

        if (smallestVisibleComp != null) {
            graphics.setColor(Color.RED);
            graphics.drawString("Widget: " + smallestVisibleComp.widget().id(), 13, 360);
            graphics.drawString("Component: " + smallestVisibleComp.index(), 13, 380);
            smallestVisibleComp.draw(graphics);
        }
    }

    public static void drawStringOverShape(Shape shape, String string) {
        Rectangle drawPoint = shape.getBounds();
        int dx = graphics2D.getFontMetrics().stringWidth(string);
        graphics2D.drawString(string, drawPoint.x - (dx / 2), drawPoint.y - 5);
    }

    public static void drawStringOverShape(Shape shape, String string, int offSet) {
        Rectangle drawPoint = shape.getBounds();
        int dx = graphics2D.getFontMetrics().stringWidth(string);
        graphics2D.drawString(string, drawPoint.x - (dx / 2), drawPoint.y - 5 + offSet);
    }
    public static void drawStopAtLevelBox(){
//        Font font1 = new Font("Perpetua", Font.PLAIN, 14);
        Graphics2D g = (Graphics2D) graphics;
        g.fill(stopAtLevelRect);
        g.draw(stopAtLevelRect);
        int dx = g.getFontMetrics().stringWidth("" + HelperSingleton.getSkillTracker().getGoal());
        g.setFont(new Font("Perpetua", Font.BOLD, 15));
        drawStringOverShape(stopAtLevelRect, "Stopping at: ");
        g.setFont(new Font("Perpetua", Font.PLAIN, 14));
        g.setColor(Color.orange);
        g.drawString("" + HelperSingleton.getSkillTracker().getGoal(), (float) stopAtLevelRect.getCenterX() - (dx / 2), (float) stopAtLevelRect.getCenterY() + 3);
    }
    public static void paintAboveChat(boolean b) {
        paintAboveChat = b;
    }
}
