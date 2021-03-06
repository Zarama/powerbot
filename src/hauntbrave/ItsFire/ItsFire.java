/* ItsFire by haunt brave. lights any kind of logs in Draynor village. */
package hauntbrave.ItsFire;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
//import org.powerbot.script.PaintListener;

import hauntbrave.ItsFire.tasks.*;
import hauntbrave.ItsFire.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Color;
//import java.awt.BasicStroke;
//import java.awt.Font;

@Script.Manifest(name="ItsFire", description = "makes fire", properties = "author=hauntbrave; topic=1337137; client = 4;")

public class ItsFire extends PollingScript<ClientContext> {
	private List<Task> taskList = new ArrayList<Task>();
	
	private SelectLog win = new SelectLog(ctx);
	private Walker walk = new Walker(ctx);

	//private String currentTask = null;

	//private final double converter = 1000000000.0;
	//private double startTime; //seconds
	//private boolean started = false;
	//private double runTime = 0;
	//private int hour = 0;
	//private int minute = 0 ;

	@Override
	public void start(){
		win.show();
		walk.setLogId(win.getLogId());

		System.out.println("Started script...");
		//started = true;
		taskList.addAll(Arrays.asList(new Deposit(ctx, win.getLogId(), walk),
						walk, 
						new LightLog(ctx, win.getLogId(), walk))); 
						

		//window shows, haulting thread. Afterwards, sets the world type.

	}

	@Override
	public void poll(){
		for (Task task: taskList){ 
			if (task.activate()) {

				task.execute();
				
			}
		}
	}

//	@Override
//	public void repaint(Graphics graphics) {
//
//		if (started)
//		{
//			if ( runTime < 60){
//				runTime = System.nanoTime() / converter - startTime;
//			}
//			else
//			{
//				startTime = System.nanoTime() / converter;
//				runTime = 0;
//				minute++;
//
//				if (minute == 60) { hour++; minute = 0; }
//			}
//		}
//
//		final Graphics2D g = (Graphics2D) graphics;
//		g.setColor(Color.BLACK);
//		g.setStroke(new BasicStroke(4));
//		g.drawRect(5, 50, 200, 105);
//
//		g.setFont(new Font("Tahoma", Font.BOLD, 12));
//		g.setColor(new Color(216, 6, 6, 100));
//		g.fillRect(5, 50, 200, 105);
//
//		g.setColor(Color.WHITE);
//
//		g.drawString(new String("ItsFire by hauntbrave"), 10, 65);
//		g.drawString(String.format("Run time: %d : %d : %f", 
//					hour,
//					minute,
//					runTime), 10, 85);
//
//		g.drawString(String.format("Wine: %,d", grab.getGrabbed()), 10, 105);
//		g.drawString(String.format("Wine/Hour: %,f", 
//					(grab.getGrabbed() * 100000.0 * 60.0) / getRuntime()), 10, 125);
//
//		if (currentTask != null)
//			g.drawString(currentTask, 10, 145);
//     }
//}
}
