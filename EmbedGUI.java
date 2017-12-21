
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EmbedGUI extends JFrame implements ActionListener, ItemListener {
	BackEndHandler client;

	private JLabel labelMaster, labelOutput, labelMasterSize, labelOutputSize, labelMessage;;
	private JLabel labelMasterFileSize, labelOutputFileSize, labelStatus, labelFiller[];
	private JLabel labelCompression, labelPassword;
	private JCheckBox checkCompress, checkEncrypt;
	private JSlider compressionS;
	private JPasswordField txtPassword;
	private JTextField textMasterFile, textOutputFile;
	private JTextArea textMessage;
	private JScrollPane scrollPane;
	private JButton buttonGo, buttonHelp, buttonCancel, buttonChangeMasterFile, buttonChangeOutputFile;

	public EmbedGUI(BackEndHandler client) {
		super("Embedding message ");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.client = client;
		Font arialFont = new Font("Arial", Font.PLAIN, 11);

		// Create filler labels
		labelFiller = new JLabel[11];
		for (int i = 0; i < 11; i++)
			labelFiller[i] = new JLabel(" ");

		labelMaster = new MyLabel("Master file", arialFont, Color.black, Color.lightGray);
		labelOutput = new MyLabel("Output file", arialFont, Color.black, Color.lightGray);
		labelMasterSize = new MyLabel("Size: ", arialFont, Color.black, Color.lightGray);
		labelOutputSize = new MyLabel("Size: ", arialFont, Color.black, Color.lightGray);
		textMasterFile = new MyJTextField(client.getMasterFile().getName(), 13, Color.blue, Color.lightGray);
		textMasterFile.setEditable(false);
		labelMasterFileSize = new MyLabel("" + client.getMasterFile().length() / 1024 + " Kb", arialFont, Color.red,
				Color.gray);

		textOutputFile = new MyJTextField(client.getOutputFile().getName(), 13, Color.blue, Color.lightGray);
		textOutputFile.setEditable(false);
		labelOutputFileSize = new MyLabel(labelMasterFileSize.getText(), arialFont, Color.red, Color.gray);
		labelMessage = new MyLabel("Message:", arialFont, Color.black, Color.lightGray);
		labelStatus = new MyLabel("Minimum required size of the Master file: 32Kb", arialFont, Color.black,
				Color.lightGray);
		// txtMessage= new JTextArea(12, 64);
		textMessage = new JTextArea(12, 50);
		scrollPane = new JScrollPane(textMessage);

		buttonChangeMasterFile = new JButton("Change");
		buttonChangeOutputFile = new JButton("Change");
		buttonGo = new JButton("   Go   ");
		buttonHelp = new JButton("  Help  ");
		buttonCancel = new JButton(" Close ");

		// Setup panelFiles
		JPanel panelFiles = new JPanel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		panelFiles.setLayout(gbl);

		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(labelMaster, gbc);
		panelFiles.add(labelMaster);
		gbc.gridx = 2;
		gbl.setConstraints(textMasterFile, gbc);
		panelFiles.add(textMasterFile);
		gbc.gridx = 3;
		gbl.setConstraints(labelMasterSize, gbc);
		panelFiles.add(labelMasterSize);
		gbc.gridx = 4;
		gbl.setConstraints(labelMasterFileSize, gbc);
		panelFiles.add(labelMasterFileSize);
		gbc.gridx = 5;
		gbl.setConstraints(labelFiller[0], gbc);
		panelFiles.add(labelFiller[0]);
		gbc.gridx = 6;
		gbl.setConstraints(labelOutput, gbc);
		panelFiles.add(labelOutput);
		gbc.gridx = 7;
		gbl.setConstraints(textOutputFile, gbc);
		panelFiles.add(textOutputFile);
		gbc.gridx = 8;
		gbl.setConstraints(labelOutputSize, gbc);
		panelFiles.add(labelOutputSize);
		gbc.gridx = 9;
		gbl.setConstraints(labelOutputFileSize, gbc);
		panelFiles.add(labelOutputFileSize);

		gbc.gridx = 2;
		gbc.gridy = 2;
		gbl.setConstraints(buttonChangeMasterFile, gbc);
		panelFiles.add(buttonChangeMasterFile);
		gbc.gridx = 7;
		gbl.setConstraints(buttonChangeOutputFile, gbc);
		panelFiles.add(buttonChangeOutputFile);
		panelFiles = UtilityOperations.createBorderedPanel(panelFiles, "Files", 19, 3);

		// Setup panelFeatures
		labelCompression = new JLabel("Compression level");
		labelPassword = new JLabel("Password (Minimum 8 chars)");
		checkCompress = new JCheckBox("Compress");
		checkEncrypt = new JCheckBox("Encrypt");
		compressionS = new JSlider(0, 9, 5);
		compressionS.setPaintTicks(true);
		compressionS.setPaintLabels(true);
		compressionS.setSnapToTicks(true);
		compressionS.setMajorTickSpacing(1);
		compressionS.setForeground(Color.blue);
		Hashtable h = new Hashtable();
		h.put(new Integer(0), new JLabel("0", JLabel.CENTER));
		h.put(new Integer(5), new JLabel("5", JLabel.CENTER));
		h.put(new Integer(9), new JLabel("9", JLabel.CENTER));
		compressionS.setLabelTable(h);

		txtPassword = new JPasswordField(9);
		checkCompress.addItemListener(this);
		checkEncrypt.addItemListener(this);
		labelCompression.setEnabled(false);
		compressionS.setEnabled(false);
		labelPassword.setEnabled(false);
		txtPassword.setEnabled(false);

		JPanel panelCompression1 = new JPanel();
		new BoxLayout(panelCompression1, BoxLayout.X_AXIS);
		panelCompression1.add(checkCompress);
		panelCompression1.add(labelCompression);

		JPanel panelCompression2 = new JPanel();
		new BoxLayout(panelCompression2, BoxLayout.X_AXIS);
		panelCompression2.add(new MyLabel("Low", arialFont, Color.blue, Color.lightGray));
		panelCompression2.add(compressionS);
		panelCompression2.add(new MyLabel("High", arialFont, Color.blue, Color.lightGray));

		JPanel panelCompression = new JPanel();
		gbl = new GridBagLayout();
		panelCompression.setLayout(gbl);
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(panelCompression1, gbc);
		panelCompression.add(panelCompression1);
		gbc.gridy = 2;
		gbl.setConstraints(panelCompression2, gbc);
		panelCompression.add(panelCompression2);
		panelCompression = UtilityOperations.createBorderedPanel(panelCompression, "Compression", 3, 3);

		JPanel panelEncryption = new JPanel();
		new BoxLayout(panelEncryption, BoxLayout.X_AXIS);
		panelEncryption.add(checkEncrypt);
		panelEncryption.add(labelPassword);
		panelEncryption.add(txtPassword);
		panelEncryption = UtilityOperations.createBorderedPanel(panelEncryption, "Encryption", 3, 15);

		JPanel panelFeatures = new JPanel();
		new BoxLayout(panelFeatures, BoxLayout.X_AXIS);
		//panelFeatures.add(panelCompression);
		panelFeatures.add(panelEncryption);

		// Setup panelText
		JPanel panelText = new JPanel();
		gbl = new GridBagLayout();
		panelText.setLayout(gbl);
		gbc.gridy = 2;
		gbc.anchor = gbc.WEST;
		gbl.setConstraints(labelMessage, gbc);
		panelText.add(labelMessage);
		gbc.gridy = 3;
		gbc.anchor = gbc.CENTER;
		gbl.setConstraints(scrollPane, gbc);
		panelText.add(scrollPane);
		gbc.gridy = 4;
		gbl.setConstraints(labelStatus, gbc);
		panelText.add(labelStatus);

		// Setup panelButtons
		JPanel panelButtons = new JPanel();
		gbl = new GridBagLayout();
		panelButtons.setLayout(gbl);
		gbc.anchor = gbc.CENTER;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbl.setConstraints(buttonGo, gbc);
		panelButtons.add(buttonGo);
		gbc.gridy = 2;
		gbl.setConstraints(labelFiller[3], gbc);
		panelButtons.add(labelFiller[3]);
		gbc.gridy = 3;
		gbl.setConstraints(buttonHelp, gbc);
		// panelButtons.add(btnHelp);
		gbc.gridy = 4;
		gbl.setConstraints(labelFiller[4], gbc);
		panelButtons.add(labelFiller[4]);
		gbc.gridy = 5;
		gbl.setConstraints(buttonCancel, gbc);
		panelButtons.add(buttonCancel);

		JPanel panelLower = new JPanel();
		new BoxLayout(panelLower, BoxLayout.X_AXIS);
		panelLower.add(panelText);
		panelLower.add(panelButtons);

		JPanel mainPanel = new JPanel();
		new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.add(panelFiles);
		mainPanel.add(labelFiller[10]);
		mainPanel.add(panelFeatures);
		mainPanel.add(panelLower);

		mainPanel = UtilityOperations.createBorderedPanel(mainPanel, 3, 3);
		setContentPane(mainPanel);

		buttonChangeMasterFile.addActionListener(this);
		buttonChangeOutputFile.addActionListener(this);
		buttonHelp.addActionListener(this);
		buttonGo.addActionListener(this);
		buttonCancel.addActionListener(this);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (0.91 * d.width);
		int height = (int) (0.935 * d.height);
		setSize(width, height);
		setLocation((d.width - width) / 2, (d.height - height) / 2);
		// setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == buttonChangeMasterFile) {
			client.chooseMasterFile();
			textMasterFile.setText(client.getMasterFile().getName());
			labelMasterFileSize.setText("" + client.getMasterFile().length() / 1024 + "Kb");
			labelOutputFileSize.setText(labelMasterFileSize.getText());
		}

		if (source == buttonChangeOutputFile) {
			client.chooseOutputFile();
			textOutputFile.setText(client.getOutputFile().getName());
		}

		if (source == buttonHelp)
			SteganographService.showHelpDialog();

		if (source == buttonCancel)
			dispose();

		if (source == buttonGo) {
			int compression = -1;
			String password = null;

			if (textMessage.getText().length() < 1) {
				JOptionPane.showMessageDialog(this,
						"Please enter the message\nYou can also paste the message on clipboard using\nCtrl+V combination.",
						"Empty message!", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (checkCompress.isSelected())
				compression = compressionS.getValue();

			if (checkEncrypt.isSelected()) {
				password = new String(txtPassword.getPassword());
				if (password.length() < 8) {
					JOptionPane.showMessageDialog(this, "Password needs to be a minimum of 8 Characters!",
							"Invalid password!", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			if (client.getOutputFile().exists()) {
				int result2 = JOptionPane.showConfirmDialog(null,
						"File " + client.getOutputFile().getName()
								+ " already exists!\nWould you like to OVERWRITE it?",
						"File already exists!", JOptionPane.YES_NO_OPTION);
				if (!(result2 == JOptionPane.YES_OPTION))
					if (!client.chooseOutputFile())
						return;
			}

			if (SteganographService.embedMessage(client.getMasterFile(), client.getOutputFile(), textMessage.getText(),
					compression, password))
				JOptionPane.showMessageDialog(this, SteganographService.getMessage(), "Operation Successful",
						JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(this, SteganographService.getMessage(), "Operation Unsuccessful",
						JOptionPane.ERROR_MESSAGE);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == checkCompress) {
			if (checkCompress.isSelected()) {
				labelCompression.setEnabled(true);
				compressionS.setEnabled(true);
			} else {
				labelCompression.setEnabled(false);
				compressionS.setEnabled(false);
			}
		} else {
			if (checkEncrypt.isSelected()) {
				labelPassword.setEnabled(true);
				txtPassword.setEnabled(true);
			} else {
				labelPassword.setEnabled(false);
				txtPassword.setEnabled(false);
			}
		}
	}
}