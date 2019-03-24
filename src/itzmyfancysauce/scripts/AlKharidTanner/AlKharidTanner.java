package itzmyfancysauce;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Script.Manifest(
        name = "Al-Kharid Tanner",
        description = "Tans hides in Al-Kharid",
        properties = "author=itzmyfancysauce;topic=1342740;client=4;"
)
public class AlKharidTanner extends PollingScript<ClientContext> implements PaintListener{
    private List<Task> taskList = new ArrayList<>();
    Calendar endTime = Calendar.getInstance();
    static Calendar startTime;
    Color transBlack = new Color(0, 0, 0, 235);
    static String currentTask = "";
    DecimalFormat df;
    Font titleFont = new Font("Verdana", Font.BOLD, 16);
    Font font = new Font("Verdana", Font.BOLD, 12);
    static int totalTanned = 0;
    final String[] tanningOptions = new String[]{"Soft Leather", "Hard Leather", "Green D'leather", "Blue D'leather", "Red D'leather", "Black D'leather"};
    static int hideID, leatherID, widgetID;

    public void chooseHide() {
        String input = (String)JOptionPane.showInputDialog(null, "Choose what to tan", "Al-Kharid Tanner", JOptionPane.QUESTION_MESSAGE, null,tanningOptions, tanningOptions[0]);

        if(input.equals(tanningOptions[0])) {
            hideID = 1739;
            leatherID = 1741;
            widgetID = 148;
        } else if(input.equals(tanningOptions[1])) {
            hideID = 1739;
            leatherID = 1743;
            widgetID = 149;
        } else if(input.equals(tanningOptions[2])) {
            hideID = 1753;
            leatherID = 1745;
            widgetID = 152;
        } else if(input.equals(tanningOptions[3])) {
            hideID = 1751;
            leatherID = 2505;
            widgetID = 153;
        } else if(input.equals(tanningOptions[4])) {
            hideID = 1749;
            leatherID = 2507;
            widgetID = 154;
        } else if(input.equals(tanningOptions[5])) {
            hideID = 1747;
            leatherID = 2509;
            widgetID = 155;
        }
    }

    @Override
    public void start() {
        chooseHide();
        ctx.input.speed(90);
        endTime.add(Calendar.MINUTE, 118);
        startTime = Calendar.getInstance();
        df = new DecimalFormat("#,#");
        df.setGroupingSize(3);

        taskList.addAll(Arrays.asList(new Bank(ctx), new Tan(ctx), new Walk(ctx)));
        System.out.println("Running");
    }

    @Override
    public void stop() {
        super.stop();
        taskList.clear();
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if(task.activate(getStorageDirectory().getAbsolutePath())) {
                currentTask = task.getClass().getSimpleName();

                if(currentTask.equals("Tan")) {
                    currentTask = "Tanning";
                } else {
                    currentTask += "ing";
                }
                task.execute();
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        int hours = (int)((Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis()) / 1000 / 3600);
        int minutes = (int)((Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis()) / 1000 / 60) % 60;
        int seconds = (int)((Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis()) / 1000 % 60);
        String runtime = "Runtime: " + hours + ":" + minutes + ":" + seconds;

        graphics.setColor(transBlack);
        graphics.drawRect(3, 250, 350, 90);
        graphics.fillRect(3, 250, 350, 90);

        graphics.setColor(Color.WHITE);

        graphics.setFont(titleFont);
        graphics.drawString("OSRS Al-Kharid Tanner", 15, 265);

        graphics.setFont(font);
        graphics.drawString(runtime, 15, 285);
        graphics.drawString("Status: " + currentTask, 15, 300);
        graphics.drawString("Tanned Hides: " + totalTanned, 15, 315);
    }
}
