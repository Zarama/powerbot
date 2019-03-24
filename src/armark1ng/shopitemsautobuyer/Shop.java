package armark1ng.shopitemsautobuyer;

import java.util.ArrayList;
import java.util.List;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.MenuCommand;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.Npc;

public class Shop extends ItemQuery<Item> {

	public static int SHOP_WIDGET = 300, MASTER_COMPONENT = 1, SHOP_ITEMS = 2, SHOP_CLOSE_COMPONENT = 11;

	private int npcId;

	public Shop(final ClientContext ctx, int npcId) {
		super(ctx);
		this.npcId = npcId;
	}

	private Npc getNpc() {
		return ctx.npcs.nearest().select().id(npcId).poll();
	}

	public boolean open() {
		if (opened()) {
			return true;
		}
		final Npc npc = getNpc();
		if (!npc.inViewport()) {
			ctx.movement.step(npc);
			ctx.camera.turnTo(npc);
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					if (npc.inViewport())
						return true;
					ctx.movement.step(npc);
					ctx.camera.turnTo(npc);
					return false;
				}
			}, 150, 15);
		}
		final Filter<MenuCommand> filter = new Filter<MenuCommand>() {
			@Override
			public boolean accept(final MenuCommand command) {
				final String action = command.action;
				return action.equalsIgnoreCase("Trade");
			}
		};

		if (npc.hover()) {
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return ctx.menu.indexOf(filter) != -1;
				}
			}, 100, 3);
		}
		if (npc.interact(filter)) {
			do {
				Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return opened();
					}
				}, 150, 15);
			} while (ctx.players.local().inMotion());
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return opened();
				}
			}, 100, 15);
		}
		return opened();
	}

	public boolean hasItem(final int id) {
		return select().id(id).poll().stackSize() >= 0;
	}

	public boolean buy(final int id, int amount) {
		return buy(select().id(id).poll(), amount);
	}

	public boolean buy(final Item item, int amount) {
		if (!opened()) {
			return false;
		}
		if (!item.valid()) {
			return false;
		}
		final String action = "Buy " + amount;
		final int cache = ctx.inventory.select().count(true);
		if (item.contains(ctx.input.getLocation())) {
			if (!(ctx.menu.click(new Filter<MenuCommand>() {
				@Override
				public boolean accept(final MenuCommand command) {
					return command.action.equalsIgnoreCase(action);
				}
			}) || item.interact(action))) {
				return false;
			}
		} else if (!item.interact(action)) {
			return false;
		}
		return Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return cache != ctx.inventory.select().count(true);
			}
		});
	}

	public int getAmountOf(final int itemId) {
		for (Item item : get()) {
			if (item.id() == itemId)
				return item.stackSize();
		}
		return -1;
	}

	@Override
	public Item nil() {
		return new Item(ctx, null, -1, -1, -1);
	}

	@Override
	protected List<Item> get() {
		final List<Item> items = new ArrayList<Item>();
		if (!opened()) {
			return items;
		}
		for (int i = 1; i < ctx.widgets.widget(SHOP_WIDGET).component(SHOP_ITEMS).components().length; i++) {
			Component c = ctx.widgets.widget(SHOP_WIDGET).component(SHOP_ITEMS).components()[i];
			final int id = c.itemId(), stack = c.itemStackSize();
			if (id >= 0 && stack >= 0) {
				items.add(new Item(ctx, c, id, stack));
			}
		}
		return items;
	}

	public boolean opened() {
		return ctx.widgets.widget(SHOP_WIDGET).component(MASTER_COMPONENT).visible();
	}

	public boolean close() {
		return !opened() || (ctx.widgets.widget(SHOP_WIDGET).component(MASTER_COMPONENT).component(SHOP_CLOSE_COMPONENT)
				.click(true) && Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return !opened();
					}
				}, 30, 10));
	}

	public int getNpcId() {
		return npcId;
	}

}
