package LoftySoM.clayHumidifier;

import LoftySoM.scriptHelper.Helper;
import LoftySoM.scriptHelper.HelperSingleton;
import LoftySoM.scriptHelper.PaintSingleton;
import LoftySoM.scriptHelper.SkillTracker;
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.*;


import java.awt.*;

/**
 * Created by Raphael on 2-3-2019.
 */
@Script.Manifest(name = "Clay Humidifier Beta", description = "Simple clay humidifier.",
        properties = "author=LoftySoM; topic=1350155; client=4;")
public class ClayHumidifier extends PollingScript<ClientContext> implements PaintListener {
    private Helper helper;
    private int clayCount = 0;
    private int astralRunesUsed;
    private SkillTracker skillTracker;
    private String clay = "Clay";
    private String softClay = "Soft clay";

    @Override
    public void start() {
        super.start();
        HelperSingleton.setContext(ctx);
        helper = HelperSingleton.getHelper();
        skillTracker = HelperSingleton.getSkillTracker();
        PaintSingleton.initiate();
        Condition.wait(ctx.game::loggedIn);
        // todo Add withdrawing Astral runes from bank
        if (!helper.inventoryContains("Astral rune")) {
            helper.alert("Please start with Astral runes in inventory", "No Astral runes found.");
            ctx.controller.stop();
            return;
        }
        // todo Add withdrawing battlestaff from bank
        if (!ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).name().toLowerCase().contains("steam")) {
            helper.alert("Please start with a Steam/Mystical steam battlestaff equipped", "No Steam battlestaff equipped");
            ctx.controller.stop();
            return;
        }
        // todo add support for water and fire runes
        if (!ctx.bank.nearest().tile().matrix(ctx).valid()) {
            helper.alert("Please start near a bank!", "No bank found.");
            ctx.controller.stop();
            return;
        }
        if (ctx.magic.book() != Magic.Book.LUNAR) {
            helper.alert("Please start on the lunar spell book!", "Wrong spell book.");
            ctx.controller.stop();
            return;
        }
        PaintSingleton.paintAboveChat(true);
    }

    @Override
    public void poll() {
        State state = getState();
        switch (state) {
            case BANK:
                helper.setStatus("Banking");
                if (ctx.bank.opened()) {
                    if (!ctx.inventory.select().name(softClay).isEmpty()) {
//                        if (!helper.drawNumber(20))
//                            ctx.bank.depositInventory();
//                        else
//                        ctx.bank.depositAllExcept("Astral rune");
                        ctx.bank.deposit(ctx.inventory.peek().id(), 27);
                        helper.sleep();
                    } else if (ctx.inventory.select().name(clay).isEmpty()) {
                        ctx.bank.select().name(clay);
                        if (!ctx.bank.isEmpty()) {
                            helper.log("Withdrawing Clay");
                            ctx.bank.withdraw(ctx.bank.peek().id(), Bank.Amount.ALL);
                            ctx.input.send("{VK_ESCAPE}");
                        } else {
                            helper.setStatus("Stopping..");
                            helper.alert("No clay found in bank", "No more clay");
                            ctx.controller.stop();
                        }
                    } else {
                        ctx.input.send("{VK_ESCAPE}");
                        helper.sleep();
                    }
                } else {
                    ctx.objects.select().name("Grand Exchange booth").action("Bank").within(2);
                    if (!ctx.objects.isEmpty())
                        Condition.wait(() -> ctx.objects.peek().interact("Bank"), 300);
                    else
                        Condition.wait(ctx.bank::open);
                    Condition.wait(ctx.bank::opened);
                }
                break;
            case CAST_HUMIDIFY:
                helper.setStatus(String.format("Casting %s", Magic.LunarSpell.HUMIDIFY));
                if (ctx.inventory.select().name("Astral rune").isEmpty()) {
                    helper.alert("No Astral runes in inventory", "No astral runes.");
                    ctx.controller.stop();
                }
                ctx.magic.cast(Magic.LunarSpell.HUMIDIFY);
                incrementClayCount();
                astralRunesUsed++;
                if (!helper.drawNumber(5)) {
                    helper.sleep();
                    ctx.objects.select().name("Bank booth").nearest().peek().hover();
                }
                Condition.wait(() -> !ctx.inventory.select().name("Soft clay").isEmpty());
                incrementClayCount();
                helper.sleep();
                break;
        }
    }

    enum State {
        BANK,
        CAST_HUMIDIFY,
        ANIMATING
    }

    private void incrementClayCount() {
        int oldClay = clayCount;
        clayCount = oldClay + ctx.inventory.select().name(softClay).count();
    }

    private State getState() {
//
//        // regular
//        boolean valid = ctx.inventory.select().name(clay).peek().valid();
//
//        // contains
//        ctx.inventory.select(i -> i.name().contains(clay));
//        boolean valid2 = ctx.inventory.peek().valid();
//        boolean contains2 = helper.inventoryContains(clay);
//
//        // regular
//        boolean valid3 = ctx.inventory.select().name(softClay).peek().valid();
//
//        // contains
//        ctx.inventory.select(i -> i.name().contains(softClay));
//        boolean valid4 = ctx.inventory.peek().valid();
//        Item item = helper.grabFromInventory(clay, false);
//        boolean itemValid = item.valid();
        ctx.inventory.select().name(clay);
        if (ctx.bank.opened() || ctx.inventory.isEmpty())
            return State.BANK;
        else if (!ctx.inventory.isEmpty())
            return State.CAST_HUMIDIFY;
        return State.ANIMATING;
    }

    @Override
    public void repaint(Graphics graphics) {
        if (!PaintSingleton.ready())
            return;
        PaintSingleton.update(graphics);
        // todo Clay softened per hour
        PaintSingleton.paintAll(
                String.format("Status: %s", helper.getStatus()),
                String.format("Astral runes used: %d", astralRunesUsed),
                String.format("Clays softened: %d", clayCount),
                String.format("Clays made per hour: %s", (int) (helper.second() * clayCount)),
                String.format("Time elapsed: %s", skillTracker.time(ctx.controller.script().getRuntime()))
        );
    }
}
