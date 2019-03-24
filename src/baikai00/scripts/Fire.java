package baikai00.scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;

@Manifest(name="AutoFireCraft", description="make fire rune",  properties = "client=6;topic=1335104;author=baikai00;")
public class Fire extends PollingScript<ClientContext> implements PaintListener{
	private DHChecker dhChecker;
	private Utils utils;
	private String username = "";
	@Override
	public void start() {
		utils = new Utils(ctx);
		dhChecker = DHChecker.getInstance();
		if (dhChecker.getState() != Thread.State.RUNNABLE && !dhChecker.isStart()){
			dhChecker.start();
		}

		System.out.println("Fire Begin");
		utils.setCamera();
		initTime = System.currentTimeMillis() + Utils.TIME_OFFSET;
		username = ctx.properties.getProperty("login.account.username");
	}


	@Override
	public void poll() {
		if (utils.animation() == -1){
			play();
		}
	}

	private void play(){
		final State state = getState();
		if (state == null){return;}
		utils.setCamera();
		switch (state){
			case BANKING:
				utils.fireBanking();
				break;
			case MAKING:
				utils.fireMaking();
				break;
			case DH:
				utils.dh();
				break;
		}
	}
	
	public State getState() {
		if (dhChecker != null && dhChecker.getIniState() == 200){
			return State.DH;
		}
		if (utils.count(Utils.ESS_ID) <= 0) {
			return State.BANKING;
		}
		return State.MAKING;
	}

	@Override
	public void stop() {
		super.stop();
		System.out.println("Fire Stop");
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss",
			Locale.US);
	private long initTime;
	private Font font = new Font("Frankruehl", Font.BOLD, 20);
	private Color color2 = Color.GREEN;
	private Color color1 = Color.RED;
	private Date date = new Date();
	
	@Override
	public void repaint(Graphics g) {
		if (utils == null){
			return;
		}
		long time = System.currentTimeMillis() - initTime;
		g.setFont(font);
		g.setColor(color2);
		g.drawString("Account: ", 5, 115 - 25 * 2);
		g.setColor(color1);
		g.drawString(username, 5, 115 - 25 * 1);
		g.setColor(color2);
		g.drawString("Time running: ", 5, 115 + 25 * 0);
		g.setColor(color1);
		date.setTime(time);
		g.drawString(sdf.format(date), 5, 115 + 25 * 1);
		g.setColor(color2);
		g.drawString("Action: ", 5, 115 + 25 * 2);
		g.setColor(color1);
		g.drawString(utils.status, 5, 115 + 25 * 3);
		g.setColor(color2);
		g.drawString("Count/Price: ", 5, 115 + 25 * 4);
		g.setColor(color1);
		g.drawString(""+utils.getFireRuneCount()+"/"+
				(utils.getPrice()/1000)+"k", 5, 115 + 25 * 5);
		g.setColor(color2);
		g.drawString("LastTime: ", 5, 115 + 25 * 6);
		g.setColor(color1);
		date.setTime(utils.lastTime);
		g.drawString(sdf.format(date), 5, 115 + 25 * 7);
		g.setColor(color2);
		g.drawString("ThisTime: ", 5, 115 + 25 * 8);
		g.setColor(color1);
		date.setTime(utils.getThisTime());
		g.drawString(sdf.format(date), 5, 115 + 25 * 9);
	}

	enum State{
		BANKING, MAKING, DH;
	}
}
