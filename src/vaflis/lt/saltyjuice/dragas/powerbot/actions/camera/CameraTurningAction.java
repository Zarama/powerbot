package vaflis.lt.saltyjuice.dragas.powerbot.actions.camera;

import vaflis.lt.saltyjuice.dragas.powerbot.actions.Action;
import org.powerbot.script.rt4.ClientContext;

public class CameraTurningAction implements Action {
	private boolean turned = false;
	private long lastExecution = 0;
	private static final long delay = 12000;
	@Override
	public boolean isUsable(ClientContext ctx) {
		return true;
	}

	@Override
	public void execute(ClientContext ctx) {
		lastExecution = System.currentTimeMillis();
		int trigger = (int) (Math.random() * 5 + 1);
		if(!turned && trigger == 5)
		{
			ctx.input.send("{VK_LEFT}");
			turned = true;
		}
		else if(turned)
		{
			ctx.input.send("{VK_RIGHT}");
			turned = false;
		}
	}

	@Override
	public boolean isFinished(ClientContext ctx) {
		return lastExecution + delay > System.currentTimeMillis();
	}

	@Override
	public void undo(ClientContext ctx) {

	}
}
