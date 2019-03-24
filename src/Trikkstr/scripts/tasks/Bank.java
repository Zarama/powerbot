package Trikkstr.scripts.tasks;

import Trikkstr.scripts.goblin_killer.Constants;
import Trikkstr.scripts.goblin_killer.GoblinKiller;
import Trikkstr.scripts.goblin_killer.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Bank extends Task
{
    private final Walker walker = new Walker(ctx);

    private int bankBalance;
    private int spendable;
    private int foodAvailable;

    public Bank(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        //will not go to the bank to get food unless at least 30 coins are in the inventory while they are near goblins,
        //and if the player has no food, and  if the player is not in combat
        //this allows the 'Fight' task to execute instead so that the bot can loot nearby coins until it has enough
        return  (ctx.inventory.select().id(Constants.FOOD).count() < 1
                && !ctx.players.local().inCombat()
                && ctx.inventory.select().id(Constants.COINS).count(true) > 29) ||
                (ctx.players.local().tile().x() > Constants.GATE_SOUTH_SIDE && !GoblinKiller.getBanked()
                        && (ctx.inventory.select().id(Constants.FOOD).count() < 1 ||
                        ctx.inventory.select().id(Constants.COINS).count(true) < 10));
    }

    @Override
    public void execute()
    {
        GoblinKiller.setStatus("Banking");
        System.out.printf("Executing Bank.\n");

        //Walk to the bank
        if(ctx.players.local().tile().distanceTo(ctx.bank.nearest()) > 5)
        {
            walkToBank();
        }

        else if(ctx.players.local().tile().distanceTo(ctx.bank.nearest()) < 6)
        {
            makeDeposit();

            makeWithdraw();
        }
    }

    private void walkToBank()
    {
        GoblinKiller.setSubstatus("Walking to bank");

        walker.walkPathReverse(Constants.AK_BANK_TO_GOBLINS);

        //if close to the gate and not already on the other side, then pay the toll
        if(ctx.objects.select().id(Constants.AL_KHARID_GATE).poll().tile().distanceTo(ctx.players.local()) < 4
                && ctx.players.local().tile().x() < Constants.GATE_NORTH_SIDE)
        {
            System.out.printf("Opening Al-Kharid Gate\n");
            if(!ctx.objects.select().id(Constants.AL_KHARID_GATE).poll().inViewport())
            {
                ctx.camera.turnTo(ctx.objects.select().id(Constants.AL_KHARID_GATE).poll());
            }

            ctx.objects.select().id(Constants.AL_KHARID_GATE).poll().interact("Pay-toll(10gp)", "Gate");

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return ctx.players.local().tile().x() > Constants.GATE_SOUTH_SIDE;
                }
            }, 500, 6);
        }
    }

    private void makeDeposit()
    {
        GoblinKiller.setSubstatus("Making deposit");

        ctx.camera.turnTo(ctx.bank.nearest());

        //open the bank
        while (!ctx.bank.opened() && ctx.players.local().tile().distanceTo(ctx.bank.nearest().tile()) <= 5)
        {
            ctx.bank.open();
        }

        ctx.bank.depositInventory();

        //wait a moment for items to deposit
        Condition.sleep(Random.nextInt(1500, 2666));
    }

    private void makeWithdraw()
    {
        GoblinKiller.setSubstatus("Making withdraw");
        //check the balance
        bankBalance = ctx.bank.select().id(Constants.COINS).count(true);
        System.out.printf("Balance: %d\n", bankBalance);

        //subtract 10 from the balance (10 coins saved to get back to the goblins
        spendable = bankBalance - 10;
        System.out.printf("Spendable: %d\n", spendable);

        //check to see if there are kebabs in the bank
        foodAvailable = ctx.bank.select().id(Constants.FOOD[1]).count(true);
        System.out.printf("Food Available In Bank: %d\n", foodAvailable);

        //if there are 10 coins or less and no food then exit (10 to cross and at least one coin or food to eat)
        if (bankBalance < 10)
        {
            System.out.printf("Not enough coins: 10 are needed to pay the gate toll.\n");
            ctx.controller.stop();
        }
        else if (bankBalance < 11 && foodAvailable < 1)
        {
            System.out.printf("Not enough supplies: Need at least 10 coins and 1 kebab OR just 11 coins.\n");
            ctx.controller.stop();
        }
        else
            ;

        ctx.bank.withdraw(Constants.COINS, 10);

        //if there is no food
        if (foodAvailable < 1)
        {
            //withdraw up to 10 additional coins
            if (spendable < 10)
                ctx.bank.withdraw(Constants.COINS, spendable);
            else
                ctx.bank.withdraw(Constants.COINS, 10);

            ctx.bank.close();
        }
        //if there are less than 10 kebabs then just withdraw what is available
        //otherwise withdraw 10 kebabs
        else if (foodAvailable < 10)
        {
            ctx.bank.withdraw(Constants.FOOD[1], foodAvailable);
            ctx.bank.close();
        }
        else
        {
            ctx.bank.withdraw(Constants.FOOD[1], 10);
            ctx.bank.close();
        }

        GoblinKiller.setBanked(true);
        Condition.sleep(1000);
    }
}
