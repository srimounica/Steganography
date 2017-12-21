
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainClient extends WindowAdapter implements ActionListener {
	private JFrame mFrame;
	private JMenuBar menuBar;
	private JMenu menuFile, menuEdit, menuView, menuHelp, menuLook;
	private JMenuItem mnuExit, mnuEmbedMessage, mnuHelp, menuAbout;
	private JMenuItem mnuRetrieveMessage, mnuModifyMaster;
	private JRadioButtonMenuItem mnuTonicFeel, mnuMetalFeel, menuFeel, mnuWindowsFeel;
	private ButtonGroup lookAndFeelButtonGroup;

	private JPanel mainPanel, panelAbout, panelButtons;
	// private JLabel lblLogo;
	private JLabel lblFiller[];
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private MyJButton btnEmbedFile, btnRetrieveFile, btnEmbedMessage, btnRetrieveMessage;
	private MyJButton btnHelp, btnAbout;
	private BackEndHandler back;

	private MainClient() {
		mFrame = new JFrame("Steganography With Audio, Video and Image");
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.addWindowListener(this);
		mFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// Setup the menu bar
		mnuExit = new MenuItem("Exit", 1, 'x');
		mnuEmbedMessage = new MenuItem("Embed Message", 6, 'm');
		// mnuEmbedFile= new MyJMenuItem("Embed File", 7, 'i');
		mnuRetrieveMessage = new MenuItem("Retrieve Message", 0, 'r');
		// mnuRetrieveFile= new MyJMenuItem("Retrieve File", 2, 't');
		mnuModifyMaster = new MenuItem("Modify Master file settings", 2, 'd');
		mnuHelp = new MenuItem("Help", 0, 'h');
		menuAbout = new MenuItem("About", 0, 'a');
		mnuTonicFeel = new RadioButtonItem("Plastic XP", 8, 'x');
		mnuMetalFeel = new RadioButtonItem("Metal", 0, 'm');
		menuFeel = new RadioButtonItem("Motif", 2, 't');
		mnuWindowsFeel = new RadioButtonItem("Windows", 0, 'w');

		// Add item listener for Look and feel menu items
		RadioListener radioListener = new RadioListener();
		mnuTonicFeel.addItemListener(radioListener);
		mnuMetalFeel.addItemListener(radioListener);
		menuFeel.addItemListener(radioListener);
		mnuWindowsFeel.addItemListener(radioListener);
		mnuTonicFeel.setSelected(true);

		setLookandFeel();

		// Add action listeners for other menu items
		mnuEmbedMessage.addActionListener(this);
		// mnuEmbedFile.addActionListener(this);
		mnuRetrieveMessage.addActionListener(this);
		// mnuRetrieveFile.addActionListener(this);
		mnuModifyMaster.addActionListener(this);
		mnuExit.addActionListener(this);
		mnuHelp.addActionListener(this);
		menuAbout.addActionListener(this);

		menuFile = new MyMenu("File", 0, 'f');
		menuFile.add(mnuEmbedMessage);
		// menuFile.add(mnuEmbedFile);
		menuFile.add(mnuRetrieveMessage);
		// menuFile.add(mnuRetrieveFile);
		menuFile.add(mnuExit);

		menuEdit = new JMenu("Edit");
		menuEdit.add(mnuModifyMaster);

		setMenuLook();

		menuHelp = new MyMenu("Help", 0, 'h');
		menuHelp.add(mnuHelp);
		// menuHelp.add(mnuAbout);

		menuBar = new JMenuBar();
		menuBar.add(menuFile);
		// menuBar.add(menuEdit);
		// menuBar.add(menuView);
		menuBar.add(menuHelp);
		mFrame.setJMenuBar(menuBar);

		mainPanel = new JPanel();
		panelAbout = new JPanel();
		panelButtons = new JPanel();

		// Create filler labels
		lblFiller = new JLabel[4];
		for (int i = 0; i < 4; i++)
			lblFiller[i] = new JLabel(" ");

		// Prepare About panel
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		panelAbout.setLayout(gbl);
		panelAbout.setBackground(Color.white);
		Color myColor = new Color(50, 153, 237);
		Font arialFont = new Font("Arial", Font.PLAIN, 14);
		Font myFont = new Font("Monotype Corsiva", Font.PLAIN, 18);

		// Prepare the Buttons panel
		panelButtons.setBackground(Color.white);
		gbl = new GridBagLayout();
		panelButtons.setLayout(gbl);
		// panelButtons.setBorder(new TitledBorder("Supported operations"));

		// lblLogo= new JLabel(new
		// lblLogo = new JLabel(new ImageIcon("./Images/Logo.bmp"));
		btnEmbedMessage = new MyJButton("./Images/EmbedMessage.bmp", "./Images/EmbedMessageHover.bmp");
		btnEmbedFile = new MyJButton("./Images/EmbedFile.bmp", "./Images/EmbedFileHover.bmp");
		btnRetrieveMessage = new MyJButton("./Images/RetrieveMessage.bmp", "./Images/RetrieveMessageHover.bmp");
		btnRetrieveFile = new MyJButton("./Images/RetrieveFile.bmp", "./Images/RetrieveFileHover.bmp");
		btnHelp = new MyJButton("./Images/Help.bmp", "./Images/HelpHover.bmp");
		btnAbout = new MyJButton("./Images/About.bmp", "./Images/AboutHover.bmp");

		// Add action listeners for the buttons
		btnEmbedMessage.addActionListener(this);
		btnEmbedFile.addActionListener(this);
		btnRetrieveMessage.addActionListener(this);
		btnRetrieveFile.addActionListener(this);
		btnHelp.addActionListener(this);
		btnAbout.addActionListener(this);

		// Add filler for rows 1 and 2
		gbc.weightx = 4;
		gbc.weighty = 2;
		gbc.fill = gbc.BOTH;
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbl.setConstraints(lblFiller[0], gbc);
		panelButtons.add(lblFiller[0]);

		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = gbc.NONE;
		gbc.gridx = 3;
		gbc.gridy = 3;
		gbl.setConstraints(btnHelp, gbc);
		// panelButtons.add(btnHelp);

		gbc.gridx = 5;
		gbl.setConstraints(btnAbout, gbc);
		// panelButtons.add(btnAbout);

		// Add filler for rows 4 and 5
		gbc.fill = gbc.BOTH;
		gbc.gridx = 1;
		gbc.weighty = 2;
		gbc.gridy = 4;
		gbl.setConstraints(lblFiller[1], gbc);
		panelButtons.add(lblFiller[1]);

		gbc.fill = gbc.NONE;
		gbc.gridx = 2;
		gbc.weighty = 1;
		gbc.gridy = 6;
		gbl.setConstraints(btnEmbedMessage, gbc);
		panelButtons.add(btnEmbedMessage);

		gbc.gridx = 4;
		gbl.setConstraints(btnRetrieveMessage, gbc);
		panelButtons.add(btnRetrieveMessage);

		// Add filler for row 7 and 8
		gbc.fill = gbc.BOTH;
		gbc.gridx = 6;
		gbc.weighty = 2;
		gbc.gridy = 7;
		gbl.setConstraints(lblFiller[2], gbc);
		panelButtons.add(lblFiller[2]);

		gbc.fill = gbc.NONE;
		gbc.gridx = 3;
		gbc.weighty = 1;
		gbc.gridy = 9;
		gbl.setConstraints(btnEmbedFile, gbc);
		panelButtons.add(btnEmbedFile);

		gbc.gridx = 5;
		gbl.setConstraints(btnRetrieveFile, gbc);
		panelButtons.add(btnRetrieveFile);

		// Add the lblLogo and two Panels to the mainPanel
		// gbl = new GridBagLayout();
		// mainPanel.setLayout(gbl);

		// JLabel label = new JLabel(new ImageIcon("Tech Innova Logo
		// New.jpeg"));
		// Box b1 = new Box(BoxLayout.X_AXIS);
		// b1.add(Box.createHorizontalStrut(300));
		// b1.add(Box.createVerticalStrut(700));
		// b1.add(label);
		// mainPanel.add(b1);

		mainPanel.setBackground(Color.gray);

		gbc.anchor = gbc.CENTER;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weighty = 2;
		gbc.fill = gbc.VERTICAL;
		// gbl.setConstraints(lblLogo, gbc);
		// mainPanel.add(lblLogo);

		gbc.gridy = 3;
		gbc.weighty = 2;
		gbl.setConstraints(panelAbout, gbc);
		mainPanel.add(panelAbout);

		gbc.gridy = 5;
		gbc.weighty = 1;
		gbl.setConstraints(panelButtons, gbc);
		mainPanel.add(panelButtons);

		gbc.gridy = 6;
		gbc.weighty = 2;
		gbl.setConstraints(lblFiller[3], gbc);
		mainPanel.add(lblFiller[3]);

		JPanel tempPanel = (JPanel) mFrame.getContentPane();
		tempPanel.add(mainPanel, BorderLayout.CENTER);
		tempPanel.add(new MyLabel(" ", Color.black, Color.darkGray), BorderLayout.SOUTH);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		mFrame.setSize(d.width, (int) (d.height - (d.height * .03)));
		mFrame.setResizable(false);
		mFrame.setVisible(true);
	}

	private void setMenuLook() {
		menuLook = new MyMenu("Look and Feel...", 0, 'l');
		menuLook.add(mnuTonicFeel);
		menuLook.add(mnuMetalFeel);
		menuLook.add(menuFeel);
		menuLook.add(mnuWindowsFeel);
		menuView = new MyMenu("View", 0, 'v');
		menuView.add(menuLook);
	}

	private void setLookandFeel() {
		lookAndFeelButtonGroup = new ButtonGroup();
		lookAndFeelButtonGroup.add(mnuTonicFeel);
		lookAndFeelButtonGroup.add(mnuMetalFeel);
		lookAndFeelButtonGroup.add(menuFeel);
		lookAndFeelButtonGroup.add(mnuWindowsFeel);
	}

	// Listener methods
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// Embed message operation
		if (source == mnuEmbedMessage || source == btnEmbedMessage) {
			back = new BackEndHandler(this, BackEndHandler.EMBEDMSG);
			back.start();
		}

		// Retrieve message operation
		if (source == mnuRetrieveMessage || source == btnRetrieveMessage) {
			back = new BackEndHandler(this, BackEndHandler.RETRIEVEMSG);
			back.start();
		}

		// Embed file operation

		// Modify Master file operation
		if (source == mnuModifyMaster) {
			back = new BackEndHandler(this, BackEndHandler.EDIT);
			back.start();
		}

		if (source == mnuHelp || source == btnHelp)
			SteganographService.showHelpDialog();

		if (source == menuAbout || source == btnAbout)
			SteganographService.showAboutDialog();

		if (source == mnuExit) {
			int result = JOptionPane.showConfirmDialog(mFrame, "Are you sure that you want to close .", "Confirm Exit",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		}
	}

	// Class for lissoning to Look and feel radio menu events
	private class RadioListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			try {
				if (item == mnuTonicFeel && mnuTonicFeel.isSelected())
					UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");

				if (item == mnuMetalFeel && mnuMetalFeel.isSelected())
					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

				if (item == menuFeel && menuFeel.isSelected())
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");

				if (item == mnuWindowsFeel && mnuWindowsFeel.isSelected())
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

				SwingUtilities.updateComponentTreeUI(mFrame);
				SteganographService.updateUserInterface();
			} catch (Exception ex) {
			}
		}
	}

	// Main method
	public static void main(String args[]) {
		new MainClient();
	}
}