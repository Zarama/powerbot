package armark1ng.shopitemsautobuyer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author ARMAR X K1NG
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = -8273271844182504919L;

	private JButton addItemButton;
	private JCheckBox bankingCheckBox;
	private JPanel bankingPanel;
	private JCheckBox customBanking;
	private JPanel generalPanel;
	private JCheckBox hopWorldsCheckBox;
	private JPanel instructionsPanel;
	private JList itemsList;
	private JPanel itemsPanel;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JLabel jLabel7;
	private JLabel jLabel8;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JTabbedPane jTabbedPane1;
	private JCheckBox membersOnlyCheckBox;
	private JLabel npcIdLable;
	private JFormattedTextField npcIdText;
	private JButton removeItemButton;
	private JComboBox shopsBox;
	private JPanel shopsPanel;
	private JButton startButton;
	private JButton DeselectAll;
	private JButton SelectAllWorlds;
	private JFormattedTextField worldHoppingDelay;
	private JFormattedTextField worldHoppingFrequancy;
	private JPanel worldHoppingSettings;
	private JList worldsList;
	private JPanel worldsSelectPanel;
	private CheckboxListItem[] worldListCheckBoxes;
	private AddItemFrame addItemFrame;
	private ShopItemsAutoBuyer premiumShopBuyer;

	public GUI(ShopItemsAutoBuyer premiumShopBuyer) {
		this.premiumShopBuyer = premiumShopBuyer;
		initComponents();
	}

	private void initComponents() {
		jTabbedPane1 = new JTabbedPane();
		generalPanel = new JPanel();
		shopsPanel = new JPanel();
		shopsBox = new JComboBox();
		jLabel1 = new JLabel();
		npcIdLable = new JLabel();
		itemsPanel = new JPanel();
		addItemButton = new JButton();
		jScrollPane1 = new JScrollPane();
		removeItemButton = new JButton();
		jLabel2 = new JLabel();
		startButton = new JButton();
		instructionsPanel = new JPanel();
		jLabel3 = new JLabel();
		jLabel4 = new JLabel();
		jLabel5 = new JLabel();
		jPanel1 = new JPanel();
		worldHoppingSettings = new JPanel();
		hopWorldsCheckBox = new JCheckBox();
		membersOnlyCheckBox = new JCheckBox();
		jLabel6 = new JLabel();
		jLabel7 = new JLabel();
		worldsSelectPanel = new JPanel();
		jScrollPane2 = new JScrollPane();
		bankingPanel = new JPanel();
		jPanel2 = new JPanel();
		bankingCheckBox = new JCheckBox();
		customBanking = new JCheckBox();
		jPanel3 = new JPanel();
		jLabel8 = new JLabel();
		SelectAllWorlds = new JButton();
		DeselectAll = new JButton();
		worldListCheckBoxes = new CheckboxListItem[Constants.WORLDS.length];
		for (int i = 0; i < Constants.WORLDS.length; i++) {
			worldListCheckBoxes[i] = new CheckboxListItem(Constants.WORLDS[i]);
		}
		NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(0);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(true);
		npcIdText = new JFormattedTextField(formatter);
		worldHoppingFrequancy = new JFormattedTextField(formatter);
		worldHoppingDelay = new JFormattedTextField(formatter);
		worldHoppingDelay.setValue(25000);
		npcIdText.setValue(0);
		worldHoppingFrequancy.setValue(3);
		setTitle("Armar's Shops Auto Buyer 1.0");
		setPreferredSize(new Dimension(408, 474));
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		shopsPanel.setBorder(BorderFactory.createTitledBorder("Shop"));
		shopsPanel.setPreferredSize(new Dimension(300, 120));

		shopsBox.setModel(new DefaultComboBoxModel(
				new String[] { "Baba Yaga's Store(Lunar)", "Magic Guild Store(Yanil)", "Custom" }));
		shopsBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				shopsBoxItemStateChanged(evt);
			}
		});

		jLabel1.setText("-Select Shop(custom if shop not there):");

		npcIdLable.setText("-Npc Id for custom shop:");
		npcIdText.setMinimumSize(new Dimension(30, 20));

		GroupLayout shopsPanelLayout = new GroupLayout(shopsPanel);
		shopsPanel.setLayout(shopsPanelLayout);
		shopsPanelLayout.setHorizontalGroup(shopsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(shopsPanelLayout.createSequentialGroup().addContainerGap()
						.addGroup(shopsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(npcIdLable, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(shopsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(shopsBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(
										npcIdText, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(27, 27, 27)));
		shopsPanelLayout
				.setVerticalGroup(shopsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(shopsPanelLayout.createSequentialGroup().addContainerGap()
								.addGroup(shopsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(shopsBox))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(shopsPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(npcIdLable, GroupLayout.PREFERRED_SIZE, 20,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(npcIdText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addContainerGap(35, Short.MAX_VALUE)));

		itemsPanel.setBorder(BorderFactory.createTitledBorder("Items"));

		addItemButton.setText("Add Item");
		addItemButton.setToolTipText("Adds item to the buy list.");
		addItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				addItemButtonActionPerformed(evt);
			}
		});

		itemsList = new JList();
		DefaultListModel itemsListModel = new DefaultListModel();
		itemsList.setModel(itemsListModel);

		jScrollPane1.setViewportView(itemsList);

		removeItemButton.setText("Remove Item");
		removeItemButton.setToolTipText("Removes the selected item from the list.");
		removeItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				removeItemButtonActionPerformed(evt);
			}
		});

		jLabel2.setText("Items to buy list:");

		GroupLayout itemsPanelLayout = new GroupLayout(itemsPanel);
		itemsPanel.setLayout(itemsPanelLayout);
		itemsPanelLayout.setHorizontalGroup(itemsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(itemsPanelLayout.createSequentialGroup().addContainerGap().addGroup(itemsPanelLayout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(itemsPanelLayout.createSequentialGroup()
								.addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addContainerGap())
						.addGroup(itemsPanelLayout.createSequentialGroup()
								.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(itemsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										.addComponent(removeItemButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(addItemButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addGap(0, 1, Short.MAX_VALUE)))));
		itemsPanelLayout.setVerticalGroup(itemsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(GroupLayout.Alignment.TRAILING, itemsPanelLayout.createSequentialGroup()
						.addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(itemsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(itemsPanelLayout.createSequentialGroup().addComponent(addItemButton)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(removeItemButton))
								.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 98,
										GroupLayout.PREFERRED_SIZE))));

		startButton.setText("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				startButtonActionPerformed(evt);
			}
		});

		instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));

		jLabel3.setText("*Login before you start the bot.");

		jLabel4.setText("*Stand next to the npc you want to buy from.");

		jLabel5.setText("*Restrict free/unwanted worlds from hopping tab.");

		GroupLayout instructionsPanelLayout = new GroupLayout(instructionsPanel);
		instructionsPanel.setLayout(instructionsPanelLayout);
		instructionsPanelLayout
				.setHorizontalGroup(instructionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		instructionsPanelLayout
				.setVerticalGroup(instructionsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(instructionsPanelLayout.createSequentialGroup()
								.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
								.addGap(0, 0, Short.MAX_VALUE)));

		GroupLayout generalPanelLayout = new GroupLayout(generalPanel);
		generalPanel.setLayout(generalPanelLayout);
		generalPanelLayout.setHorizontalGroup(generalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(generalPanelLayout.createSequentialGroup().addContainerGap().addGroup(generalPanelLayout
						.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
						.addComponent(startButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(instructionsPanel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(itemsPanel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(shopsPanel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 380,
								Short.MAX_VALUE))
						.addContainerGap(32, Short.MAX_VALUE)));
		generalPanelLayout.setVerticalGroup(generalPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(generalPanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(shopsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(itemsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(instructionsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(startButton, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jTabbedPane1.addTab("General", generalPanel);

		worldHoppingSettings.setBorder(BorderFactory.createTitledBorder("Settings"));
		worldHoppingSettings.setPreferredSize(new Dimension(300, 120));

		hopWorldsCheckBox.setText("Hop Worlds");
		hopWorldsCheckBox
				.setToolTipText("If not checked bot will not hop worlds, and wait for items in shop to restock.");

		membersOnlyCheckBox.setText("Members Only Worlds");
		membersOnlyCheckBox.setToolTipText("Checking this will restrict all free worlds automaticly.");
		membersOnlyCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JCheckBox box = (JCheckBox) evt.getSource();
				if (!box.isSelected())
					return;
				for (CheckboxListItem world : worldListCheckBoxes) {
					if (world == null || !world.isSelected()
							|| !world.toString().toLowerCase().contains("free to play"))
						continue;
					world.setSelected(false);
				}
				worldsList.repaint();
			}
		});
		jLabel6.setText("World Hopping Delay (In millieseconds):");
		jLabel6.setToolTipText("Time to wait before trying to hop worlds. (0 if you want it to hop instantly)");

		worldHoppingDelay
				.setToolTipText("Time to wait before trying to hop worlds. (0 if you want it to hop instantly)");

		jLabel7.setText("World Hopping Delay Frequency:");
		jLabel7.setToolTipText("How many worlds to hop before delay happens.");

		worldHoppingFrequancy.setValue((Integer) 3);
		worldHoppingFrequancy.setToolTipText("How many worlds to hop before delay happens.");

		GroupLayout worldHoppingSettingsLayout = new GroupLayout(worldHoppingSettings);
		worldHoppingSettings.setLayout(worldHoppingSettingsLayout);
		worldHoppingSettingsLayout.setHorizontalGroup(worldHoppingSettingsLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(worldHoppingSettingsLayout.createSequentialGroup().addContainerGap()
						.addGroup(worldHoppingSettingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(hopWorldsCheckBox)
								.addComponent(membersOnlyCheckBox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jLabel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
						.addGroup(worldHoppingSettingsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(worldHoppingDelay, GroupLayout.Alignment.TRAILING,
										GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
								.addComponent(worldHoppingFrequancy, GroupLayout.Alignment.TRAILING,
										GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		worldHoppingSettingsLayout.setVerticalGroup(worldHoppingSettingsLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(worldHoppingSettingsLayout.createSequentialGroup().addContainerGap()
						.addComponent(hopWorldsCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(membersOnlyCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(worldHoppingSettingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel6).addComponent(worldHoppingDelay, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(worldHoppingSettingsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
								.addComponent(worldHoppingFrequancy, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))));

		worldsSelectPanel.setBorder(BorderFactory.createTitledBorder("Worlds Select"));
		worldsList = new JList(worldListCheckBoxes);

		worldsList.setCellRenderer(new CheckboxListRenderer());
		worldsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		worldsList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				int index = worldsList.locationToIndex(event.getPoint());
				CheckboxListItem world = (CheckboxListItem) worldsList.getModel().getElementAt(index);
				if ((world.toString().toLowerCase().contains("free to play")) && membersOnlyCheckBox.isSelected()) {
					JOptionPane.showMessageDialog(getContentPane(), "You can't select that world!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				world.setSelected(!world.isSelected());
				worldsList.repaint(worldsList.getCellBounds(index, index));
			}
		});

		jScrollPane2.setViewportView(worldsList);
		SelectAllWorlds.setText("Select All");
		SelectAllWorlds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SelectAllWorldsActionPerformed(evt);
			}
		});

		DeselectAll.setText("Deselect all");
		DeselectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				DeselectAllActionPerformed(evt);
			}
		});
		GroupLayout worldsSelectPanelLayout = new GroupLayout(worldsSelectPanel);
		worldsSelectPanel.setLayout(worldsSelectPanelLayout);
		worldsSelectPanelLayout.setHorizontalGroup(worldsSelectPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(worldsSelectPanelLayout.createSequentialGroup().addContainerGap()
						.addGroup(worldsSelectPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane2)
								.addComponent(SelectAllWorlds, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(DeselectAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap()));
		worldsSelectPanelLayout.setVerticalGroup(worldsSelectPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(worldsSelectPanelLayout.createSequentialGroup()
						.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(SelectAllWorlds, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(DeselectAll, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(worldsSelectPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(worldHoppingSettings, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
						.addContainerGap(36, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
						.addComponent(worldHoppingSettings, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(worldsSelectPanel,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(142, Short.MAX_VALUE)));
		jTabbedPane1.addTab("World Hopping", jPanel1);
		jPanel2.setBorder(BorderFactory.createTitledBorder("Settings"));
		bankingCheckBox.setText("Bank Items(Will not work for custom shops for now)");
		bankingCheckBox.setToolTipText("If checked will bank items when inventory full.(will bank unstackables only)");
		customBanking.setText("Custom Banking");
		customBanking.setToolTipText("Use custom banking path.");
		GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(bankingCheckBox).addComponent(customBanking))
						.addContainerGap(89, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(bankingCheckBox)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(customBanking)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel3.setBorder(BorderFactory.createTitledBorder("Custom Banking"));
		jLabel8.setText("*Custom Banking is not added yet.");
		GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				GroupLayout.Alignment.TRAILING,
				jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addGap(115, 115, 115)
						.addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(115, Short.MAX_VALUE)));

		GroupLayout bankingPanelLayout = new GroupLayout(bankingPanel);
		bankingPanel.setLayout(bankingPanelLayout);
		bankingPanelLayout.setHorizontalGroup(bankingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(bankingPanelLayout.createSequentialGroup().addContainerGap()
						.addGroup(bankingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addContainerGap(32, Short.MAX_VALUE)));
		bankingPanelLayout.setVerticalGroup(bankingPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(bankingPanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(135, Short.MAX_VALUE)));

		jTabbedPane1.addTab("Banking", bankingPanel);

		getContentPane().add(jTabbedPane1, BorderLayout.CENTER);

		pack();
		DefaultComboBoxModel model = (DefaultComboBoxModel) shopsBox.getModel();
		String selectedItem = (String) model.getElementAt(shopsBox.getSelectedIndex());
		if (!selectedItem.equalsIgnoreCase("custom")) {
			shopsPanel.remove(npcIdLable);
			shopsPanel.remove(npcIdText);
			shopsPanel.revalidate();
			shopsPanel.repaint();
		}
		setLocationRelativeTo(null);
	}

	private void startButtonActionPerformed(ActionEvent evt) {
		DefaultComboBoxModel shopsBoxModel = (DefaultComboBoxModel) shopsBox.getModel();
		DefaultListModel itemsListModel = (DefaultListModel) itemsList.getModel();
		List<RegisteredItem> itemsToBuy = new ArrayList<RegisteredItem>();
		List<Integer> restrictedWorlds = new ArrayList<Integer>();
		String selectedItem = (String) shopsBoxModel.getElementAt(shopsBox.getSelectedIndex());
		int npcId = (Integer) npcIdText.getValue();
		int worldHopDelay = (Integer) worldHoppingDelay.getValue();
		int worldHopFrequancy = (Integer) worldHoppingFrequancy.getValue();
		if (npcId == 0 && selectedItem.equalsIgnoreCase("custom")) {
			JOptionPane.showMessageDialog(getContentPane(), "You cant leave the npcId field 0 when using custom shop.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		npcId = selectedItem.equalsIgnoreCase("custom") ? npcId : shopsBox.getSelectedIndex() == 0 ? 3837 : 3247;
		if (itemsListModel.isEmpty()) {
			JOptionPane.showMessageDialog(getContentPane(), "You cant need to add items first.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		boolean emptyWorldList = true;
		for (CheckboxListItem world : worldListCheckBoxes) {
			if (world == null || !world.isSelected)
				continue;
			if (world.isSelected) {
				emptyWorldList = false;
				break;
			}
		}
		if (emptyWorldList && hopWorldsCheckBox.isSelected()) {
			JOptionPane.showMessageDialog(getContentPane(),
					"You cannot have empty world list while choosing to hop world.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		for (Object item : Collections.list(itemsListModel.elements())) {
			itemsToBuy.add((RegisteredItem) item);
		}
		for (int always : ShopItemsAutoBuyer.ALWAYS_RESTRICTED)
			restrictedWorlds.add(always);
		for (CheckboxListItem world : worldListCheckBoxes) {
			if (world == null || world.isSelected)
				continue;
			restrictedWorlds.add(world.getWorldId());
		}
		premiumShopBuyer.setStartTime(System.currentTimeMillis());
		premiumShopBuyer.setNpcId(npcId);
		premiumShopBuyer.setWorldHopDelay(worldHopDelay);
		premiumShopBuyer.setWorldHopFrequncy(worldHopFrequancy);
		premiumShopBuyer.setHopWorlds(hopWorldsCheckBox.isSelected());
		premiumShopBuyer.setBanking(bankingCheckBox.isSelected());
		premiumShopBuyer.setItemsToBuy(itemsToBuy);
		premiumShopBuyer.setRestrictedWorlds(restrictedWorlds);
		if (!premiumShopBuyer.startGUI()) {
			JOptionPane.showMessageDialog(getContentPane(), "Couldn't find the shop npc near you.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		started = true;
		setVisible(false);
	}

	private boolean started;

	public boolean isStarted() {
		return started;
	}

	private void removeItemButtonActionPerformed(ActionEvent evt) {
		DefaultListModel model = (DefaultListModel) itemsList.getModel();
		if (model.isEmpty()) {
			JOptionPane.showMessageDialog(getContentPane(), "Your items to buy list is empty.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (itemsList.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(getContentPane(), "You must select the item you want to remove first!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		model.remove(itemsList.getSelectedIndex());
		itemsList.repaint();
	}

	private void addItemButtonActionPerformed(ActionEvent evt) {
		if (addItemFrame != null && !addItemFrame.isVisible())
			addItemFrame = null;
		if (addItemFrame != null) {
			JOptionPane.showMessageDialog(getContentPane(), "You already opened the add item dialoge.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		addItemFrame = new AddItemFrame();
		addItemFrame.setLocationRelativeTo(this);
		addItemFrame.setVisible(true);
	}

	private void SelectAllWorldsActionPerformed(ActionEvent evt) {
		for (CheckboxListItem world : worldListCheckBoxes) {
			if (world == null || world.isSelected
					|| (membersOnlyCheckBox.isSelected() && world.toString().toLowerCase().contains("free to play")))
				continue;
			world.setSelected(true);
		}
		worldsList.repaint();
	}

	private void DeselectAllActionPerformed(ActionEvent evt) {
		for (CheckboxListItem world : worldListCheckBoxes) {
			if (world == null || !world.isSelected)
				continue;
			world.setSelected(false);
		}
		worldsList.repaint();
	}

	private void shopsBoxItemStateChanged(ItemEvent evt) {
		DefaultComboBoxModel model = (DefaultComboBoxModel) shopsBox.getModel();
		String selectedItem = (String) model.getElementAt(shopsBox.getSelectedIndex());
		if (selectedItem.equalsIgnoreCase("custom")) {
			shopsPanel.add(npcIdLable);
			shopsPanel.add(npcIdText);
		} else {
			shopsPanel.remove(npcIdLable);
			shopsPanel.remove(npcIdText);
		}
		shopsPanel.revalidate();
		shopsPanel.repaint();
	}

	class CheckboxListItem {
		private String label;
		private boolean isSelected = true;

		public CheckboxListItem(String label) {
			this.label = label;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public String toString() {
			return label;
		}

		public int getWorldId() {
			return Integer.parseInt(label.split("-", 2)[0].toLowerCase().replace("world ", "").replace(" ", ""));
		}
	}

	public class CheckboxListRenderer extends JCheckBox implements ListCellRenderer {

		private static final long serialVersionUID = 2610317938592506544L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setEnabled(list.isEnabled());
			setSelected(((JCheckBox) value).isSelected());
			setFont(list.getFont());
			setToolTipText(value.toString());
			setBackground(list.getBackground());
			setForeground(list.getForeground());
			setText(value.toString());
			return this;
		}
	}

	public AddItemFrame getAddItemFrame() {
		return addItemFrame;
	}

	public void setAddItemFrame(AddItemFrame addItemFrame) {
		this.addItemFrame = addItemFrame;
	}

	public class AddItemFrame extends JFrame {
		private static final long serialVersionUID = 1L;
		private JButton ItemFrameAddItemButton;
		private JButton ItemFrameCancelButton;
		private JComboBox ItemTypeComboBox;
		private JLabel jLabel1;
		private JLabel jLabel2;
		private JLabel jLabel3;
		private JLabel jLabel4;
		private JLabel jLabel5;
		private JPanel jPanel1;
		private JFormattedTextField ItemIdField;
		private JFormattedTextField minAmountField;
		private JFormattedTextField packIdField;
		private JFormattedTextField priceField;

		public AddItemFrame() {
			try {
				for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Windows".equals(info.getName())) {
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(AddItemFrame.class.getName()).log(Level.SEVERE, null, ex);
			} catch (InstantiationException ex) {
				Logger.getLogger(AddItemFrame.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				Logger.getLogger(AddItemFrame.class.getName()).log(Level.SEVERE, null, ex);
			} catch (UnsupportedLookAndFeelException ex) {
				Logger.getLogger(AddItemFrame.class.getName()).log(Level.SEVERE, null, ex);
			}
			initComponents();
		}

		private void initComponents() {
			jPanel1 = new JPanel();
			ItemFrameCancelButton = new JButton();
			ItemFrameAddItemButton = new JButton();
			jLabel1 = new JLabel();
			jLabel2 = new JLabel();
			jLabel3 = new JLabel();
			ItemTypeComboBox = new JComboBox();
			jLabel4 = new JLabel();
			jLabel5 = new JLabel();
			NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
			formatter.setValueClass(Integer.class);
			formatter.setMinimum(0);
			formatter.setMaximum(Integer.MAX_VALUE);
			formatter.setAllowsInvalid(false);
			formatter.setCommitsOnValidEdit(true);
			ItemIdField = new JFormattedTextField(formatter);
			priceField = new JFormattedTextField(formatter);
			minAmountField = new JFormattedTextField(formatter);
			packIdField = new JFormattedTextField(formatter);
			ItemIdField.setValue(0);
			priceField.setValue(0);
			minAmountField.setValue(0);
			packIdField.setValue(0);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("Add item");
			setPreferredSize(new Dimension(280, 325));
			ItemFrameCancelButton.setText("Cancel");
			ItemFrameCancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					ItemFrameCancelButtonActionPerformed(evt);
				}
			});
			ItemFrameAddItemButton.setText("Add item");
			ItemFrameAddItemButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					ItemFrameAddItemButtonActionPerformed(evt);
				}
			});
			jLabel1.setText("Item Id:");
			jLabel1.setToolTipText("Id of the item you want to buy.");
			jLabel2.setText("Min Amount:");
			jLabel2.setToolTipText("The minimum amount you want to keep in shop.");
			jLabel3.setText("Price At Min Amount:");
			jLabel3.setToolTipText("Price of the item at the min amount.");
			ItemTypeComboBox.setModel(new DefaultComboBoxModel(new String[] { "Stackable", "Unstackable", "Pack" }));
			ItemTypeComboBox.setToolTipText("Select the type of the item you want to buy.");
			ItemTypeComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					ItemTypeComboBoxItemStateChanged(evt);
				}
			});
			jLabel4.setText("Item Type:");
			jLabel4.setToolTipText("Select the type of the item you want to buy.");
			jLabel5.setText("Id of the item inside pack:");
			jLabel5.setToolTipText("Id of the item you get on opening pack.");
			ItemIdField.setToolTipText("Id of the item you want to buy.");
			priceField.setToolTipText("Price of the item at the min amount.");
			minAmountField.setToolTipText("The minimum amount you want to keep in shop.");
			packIdField.setToolTipText("Id of the item you get on opening pack.");
			GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout
							.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
									.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
											.addComponent(jLabel4, GroupLayout.PREFERRED_SIZE, 98,
													GroupLayout.PREFERRED_SIZE)
											.addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
											.addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
									.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(priceField, GroupLayout.PREFERRED_SIZE, 98,
													GroupLayout.PREFERRED_SIZE)
											.addComponent(packIdField, GroupLayout.PREFERRED_SIZE, 98,
													GroupLayout.PREFERRED_SIZE)))
							.addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
									.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(jLabel1).addComponent(jLabel2, GroupLayout.PREFERRED_SIZE,
													126, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
											Short.MAX_VALUE)
									.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
											.addComponent(ItemTypeComboBox, 0, 98, Short.MAX_VALUE)
											.addComponent(ItemIdField)
											.addComponent(minAmountField, GroupLayout.Alignment.TRAILING)))
							.addComponent(ItemFrameAddItemButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
									Short.MAX_VALUE)
							.addComponent(ItemFrameCancelButton, GroupLayout.Alignment.TRAILING,
									GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addContainerGap()));
			jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGap(14, 14, 14)
							.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
									.addComponent(ItemTypeComboBox)
									.addComponent(jLabel4, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(ItemIdField, GroupLayout.PREFERRED_SIZE, 24,
											GroupLayout.PREFERRED_SIZE)
									.addGroup(jPanel1Layout.createSequentialGroup()
											.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 29,
													GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
													.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 29,
															GroupLayout.PREFERRED_SIZE)
													.addComponent(minAmountField, GroupLayout.PREFERRED_SIZE, 24,
															GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(
									jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 29,
													GroupLayout.PREFERRED_SIZE)
											.addComponent(priceField, GroupLayout.PREFERRED_SIZE, 24,
													GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(
									jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 29,
													GroupLayout.PREFERRED_SIZE)
											.addComponent(packIdField, GroupLayout.PREFERRED_SIZE, 24,
													GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
							.addComponent(ItemFrameAddItemButton, GroupLayout.PREFERRED_SIZE, 31,
									GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(ItemFrameCancelButton, GroupLayout.PREFERRED_SIZE, 31,
									GroupLayout.PREFERRED_SIZE)
							.addContainerGap()));

			setResizable(false);
			getContentPane().add(jPanel1, BorderLayout.CENTER);
			pack();
			DefaultComboBoxModel model = (DefaultComboBoxModel) ItemTypeComboBox.getModel();
			String selectedItem = (String) model.getElementAt(ItemTypeComboBox.getSelectedIndex());
			if (!selectedItem.equalsIgnoreCase("pack")) {
				jPanel1.remove(jLabel5);
				jPanel1.remove(packIdField);
				jPanel1.revalidate();
				jPanel1.repaint();
			}
		}

		private void ItemFrameAddItemButtonActionPerformed(ActionEvent evt) {
			DefaultComboBoxModel modelItemType = (DefaultComboBoxModel) ItemTypeComboBox.getModel();
			String selectedItem = (String) modelItemType.getElementAt(ItemTypeComboBox.getSelectedIndex());
			int itemId = ((Integer) ItemIdField.getValue());
			int price = ((Integer) priceField.getValue());
			int minamount = ((Integer) minAmountField.getValue());
			int packId = ((Integer) packIdField.getValue());
			if (itemId == 0 || price == 0 || (packId == 0 && selectedItem.equalsIgnoreCase("pack"))) {
				JOptionPane.showMessageDialog(getContentPane(), "You haven't set some of the values reqiured.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (itemId == packId) {
				JOptionPane.showMessageDialog(getContentPane(), "You itemId and item inside pack id can't be the same.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			DefaultListModel model = (DefaultListModel) itemsList.getModel();
			for (Object item : Collections.list(model.elements())) {
				if (((RegisteredItem) item).getItemId() == itemId) {
					JOptionPane.showMessageDialog(getContentPane(), "You already have that item in your item list.",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			model.addElement(new RegisteredItem(itemId, minamount, price,
					selectedItem.equalsIgnoreCase("pack") ? packId : -1, selectedItem.equalsIgnoreCase("stackable")));
			addItemFrame = null;
			dispose();
			itemsList.repaint();
		}

		private void ItemTypeComboBoxItemStateChanged(ItemEvent evt) {
			DefaultComboBoxModel model = (DefaultComboBoxModel) ItemTypeComboBox.getModel();
			String selectedItem = (String) model.getElementAt(ItemTypeComboBox.getSelectedIndex());
			if (selectedItem.equalsIgnoreCase("pack")) {
				jPanel1.add(jLabel5);
				jPanel1.add(packIdField);
			} else {
				jPanel1.remove(jLabel5);
				jPanel1.remove(packIdField);
			}
			jPanel1.revalidate();
			jPanel1.repaint();
		}

		private void ItemFrameCancelButtonActionPerformed(ActionEvent evt) {
			addItemFrame = null;
			dispose();
		}

	}
}
