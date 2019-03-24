package vaflis.lt.saltyjuice.dragas.powerbot.actions.interacting;

import vaflis.lt.saltyjuice.dragas.powerbot.Constant;
import vaflis.lt.saltyjuice.dragas.powerbot.Utility;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class HosidiusCookerInteractingAction extends ObjectInteractingAction
{

	@Override
	protected int getObjectID() {
		return Constant.Objects.Stove.HOSIDIUS_CLAY_COOKER;
	}

	@Override
	protected int getSearchRadius() {
		return 9;
	}

	@Override
	protected void interact(GameObject obj) {
		obj.ctx.camera.turnTo(obj);
		obj.ctx.inventory.select().id(Constant.Item.RAW_LOBSTER).poll().interact("Use");
		obj.interact("Use", "Clay oven");
		Condition.wait(() -> isFinished(obj.ctx), 300, 20);
	}

	@Override
	public boolean isFinished(ClientContext ctx) {
		return Utility.areChoicesVisible(ctx);
	}
}
