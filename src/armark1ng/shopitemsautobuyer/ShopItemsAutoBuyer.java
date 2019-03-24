package armark1ng.shopitemsautobuyer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Player;
import armark1ng.shopitemsautobuyer.WorldSwitcher.World;

@Script.Manifest(name = "Shop Items Auto Buyer", description = "Buys items from any shop supports World Hopping/Opening Packs/Banking For both f2p/p2p players.", properties = "author=armark1ng;topic=1323639;client=4;hidden=true;")
public class ShopItemsAutoBuyer extends PollingScript<ClientContext> implements PaintListener {

	public static int[] RESTRICTED_WORLDS = { 73, 66, 61, 53, 49, 25, 37 };

	public static int[] ALWAYS_RESTRICTED = { 25, 37, 65, 45 };

	private long startTime, currentWorldHopDelay, worldHopDelay;
	private int[] itemsBoughtCount, startCount, bankedCount;
	private String[] itemNames;
	private Shop currentShop;
	private WorldSwitcher worldSwitcher;
	private State currentState;
	private int npcId, runPercentage, worldHopCounter, worldHopFrequncy, maxPlayersInWorld;
	private List<RegisteredItem> itemsToBuy;
	private List<Integer> restrictedWorlds, unRegisteredItemsIds;
	private boolean hopWorlds, banking, doingAction;
	private World currentWorld, nextWorld;
	private GUI gui;

	@Override
	public void start() {
		if (!ctx.game.loggedIn()) {
			log.info("Please Login First.");
			ctx.controller.stop();
			return;
		}
		gui = new GUI(this);
		gui.setVisible(true);
	}

	public boolean startGUI() {
		if (ctx.npcs.select().id(npcId).isEmpty()) {
			log.warning("Couldn't find shop npc with id " + npcId + " in this area!");
			return false;
		}
		if (!Constants.hasBanking(npcId))
			banking = false;
		runPercentage = 20;
		currentState = State.IDLE;
		unRegisteredItemsIds = new ArrayList<Integer>();
		for (Item item : ctx.inventory.items()) {
			if (item == null || item.id() == 995 || isRegisteredItem(item.id()))
				continue;
			if (item.stackSize() <= 0)
				continue;
			unRegisteredItemsIds.add(item.id());
		}
		maxPlayersInWorld = 1500;
		currentWorld = null;
		nextWorld = null;
		startCount = new int[itemsToBuy.size()];
		itemsBoughtCount = new int[itemsToBuy.size()];
		bankedCount = new int[itemsToBuy.size()];
		itemNames = new String[itemsToBuy.size()];
		for (int i = 0; i < itemsToBuy.size(); i++) {
			RegisteredItem registeredItem = itemsToBuy.get(i);
			if (registeredItem == null)
				continue;
			Item item = ctx.inventory.select().id(registeredItem.getItemId()).poll();
			if (item == null)
				continue;
			if (itemNames[i] == null || itemNames[i].equals(""))
				itemNames[i] = item.name();
			if (registeredItem.isPack()) {
				int itemInPackId = registeredItem.getOpenedId();
				item = ctx.inventory.select().id(itemInPackId).poll();
				startCount[i] = item.stackSize() == -1 ? 0 : item.stackSize();
			} else if (!registeredItem.isStackable())
				startCount[i] = getAmountOf(registeredItem.getItemId());
			else
				startCount[i] = item.stackSize() == -1 ? 0 : item.stackSize();
		}
		return true;
	}

	public boolean isRegisteredItem(int itemId) {
		for (RegisteredItem item : itemsToBuy) {
			if (item == null)
				continue;
			if (item.getItemId() == itemId || (item.isPack() && item.getOpenedId() == itemId))
				return true;
		}
		return false;
	}

	@Override
	public void poll() {
		if (gui == null)
			return;
		if (!gui.isStarted() && !gui.isVisible()) {
			ctx.controller.stop();
			return;
		}
		currentState = currentState == State.HOPPING_WORLDS || currentState == State.OPENING_PACKAGE
				|| currentState == State.BANKING || currentState == State.FINDING_WORLDS ? currentState : getState();
		if (currentState == null)
			return;
		if (currentShop == null && ctx.widgets.select().id(300).poll().component(1).visible()) {
			interactWith(ctx.widgets.widget(300).component(1).component(11), "Close", new Condition.Check() {
				@Override
				public boolean poll() {
					return !ctx.widgets.select().id(300).poll().component(1).visible();
				}
			});
			return;
		}
		if (doingAction)
			return;
		doingAction = true;
		switch (currentState) {
		case IDLE:
			break;
		case FINDING_WORLDS:
			if (!ctx.game.loggedIn() || ctx.game.clientState() != org.powerbot.script.rt4.Constants.GAME_LOADED)
				break;
			if (worldSwitcher != null || currentWorld != null || nextWorld != null) {
				worldSwitcher = null;
			}
			if (worldSwitcher == null) {
				worldSwitcher = new WorldSwitcher(ctx);
				worldSwitcher.open();
			}
			if (worldSwitcher != null && worldSwitcher.opened()) {
				currentWorld = worldSwitcher.world();
				if (!worldSwitcher.availableWorlds(restrictedWorlds, Constants.isMembers(npcId), maxPlayersInWorld)
						.contains(currentWorld)) {
					log.info("Current World is restricted Stopping script.");
					ctx.controller.stop();
					return;
				}
				nextWorld = worldSwitcher.nextWorld(currentWorld, restrictedWorlds, Constants.isMembers(npcId));
				currentState = getState();
			}
			break;
		case BUYING:
			if (currentShop == null)
				break;
			for (int i = 0; i < itemsToBuy.size(); i++) {
				final RegisteredItem registeredItem = itemsToBuy.get(i);
				if (registeredItem == null)
					continue;
				if (!currentShop.hasItem(registeredItem.getItemId())) {
					currentShop.close();
					currentShop = null;
					doingAction = false;
					return;
				}
				Item coins = ctx.inventory.select().id(995).poll();
				if (coins.stackSize() < registeredItem.getMinCoins()) {
					log.info("Not enough coins stopping script.");
					ctx.controller.stop();
					return;
				}
				final int minInShop = registeredItem.getMinAmountInShop();
				while (currentShop.getAmountOf(registeredItem.getItemId()) > minInShop) {
					if (!ctx.game.loggedIn() || ctx.controller.isSuspended() || ctx.controller.isStopping())
						return;
					int amountInShop = currentShop.getAmountOf(registeredItem.getItemId());
					int buyQ = minInShop == 0 ? 10
							: ((amountInShop >= 10 && (amountInShop - 10) >= minInShop) ? 10
									: (amountInShop >= 5 && (amountInShop - 5) >= minInShop) ? 5
											: (amountInShop >= 1 && (amountInShop - 1) >= minInShop) ? 1 : 0);
					if (buyQ == 0)
						break;
					if (getFreeSlots() == 0) {
						if ((registeredItem.isPack() || !registeredItem.isStackable())
								|| (registeredItem.isStackable() && getAmountOf(registeredItem.getItemId()) == 0))
							break;
					}
					if (!currentShop.buy(registeredItem.getItemId(), buyQ))
						break;
				}
			}
			if (hasPack()) {
				currentShop.close();
				currentShop = null;
				doingAction = false;
				currentState = State.OPENING_PACKAGE;
				return;
			}
			if (banking && getFreeSlots() == 0) {
				currentState = State.BANKING;
				break;
			}
			if (!hopWorlds || (hopWorlds && currentWorldHopDelay > System.currentTimeMillis())) {
				break;
			}
			currentShop.close();
			currentShop = null;
			currentState = State.HOPPING_WORLDS;
			break;
		case OPENING_PACKAGE:
			if (!ctx.widgets.widget(149).component(0).visible()) {
				final int widgetId = ctx.game.resizable() ? 164 : 548;
				final int componentId = ctx.game.resizable() ? 44 : 48;
				interactWith(ctx.widgets.widget(widgetId).component(componentId), "Inventory", new Condition.Check() {
					@Override
					public boolean poll() {
						return ctx.widgets.select().id(149).poll().component(0).visible();
					}
				});
			}
			for (int i = 0; i < itemsToBuy.size(); i++) {
				RegisteredItem registeredItem = itemsToBuy.get(i);
				if (registeredItem == null || !registeredItem.isPack())
					continue;
				for (Item items : ctx.inventory.select().id(registeredItem.getItemId())) {
					if (!ctx.game.loggedIn() || ctx.controller.isSuspended() || ctx.controller.isStopping())
						break;
					items.interact("Open");
				}
			}
			currentState = State.OPENING_SHOP;
			break;
		case OPENING_SHOP:
			if (currentShop != null)
				currentShop = null;
			if (currentShop == null) {
				currentShop = new Shop(ctx, npcId);
				currentShop.open();
			}
			break;
		case HOPPING_WORLDS:
			if (worldSwitcher == null || !worldSwitcher.opened()) {
				worldSwitcher = new WorldSwitcher(ctx);
				worldSwitcher.open();
			}
			if (worldSwitcher.opened()) {
				if (worldSwitcher.switchToWorld(nextWorld)) {
					worldHopCounter++;
					if (worldHopFrequncy > 0) {
						currentWorldHopDelay = worldHopCounter == worldHopFrequncy
								? (System.currentTimeMillis() + worldHopDelay) : 0;
						if (worldHopCounter >= worldHopFrequncy)
							worldHopCounter = 0;
					}
					worldSwitcher = null;
					currentWorld = null;
					nextWorld = null;
					currentState = State.FINDING_WORLDS;
				}
			}
			break;
		case BANKING:
			if (currentShop != null && currentShop.opened()) {
				currentShop.close();
				currentShop = null;
				break;
			}
			if (!ctx.movement.running() && ctx.movement.energyLevel() >= runPercentage && ctx.movement.running(true))
				break;
			switch (npcId) {
			case 3247:
				final Player local = ctx.players.local();
				if (local.tile().floor() == 1) {
					final GameObject stairCase = getGameObject(new Tile(2591, 3092, 1), 15648);
					if (stairCase == null) {
						log.warning("Couldn't find staircase");
						break;
					}
					if (!stairCase.inViewport()) {
						ctx.movement.step(stairCase);
						ctx.camera.turnTo(stairCase);
						Condition.wait(new Condition.Check() {
							@Override
							public boolean poll() {
								if (stairCase.inViewport())
									return true;
								ctx.movement.step(stairCase);
								ctx.camera.turnTo(stairCase);
								return false;
							}
						}, 150, 15);
					}
					if (stairCase.inViewport()) {
						interactWith(stairCase, "Climb-down", new Condition.Check() {
							@Override
							public boolean poll() {
								return local.tile().floor() == 0;
							}
						});
					}
				}
				if (local.tile().floor() == 0 && local.tile().x() >= 2585 && local.tile().x() <= 2596) {
					if (getFreeSlots() == 0) {// Goin to bank
						final GameObject magicGuildDoor = getGameObject(new Tile(2596, 3088, 0),
								Random.nextInt(0, 2) == 0 ? 1733 : 1732);
						if (magicGuildDoor == null) {
							log.warning("Couldn't find magicGuildDoor!");
							break;
						}
						if (!magicGuildDoor.inViewport()) {
							ctx.movement.step(magicGuildDoor);
							ctx.camera.turnTo(magicGuildDoor);
							Condition.wait(new Condition.Check() {
								@Override
								public boolean poll() {
									if (magicGuildDoor.inViewport())
										return true;
									ctx.movement.step(magicGuildDoor);
									ctx.camera.turnTo(magicGuildDoor);
									return false;
								}
							}, 150, 15);
						}
						if (magicGuildDoor.inViewport()) {
							interactWith(magicGuildDoor, "Open", new Condition.Check() {
								@Override
								public boolean poll() {
									return local.tile().x() < 2585 || local.tile().x() > 2596;
								}
							});
						}
					} else {// Goin to shop
						final GameObject stairCase = getGameObject(new Tile(2591, 3088, 0), 15645);
						if (stairCase == null) {
							log.warning("Couldn't find staircase");
							break;
						}
						if (!stairCase.inViewport()) {
							ctx.movement.step(stairCase);
							ctx.camera.turnTo(stairCase);
							Condition.wait(new Condition.Check() {
								@Override
								public boolean poll() {
									if (stairCase.inViewport())
										return true;
									ctx.movement.step(stairCase);
									ctx.camera.turnTo(stairCase);
									return false;
								}
							}, 150, 15);
						}
						if (stairCase.inViewport()) {
							interactWith(stairCase, "Climb-up", new Condition.Check() {
								@Override
								public boolean poll() {
									if (local.tile().floor() == 1) {
										currentState = State.OPENING_SHOP;
										return true;
									}
									return false;
								}
							});
						}
					}
				}
				if (local.tile().floor() == 0 && local.tile().x() > 2596 && local.tile().x() <= 2613) {
					if (getFreeSlots() == 0) {
						if (local.tile().x() >= 2597 && local.tile().x() <= 2600) {
							Tile step = new Tile(2604 + Random.nextInt(0, 3), 3090 + Random.nextInt(0, 2));
							moveTo(step, new Condition.Check() {
								@Override
								public boolean poll() {
									return local.tile().x() >= 2601 && local.tile().x() <= 2608;
								}
							});
						}
						if (local.tile().x() >= 2601 && local.tile().x() <= 2608) {
							Tile step = new Tile(2609 + Random.nextInt(2, 4), 3091 + Random.nextInt(0, 4));
							moveTo(step, new Condition.Check() {
								@Override
								public boolean poll() {
									return local.tile().x() >= 2609 && local.tile().x() <= 2613;
								}
							});
						}
						if (local.tile().x() >= 2609 && local.tile().x() <= 2613) {
							if (!ctx.bank.opened() && !ctx.widgets.widget(213).component(0).visible()) {
								ctx.bank.open();
							}
							if (ctx.bank.opened()) {
								List<Integer> toKeep = new ArrayList<Integer>();
								toKeep.add(995);
								for (int i = 0; i < itemsToBuy.size(); i++) {
									RegisteredItem item = itemsToBuy.get(i);
									if (item == null)
										continue;
									if (!item.isStackable()) {
										bankedCount[i] += getAmountOf(item.getItemId());
										if (startCount[i] > 0) {
											bankedCount[i] -= startCount[i];
											startCount[i] = 0;
										}
										continue;
									} else if (item.isPack())
										toKeep.add(item.getOpenedId());
									else
										toKeep.add(item.getItemId());
								}
								int[] items = new int[toKeep.size()];
								for (int i = 0; i < toKeep.size(); i++)
									items[i] = toKeep.get(i);
								ctx.bank.depositAllExcept(items);
								ctx.bank.close();
								unRegisteredItemsIds.clear();
								if (worldHopCounter > 0)
									worldHopCounter = 0;
								break;
							}
						}
					} else {// going to store
						if (local.tile().x() >= 2609 && local.tile().x() <= 2613) {
							Tile step = new Tile(2604 + Random.nextInt(0, 3), 3090 + Random.nextInt(0, 2));
							moveTo(step, new Condition.Check() {
								@Override
								public boolean poll() {
									return local.tile().x() <= 2608 && local.tile().x() >= 2601;
								}
							});
						}
						if (local.tile().x() >= 2601 && local.tile().x() <= 2608) {
							Tile step = new Tile(2597 + Random.nextInt(0, 3), 3086 + Random.nextInt(0, 3));
							moveTo(step, new Condition.Check() {
								@Override
								public boolean poll() {
									return local.tile().x() <= 2600 && local.tile().x() >= 2597;
								}
							});
						}
						if (local.tile().x() >= 2597 && local.tile().x() <= 2600) {
							final GameObject magicGuildDoor = getGameObject(new Tile(2596, 3088, 0),
									Random.nextInt(0, 2) == 0 ? 1733 : 1732);
							if (magicGuildDoor == null) {
								log.warning("Couldn't find magicGuildDoor!");
								break;
							}
							if (!magicGuildDoor.inViewport()) {
								ctx.movement.step(magicGuildDoor);
								ctx.camera.turnTo(magicGuildDoor);
								Condition.wait(new Condition.Check() {
									@Override
									public boolean poll() {
										if (magicGuildDoor.inViewport())
											return true;
										ctx.movement.step(magicGuildDoor);
										ctx.camera.turnTo(magicGuildDoor);
										return false;
									}
								}, 150, 15);
							}
							if (magicGuildDoor.inViewport()) {
								interactWith(magicGuildDoor, "Open", new Condition.Check() {
									@Override
									public boolean poll() {
										return local.tile().x() <= 2596;
									}
								});
							}
						}
						break;
					}
				}
				break;
			case 3837:// Lunar
				final Player lunarLocal = ctx.players.local();
				if (lunarLocal.tile().y() >= 4645 && lunarLocal.tile().y() <= 4649) {
					ctx.camera.angle('s');
					final GameObject door = getGameObject(new Tile(2451, 4645, 0), 16774);
					if (door == null) {
						log.info("couldnt fine bankBooth");
						break;
					}
					interactWith(door, "Open", new Condition.Check() {
						@Override
						public boolean poll() {
							return lunarLocal.tile().y() >= 3924 && lunarLocal.tile().y() <= 3935
									&& lunarLocal.tile().x() >= 2072 && lunarLocal.tile().x() <= 2110;
						}
					});
				} else if (lunarLocal.tile().y() >= 3924 && lunarLocal.tile().y() <= 3935
						&& lunarLocal.tile().x() >= 2072 && lunarLocal.tile().x() <= 2110) {
					if (lunarLocal.tile().x() <= 2091) {// far
						if (getFreeSlots() == 0) {
							Tile step = new Tile(2100 + Random.nextInt(0, 2), 3926 + Random.nextInt(0, 3));
							moveTo(step, new Condition.Check() {
								@Override
								public boolean poll() {
									return lunarLocal.tile().x() > 2091;
								}
							});
						} else {
							final Npc house = ctx.npcs.nearest().select().id(3836).poll();
							if (!house.inViewport()) {
								ctx.camera.turnTo(house);
								Condition.wait(new Condition.Check() {

									@Override
									public boolean poll() {
										if (house.inViewport())
											return true;
										ctx.camera.turnTo(house);
										return false;
									}
								}, 150, 15);
							}
							if (house.inViewport()) {
								interactWith(house, "Go-inside", new Condition.Check() {
									@Override
									public boolean poll() {
										if (lunarLocal.tile().y() >= 4645 && lunarLocal.tile().y() <= 4649) {
											currentState = State.OPENING_SHOP;
											return true;
										}
										return false;
									}
								});
							}
						}
					} else {// close to bank
						if (getFreeSlots() == 0) {
							final Tile step = new Tile(2097 + Random.nextInt(0, 3), 3919);
							moveTo(step, new Condition.Check() {
								@Override
								public boolean poll() {
									return lunarLocal.tile().x() >= 2097 && lunarLocal.tile().x() <= 2100
											&& lunarLocal.tile().y() == 3917 && lunarLocal.tile().y() <= 3919;
								}
							});
						} else {
							Tile step = new Tile(2085 + Random.nextInt(0, 4), 3929 + Random.nextInt(0, 4));
							moveTo(step, new Condition.Check() {
								@Override
								public boolean poll() {
									return lunarLocal.tile().x() > 2091;
								}
							});
						}
					}
				} else if (lunarLocal.tile().x() >= 2097 && lunarLocal.tile().x() <= 2104
						&& lunarLocal.tile().y() >= 3917 && lunarLocal.tile().y() <= 3919) {
					if (getFreeSlots() == 0) {
						final GameObject bankBooth = getGameObject(new Tile(2098, 3920, 0), 16700, 0);
						if (bankBooth == null) {
							log.info("couldnt fine bankBooth");
							break;
						}
						if (!bankBooth.inViewport()) {
							ctx.camera.turnTo(bankBooth);
							Condition.wait(new Condition.Check() {

								@Override
								public boolean poll() {
									if (bankBooth.inViewport())
										return true;
									ctx.camera.turnTo(bankBooth);
									return false;
								}
							}, 150, 15);
						}
						if (!ctx.bank.opened()) {
							interactWith(bankBooth, "Bank", new Condition.Check() {

								@Override
								public boolean poll() {
									return ctx.bank.opened() || ctx.widgets.widget(213).component(0).visible();
								}
							});
						}
						if (ctx.bank.opened()) {
							List<Integer> toKeep = new ArrayList<Integer>();
							toKeep.add(995);
							for (int i = 0; i < itemsToBuy.size(); i++) {
								RegisteredItem item = itemsToBuy.get(i);
								if (item == null)
									continue;
								if (!item.isStackable()) {
									bankedCount[i] += getAmountOf(item.getItemId());
									if (startCount[i] > 0) {
										bankedCount[i] -= startCount[i];
										startCount[i] = 0;
									}
									continue;
								} else if (item.isPack())
									toKeep.add(item.getOpenedId());
								else
									toKeep.add(item.getItemId());
							}
							int[] items = new int[toKeep.size()];
							for (int i = 0; i < toKeep.size(); i++)
								items[i] = toKeep.get(i);
							ctx.bank.depositAllExcept(items);
							ctx.bank.close();
							unRegisteredItemsIds.clear();
							if (worldHopCounter > 0)
								worldHopCounter = 0;
							break;
						}
					} else {
						Tile step = new Tile(2096 + Random.nextInt(0, 3), 3929 + Random.nextInt(0, 2));
						moveTo(step, new Condition.Check() {

							@Override
							public boolean poll() {
								return lunarLocal.tile().y() >= 3924;
							}
						});
					}
				}
				break;
			}
			break;
		}
		doingAction = false;
	}

	public boolean hasPack() {
		for (RegisteredItem item : itemsToBuy) {
			if (item == null)
				continue;
			if (item.isPack() && getAmountOf(item.getItemId()) > 0)
				return true;
		}
		return false;
	}

	public void interactWith(Interactive interactive, String action, Condition.Check checkable) {
		if (interactive.interact(action)) {
			do {
				Condition.wait(checkable, 150, 15);
			} while (ctx.players.local().inMotion());
			Condition.wait(checkable, 100, 15);
		}
	}

	public void moveTo(Tile tile, Condition.Check checkable) {
		if (ctx.movement.step(tile)) {
			do {
				Condition.wait(checkable, 100, 15);
			} while (ctx.players.local().inMotion());
			Condition.wait(checkable, 50, 15);
		}
	}

	public GameObject getGameObject(Tile tile, int objectId, int radius) {
		for (GameObject check : ctx.objects.get(tile, radius)) {
			if (check == null || check.id() != objectId)
				continue;
			return check;
		}
		return null;
	}

	public GameObject getGameObject(Tile tile, int objectId) {
		return getGameObject(tile, objectId, 2);
	}

	@Override
	public void resume() {
		doingAction = false;
	}

	public int getAmountOf(int itemId) {
		int count = 0;
		for (Item item : ctx.inventory.items()) {
			if (item == null || item.stackSize() <= 0 || item.id() != itemId)
				continue;
			if (item.stackable()) {
				return item.stackSize() == -1 ? 0 : item.stackSize();
			}
			count++;
		}
		return count;
	}

	public State getState() {
		if (ctx.controller.isStopping() || ctx.controller.isSuspended())
			return State.IDLE;
		if (gui.isVisible())
			return null;
		if ((currentWorld == null || nextWorld == null) && hopWorlds)
			return State.FINDING_WORLDS;
		if (ctx.widgets.widget(Shop.SHOP_WIDGET).component(Shop.MASTER_COMPONENT).visible())
			return State.BUYING;
		if (!ctx.widgets.widget(Shop.SHOP_WIDGET).component(Shop.MASTER_COMPONENT).visible())
			return State.OPENING_SHOP;
		else
			return null;
	}

	public int getFreeSlots() {
		int count = 0;
		for (Item item : ctx.inventory.items()) {
			if (item == null || item.stackSize() <= 0)
				continue;
			count++;
		}
		return 28 - count;
	}

	public static enum State {
		IDLE, OPENING_PACKAGE, OPENING_SHOP, HOPPING_WORLDS, BUYING, FINDING_WORLDS, BANKING;
	}

	@Override
	public void repaint(Graphics g) {
		if (gui == null)
			return;
		Font font = new Font("Comic Sans MS", 0, 12);
		g.setFont(font);
		g.setColor(Color.ORANGE);
		if (!gui.isStarted()) {
			if (!gui.isVisible()) {
				ctx.controller.stop();
				return;
			}
			g.drawString("Shop Items Auto Buyer 1.0", 10, 70);
			g.drawString("Waiting for player input...", 10, 152);
			return;
		}
		if (itemsToBuy == null)
			return;
		if (!ctx.game.loggedIn() && doingAction)
			doingAction = false;
		for (int i = 0; i < itemsToBuy.size(); i++) {
			RegisteredItem registeredItem = itemsToBuy.get(i);
			if (registeredItem == null)
				continue;
			Item item = ctx.inventory.select().id(registeredItem.getItemId()).poll();
			if (item == null)
				continue;
			if (itemNames[i] == null || itemNames[i].equals(""))
				itemNames[i] = item.name();
			if (registeredItem.isPack()) {
				int itemInPackId = registeredItem.getOpenedId();
				item = ctx.inventory.select().id(itemInPackId).poll();
				itemsBoughtCount[i] = ((item.stackSize() == -1 ? 0 : item.stackSize()) - startCount[i]);
				itemsBoughtCount[i] /= 100;
			} else if (!registeredItem.isStackable())
				itemsBoughtCount[i] = (banking && bankedCount != null ? bankedCount[i] : 0)
						+ getAmountOf(registeredItem.getItemId()) - startCount[i];
			else
				itemsBoughtCount[i] = ((item.stackSize() == -1 ? 0 : item.stackSize()) - startCount[i]);
		}

		long timeElapsed = System.currentTimeMillis() - startTime;
		int seconds = (int) (timeElapsed / 1000) % 60;
		int minutes = (int) ((timeElapsed / (1000 * 60)) % 60);
		int hours = (int) ((timeElapsed / (1000 * 60 * 60)) % 24);
		g.drawString("Shop Items Auto Buyer 1.0", 10, 70);
		g.drawString(
				"Time Elapsed: " + (hours < 10 ? ("0" + hours) : hours) + ":"
						+ (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds),
				10, 152);
		for (int i = 0; i < itemsToBuy.size(); i++) {
			RegisteredItem registeredItem = itemsToBuy.get(i);
			if (registeredItem == null)
				continue;
			String name = itemNames[i] == null || itemNames[i].equals("") ? ("ItemId: " + registeredItem.getItemId())
					: ("Item Name: " + itemNames[i]);
			g.drawString(name + ", Bought(Per Hour): " + getFormattedNumber(itemsBoughtCount[i]) + " ("
					+ getFormattedNumber(getPerHour(itemsBoughtCount[i])) + ")", 10, (172 + (i * 20)));
		}
		String stateMessage = "";
		if (currentState == State.BUYING)
			stateMessage = "BUYING";
		else if (currentState == State.OPENING_PACKAGE)
			stateMessage = "OPENING_PACKAGE";
		else if (currentState == State.OPENING_SHOP)
			stateMessage = "OPENING_SHOP";
		else if (currentState == State.HOPPING_WORLDS)
			stateMessage = "HOPPING_WORLDS";
		else if (currentState == State.FINDING_WORLDS)
			stateMessage = "FINDING_WORLDS";
		else if (currentState == State.BANKING)
			stateMessage = "BANKING";
		else
			stateMessage = "Idle";
		g.drawString("Current State: " + stateMessage, 10, 192 + ((itemsToBuy.size() - 1) * 20));
		if (hopWorlds) {
			g.drawString("Current World: " + (currentWorld == null ? "N/A" : currentWorld) + ", NextWorld: "
					+ (nextWorld == null ? "N/A" : nextWorld), 10, 212 + ((itemsToBuy.size() - 1) * 20));
			long worldHopDelay = this.currentWorldHopDelay < System.currentTimeMillis() ? 0
					: (this.currentWorldHopDelay - System.currentTimeMillis());
			seconds = (int) (worldHopDelay / 1000) % 60;
			minutes = (int) ((worldHopDelay / (1000 * 60)) % 60);
			hours = (int) ((worldHopDelay / (1000 * 60 * 60)) % 24);
			g.drawString("Next World Hop in: " + (hours < 10 ? ("0" + hours) : hours) + ":"
					+ (minutes < 10 ? ("0" + minutes) : minutes) + ":" + (seconds < 10 ? ("0" + seconds) : seconds), 10,
					232 + ((itemsToBuy.size() - 1) * 20));
		}
	}

	public String getFormattedNumber(int amount) {
		return new DecimalFormat("#,###,###").format(amount);
	}

	public int getPerHour(int count) {
		long timeElapsed = System.currentTimeMillis() - startTime;
		return (int) ((count * 3600000D) / timeElapsed);
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setWorldHopDelay(long worldHopDelay) {
		this.worldHopDelay = worldHopDelay;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public void setWorldHopFrequncy(int worldHopFrequncy) {
		this.worldHopFrequncy = worldHopFrequncy;
	}

	public void setItemsToBuy(List<RegisteredItem> itemsToBuy) {
		this.itemsToBuy = itemsToBuy;
	}

	public void setRestrictedWorlds(List<Integer> restrictedWorlds) {
		this.restrictedWorlds = restrictedWorlds;
	}

	public void setHopWorlds(boolean hopWorlds) {
		this.hopWorlds = hopWorlds;
	}

	public void setBanking(boolean banking) {
		this.banking = banking;
	}

}
