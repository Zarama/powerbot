package PhDinStupidity.chocduster.tasks;

import PhDinStupidity.chocduster.DConstants;
import PhDinStupidity.chocduster.interfaces.Task;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.logging.Level;

public class BankChocolate extends Task
{
    public BankChocolate(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        //If we do not have any chocolate bars or a knife in our inventory.
        return     0 >= ctx.inventory.select().id(DConstants.CHOCOLATE_BAR).size()
                || 0 >= ctx.inventory.select().id(DConstants.KNIFE).size();
    }

    @Override
    public void execute()
    {
        //Check if an item is selected. If it is, just click it to de-select.
        Item selectedItem = ctx.inventory.selectedItem();
        if (selectedItem.valid())
            selectedItem.click();

        //Open the bank and make sure we're not withdrawing noted.
        if (ctx.bank.open() && ctx.bank.withdrawModeNoted(false))
        {
            //Deposit all of the chocolate dust.
            if (ctx.bank.depositAllExcept(DConstants.KNIFE))
            {
                //If we do not have a knife in our inventory, withdraw one.
                if (0 >= ctx.inventory.select().id(DConstants.KNIFE).size())
                    ctx.bank.withdraw(DConstants.KNIFE, 1);

                //Withdraw chocolate bars.
                if (ctx.bank.withdraw(DConstants.CHOCOLATE_BAR, Bank.Amount.ALL))
                    ctx.bank.close();
                else
                {
                    //We could not withdraw chocolate bars. Log that we could not and end the script.
                    ctx.controller.script().log.log(Level.SEVERE, "Unable to withdraw Chocolate Bars.");
                    ctx.controller.stop();
                }
            }
        }
    }
}