package superchaoran.GraniteSplitter.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import superchaoran.GraniteSplitter.GraniteSpliterMain;
import superchaoran.GraniteSplitter.constants.GraniteRaw;

public class GUI extends JFrame {

    private JPanel contentPane;

    public GUI(final GraniteSpliterMain script, final ClientContext ctx) {
        Tile loc = ctx.players.local().tile();
        setTitle("Graniter Splitter");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 223, 220);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Granite Splitter");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(10, 11, 200, 30);
        contentPane.add(lblNewLabel);

        final JComboBox cmbLog = new JComboBox(GraniteRaw.values());
        cmbLog.setBounds(76, 50, 131, 34);
        contentPane.add(cmbLog);

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ctx.game.clientState() == Constants.GAME_MAP_LOADED) {
                    script.submit((GraniteRaw) cmbLog.getSelectedItem());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please start logged in.", "", JOptionPane.WARNING_MESSAGE);
                    ctx.controller.stop();
                    dispose();
                }
            }
        });
        btnStart.setBounds(96, 121, 111, 23);
        contentPane.add(btnStart);

        JLabel lblLogType = new JLabel("Type:");
        lblLogType.setBounds(20, 53, 46, 28);
        contentPane.add(lblLogType);
    }
}
