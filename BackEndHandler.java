
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;

public class BackEndHandler extends Thread {
	public static final short EMBEDMSG = 0;
	public static final short RETRIEVEMSG = 2;
	public static final short EDIT = 4;

	private short oprtn;
	private WindowAdapter windowAdapter;

	private JFileChooser fileSelector;
	private MyFileView myFileView;
	private File masterFile, dataFile, outputFile;

	private int result, result2;

	public BackEndHandler(WindowAdapter client, short operation) {
		this.windowAdapter = client;
		this.oprtn = operation;

		// Setup file chooser
		fileSelector = new JFileChooser("./");
		fileSelector.setFileSelectionMode(fileSelector.FILES_ONLY);
		fileSelector.setDialogType(fileSelector.CUSTOM_DIALOG);
		fileSelector.setAccessory(new FilePreview(fileSelector));
	}

	public void run() {
		if (!chooseMasterFile())
			return;

		if (oprtn == EMBEDMSG)
			if (!chooseOutputFile())
				return;

		SteganoInformation steg;
		switch (oprtn) {
		case EMBEDMSG:
			new EmbedGUI(this);
			break;

		case RETRIEVEMSG:
			steg = new SteganoInformation(masterFile);
			if (steg.isEster())
				showEster(steg);
			else if (!steg.isValid())
				JOptionPane.showMessageDialog(null,
						"File '" + masterFile.getName()
								+ "' does not contain any message or file\nembedded using Seganograph or later!",
						"Invalid Steganograph file!", JOptionPane.WARNING_MESSAGE);
			else
				new RetrieveGUI(steg, RetrieveGUI.RETRIEVEMSG);
			break;

		}
	}

	// Method for choosing input file
	public boolean chooseMasterFile() {
		int result;
		do {
			result = fileSelector.showDialog(null, "Select Master file");
			if (result == fileSelector.APPROVE_OPTION) {
				masterFile = fileSelector.getSelectedFile();
				if (!checkFileExistency(masterFile))
					continue;
				else
					break;
			}
		} while (result != fileSelector.CANCEL_OPTION);

		if (result == fileSelector.CANCEL_OPTION)
			return false;
		else
			return true;
	}

	// Method for choosing output file
	public boolean chooseOutputFile() {
		int result;
		do {
			File previousFile = fileSelector.getSelectedFile();
			result = fileSelector.showDialog(null, "Select Output file");
			if (result == fileSelector.APPROVE_OPTION) {
				outputFile = fileSelector.getSelectedFile();
				if (outputFile.exists()) {
					result2 = JOptionPane.showConfirmDialog(null,
							"File " + outputFile.getName() + " already exists!\nWould you like to OVERWRITE it?",
							"File already exists!", JOptionPane.YES_NO_OPTION);
					if (result2 == JOptionPane.NO_OPTION) {
						if (previousFile != null)
							fileSelector.setSelectedFile(previousFile);
						continue;
					}
				}
				break;
			}
		} while (result != fileSelector.CANCEL_OPTION);

		if (result == fileSelector.CANCEL_OPTION)
			return false;
		else
			return true;
	}

	// Accessor methods
	public File getMasterFile() {
		return masterFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	// Mutator methods
	public void setMasterFile(File file) {
		masterFile = file;
	}

	public void setOutputFile(File file) {
		outputFile = file;
	}

	// Checks whether given file actually exists
	private boolean checkFileExistency(File file) {
		if (!file.exists()) {
			JOptionPane.showMessageDialog(null, "File " + file.getName() + " does not exist!", "Inexistent file!",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private void showEster(SteganoInformation steg) {
		Object message[] = new Object[3];
		message[0] = new MyLabel("This is an encrypted zone.", Color.red, Color.gray);
		message[1] = new JLabel("Please enter password to continue.");
		JPasswordField pass = new JPasswordField(10);
		message[2] = pass;

		String options[] = { "Retrieve now", "Cancel" };
		int result = JOptionPane.showOptionDialog(null, message, "Encrypted zone", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

		if (result == 1)
			return;

		String password = new String(pass.getPassword());
		if (password.length() < 8)
			JOptionPane.showMessageDialog(null, "This was not the right password!", "Invalid password",
					JOptionPane.OK_OPTION);
		else {
			int fileSize = (int) steg.getFile().length();
			byte[] byteArray = new byte[fileSize];
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(steg.getFile()));
				in.read(byteArray, 0, fileSize);
				in.close();

				Cipher cipher = Cipher.getInstance("DES");
				cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(password.substring(0, 8).getBytes(), "DES"));

				byteArray = cipher.doFinal(byteArray);
			} catch (Exception e) {
				return;
			}

			JFrame frame = new JFrame("Enjoy the ester egg...");
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.getContentPane().add(new JScrollPane(new JLabel(new ImageIcon(byteArray))));
			frame.setBackground(Color.white);

			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setSize(d.width, d.height / 2);
			frame.setVisible(true);
		}
	}
}