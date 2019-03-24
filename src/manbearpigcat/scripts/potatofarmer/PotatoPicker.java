package manbearpigcat.scripts.potatofarmer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import manbearpigcat.scripts.potatofarmer.tasks.AntiBan;
import manbearpigcat.scripts.potatofarmer.tasks.Bank;
import manbearpigcat.scripts.potatofarmer.tasks.OpenGate;
import manbearpigcat.scripts.potatofarmer.tasks.Pick;
import manbearpigcat.scripts.potatofarmer.tasks.Task;
import manbearpigcat.scripts.potatofarmer.tasks.WalkToBank;
import manbearpigcat.scripts.potatofarmer.tasks.WalkToField;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

/**
 * Created by Shan on 2016-08-17.
 */
@Script.Manifest(
        name = "Potato Picker", properties = "author=Manbearpigcat; topic=1320777; client=6;",
        description = "A Simple No Requirements Potato Picker"
)
public class PotatoPicker extends PollingScript<ClientContext> implements PaintListener, ActionListener{

    private List<Task> taskList = new ArrayList<Task>();
    private static final Font TAHOMA = new Font("Tahoma", Font.BOLD, 18);
    private static final Font TAHOMA_BIG = new Font("Tahoma", Font.BOLD, 24);
    private static final int POT = 1942;
    private static final int POT_GE = new GeItem(POT).price;
    public static Stats sPots = new Stats(0);
    private Color lBlue = Color.decode("#232526");
    private Color dBlue = Color.decode("#414345");
    //Color text = Color.decode("#FF9068");
    private GradientPaint bg = new GradientPaint(5, 5, lBlue, 250, 160, dBlue);
    private JRadioButton option1;
    private JRadioButton option2;
    private JFrame frame;


    public void start(){
        initUI();
    }

    public void initUI(){

        frame = new JFrame("Options");

        JPanel panel = new JPanel();

        JButton start = new JButton("Start Script");
        start.addActionListener(this);
        start.setVerticalTextPosition(AbstractButton.CENTER);

        JLabel label = new JLabel("Choose your banking location:");

        ButtonGroup group = new ButtonGroup();
        option1 = new JRadioButton("Varrock");
        option2 = new JRadioButton("Al-Kharid");
        option1.setSelected(true);
        group.add(option1);
        group.add(option2);

        panel.add(label);
        panel.add(option1);
        panel.add(option2);
        panel.add(start);

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ctx.controller.suspend();
    }

    @SuppressWarnings("unchecked")
    public void actionPerformed(ActionEvent e) {
        int bank = 0;
        if(option1.isSelected()){
            bank = 1;
        }
        else{
            bank = 2;
        }
        frame.dispose();

        taskList.addAll(Arrays.asList(new WalkToField(ctx), new OpenGate(ctx), new Pick(ctx), new WalkToBank(ctx, bank),
                new Bank(ctx, bank), new AntiBan(ctx)));

        ctx.controller.resume();
        ctx.camera.pitch(90);
    }

    @Override
    public void poll(){
        for(Task task : taskList){
            if(task.activate()){
                task.execute();
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA_BIG);

        int potFarmed = sPots.getPotFarmed();
        String states = sPots.getState();

        final int profit = POT_GE * potFarmed;
        final int profitHr = (int) ((profit * 3600000D) / getRuntime());
        final int potHr = (int) ((potFarmed * 3600000D) / getRuntime());

        g.setColor(Color.BLACK);
        g.fillRect(5,5,265,175);
        g.setPaint(bg);
        g.fillRect(10, 10, 255, 165);

        g.setColor(Color.WHITE);

        long hours = (getRuntime()/1000) / 3600;
        long minutes = ((getRuntime()/1000) % 3600) / 60;
        long seconds = (getRuntime()/1000) % 60;


        g.drawString(String.format("--- Potato Farmer ---"), 15, 30);
        g.setColor(Color.WHITE);
        g.setFont(TAHOMA);
        //g.setColor(Color.WHITE);
        g.drawString(String.format("Runtime: " + "%02d:%02d:%02d", hours, minutes, seconds), 15, 60);
        g.drawString(String.format("Pot Farmed: %,d (%,d)", potFarmed, potHr), 15, 90);
        g.drawString(String.format("Profit: %,d (%,d)", profit, profitHr), 15, 120);
        g.drawString(String.format("State: %s", states), 15, 150);
    }
}
