package PhDinStupidity.chocduster.tasks;

import PhDinStupidity.chocduster.DConstants;
import PhDinStupidity.chocduster.Duster;
import PhDinStupidity.chocduster.interfaces.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;

import java.util.Comparator;
import java.util.concurrent.Callable;

public class KnifeDustChocolate extends Task
{
    public KnifeDustChocolate(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        //If we have chocolate bars and a knife in our inventory.
        return     ctx.inventory.select().id(DConstants.CHOCOLATE_BAR).size() > 0
                && ctx.inventory.select().id(DConstants.KNIFE).size() > 0;
    }

    @Override
    public void execute()
    {
        //Grab the knife so we don't have to query it multiple times.
        Item knife = ctx.inventory.select().id(DConstants.KNIFE).poll();
        //Cache the chocolate dust request since we're calling it more than once.
        ItemQuery<Item> chocDust = ctx.inventory.select().id(DConstants.CHOCOLATE_DUST);
        int chocolateDustCount = (chocDust.size() > 0) ? chocDust.size() : 0; //Am I confusing this with another function? I cannot recall if size() returns -1 on empty.

        //While the script isn't being stopped and we have chocolate bars in our inventory.
        while (!ctx.controller.isStopping() && (ctx.inventory.select().id(DConstants.CHOCOLATE_BAR).size() > 0))
        {
            //Make sure we're in the inventory.
            if (ctx.game.tab(Game.Tab.INVENTORY))
            {
                //If the knife isn't in the last slot of our inventory, move it there.
                while (ctx.inventory.items()[27].id() != DConstants.KNIFE)
                {
                    ctx.input.move(knife.nextPoint());
                    ctx.input.drag(ctx.widgets.widget(261).component(77).nextPoint(), true);
                    Condition.sleep(Random.getDelay() / 2);
                }


                //If the knife is selected, otherwise click the knife.
                if (ctx.inventory.selectedItem().id() == DConstants.KNIFE || knife.click())
                {
                    //We've just clicked the knife, let's wait a drop.
                    Condition.sleep(Random.getDelay() / 2);
                    //Check that we successfully clicked the knife. Prevents eating bars.
                    if (ctx.inventory.selectedItem().id() == DConstants.KNIFE)
                    {
                        //Make sure we still have chocolate bars in our inventory. Otherwise click the knife to de-select it.
                        if (ctx.inventory.select().id(DConstants.CHOCOLATE_BAR).size() > 0)
                        {
                            //If item [26] (second to last item) is chocolate, just click that. Else find the closest one and click that.
                            if (ctx.inventory.items()[26].id() == DConstants.CHOCOLATE_BAR)
                                ctx.inventory.items()[26].interact("Use");
                            else
                                ctx.inventory.select().id(DConstants.CHOCOLATE_BAR).sort(new Comparator<Item>()
                                {
                                    @Override
                                    public int compare(Item o1, Item o2)
                                    {
                                        return (ctx.input.getLocation().distance(o1.nextPoint()) > ctx.input.getLocation().distance(o2.nextPoint())) ? 1 : -1;
                                    }
                                }).first().poll().interact("Use");
                        }
                        else if (ctx.inventory.selectedItem().valid())
                            knife.click();
                    }
                }

                //If we have 1 chocolate bar left, we probably just clicked it. So wait to see if it was. Else, wait random amount of time.
                if (ctx.inventory.select().id(DConstants.CHOCOLATE_BAR).size() == 1)
                    Condition.wait(new Callable<Boolean>()
                    {
                        @Override
                        public Boolean call() throws Exception
                        {
                            return ctx.inventory.select().id(DConstants.CHOCOLATE_BAR).size() < 1;
                        }
                    }, 25, 10);
                else
                    Condition.sleep(Random.getDelay() / 2);
            }

            //Add any new chocolate dust to our counter.
            if ((chocDust = ctx.inventory.select().id(DConstants.CHOCOLATE_DUST)).size() > chocolateDustCount)
            {
                Duster.chocolateDusted += chocDust.size() - chocolateDustCount;
                chocolateDustCount = chocDust.size();
            }
        }

        //If we have no more chocolate, but the knife is still selected, click it to de-select.
        if (ctx.inventory.selectedItem().valid())
            knife.click();
    }
}
