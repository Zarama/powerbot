package vaflis.lt.saltyjuice.dragas.powerbot;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.BankClosingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.BankOpeningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.ParticularBankOpeningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.depositing.DepositAllAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.withdrawing.ParticularWithdrawingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.withdrawing.WithdrawingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.camera.CameraTurningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.FoodEatingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.OuraniaAltarInteractingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.ParticularObjectInteractingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.TeleportingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.opening.TabOpeningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.walking.ParticularWalkingAction;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Magic;

import java.util.*;
import java.util.logging.Level;

@Script.Manifest(
        name="Ourunia",
        description = "Runs ourania altar for that sweet runecrafting XP. Read the thread for requirements!",
        properties = "author=Vaflis; client=4; topic=1345732")
public class Ourunia extends AbstractPollingScript
{
    private Deque<Action> queue;
    private Tile ouraniaTeleportDestination;
    private Tile moonClanTeleportDestination;
    private Tile ouraniaAltarDestination;
    private Tile moonClanBankDestination;
    private Tile trapdoorDestination;
    private Tile midpoint;
    private Tile ladderUndergroundLocation;
    private Tile bankTile;

    @Override
    void onPoll()
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
            //Condition.wait(() -> !action.isUsable(ctx), 200, 10);
        }
        else
        {
            previousAction();
        }
    }

    @Override
    public void start()
    {
        log.setLevel(Level.ALL);
        queue = new LinkedList<>();
        final boolean jackIsThere = ctx.npcs.select().name("'Bird's-Eye' Jack").first().peek().valid();
        this.ouraniaTeleportDestination =  new Tile(2467, 3245, 0);
        this.moonClanTeleportDestination = new Tile(2112, 3914, 0);
        this.ouraniaAltarDestination = new Tile(3058, 5579, 0);
        this.moonClanBankDestination = new Tile(2099, 3918, 0);
        this.trapdoorDestination = new Tile(2455, 3233, 0);
        this.midpoint = new Tile(3017, 5593, 0);
        this.ladderUndergroundLocation = new Tile(3015, 5629, 0);
        this.bankTile =  new Tile(2097, 3919, 0);
        if(jackIsThere)
            queue.add(new BankOpeningAction());
        else
            queue.add(new ParticularBankOpeningAction(16700, bankTile));
        /*queue.add(new DepositAllAction());
        queue.add(new ParticularWithdrawingAction(Constant.Item.COOKED_LOBSTER, 8));
        queue.add(new BankClosingAction());
        queue.add(new TabOpeningAction(Game.Tab.INVENTORY));
        queue.add(new FoodEatingAction(Constant.Item.COOKED_LOBSTER, 12));
        queue.add(new ParticularBankOpeningAction(16700, bankTile));*/
        queue.add(new DepositAllAction());
        queue.add(new CameraTurningAction());
        queue.add(new ParticularWithdrawingAction(Constant.Item.LAW_RUNE, 2));
        //queue.add(new ParticularWithdrawingAction(Constant.Item.EARTH_RUNE, 8));
        queue.add(new ParticularWithdrawingAction(Constant.Item.ASTRAL_RUNE, 4));
        queue.add(new CameraTurningAction());
        queue.add(new ParticularWithdrawingAction(Constant.Item.PURE_ESSENCE, 0));
        queue.add(new BankClosingAction());
        queue.add(new TabOpeningAction(Game.Tab.MAGIC));
        queue.add(new CameraTurningAction());
        queue.add(new TeleportingAction(Magic.LunarSpell.OURANIA_TELEPORT, ouraniaTeleportDestination));
        //running
        queue.add(new ParticularWalkingAction(trapdoorDestination, ouraniaTeleportDestination));
        queue.add(new ParticularObjectInteractingAction(29635, "Climb"));
        queue.add(new ParticularWalkingAction(midpoint, ladderUndergroundLocation));
        queue.add(new ParticularWalkingAction(ouraniaAltarDestination, midpoint));
        queue.add(new OuraniaAltarInteractingAction());
        queue.add(new TeleportingAction(Magic.LunarSpell.MOONCLAN_TELEPORT, moonClanTeleportDestination));
        queue.add(new ParticularWalkingAction(moonClanBankDestination, moonClanTeleportDestination));
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
    public void stop()
    {
        super.stop();
        log.fine("Stopping.");
    }
}
