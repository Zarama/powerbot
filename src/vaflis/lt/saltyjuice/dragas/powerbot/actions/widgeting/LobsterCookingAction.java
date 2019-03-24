package vaflis.lt.saltyjuice.dragas.powerbot.actions.widgeting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import vaflis.lt.saltyjuice.dragas.powerbot.Utility;
import vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting.ParticularChoiceInteractingAction;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Widget;

public class LobsterCookingAction extends ParticularChoiceInteractingAction
{
	public LobsterCookingAction()
	{
		super(Constant.Widget.CHOICE.FIRST, "Cook");
	}

	@Override
	protected void interact(Widget w, Component c) {
		super.interact(w, c);
		Condition.wait(() -> Utility.hasLeveledUp(w.ctx) || isFinished(w.ctx), 500, 3600);
	}

	@Override
	public boolean isFinished(ClientContext ctx) {
		return ctx.inventory.select().id(Constant.Item.RAW_LOBSTER).count() == 0;
	}
}
