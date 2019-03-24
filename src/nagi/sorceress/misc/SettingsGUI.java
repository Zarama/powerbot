package nagi.sorceress.misc;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.powerbot.script.Tile;

import nagi.sorceress.SorceressGarden;
import nagi.sorceress.script.Season;
import nagi.sorceress.script.SeasonScript;
import nagi.sorceress.script.summer.Summer;
import nagi.sorceress.type.ClientPhase;

@SuppressWarnings("serial")
public class SettingsGUI extends JFrame {

    public static void main(String[] args) throws Exception {
        new SettingsGUI(null);
    }

    public final JPanel mainInterface;
    public final Border mainBorder;

    public final LayoutManager layout;

    public final JLabel gardenLabel;
    public final JComboBox<String> gardenBox;

    public final JLabel objectiveLabel;
    public final JComboBox<String> objectiveBox;

    public final JButton cancelButton;
    public final ActionListener cancelListener;

    public final JButton collectButton;
    public final ActionListener collectListener;

    public final WindowAdapter closeListener;

    private boolean success = false;

    public SettingsGUI(final SorceressGarden garden) {
        this.mainInterface = new JPanel();

        this.mainBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        this.mainInterface.setBorder(this.mainBorder);

        this.layout = new GridLayout(0, 2, 10, 10);
        this.mainInterface.setLayout(this.layout);

        this.gardenLabel = new JLabel("Select the Garden:", JLabel.CENTER);
        this.gardenBox = new JComboBox<String>(new String[] { 
//                "Winter (1 Thieving)", "Spring (25 Thieving)", "Autumn (45 Thieving)", 
                "Summer (65 Thieving)" });
        this.gardenBox.setEnabled(false);
        
        JLabel gardenRender = (JLabel) this.gardenBox.getRenderer();
        gardenRender.setHorizontalAlignment(JLabel.CENTER);
        gardenRender.setVerticalAlignment(JLabel.CENTER);

        this.objectiveLabel = new JLabel("Select the Objective: ", SwingConstants.CENTER);
        this.objectiveBox = new JComboBox<String>(new String[] { "Collect Sq'irk Juice", "Collect Herbs" });
        JLabel objectiveRender = (JLabel) this.objectiveBox.getRenderer();
        objectiveRender.setHorizontalAlignment(JLabel.CENTER);
        objectiveRender.setVerticalAlignment(JLabel.CENTER);

        this.cancelListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsGUI oi = getInstance();
                oi.dispose();

                garden.ctx().controller.stop();
            }
        };

        this.cancelButton = new JButton("Close Script");
        this.cancelButton.addActionListener(this.cancelListener);

        this.collectListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsGUI oi = getInstance();
                oi.dispose();

                Season season = null;
//                if (gardenBox.getSelectedIndex() == 3) {
//                    season = new Summer(garden);
//                }
                season = new Summer(garden);

                SeasonScript script;
                try {
                    script = objectiveBox.getSelectedIndex() == 0 ? season.getFruitsScript().getConstructor(Season.class).newInstance(season) : season.getHerbsScript().getConstructor(Season.class).newInstance(season);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    garden.ctx().controller.stop();
                    return;
                }

                garden.setSeason(season);
                garden.setSeasonScript(script);

                garden.getStatistics().resetStartTime();

                Tile tile = garden.ctx().players.local().tile();
                if (garden.ctx().inventory.select().count() < 28) {
                    if (garden.getSeason().inGarden(tile) || garden.getSeason().inLobby(tile)) {
                        garden.setClientPhase(ClientPhase.COLLECTING);
                    } else {
                        garden.getBankScript().generateReturnPath();
                        garden.setClientPhase(ClientPhase.RETURNING);
                    }
                } else {
                    garden.getBankScript().generateBankPath();
                    garden.setClientPhase(ClientPhase.BANKING);
                }

                System.out.println(gardenBox.getSelectedIndex() + " -> " + gardenBox.getSelectedItem().toString());
                System.out.println(objectiveBox.getSelectedIndex() + " -> " + objectiveBox.getSelectedItem().toString());
            }
        };

        this.collectButton = new JButton("Start Script");
        this.collectButton.addActionListener(this.collectListener);

        this.closeListener = new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                SettingsGUI oi = getInstance();
                oi.dispose();

                garden.ctx().controller.stop();
            }
        };

        this.addWindowListener(this.closeListener);

        this.mainInterface.add(this.gardenLabel);
        this.mainInterface.add(this.gardenBox);
        this.mainInterface.add(this.objectiveLabel);
        this.mainInterface.add(this.objectiveBox);
        this.mainInterface.add(this.cancelButton);
        this.mainInterface.add(this.collectButton);
        this.add(this.mainInterface);

        this.setTitle("Sorceress' Garden");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);

        this.pack();
        this.setVisible(true);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
    }

    public boolean success() {
        return this.success;
    }

    public SettingsGUI getInstance() {
        return this;
    }
}
