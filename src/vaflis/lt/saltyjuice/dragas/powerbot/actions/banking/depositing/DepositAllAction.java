package vaflis.lt.saltyjuice.dragas.powerbot.actions.banking.depositing;

import org.powerbot.script.rt4.ClientContext;

public class DepositAllAction extends DepositingAction {
	@Override
	protected int getItemId() {
		return 0;
	}

	@Override
	protected int getItemCount() {
		return 0;
	}

	@Override
	protected void deposit(ClientContext ctx) {
		ctx.bank.depositInventory();
	}

	@Override
	public boolean isUsable(ClientContext ctx) {
		return ctx.inventory.select().count() > 0;
	}

	@Override
	public boolean isFinished(ClientContext ctx) {
		return ctx.inventory.select().count() == 0;
	}
}
