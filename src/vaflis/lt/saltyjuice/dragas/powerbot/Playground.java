package vaflis.lt.saltyjuice.dragas.powerbot;


import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.BankClosingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.BankOpeningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.depositing.DepositAllAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.camera.CameraTurningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.withdrawing.FlaxWithdrawingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.climbing.lumbridge.ClimbDownAtSouthAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.climbing.lumbridge.ClimbUpAtSouthAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.SpinnerInteractingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.opening.LumbridgeSpinnerRoomDoorOpeningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.lumbridge.GotoBankAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.lumbridge.GotoSouthStairsAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.widgeting.FlaxSpinningAction;
import org.powerbot.script.Condition;

import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;

/*@Script.Manifest(
        name = "Playground",
        description = "It's going to do things"
)*/
public class Playground extends AbstractPollingScript
{
    Deque<Action> queue;
    //Thread pollingThread;
    @Override
    public void onPoll()
    {
        final Action action = queue.peek();
        log.fine("Got action" + action.toString());
        if(action.isFinished(ctx))
        {
            nextAction();
        }
        else if(action.isUsable(ctx))
        {
            action.execute(ctx);
            Condition.wait(() -> !action.isUsable(ctx), 200, 10);
        }
        else
        {
            action.undo(ctx);
            previousAction();
        }
        /*
        Widget a = ctx.widgets.select().id(Constant.Widget.CHOICES).poll();
        Condition.wait(a::valid);
        Component b = a.component(Constant.Widget.CHOICE.THIRD);
        b.interact("Spin");
        Condition.wait(() -> ctx.inventory.select(it -> it.id() == Constant.Item.FLAX).count() == 0, 1000, 10);*/
    }

    private void nextAction()
    {
        Action a = queue.removeFirst();
        queue.addLast(a);
    }

    private void previousAction()
    {
        Action lastAction = queue.removeLast();
        queue.addFirst(lastAction);
    }

    @Override
    public void start()
    {
        //factory = new ActionFactory();
        queue = new LinkedList<>();
        queue.addLast(new BankOpeningAction());
        queue.addLast(new DepositAllAction());
        queue.addLast(new FlaxWithdrawingAction());
        queue.addLast(new BankClosingAction());
        queue.addLast(new GotoSouthStairsAction());
        queue.addLast(new ClimbDownAtSouthAction());
        //queue.addLast(new GotoSpinnerRoomAction());
        queue.addLast(new CameraTurningAction());
        queue.addLast(new LumbridgeSpinnerRoomDoorOpeningAction());
        queue.addLast(new SpinnerInteractingAction());
        queue.addLast(new FlaxSpinningAction());
        queue.addLast(new LumbridgeSpinnerRoomDoorOpeningAction());
        queue.addLast(new GotoSouthStairsAction());
        queue.addLast(new ClimbUpAtSouthAction());
        queue.addLast(new GotoBankAction());
        log.setLevel(Level.ALL);
        log.fine("Starting script");
    }

    @Override
    public void stop()
    {
        log.fine("Stopping script");
    }

    @Override
    public void suspend()
    {
        log.fine("Suspended");
    }

    @Override
    public void resume()
    {
        log.fine("Resumed");
    }
}
