package IntenseChanging.scripts.DragonBitterBuyer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.GeItem;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.TilePath;

@Script.Manifest(name = "Dragon Bitter Buyer", description = "Buys Dragon Bitter in Yanille, then banks it",
				 properties = "author=IntenseChanging; topic=1331122; client=6;")
/**
 * @author IntenseChanging
 *
 * Special thanks to Coma for his "Writing your second script" tutorial
 * which this script is based on: https://gist.github.com/Strikegently/363d77f74dab204e78ba
 * Created: 4/16/17
 */
public class DragonBitterBuyer extends PollingScript<ClientContext> implements MessageListener, PaintListener  {

	private static final int DOOR_CLOSED_ID = 1533;
	private static final Area DOOR_AREA = new Area(new Tile(2552, 3081, 0), new Tile(2550, 3084, 0));
	private static final int BARTENDER_ID = 739;
	private static final int DRAGON_BITTER_ID = 1911;
	private static final int COST = 2;
	private static final String CHAT_OPTION = "I'll try the Dragon Bitter.";
	private static final String PURCHASED_TEXT = "You buy a pint of Dragon Bitter.";

    public static final Tile[] PATH_TO_BANK = {
    		new Tile(2565, 3090, 0),
    		new Tile(2578, 3095, 0),
    		new Tile(2592, 3098, 0),
    		new Tile(2604, 3095, 0),
    		new Tile(2612, 3095, 0),
    };

    public static final Tile[] PATH_TO_BARTENDER = {
    		new Tile(2599, 3098, 0),
    		new Tile(2585, 3097, 0),
    		new Tile(2575, 3090, 0),
    		new Tile(2560, 3090, 0),
    		new Tile(2551, 3084, 0),
    };

	private int moneyToSpend = -1;
	private int totalToBuy = -1;
	private int totalBought = 0;
	private int totalSpent = 0;

	private TilePath pathToBank;
	private TilePath pathToBartender;
	private int currentPriceInGE = 0;

    @Override
    public void start() {
        pathToBartender = new TilePath(ctx, PATH_TO_BARTENDER);
        pathToBank = new TilePath(ctx, PATH_TO_BANK);
        currentPriceInGE = new GeItem(DRAGON_BITTER_ID).price;
    }

	@Override
	public void poll() {
		if (ctx.backpack.moneyPouchCount() < COST) {
			ctx.controller.stop(); //Come on dude, it's 2gp
		}

		final State state = getState();

		if (state == null) {
			return;
		}
		switch (state) {
		case WAIT:
			break;
		case BUY:

			if (!ctx.chat.chatting()) {
				final Npc trader = ctx.npcs.select().id(BARTENDER_ID).nearest().poll();
				if (trader.interact("Talk-to")) {
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.chat.chatting();
						}
					}, 250, 10);
				}
			} else if (ctx.chat.canContinue()) {
				ctx.chat.clickContinue(true);
				Condition.sleep(Random.nextInt(350, 500));
			}
			else if (!ctx.chat.select().text(CHAT_OPTION).isEmpty()) {
				final ChatOption option = ctx.chat.poll();
				if (option.select(true)) {
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return !option.valid();
						}
					}, 250, 10);
				}
			}
			break;

		case MOVE_TO_BANK:
            openDoorIfClosed();
			pathToBank.traverse();
			break;
		case DEPOSIT_TO_BANK:
			if (!ctx.bank.opened()) {
				ctx.bank.open();
			} else if (!ctx.backpack.select().id(DRAGON_BITTER_ID).isEmpty()) {
				ctx.bank.depositInventory();
			} else {
				ctx.bank.close();
			}
			break;
		case MOVE_TO_BARTENDER:
            pathToBartender.traverse();
            openDoorIfClosed();
            break;
		}
	}

	private State getState() {

		if (totalToBuy != -1 && totalBought >= totalToBuy) {
			ctx.controller.stop();
		}
		if (moneyToSpend != -1 && totalSpent >= moneyToSpend) {
			ctx.controller.stop();
		}


        if (ctx.bank.opened()) {
            return State.DEPOSIT_TO_BANK;
        } else if (ctx.backpack.select().count() < 28) {
            if (!ctx.npcs.select().id(BARTENDER_ID).within(10).isEmpty()) {
                return State.BUY;
            } else {
                return State.MOVE_TO_BARTENDER;
            }
        } else if (!ctx.bank.inViewport()) {
            return State.MOVE_TO_BANK;
        } else if (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 10) {
            return State.DEPOSIT_TO_BANK;
        }
		return State.WAIT;
	}

	private enum State {
		BUY, MOVE_TO_BANK, MOVE_TO_BARTENDER, DEPOSIT_TO_BANK, WAIT
	}

	private void openDoorIfClosed() {
        final GameObject closedDoor = ctx.objects.select().within(DOOR_AREA).id(DOOR_CLOSED_ID).nearest().poll();
        if (closedDoor.valid()) {
            closedDoor.interact("Open");
            Condition.sleep(500);
        }
	}

	@Override
	public void messaged(MessageEvent e) {
		final String msg = e.text().toLowerCase();
		if (msg.equalsIgnoreCase(PURCHASED_TEXT)) {
			totalBought++;
			totalSpent += 2;
		}
	}

	public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

	@Override
	public void repaint(Graphics graphics) {
		final Graphics2D g = (Graphics2D) graphics;
		g.setFont(TAHOMA);

		final int profit = (currentPriceInGE * totalBought) - totalSpent;
		final int profitHr = (int) ((profit * 3600000D) / getRuntime());
		final int boughtHr = (int) ((totalBought * 3600000D) / getRuntime());

		g.setColor(Color.BLACK);
		g.fillRect(5, 5, 150, 45);

		g.setColor(Color.WHITE);

		g.drawString(String.format("Dragon Bitter: %,d (%,d)", totalBought, boughtHr), 10, 20);
		g.drawString(String.format("Profit: %,d (%,d)", profit, profitHr), 10, 40);
	}
}
