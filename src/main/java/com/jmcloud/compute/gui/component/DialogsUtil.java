package com.jmcloud.compute.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

public class DialogsUtil {

	public static String[] showAccountInputDialog(Component component,
			String title, int optionType, int messageOption) {

		JPanel accountInputDialogPanel = new JPanel();
		accountInputDialogPanel.setLayout(new BorderLayout());

		JPanel multiInputPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		// gridBagLayout.columnWidths = new int[] { 100, 200 };
		multiInputPanel.setLayout(gridBagLayout);

		JLabel iDLabel = new JLabel("ID : ");
		iDLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcl00 = new GridBagConstraints();
		gbcl00.fill = GridBagConstraints.HORIZONTAL;
		gbcl00.gridx = 0;
		gbcl00.gridy = 0;
		multiInputPanel.add(iDLabel, gbcl00);

		JTextField iDJTextField = new JTextField();
		iDJTextField.setColumns(14);
		GridBagConstraints gbct10 = new GridBagConstraints();
		gbct10.fill = GridBagConstraints.HORIZONTAL;
		gbct10.gridx = 1;
		gbct10.gridy = 0;
		multiInputPanel.add(iDJTextField, gbct10);

		JLabel passwordLabel = new JLabel("Password : ");
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbcl01 = new GridBagConstraints();
		gbcl01.fill = GridBagConstraints.HORIZONTAL;
		gbcl01.gridx = 0;
		gbcl01.gridy = 1;
		multiInputPanel.add(passwordLabel, gbcl01);

		JPasswordField passwordField = new JPasswordField();
		passwordField.setColumns(14);
		GridBagConstraints gbct11 = new GridBagConstraints();
		gbct11.fill = GridBagConstraints.HORIZONTAL;
		gbct11.gridx = 1;
		gbct11.gridy = 1;
		multiInputPanel.add(passwordField, gbct11);

		accountInputDialogPanel.add(multiInputPanel, BorderLayout.CENTER);
		String[] inputTexts = new String[2];
		int result = JOptionPane.showConfirmDialog(component,
				accountInputDialogPanel, title, optionType, messageOption);
		if (result == JOptionPane.OK_OPTION) {
			inputTexts[0] = iDJTextField.getText();
			inputTexts[1] = new String(passwordField.getPassword());
			return inputTexts;
		}
		return null;
	}

	public static String[] showMultiInputDialog(Component component,
			String title, String message, String[] inputLabels,
			JTextField[] inputJComponents, int optionType, int messageOption) {

		JPanel multiInputDialogMainPanel = new JPanel();
		message = "<html>" + message.replace("\n", "<br>") + "</html>";
		JLabel messageLabel = new JLabel(message);
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel multiInputPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		// gridBagLayout.columnWidths = new int[] { 100, 200 };
		multiInputPanel.setLayout(gridBagLayout);

		for (int i = 0; i < inputLabels.length; i++) {
			JLabel computeNameLabel = new JLabel(inputLabels[i]);
			computeNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			GridBagConstraints gbcl = new GridBagConstraints();
			gbcl.fill = GridBagConstraints.HORIZONTAL;
			gbcl.gridx = 0;
			gbcl.gridy = i;
			multiInputPanel.add(computeNameLabel, gbcl);

			JTextField computeNameJComponent = inputJComponents[i];
			GridBagConstraints gbct = new GridBagConstraints();
			gbct.fill = GridBagConstraints.HORIZONTAL;
			gbct.gridx = 1;
			gbct.gridy = i;
			multiInputPanel.add(computeNameJComponent, gbct);
		}

		multiInputDialogMainPanel.add(messageLabel, BorderLayout.NORTH);
		multiInputDialogMainPanel.add(multiInputPanel, BorderLayout.CENTER);
		String[] inputTexts = new String[inputJComponents.length];
		int result = JOptionPane.showConfirmDialog(component,
				multiInputDialogMainPanel, title, optionType, messageOption);
		if (result == JOptionPane.OK_OPTION) {
			for (int i = 0; i < inputJComponents.length; i++) {
				inputTexts[i] = inputJComponents[i].getText();
			}
			return inputTexts;
		}
		return null;
	}

	public static Properties showChangePropertiesDialog(Properties properties,
			Component component, String title, String message) {
		ChangePropertiesJPanel multiInputDialogMainPanel = new ChangePropertiesJPanel(
				properties);
		message = "<html>" + message.replace("\n", "<br>") + "</html>";
		JLabel messageLabel = multiInputDialogMainPanel.getMessageLabel();
		messageLabel.setText(message);
		int result = JOptionPane.showConfirmDialog(component,
				multiInputDialogMainPanel, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			Map<String, JTextField> textFieldMap = multiInputDialogMainPanel
					.getTextFieldMap();
			Iterator<String> keyIterator = textFieldMap.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				properties.put(key, textFieldMap.get(key).getText());
			}
		}
		return properties;
	}

	private static Properties regionAMIMappingProperties;

	public static String[] showCreateComputeInputDialog(Component mainFrame,
			String title, String[] defaultRegions, String currentRegion,
			String groupName) {
		if (regionAMIMappingProperties == null) {
			regionAMIMappingProperties = SysUtils
					.getProperties(SystemEnviroment.getRegionAMIMappingPath());
		}
		CreateComputeInputJPanel createComputeInputPanel = new CreateComputeInputJPanel();
		JLabel messageLabel = createComputeInputPanel.getMessageLabel();
		JTextField computeNameTextField = createComputeInputPanel
				.getComputeNameTextField();
		JComboBox<String> imageIDComboBox = createComputeInputPanel
				.getImageIDComboBox();
		String[] idAMIs = regionAMIMappingProperties.getProperty(currentRegion)
				.split(",");
		final Map<String, String> idAMIMap = new HashMap<>();
		for (String s : idAMIs) {
			String[] Strings = s.split(":");
			idAMIMap.put(Strings[0], Strings[1]);
		}
		for (String key : idAMIMap.keySet()) {
			imageIDComboBox.addItem(key);
		}

		imageIDComboBox.setBackground(Color.white);

		imageIDComboBox.setRenderer(new ListCellRenderer<String>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends String> list, String value, int index,
					boolean isSelected, boolean cellHasFocus) {
				JLabel lbl = new JLabel(value);
				if (isSelected) {
					lbl.setForeground(Color.blue);
				}
				lbl.setToolTipText(idAMIMap.get(value));
				return lbl;
			}
		});
		imageIDComboBox.setSelectedIndex(-1);
		JComboBox<String> computeTypesComboBox = createComputeInputPanel
				.getComputeTypeComboBox();
		for (String instanceType : defaultRegions) {
			computeTypesComboBox.addItem(instanceType);
		}
		computeTypesComboBox.setSelectedIndex(0);
		JComboBox<String> numOfComputeComboBox = createComputeInputPanel
				.getNumOfComputeComboBox();

		String message = "<html>Input A Compute Infomation!<br>Current Region : "
				+ currentRegion + ", Group : " + groupName + "</html>";
		messageLabel.setText(message);
		if ("Create Compute".equals(title)) {
			numOfComputeComboBox.setSelectedIndex(0);
			numOfComputeComboBox.setEnabled(false);
		}

		String[] inputTexts = new String[4];
		int result = JOptionPane.showConfirmDialog(mainFrame,
				createComputeInputPanel, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		int imageIDIndex;
		while (result == JOptionPane.OK_OPTION) {
			inputTexts[0] = computeNameTextField.getText();
			imageIDIndex = imageIDComboBox.getSelectedIndex();
			inputTexts[1] = imageIDComboBox.getSelectedItem().toString();
			inputTexts[2] = computeTypesComboBox.getSelectedItem().toString();
			inputTexts[3] = numOfComputeComboBox.getSelectedItem().toString();
			Scanner intScanner = new Scanner(inputTexts[3]);
			if (!intScanner.hasNextInt() || intScanner.nextInt() <= 0
					|| "".equals(inputTexts[0]) || "".equals(inputTexts[1])
					|| "".equals(inputTexts[2])) {
				messageLabel
						.setText(message
								.replace("</html>",
										"<br>Warnning : Must Properly Fill In All Fields!!!</html>"));
				computeNameTextField.setText(inputTexts[0]);
				imageIDComboBox.setSelectedIndex(imageIDIndex);
				computeTypesComboBox.setSelectedItem(inputTexts[2]);
				numOfComputeComboBox.setSelectedItem(inputTexts[3]);
				result = JOptionPane.showConfirmDialog(mainFrame,
						createComputeInputPanel, title,
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				intScanner.close();
			} else {
				intScanner.close();
				return inputTexts;
			}
		}

		return new String[0];
	}

	public static String[] showSecurityRuleInputDialog(Component mainFrame,
			String title, String region, String groupName) {
		SecurityRuleInputJPanel securityRuleInputJPanel = new SecurityRuleInputJPanel();
		JLabel messageLabel = securityRuleInputJPanel.getMessageLabel();
		JComboBox<String> protocalComboBox = securityRuleInputJPanel
				.getProtocalComboBox();
		JTextField portRangeTextField = securityRuleInputJPanel
				.getPortRangeTextField();
		JTextField ipRangeTextField = securityRuleInputJPanel
				.getIpRangeTextField();
		String message = "<html>Input Security Rule!<br>Current Region : "
				+ region + ", Group : " + groupName + "</html>";
		messageLabel.setText(message);

		String[] inputTexts = new String[3];
		int result = JOptionPane.showConfirmDialog(mainFrame,
				securityRuleInputJPanel, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		while (result == JOptionPane.OK_OPTION) {
			inputTexts[0] = protocalComboBox.getSelectedItem().toString()
					.split(" ")[0];
			inputTexts[1] = portRangeTextField.getText();
			inputTexts[2] = ipRangeTextField.getText();
			if ((!inputTexts[0].equals("ICMP") && inputTexts[1].equals(""))
					|| inputTexts[2].equals("")) {
				messageLabel
						.setText(message
								.replace("</html>",
										"<br>Warnning : Must Properly Fill In All Fields!!!</html>"));
				protocalComboBox.setSelectedItem(inputTexts[0]);
				portRangeTextField.setText(inputTexts[1]);
				ipRangeTextField.setText(inputTexts[2]);
				result = JOptionPane.showConfirmDialog(mainFrame,
						securityRuleInputJPanel, title,
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
			} else {
				return inputTexts;
			}
		}
		return new String[0];
	}

	public static String[] showCreateComputeGroupInputDialog(JFrame mainFrame,
			String title, String region) {
		CreateComputeGroupInputJPanel createComputeGroupInputJPanel = new CreateComputeGroupInputJPanel();
		JLabel messageLabel = createComputeGroupInputJPanel.getMessageLabel();
		JTextField computeNameTextField = createComputeGroupInputJPanel
				.getComputeNameTextField();
		JCheckBox setDefaultSecurityChckbx = createComputeGroupInputJPanel
				.getSetDefaultSecurityChckbx();
		String message = "<html>Input A Group Name!<br>Selected Region : "
				+ region + "</html>";
		messageLabel.setText(message);

		String[] inputTexts = new String[2];
		int result = JOptionPane.showConfirmDialog(mainFrame,
				createComputeGroupInputJPanel, title,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		while (result == JOptionPane.OK_OPTION) {
			inputTexts[0] = computeNameTextField.getText();
			inputTexts[1] = new Boolean(setDefaultSecurityChckbx.isSelected())
					.toString();
			if ("".equals(inputTexts[0])) {
				messageLabel
						.setText(message
								.replace("</html>",
										"<br>Warnning : Must Properly Fill In All Fields!!!</html>"));
				result = JOptionPane.showConfirmDialog(mainFrame,
						createComputeGroupInputJPanel, title,
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
			} else {
				return inputTexts;
			}
		}
		return new String[0];
	}

	public static Properties showUserPropertiesInputDialog(JFrame mainFrame,
			String title, Properties defaultProperties) {
		UserPropertiesInputJPanel userPropertiesInputJPanel = new UserPropertiesInputJPanel();
		JLabel messageLabel = userPropertiesInputJPanel.getMessageLabel();
		JTextField accessKeyTextField = userPropertiesInputJPanel
				.getAccessKeyTextField();
		JTextField secretKeyTextField = userPropertiesInputJPanel
				.getSecretKeyTextField();
		String message = "<html>Input Your AWS EC2 Properties!<br>Save As : "
				+ SystemEnviroment.getUserEnvPath().replace("\\", "/")
				+ "</html>";
		messageLabel.setText(message);

		String AWS_ACCESS_KEY = "AWS_ACCESS_KEY";
		String AWS_SECRET_KEY = "AWS_SECRET_KEY";

		if (defaultProperties != null) {
			accessKeyTextField.setText(defaultProperties
					.getProperty(AWS_ACCESS_KEY));
			secretKeyTextField.setText(defaultProperties
					.getProperty(AWS_SECRET_KEY));
		}

		int result = JOptionPane.showConfirmDialog(mainFrame,
				userPropertiesInputJPanel, title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			Properties newProperties = new Properties();
			newProperties.put(AWS_ACCESS_KEY, accessKeyTextField.getText());
			newProperties.put(AWS_SECRET_KEY, secretKeyTextField.getText());
			return newProperties;
		}
		return null;
	}

	public static void showErrorDialogAndConnetWikiAndExit(Frame mainFrame,
			String message) {
		JOptionPane.showMessageDialog(mainFrame, message, mainFrame.getTitle(),
				JOptionPane.ERROR_MESSAGE);
		String wikiPage = "https://github.com/JM-Lab/JMCloud-ComputeManager/wiki#requirements";
		try {
			Desktop.getDesktop().browse(new URI(wikiPage));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

}
