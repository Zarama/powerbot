package j333.scripts.smithing;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class JPSGeneralSmithingForm {
    private final static String CONFIG_FILE_NAME = "JPSGeneralSmithingConfig.ini";

    private JPSFormEventHandler eventHandler;

    public void setEventHandler(JPSFormEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    private JLabel bankIdsLabel;
    private JLabel smelterNamesLabel;
    private JLabel barIdsLabel;
    private JLabel oreIdsLabel;
    private JLabel oreAmountsLabel;
    private JButton saveButton;
    private JLabel retryAttemptsLabel;
    private JLabel defaultWaitLabel;
    private JPanel contentPanel;

    private JTextField oreAmountsTextField;

    public List<Integer> getOreAmounts() {
        return this.getIntegerListFromString(this.oreAmountsTextField.getText().trim());
    }

    private JTextField bankIdsTextField;

    public List<Integer> getBankIds() {
        return this.getIntegerListFromString(this.bankIdsTextField.getText().trim());
    }

    private JTextField smelterNamesTextField;

    public List<String> getSmelterNames() {
        String[] components = this.smelterNamesTextField.getText().trim().split(",");
        return Arrays.asList(Arrays.stream(components).map(String::trim).toArray(String[]::new));
    }

    private JTextField barIdTextField;

    public List<Integer> getBarIds() {
        return this.getIntegerListFromString(this.barIdTextField.getText().trim());
    }

    private JTextField oreIdsTextField;

    public List<Integer> getOreIds() {
        return this.getIntegerListFromString(this.oreIdsTextField.getText().trim());
    }

    private JSpinner retryAttemptsSpinner;

    public Integer getRetryAttempts() {
        return (Integer) this.retryAttemptsSpinner.getValue();
    }

    private JSpinner defaultWaitSpinner;

    public Integer getDefaultWait() {
        return (Integer) this.retryAttemptsSpinner.getValue();
    }

    private File storageDirectory;

    public JPSGeneralSmithingForm(File storageDirectory) {
        this.storageDirectory = storageDirectory;

        this.setupUI();

        JFrame frame = new JFrame("Configuration");
        frame.setContentPane(this.contentPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Properties config = this.getSavedConfig();
        if (config != null) {
            this.loadConfig(config);
        }

        this.saveButton.addActionListener((e) ->
        {
            try (FileOutputStream stream = new FileOutputStream(new File(this.storageDirectory, JPSGeneralSmithingForm.CONFIG_FILE_NAME))) {
                Properties loadedConfig = this.getLoadedConfig();
                loadedConfig.store(stream, null);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private Properties getLoadedConfig() {
        Properties config = new Properties();
        config.setProperty("barIds", this.barIdTextField.getText());
        config.setProperty("oreIds", this.oreIdsTextField.getText());
        config.setProperty("bankIds", this.bankIdsTextField.getText());
        config.setProperty("oreAmounts", this.oreAmountsTextField.getText());
        config.setProperty("smelterNames", this.smelterNamesTextField.getText());
        config.setProperty("defaultWait", this.defaultWaitSpinner.getValue().toString());
        config.setProperty("retryAttempts", this.retryAttemptsSpinner.getValue().toString());
        return config;
    }

    private Properties getSavedConfig() {
        try (FileInputStream stream = new FileInputStream(new File(this.storageDirectory, JPSGeneralSmithingForm.CONFIG_FILE_NAME))) {
            Properties properties = new Properties();
            properties.load(stream);
            return properties;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadConfig(Properties config) {
        this.barIdTextField.setText(config.get("barIds").toString());
        this.oreIdsTextField.setText(config.get("oreIds").toString());
        this.bankIdsTextField.setText(config.get("bankIds").toString());
        this.oreAmountsTextField.setText(config.get("oreAmounts").toString());
        this.smelterNamesTextField.setText(config.get("smelterNames").toString());
        this.defaultWaitSpinner.setValue(Integer.parseInt(config.get("defaultWait").toString()));
        this.retryAttemptsSpinner.setValue(Integer.parseInt(config.get("retryAttempts").toString()));
    }

    public List<Integer> getIntegerListFromString(String str) {
        String[] components = str.split(",");
        List<Integer> list = new ArrayList<>();

        for (String component : components) {
            list.add(Integer.parseInt(component.trim()));
        }

        return list;
    }

    private void setupUI() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(new Color(-12236470));
        contentPanel.setMaximumSize(new Dimension(-1, -1));
        contentPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        bankIdsTextField = new JTextField();
        bankIdsTextField.setAlignmentX(0.0f);
        bankIdsTextField.setBackground(new Color(-13881809));
        bankIdsTextField.setCaretColor(new Color(-1));
        bankIdsTextField.setFont(new Font("Helvetica Neue", Font.PLAIN, bankIdsTextField.getFont().getSize()));
        bankIdsTextField.setForeground(new Color(-1));
        bankIdsTextField.setName("bankIds");
        bankIdsTextField.setText("");
        bankIdsTextField.setToolTipText("Comma seperated");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(bankIdsTextField, gbc);
        barIdTextField = new JTextField();
        barIdTextField.setAlignmentX(0.0f);
        barIdTextField.setBackground(new Color(-13881809));
        barIdTextField.setCaretColor(new Color(-1));
        barIdTextField.setFont(new Font("Helvetica Neue", Font.PLAIN, barIdTextField.getFont().getSize()));
        barIdTextField.setForeground(new Color(-1));
        barIdTextField.setName("barId");
        barIdTextField.setToolTipText("Comma seperated");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(barIdTextField, gbc);
        oreAmountsTextField = new JTextField();
        oreAmountsTextField.setAlignmentX(0.0f);
        oreAmountsTextField.setBackground(new Color(-13881809));
        oreAmountsTextField.setCaretColor(new Color(-1));
        oreAmountsTextField.setFont(new Font("Helvetica Neue", Font.PLAIN, oreAmountsTextField.getFont().getSize()));
        oreAmountsTextField.setForeground(new Color(-1));
        oreAmountsTextField.setName("oreAmounts");
        oreAmountsTextField.setToolTipText("Comma seperated");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(oreAmountsTextField, gbc);
        bankIdsLabel = new JLabel();
        bankIdsLabel.setBackground(new Color(-984065));
        bankIdsLabel.setEnabled(true);
        bankIdsLabel.setFocusable(false);
        bankIdsLabel.setFont(new Font("Helvetica Neue", Font.BOLD, bankIdsLabel.getFont().getSize()));
        bankIdsLabel.setForeground(new Color(-1));
        bankIdsLabel.setHorizontalAlignment(0);
        bankIdsLabel.setHorizontalTextPosition(4);
        bankIdsLabel.setOpaque(false);
        bankIdsLabel.setText("Bank Ids");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(bankIdsLabel, gbc);
        smelterNamesTextField = new JTextField();
        smelterNamesTextField.setAlignmentX(0.0f);
        smelterNamesTextField.setBackground(new Color(-13881809));
        smelterNamesTextField.setCaretColor(new Color(-1));
        smelterNamesTextField.setFont(new Font("Helvetica Neue", Font.PLAIN, smelterNamesTextField.getFont().getSize()));
        smelterNamesTextField.setForeground(new Color(-1));
        smelterNamesTextField.setName("smelterNames");
        smelterNamesTextField.setToolTipText("Comma seperated");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(smelterNamesTextField, gbc);
        smelterNamesLabel = new JLabel();
        smelterNamesLabel.setBackground(new Color(-984065));
        smelterNamesLabel.setFont(new Font("Helvetica Neue", Font.BOLD, smelterNamesLabel.getFont().getSize()));
        smelterNamesLabel.setForeground(new Color(-1));
        smelterNamesLabel.setHorizontalAlignment(0);
        smelterNamesLabel.setHorizontalTextPosition(4);
        smelterNamesLabel.setText("Smelter Names");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(smelterNamesLabel, gbc);
        barIdsLabel = new JLabel();
        barIdsLabel.setBackground(new Color(-984065));
        barIdsLabel.setFont(new Font("Helvetica Neue", Font.BOLD, barIdsLabel.getFont().getSize()));
        barIdsLabel.setForeground(new Color(-1));
        barIdsLabel.setHorizontalAlignment(0);
        barIdsLabel.setHorizontalTextPosition(4);
        barIdsLabel.setText("Bar Id");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(barIdsLabel, gbc);
        oreIdsTextField = new JTextField();
        oreIdsTextField.setAlignmentX(0.0f);
        oreIdsTextField.setBackground(new Color(-13881809));
        oreIdsTextField.setCaretColor(new Color(-1));
        oreIdsTextField.setFont(new Font("Helvetica Neue", Font.PLAIN, oreIdsTextField.getFont().getSize()));
        oreIdsTextField.setForeground(new Color(-1));
        oreIdsTextField.setName("oreIds");
        oreIdsTextField.setToolTipText("Comma seperated");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(oreIdsTextField, gbc);
        oreIdsLabel = new JLabel();
        oreIdsLabel.setBackground(new Color(-984065));
        oreIdsLabel.setFont(new Font("Helvetica Neue", Font.BOLD, oreIdsLabel.getFont().getSize()));
        oreIdsLabel.setForeground(new Color(-1));
        oreIdsLabel.setHorizontalAlignment(0);
        oreIdsLabel.setHorizontalTextPosition(4);
        oreIdsLabel.setText("Ore Ids");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(oreIdsLabel, gbc);
        oreAmountsLabel = new JLabel();
        oreAmountsLabel.setAlignmentX(0.0f);
        oreAmountsLabel.setAlignmentY(0.5f);
        oreAmountsLabel.setBackground(new Color(-984065));
        oreAmountsLabel.setFont(new Font("Helvetica Neue", Font.BOLD, oreAmountsLabel.getFont().getSize()));
        oreAmountsLabel.setForeground(new Color(-1));
        oreAmountsLabel.setHorizontalAlignment(0);
        oreAmountsLabel.setHorizontalTextPosition(4);
        oreAmountsLabel.setText("Ore Amounts");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(oreAmountsLabel, gbc);
        saveButton = new JButton();
        saveButton.setBackground(new Color(-1311233));
        saveButton.setFont(new Font("Helvetica Neue", Font.PLAIN, saveButton.getFont().getSize()));
        saveButton.setForeground(new Color(-16777216));
        saveButton.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 10, 10);
        contentPanel.add(saveButton, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(separator1, gbc);
        defaultWaitLabel = new JLabel();
        defaultWaitLabel.setFont(new Font("Helvetica Neue", Font.BOLD, defaultWaitLabel.getFont().getSize()));
        defaultWaitLabel.setForeground(new Color(-1));
        defaultWaitLabel.setText("Default Wait");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(defaultWaitLabel, gbc);
        retryAttemptsLabel = new JLabel();
        retryAttemptsLabel.setFont(new Font("Helvetica Neue", Font.BOLD, retryAttemptsLabel.getFont().getSize()));
        retryAttemptsLabel.setForeground(new Color(-1));
        retryAttemptsLabel.setText("Retry Attempts");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 0, 10);
        contentPanel.add(retryAttemptsLabel, gbc);
        retryAttemptsSpinner = new JSpinner();
        retryAttemptsSpinner.setBackground(new Color(-13881809));
        retryAttemptsSpinner.setFont(new Font("Helvetica Neue", retryAttemptsSpinner.getFont().getStyle(), retryAttemptsSpinner.getFont().getSize()));
        retryAttemptsSpinner.setForeground(new Color(-1));
        retryAttemptsSpinner.setName("retryAttempts");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(retryAttemptsSpinner, gbc);
        defaultWaitSpinner = new JSpinner();
        defaultWaitSpinner.setBackground(new Color(-13881809));
        defaultWaitSpinner.setFont(new Font("Helvetica Neue", defaultWaitSpinner.getFont().getStyle(), defaultWaitSpinner.getFont().getSize()));
        defaultWaitSpinner.setForeground(new Color(-1));
        defaultWaitSpinner.setName("defaultWait");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 10);
        contentPanel.add(defaultWaitSpinner, gbc);
        bankIdsLabel.setLabelFor(bankIdsTextField);
        smelterNamesLabel.setLabelFor(smelterNamesTextField);
        barIdsLabel.setLabelFor(barIdTextField);
        oreIdsLabel.setLabelFor(oreIdsTextField);
        oreAmountsLabel.setLabelFor(oreAmountsTextField);
        defaultWaitLabel.setLabelFor(defaultWaitSpinner);
        retryAttemptsLabel.setLabelFor(retryAttemptsSpinner);
    }
}
