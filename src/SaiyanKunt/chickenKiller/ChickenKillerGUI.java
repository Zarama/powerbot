package SaiyanKunt.chickenKiller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by SaiyanKunt on 3/27/2017.
 */
public class ChickenKillerGUI extends JFrame
{
    private boolean done = false;
    private boolean killChickens = true;
    private boolean pickupFeathers = true;
    private boolean buryBones = true;

    public ChickenKillerGUI()
    {
        setLayout(new GridBagLayout());
        setSize(250, 200);
        setTitle("SaiyanKunt's Chicken Killer");
        JLabel label = new JLabel("Select the tasks to perform");
        final JCheckBox killChickensCB = new JCheckBox("Kill Chickens", true);
        final JCheckBox pickupFeathersCB = new JCheckBox("Loot Feathers", true);
        final JCheckBox buryBonesCB = new JCheckBox("Bury Bones", true);
        JButton button = new JButton("Start");

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.LINE_START;
        gc.weightx = 0.5; gc.weighty = 0.5; gc.gridx = 0; gc.gridy = 0;
        add(label, gc);
        gc.gridx = 0; gc.gridy = 1;
        add(killChickensCB, gc);
        gc.gridx = 0; gc.gridy = 2;
        add(pickupFeathersCB, gc);
        gc.gridx = 0; gc.gridy = 3;
        add(buryBonesCB, gc);
        gc.anchor = GridBagConstraints.CENTER; gc.gridx = 0; gc.gridy = 4;
        add(button, gc);

        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!killChickensCB.isSelected() && !pickupFeathersCB.isSelected() && !buryBonesCB.isSelected())
                {
                    System.out.print("Please select at least one task");
                    return;
                }
                killChickens = killChickensCB.isSelected();
                pickupFeathers = pickupFeathersCB.isSelected();
                buryBones = buryBonesCB.isSelected();

                dispose();
                done = true;
            }
        });

    }

    public boolean killChickens()
    {
        return killChickens;
    }

    public boolean pickupFeathers()
    {
        return pickupFeathers;
    }

    public boolean buryBones()
    {
        return buryBones;
    }

    public boolean done()
    {
        return done;
    }
}
