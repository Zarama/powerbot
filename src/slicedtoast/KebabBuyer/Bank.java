package slicedtoast.KebabBuyer;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Bank extends Task
{
    public Bank(ClientContext ctx) {
        super(ctx);
    }

    final int[] bankBounds = {-48, 56, -208, 0, -64, 44};

    @Override
    public boolean activate()
    {
        return ctx.inventory.select().count() == 28 || ctx.inventory.select().id(995).isEmpty(); //activate if inventory is full, or out of coins
    }

    @Override
    public void execute()
    {
        //checkRun();
        rotateCamBank();
        GameObject bank = ctx.objects.select().id(6943).nearest().poll(); //select a bank booth that is viewable
        bank.bounds(bankBounds); //set click boundary
        bank.interact("Bank");
        sleep(1000);
        while(!ctx.players.local().inMotion() && !ctx.widgets.widget(231).component(2).visible())
        {
            //checkDoor(); //open door if need be
            ctx.movement.step(bank); //walk towards it
            ctx.camera.turnTo(bank); //turn towards it
            bank.interact("Bank");
        }
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.bank.opened();
            }
        }, 1000, 20); //check to see if bank is open once every second for 20 seconds
        sleep(getRand(950, 3050));
        ctx.bank.depositAllExcept(995); //deposit everything except coins
        sleep(getRand(300, 500));
        ctx.bank.close();
    }

    private void rotateCamBankMulti() throws InterruptedException
    {
        Thread thread1 = new Thread()
        {
            public void run()
            {
                ctx.camera.angle(getRand(132, 190));
            }
        };
        Thread thread2 = new Thread()
        {
            public void run()
            {
                ctx.camera.pitch(getRand(0, 27));
            }
        };
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private void rotateCamBank()
    {
        try
        {
            rotateCamBankMulti(); //rotate camera towards the thingy
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}