package vaflis.lt.saltyjuice.dragas.powerbot;

import java.util.Deque;
import java.util.LinkedList;
import java.util.logging.Level;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.BankClosingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.camera.CameraTurningAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.depositing.DepositAllAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.withdrawing.LobsterWithdrawingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.HosidiusBankInteractingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.HosidiusCookerInteractingAction;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.widgeting.LobsterCookingAction;
import org.powerbot.script.Condition;

/*@Script.Manifest(
		name = "Troubled lobsters",
		description = "Cooks lobsters in hosidius house kitchen for that sweet sweet xp"
)*/
public class TroubledLobster extends AbstractPollingScript {

	private Deque<Action> queue = null;

	@Override
	void onPoll() {
		final Action action = queue.peek();
		log.fine("Got action" + action.toString());
		if(action.isFinished(ctx))
		{
			queue.removeFirst();
			queue.addLast(action);
		}
		else if(action.isUsable(ctx))
		{
			action.execute(ctx);
			Condition.wait(() -> action.isFinished(ctx), 200, 10);
		}
		else
		{
			Action lastAction = queue.removeLast();
			action.undo(ctx);
			queue.addFirst(lastAction);
		}
	}

	@Override
	public void stop() {
		log.fine("Stopping");
	}

	@Override
	public void suspend() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void start() {
		log.setLevel(Level.ALL);
		log.fine("Starting script");
		queue = new LinkedList<>();
		queue.addLast(new HosidiusBankInteractingAction());
		queue.addLast(new DepositAllAction());
		queue.addLast(new LobsterWithdrawingAction());
		queue.addLast(new BankClosingAction());
		queue.addLast(new HosidiusCookerInteractingAction());
		queue.addLast(new LobsterCookingAction());
		queue.addLast(new CameraTurningAction());
	}
}
