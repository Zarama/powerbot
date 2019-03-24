package slicedtoast.KebabBuyer;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.Tile;

import java.util.concurrent.Callable;

public class Kebab extends Task {

    public Kebab(ClientContext ctx) {
        super(ctx);
    }

    Area kebabRoom = new Area(new Tile(3271, 3183), new Tile(3275, 3179));

    @Override
    public boolean activate()
    {
        return ctx.inventory.select().count() != 28 && ctx.inventory.select().id(995).count() != 0;
        //smith when inventory isn't full and you have coins
    }

    @Override
    public void execute()
    {
        Npc karim = ctx.npcs.select().id(529).poll();
        if(ctx.objects.select().id(1536).poll().inViewport() && !kebabRoom.contains(ctx.players.local())) //if door is closed
        {
            ctx.objects.select().id(1536).poll().interact("Open"); //open it
        }
        ctx.camera.turnTo(karim);
        karim.interact(false, "Talk-to", "Karim"); //right-click talk to karim for the first time
        ctx.camera.pitch(99); //make sure camera pitch is all the way up
        sleep(1000); //wait 1 second, and if you haven't moved yet, try again
        if(!ctx.players.local().inMotion() && !ctx.widgets.widget(231).component(3).visible())
        {
            ctx.camera.turnTo(karim); //turn towards him
            ctx.movement.step(karim); //walk towards him
            karim.interact("Talk-to");
        }
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.widgets.widget(231).component(3).visible();
            }
        }, 1000, 15); //check to see if chat log is open every second for 15 seconds for the first time
        buyKebab(); //run through the rest of the chat
        while(ctx.inventory.select().count() < 27 && ctx.inventory.select().id(995).count() >= 1) //while your inventory is not full, or you're not out of coins
        {
            if(ctx.controller.isStopping()){
                break;
            }
            if(!kebabRoom.contains(ctx.players.local())) //if we're not in the kebab room, relocate the guy
            {
                break;
            } else {
            karim.interact("Talk-to"); //try to left click him
            buyKebab(); //then buy 1 kebab
            }
        }
    }

    private void buyKebab()
    {
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.widgets.widget(231).component(3).visible();
            }
        }, 100, 60); //check to see if chat log is open every 100 ms for 6 seconds
        ctx.widgets.widget(231).component(3).click();
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.widgets.widget(219).component(1).component(2).visible();
            }
        }, 100, 30); //check to see if next chat log is open every 100 ms for 3 seconds
        ctx.widgets.widget(219).component(1).component(2).click();
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.widgets.widget(217).component(3).visible();
            }
        }, 100, 30); //check to see if next chat log is open every 100 ms for 3 seconds
        ctx.widgets.widget(217).component(3).click();
    }
}
