package sscripts.sgaltar.gui;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import sscripts.sgaltar.data.Data;
import sscripts.sgaltar.tasks.PortalChat;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.*;
import sscripts.sgaltar.tasks.rimmington.Phials;
import sscripts.sgaltar.tasks.rimmington.ToPortal;
import sscripts.sgaltar.tasks.walk.WalkEnergy;
import sscripts.sgaltar.tasks.walk.WalkToPortal;
import sscripts.sgaltar.tasks.walk.Walking;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

import static sscripts.sgaltar.SGAltar.data;

public class Gui extends ClientAccessor {

    private List<Task> tasks;
    private JFrame frame = new JFrame("SGAltar - V2.03 - by sscripts ");
    public Gui(ClientContext arg0, List<Task> tasks) {
        super(arg0);
        this.tasks = tasks;
        init();
    }



    public void init() {
        frame.setSize(500,200);
        frame.setVisible(true);

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        final JPanel uperPanel = new JPanel();
        uperPanel.setLayout(new FlowLayout());

        final JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());

        final JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new FlowLayout());

        final NumberFormat format = NumberFormat.getIntegerInstance();
        final NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        final JRadioButton yanille = new JRadioButton("Yanille");
        uperPanel.add(yanille);

        final JRadioButton rimm = new JRadioButton("Rimmington");
        uperPanel.add(rimm);

        final JLabel label = new JLabel("Select Bone");
        centerPanel.add(label);




        final JComboBox bone_selection = new JComboBox(Data.values());
        centerPanel.add(bone_selection);
       // final JFormattedTextField boneid = new JFormattedTextField(formatter);
        //centerPanel.add(boneid);

        final JLabel label2 = new JLabel("Enter Host-Name (Playername)");
        centerPanel.add(label2);

        final JTextField playerid = new JTextField();
        centerPanel.add(playerid);

     //   boneid.setColumns(6);
        playerid.setColumns(15);

        final JCheckBox fountain = new JCheckBox("Refresh Run-Energy?");
        lowPanel.add(fountain);
        final JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {


                SGAltar.data = (Data) bone_selection.getSelectedItem();
                SGAltar.bone_ID = (Integer) ((Data) bone_selection.getSelectedItem()).getBone_ID();
                SGAltar.boneName = (String) ((Data) bone_selection.getSelectedItem()).getBone_name();
               // if (boneid.getValue() != null){
               //     SGAltar.boneID = (Integer) boneid.getValue();
              //  }
                // IF RIMM WE NEED THE NOTED BONE ID´S!! SO WE CAN INTERACT WITH ALTAR AND NPC!!
                if (playerid.getText() != null){
                    SGAltar.playername = playerid.getText();
                }if (rimm.isSelected()){
                    SGAltar.rimm = true;
                    if (fountain.isSelected()){
                        synchronized (tasks) {
                            SGAltar.useFountain = true;
                            tasks.add(new EnterPortal(ctx));
                            tasks.add(new Altar(ctx,data));
                            tasks.add(new ToPortal(ctx));
                            tasks.add(new Phials(ctx));
                            tasks.add(new PortalChat(ctx));
                            tasks.add(new LeaveHouse(ctx));
                            tasks.add(new WalkEnergy(ctx));
                            tasks.add(new Antiban(ctx));
                            tasks.notifyAll();
                        }
                    }else {
                        synchronized (tasks) {
                            SGAltar.useFountain = false;
                            tasks.add(new EnterPortal(ctx));
                            tasks.add(new Altar(ctx,data));
                            tasks.add(new ToPortal(ctx));
                            tasks.add(new Phials(ctx));
                            tasks.add(new PortalChat(ctx));
                            tasks.add(new LeaveHouse(ctx));
                            tasks.add(new Antiban(ctx));

                            tasks.notifyAll();
                        }
                    }
                }
                if (yanille.isSelected()) {
                    SGAltar.yanille = true;
                    if (fountain.isSelected()) {
                        synchronized (tasks) {
                            SGAltar.useFountain = true;
                            tasks.add(new Bank(ctx));
                            tasks.add(new Walking(ctx));
                            tasks.add(new EnterPortal(ctx));
                            tasks.add(new Altar(ctx,data));
                            tasks.add(new Banking(ctx, data));
                            tasks.add(new CloseBank(ctx));
                            tasks.add(new WalkToPortal(ctx));
                            tasks.add(new PortalChat(ctx));
                            tasks.add(new LeaveHouse(ctx));
                            tasks.add(new WalkEnergy(ctx));
                            tasks.add(new Antiban(ctx));

                            tasks.notifyAll();
                        }
                    } else {
                        synchronized (tasks) {
                            SGAltar.useFountain = false;
                            tasks.add(new Bank(ctx));
                            tasks.add(new Walking(ctx));
                            tasks.add(new EnterPortal(ctx));
                            tasks.add(new Altar(ctx,data));
                            tasks.add(new Banking(ctx, data));
                            tasks.add(new CloseBank(ctx));
                            tasks.add(new WalkToPortal(ctx));
                            tasks.add(new PortalChat(ctx));
                            tasks.add(new LeaveHouse(ctx));
                            tasks.add(new Antiban(ctx));

                            tasks.notifyAll();
                            // add task without fountain usage
                        }
                    }
                }

                frame.dispose();
            }

        });


        mainPanel.add(uperPanel, BorderLayout.NORTH);
        //mainPanel.add(label, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(lowPanel, BorderLayout.SOUTH);
        uperPanel.add(yanille, BorderLayout.NORTH);
        centerPanel.add(label2, BorderLayout.SOUTH);
        lowPanel.add(startButton, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.pack();
    }
}
