package armark1ng.shopitemsautobuyer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.MenuCommand;
import org.powerbot.script.StringUtils;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

public class WorldSwitcher extends ClientAccessor {

	public static final int WORLD_SWITCHER_WIDGET = 69, WORLD_SWITCHER_MASTER = 1, WORLD_SWITCHER_CLOSE = 3,
			WORLD_SWITCHER_CURRENT_WORLD = 2, WORLD_SWITCHER_WORLDS = 7, WORLDS_SCROLL = 15, MEMBERS_TEXTURE = 1131,
			FREE_TEXTURE = 1130;

	/*
	 * Indexes
	 */
	public static final int WORLDS_BAR = 0;
	public static final int WORLDS_TYPE = 1;
	public static final int WORLDS_NUMBER = 2;
	public static final int WORLDS_FLAG = 3;
	public static final int WORLDS_PLAYERS = 4;
	public static final int WORLDS_ACTIVITY = 5;

	public WorldSwitcher(ClientContext ctx) {
		super(ctx);
	}

	public boolean opened() {
		return ctx.widgets.widget(WORLD_SWITCHER_WIDGET).component(WORLD_SWITCHER_MASTER).visible() && ctx.widgets
				.widget(WORLD_SWITCHER_WIDGET).component(WORLD_SWITCHER_CURRENT_WORLD).text().contains("Current world");
	}

	public boolean open() {
		if (opened())
			return true;
		if (!ctx.widgets.widget(182).component(0).visible()) {
			final int widgetId = ctx.game.resizable() ? 164 : 548;
			final int componentId = ctx.game.resizable() ? 22 : 31;
			if (ctx.widgets.widget(widgetId).component(componentId).interact("Logout")) {
				Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						ctx.widgets.widget(widgetId).component(componentId).interact("Logout");
						return opened() || ctx.widgets.widget(182).component(0).visible();
					}
				}, 150, 10);
			}
		}
		final Filter<MenuCommand> filter = new Filter<MenuCommand>() {
			@Override
			public boolean accept(final MenuCommand command) {
				final String action = command.action;
				return action.equalsIgnoreCase("World Switcher");
			}
		};
		if (ctx.widgets.widget(182).component(1).hover()) {
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return ctx.menu.indexOf(filter) != -1;
				}
			}, 100, 3);
		}
		if (ctx.widgets.widget(182).component(1).interact("World Switcher")) {
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

	public boolean close() {
		return !opened() || ctx.widgets.widget(WORLD_SWITCHER_WIDGET).component(WORLD_SWITCHER_CLOSE).click(true)
				&& Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return !opened();
					}
				});
	}

	public boolean switchToWorld(final World nextWorld) {
		if (nextWorld == null || nextWorld.number == -1) {
			return false;
		}
		if (world().number == nextWorld.number)
			return true;
		final Component toWorld = ctx.widgets.widget(WORLD_SWITCHER_WIDGET).component(WORLD_SWITCHER_WORLDS)
				.component((nextWorld.index * 6) + WORLDS_BAR);
		final Component worlds = ctx.widgets.widget(WORLD_SWITCHER_WIDGET).component(WORLD_SWITCHER_WORLDS);
		final Component scrollbar = ctx.widgets.widget(WORLD_SWITCHER_WIDGET).component(WORLDS_SCROLL);
		return opened() && ctx.widgets.scroll(worlds, toWorld, scrollbar)
				&& toWorld.click("Switch", "" + nextWorld.number) && Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return world().number == nextWorld.number;
					}
				});
	}

	public World world() {
		final World nil = new World(-1, -1, -1, -1, -1, "");
		if (!opened()) {
			return nil;
		}
		final String cw = ctx.widgets.widget(WORLD_SWITCHER_WIDGET).component(WORLD_SWITCHER_CURRENT_WORLD).text()
				.replace("Current world - ", "");
		final int number;
		try {
			number = Integer.parseInt(cw) - 300;
		} catch (final NumberFormatException ignored) {
			return nil;
		}
		final List<World> worlds = worlds(new Filter<World>() {
			@Override
			public boolean accept(final World world) {
				return world.number() == number;
			}
		});
		return worlds.size() == 1 ? worlds.get(0) : nil;
	}

	public World nextWorld(final World currentWorld, final List<Integer> restrictedWorlds,
			final boolean memberWorldsOnly) {
		if (availableWorlds.isEmpty())
			return null;
		int index = 0;
		for (int i = 0; i < availableWorlds.size(); i++) {
			World available = availableWorlds.get(i);
			if (available != null && available.index == currentWorld.index) {
				index = i;
				break;
			}
		}
		return availableWorlds.get((index == availableWorlds.size() - 1) ? 0 : index + 1);
	}

	public void dumpAllWorlds() {
		List<String> worlds = new ArrayList<String>();
		for (int i = 0; i < worlds().size(); i++) {
			World world = worlds().get(i);
			if (world == null || world.number == -1)
				continue;
			if (world.number == 25 || world.number == 37)
				continue;
			worlds.add("\"World " + world.number + " - " + (world.members() ? "Member" : "Free to play") + " - "
					+ world.activity + "\"");
		}
		System.out.println(Arrays.toString(worlds.toArray(new String[worlds.size()])));
	}

	public List<World> worlds() {
		return worlds(new Filter<World>() {
			@Override
			public boolean accept(final World world) {
				return true;
			}
		});
	}

	private List<World> availableWorlds;// cache it on first time

	public List<World> availableWorlds(final List<Integer> restrictedWorlds, final boolean memberWorldsOnly,
			final int maxPlayersInWorld) {
		if (availableWorlds == null)
			availableWorlds = worlds(new Filter<World>() {
				@Override
				public boolean accept(final World world) {
					if (restrictedWorlds.contains(world.number) || (memberWorldsOnly && !world.members())
							|| world.players >= maxPlayersInWorld)
						return false;
					return true;
				}
			});
		return availableWorlds;
	}

	public World world(final int number) {
		final World nil = new World(-1, -1, -1, -1, -1, "");
		final List<World> l = worlds(new Filter<World>() {
			@Override
			public boolean accept(final World world) {
				return world.number == number;
			}
		});
		return l.size() == 1 ? l.get(0) : nil;
	}

	public List<World> worlds(final Filter<World> f) {
		final ArrayList<World> list = new ArrayList<World>();
		if (!opened()) {
			return list;
		}
		final Component w = ctx.widgets.widget(WORLD_SWITCHER_WIDGET).component(WORLD_SWITCHER_WORLDS);
		final int groupsLength = 6;
		final int worldsCount = w.componentCount() / groupsLength;
		for (int i = 0; i < worldsCount; i++) {
			final int type = (w.component((i * 6) + WORLDS_TYPE).textureId() == MEMBERS_TEXTURE) ? 1 : 0;
			final int number = StringUtils.parseInt(w.component((i * 6) + WORLDS_NUMBER).text());
			final int flagTexture = w.component((i * 6) + WORLDS_FLAG).textureId();
			final int players;
			try {
				players = Integer.parseInt(w.component((i * 6) + WORLDS_PLAYERS).text());
			} catch (final NumberFormatException ignored) {
				continue;
			}
			final String activity = w.component((i * 6) + WORLDS_ACTIVITY).text();
			final World world = new World(i, type, number, flagTexture, players, activity);
			if (f.accept(world)) {
				list.add(world);
			}
		}
		return list;
	}

	public final class World {

		private final int index;
		private final int number;
		private final int players;
		private final String activity;
		private final int type;
		private final int flagTexture;

		public World(final int index, final int type, final int number, final int flagTexture, final int players,
				final String activity) {
			this.index = index;
			this.number = number;
			this.players = players;
			this.activity = activity;
			this.flagTexture = flagTexture;
			this.type = type;
		}

		public int componentIndex() {
			return index;
		}

		public int number() {
			return number;
		}

		public int flagTexture() {
			return flagTexture;
		}

		public int players() {
			return players;
		}

		public String activity() {
			return activity;
		}

		public int type() {
			return type;
		}

		public boolean members() {
			return type == 1;
		}

		@Override
		public boolean equals(final Object o) {
			return o instanceof World && ((World) o).number == number;
		}

		@Override
		public String toString() {
			return "" + number;
		}

	}

}
