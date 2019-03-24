package Leroux.FreeWorldCooker.Script;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Leroux.FreeWorldCooker.Tasks.Cook;
import Leroux.FreeWorldCooker.Tasks.Deposit;
import Leroux.FreeWorldCooker.Tasks.Walk_ToBank;
import Leroux.FreeWorldCooker.Tasks.Walk_ToRange;
import Leroux.FreeWorldCooker.Tasks.Withdraw;
import org.powerbot.script.rt6.ClientContext;

public class GUI extends JFrame {

    private static final long serialVersionUID = -7756889463610098394L;

    private ClientContext ctx;

    public boolean guiDone = false;
    private JPanel panel;

    public GUI(ClientContext ctx) {
        this.ctx = ctx;
        setTitle("Leroux");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 200);
        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(null);
        panel.setBackground(Color.LIGHT_GRAY);

        final JLabel title = new JLabel("Welcome to the FreeWorld Cooker");
        title.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        title.setBounds(125, 11, 200, 15);
        panel.add(title);

        final JTextArea instructions = new JTextArea();
        instructions.setEditable(false);
        instructions.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setBackground(Color.lightGray);
        instructions.setRows(2);
        instructions.setText("Enter the ID's of what you want to cook. \r\nMake sure they are separated by a comma.");
        instructions.setBounds(125, 37, 225, 40);
        panel.add(instructions);

        final JTextField ids = new JTextField("Input");
        ids.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        ids.setBounds(153, 80, 154, 20);
        panel.add(ids);

        final JButton start = new JButton("Start");
        start.setEnabled(true);
        start.setBounds(180, 100, 90, 23);
        panel.add(start);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = ids.getText().split(",");
                int[] inputIDS = new int[input.length];

                for (int i = 0; i < input.length; i++) {
                    try {
                        inputIDS[i] = Integer.parseInt(input[i]);
                    } catch (NumberFormatException nfe) {

                    }
                }

                FreeWorldCooker.cookingIDs = inputIDS;
                addTasks();
                setVisible(false);
                guiDone = true;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void addTasks() {
        FreeWorldCooker.taskList.addAll(Arrays.asList(new Cook(ctx), new Deposit(ctx), new Withdraw(ctx), new Walk_ToBank(ctx), new Walk_ToRange(ctx)));
    }
}
