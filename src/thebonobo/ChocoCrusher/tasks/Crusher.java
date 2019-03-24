package thebonobo.ChocoCrusher.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import thebonobo.ChocoCrusher.utils.Antiban;
import thebonobo.ChocoCrusher.utils.Info;
import thebonobo.ChocoCrusher.utils.Items;

import java.awt.*;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 16/09/17
 */

public class Crusher extends Task<ClientContext> {
    private Item knife;
    private Item chocolateBar;
    private int xKnife;
    private int yKnife;
    private int xBar;
    private int yBar;

    public Crusher(ClientContext ctx) {
        super(ctx);
        this.knife = ctx.inventory.select().id(Items.KNIFE).poll();
    }

    @Override
    public boolean activate() {
        return !ctx.bank.opened() && ctx.inventory.select().id(Items.CHOCOLATE_BAR).count() > 0 &&
                ctx.inventory.select().id(Items.KNIFE).count() == 1;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Crushing");
        Condition.wait(() -> { useKnifeOnBar(); return ctx.inventory.select().id(Items.CHOCOLATE_BAR).count() == 0;}, 50, 28);
    }

    private void useKnifeOnBar() {
//        if (Random.nextInt(0, 50) == 3) {
//            Antiban.MoveMouseOffScreen(ctx);
//            Condition.sleep(1250);
//        }
//
        if (Random.nextInt(0, 60) == 1)
            Antiban.MissClick(ctx, Items.KNIFE);

        clickKnife();
        Antiban.Wait(1, 15);
        clickChocolateBar();
    }

    public void clickKnife(){
        xKnife = knife.centerPoint().x - 11 + Random.nextInt(0, 25);
        yKnife = knife.centerPoint().y - 12 + Random.nextInt(0, 25);
        ctx.input.click(new Point(xKnife, yKnife), true);
    }

    public void clickChocolateBar(){
        chocolateBar = ctx.inventory.select().id(Items.CHOCOLATE_BAR).reverse().poll();
        xBar = chocolateBar.centerPoint().x - 11 + Random.nextInt(0, 25);
        yBar = chocolateBar.centerPoint().y - 12 + Random.nextInt(0, 25);
        System.out.println("clicking bar at "+xBar+":"+yBar);
        if (ctx.inventory.selectedItem().valid())
            ctx.input.click(new Point(xBar, yBar), true);
    }
}
