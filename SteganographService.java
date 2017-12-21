
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SteganographService {
	public static final String VERSION = "2.0.0";
	public static final byte[] VERSION_BYTE = { '2', '0', '0' };
	public static final int OFFSET_JPG = 3;
	public static final int OFFSET_PNG = 42;
	public static final int OFFSET_GIF_BMP_TIF = 32;
	public static final short HEADER_LENGTH = 15 * 4;
	public static final byte UUM = 0;
	public static final byte UUF = 1;
	public static final byte UEM = 2;
	public static final byte UEF = 3;
	public static final byte CUM = 4;
	public static final byte CUF = 5;
	public static final byte CEM = 6;
	public static final byte CEF = 7;

	private static Cipher cipher;

	private static SecretKeySpec spec;
	private static String masterExtension, message;
	private static AboutFrame about = new AboutFrame();

	private static File masterFile;

	private static byte features;
	private static int inputFileSize;
	private static int i, j, inputOutputMarker, messageSize, tempInt;
	private static short compressionRatio = 0;
	private static byte byte1, byte2, byte3, byteArrayIn[];
	private static ByteArrayOutputStream byteOut;

	private SteganographService() {
		System.out.println(" " + " ready...");
	}

	public static String getMessage() {
		return message;
	}

	public static boolean embedMessage(File masterFile, File outputFile, String msg, int compression, String password) {
		if (msg == null) {
			message = "Message is empty";
			return false;
		}
		if (msg.length() < 1) {
			message = "Message is empty";
			return false;
		}

		if (password != null && password.length() < 8) {
			message = "Password should be minimum of 8 Characters";
			return false;
		}

		messageSize = msg.length();

		if (compression != -1) {
			if (compression < 0)
				compression = 0;
			if (compression > 9)
				compression = 9;

			if (password == null)
				features = CUM;
			else
				features = CEM;
		} else {
			if (password == null)
				features = UUM;
			else
				features = UEM;
		}
		try {
			byteOut = new ByteArrayOutputStream();
			byte[] messageArray = msg.getBytes();
			messageSize = messageArray.length;
			inputFileSize = (int) masterFile.length();

			byteArrayIn = new byte[inputFileSize];

			DataInputStream in = new DataInputStream(new FileInputStream(masterFile));
			in.read(byteArrayIn, 0, inputFileSize);
			in.close();

			String fileName = masterFile.getName();
			masterExtension = fileName.substring(fileName.length() - 3, fileName.length());

			if (masterExtension.equalsIgnoreCase("jpg")) {
				byteOut.write(byteArrayIn, 0, OFFSET_JPG);
				inputOutputMarker = OFFSET_JPG;
			} else if (masterExtension.equalsIgnoreCase("png")) {
				byteOut.write(byteArrayIn, 0, OFFSET_PNG);
				inputOutputMarker = OFFSET_PNG;
			} else {
				byteOut.write(byteArrayIn, 0, OFFSET_GIF_BMP_TIF);
				inputOutputMarker = OFFSET_GIF_BMP_TIF;
			}
			byte tempByte[] = new byte[4];
			for (i = 24, j = 0; i >= 0; i -= 8, j++) {
				tempInt = inputFileSize;
				tempInt >>= i;
				tempInt &= 0x000000FF;
				tempByte[j] = (byte) tempInt;
			}
			embedBytes(tempByte);

			byteOut.write(byteArrayIn, inputOutputMarker, inputFileSize - inputOutputMarker);
			inputOutputMarker = inputFileSize;

			// Embed the 3 byte version information into the file
			writeBytes(VERSION_BYTE);

			// Write 1 byte for features
			writeBytes(new byte[] { features });

			// Compress the message if required
			if (features == CUM || features == CEM) {
				ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
				ZipOutputStream zOut = new ZipOutputStream(arrayOutputStream);
				ZipEntry entry = new ZipEntry("MESSAGE");
				zOut.setLevel(compression);
				zOut.setMethod(entry.DEFLATED);
				zOut.putNextEntry(entry);

				zOut.write(messageArray, 0, messageSize);
				zOut.closeEntry();
				zOut.finish();
				zOut.close();

				// Get the compressed message byte array
				messageArray = arrayOutputStream.toByteArray();
				compressionRatio = (short) ((double) messageArray.length / (double) messageSize * 100.0);
				messageSize = messageArray.length;
			}

			// Embed 1 byte compression ratio into the output file
			writeBytes(new byte[] { (byte) compressionRatio });

			// Encrypt the message if required
			if (features == UEM || features == CEM) {
				Cipher cipher = Cipher.getInstance("DES");
				SecretKeySpec spec = new SecretKeySpec(password.substring(0, 8).getBytes(), "DES");
				cipher.init(Cipher.ENCRYPT_MODE, spec);
				messageArray = cipher.doFinal(messageArray);
				messageSize = messageArray.length;
			}

			// Convert the 32 bit message size into byte array
			tempByte = new byte[4];
			for (i = 24, j = 0; i >= 0; i -= 8, j++) {
				tempInt = messageSize;
				tempInt >>= i;
				tempInt &= 0x000000FF;
				tempByte[j] = (byte) tempInt;
			}
			// Embed 4 byte messageSize array into the master file
			writeBytes(tempByte);

			// Embed the message
			writeBytes(messageArray);

			DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFile));
			// out.write(byteArrayOut, 0, byteArrayOut.length);
			byteOut.writeTo(out);
			out.close();
		} catch (EOFException e) {
		} catch (Exception e) {
			message = "Oops!!\nError: " + e.toString();
			e.printStackTrace();
			return false;
		}

		message = "Message embedded successfully in file '" + outputFile.getName() + "'.";
		return true;
	}

	// Retrieves an embedded message from a Master file
	public static String retrieveMessage(SteganoInformation info, String password) {
		String messg = null;
		features = info.getFeatures();

		try {
			masterFile = info.getFile();
			byteArrayIn = new byte[(int) masterFile.length()];

			DataInputStream in = new DataInputStream(new FileInputStream(masterFile));
			in.read(byteArrayIn, 0, (int) masterFile.length());
			in.close();

			messageSize = info.getDataLength();

			if (messageSize <= 0) {
				message = "Unexpected size of message: 0.";
				return ("#FAILED#");
			}

			byte[] messageArray = new byte[messageSize];
			inputOutputMarker = info.getInputMarker();
			readBytes(messageArray);

			// Decrypt the message if required
			if (features == CEM || features == UEM) {
				password = password.substring(0, 8);
				byte passwordBytes[] = password.getBytes();
				cipher = Cipher.getInstance("DES");
				spec = new SecretKeySpec(passwordBytes, "DES");
				cipher.init(Cipher.DECRYPT_MODE, spec);
				try {
					messageArray = cipher.doFinal(messageArray);
				} catch (Exception bp) {
					message = "Incorrent Password";
					bp.printStackTrace();
					return "#FAILED#";
				}
				messageSize = messageArray.length;
			}

			// Uncompress the message if required
			if (features == CUM || features == CEM) {
				messageArray = unCompress(messageArray);
			}
			messg = new String(SteganoInformation.byteToCharArray(messageArray));
		} catch (Exception e) {
			message = "Oops!!\n Error: " + e;
			e.printStackTrace();
			return ("#FAILED#");
		}

		message = "Message size: " + messageSize + " B";
		return messg;
	}

	private static byte[] unCompress(byte[] messageArray) throws IOException {
		ByteArrayOutputStream by = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(by);

		ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(messageArray));
		zipIn.getNextEntry();
		byteArrayIn = new byte[1024];
		while ((tempInt = zipIn.read(byteArrayIn, 0, 1024)) != -1)
			out.write(byteArrayIn, 0, tempInt);

		zipIn.close();
		out.close();
		messageArray = by.toByteArray();
		messageSize = messageArray.length;
		return messageArray;
	}

	// Method used to write bytes into the output array
	private static void embedBytes(byte[] bytes) {
		int size = bytes.length;

		for (int i = 0; i < size; i++) {
			byte1 = bytes[i];
			for (int j = 6; j >= 0; j -= 2) {
				byte2 = byte1;
				byte2 >>= j;
				byte2 &= 0x03;

				byte3 = byteArrayIn[inputOutputMarker];
				byte3 &= 0xFC;
				byte3 |= byte2;
				byteOut.write(byte3);
				inputOutputMarker++;
			}
		}
	}

	// Method used to write bytes into the output array
	private static void writeBytes(byte[] bytes) {
		int size = bytes.length;

		for (int i = 0; i < size; i++) {
			byteOut.write(bytes[i]);
			inputOutputMarker++;
		}
	}

	// Method used to retrieve bytes into the output array
	private static void retrieveBytes(byte[] bytes) {
		int size = bytes.length;

		for (int i = 0; i < size; i++) {
			byte1 = 0;
			for (int j = 6; j >= 0; j -= 2) {
				byte2 = byteArrayIn[inputOutputMarker];
				inputOutputMarker++;

				byte2 &= 0x03;
				byte2 <<= j;
				byte1 |= byte2;
			}
			bytes[i] = byte1;
		}
	}

	// Method used to read bytes into the output array
	private static void readBytes(byte[] bytes) {
		int size = bytes.length;

		for (int i = 0; i < size; i++) {
			bytes[i] = byteArrayIn[inputOutputMarker];
			inputOutputMarker++;
		}
	}

	public static void showAboutDialog() {
		about.setDisplayed(true);
	}

	public static void updateUserInterface() {
		SwingUtilities.updateComponentTreeUI(about);
	}

	public static void showHelpDialog() {
		new HTMLFrame("file:" + System.getProperty("user.dir") + System.getProperty("file.separator") + "help.html",
				false);
	}

	// Inner class for description about Steganograph in a frame

	private static class AboutFrame extends JFrame implements ActionListener {
		private final Color GREEN = Color.green;
		private final Color YELLOW = Color.yellow;
		private final Color BLACK = Color.black;
		private JLabel lblTitle, lblImage, lblName, lblEmail, lblPhone;
		private JLabel filler1, filler2, filler3;
		private JButton btnVisit, btnComments, btnHelp, btnClose;

		private GridBagLayout gbl;
		private GridBagConstraints gbc;

		public AboutFrame() {
			setTitle("About Steganograph " + SteganographService.VERSION);
			filler1 = new JLabel(" ");
			filler2 = new JLabel(" ");
			filler3 = new JLabel(" ");
			lblTitle = new MyLabel("Steganography Implementation " + SteganographService.VERSION,
					new Font("Arial", Font.PLAIN, 15), YELLOW, BLACK);
			lblName = new MyLabel("By:   Mohammad Mujeeb", new Font("Monotype Corsiva", Font.PLAIN, 14), GREEN, BLACK);
			lblEmail = new MyLabel("      mohammad.mujeeb@gmail.com", GREEN, BLACK);
			lblPhone = new MyLabel("      +91 9342204029", GREEN, BLACK);
			if (new File("Images/About").exists())
				lblImage = new JLabel(new ImageIcon("Images/Myself"));
			else {
				lblImage = new MyLabel("Steganograph " + SteganographService.VERSION,
						new Font("Times new roman", Font.PLAIN, 50), Color.red, Color.black);
				lblImage.setPreferredSize(new Dimension(222, 231));
			}
			btnVisit = new MyJButton("Author's Homepage", GREEN, BLACK);
			btnComments = new MyJButton("Send Comments", GREEN, BLACK);
			btnHelp = new MyJButton("Help", GREEN, BLACK);
			btnClose = new MyJButton("Close", GREEN, BLACK);

			btnVisit.addActionListener(this);
			btnHelp.addActionListener(this);
			btnClose.addActionListener(this);
			btnComments.addActionListener(this);

			JPanel panelAbout = new JPanel();
			panelAbout.setBackground(BLACK);
			panelAbout.setForeground(GREEN);
			gbl = new GridBagLayout();
			gbc = new GridBagConstraints();
			panelAbout.setLayout(gbl);
			;

			gbc.anchor = GridBagConstraints.CENTER;
			gbc.weightx = 2;
			gbc.weighty = 2;
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbl.setConstraints(lblTitle, gbc);
			panelAbout.add(lblTitle);
			gbc.gridy = 2;
			gbl.setConstraints(filler1, gbc);
			panelAbout.add(filler1);
			gbc.gridy = 3;
			gbl.setConstraints(lblName, gbc);
			panelAbout.add(lblName);
			gbc.gridy = 4;
			gbl.setConstraints(filler2, gbc);
			panelAbout.add(filler2);
			gbc.gridy = 5;
			gbl.setConstraints(lblEmail, gbc);
			panelAbout.add(lblEmail);
			gbc.gridy = 6;
			gbl.setConstraints(filler3, gbc);
			panelAbout.add(filler3);
			gbc.gridy = 7;
			gbl.setConstraints(lblPhone, gbc);
			panelAbout.add(lblPhone);
			panelAbout = UtilityOperations.createBorderedPanel(panelAbout, "Cryptography With Audio", 3, 3);

			JPanel panelUpper = new JPanel();
			panelUpper.setBackground(BLACK);
			panelUpper.setLayout(new FlowLayout());
			panelUpper.add(lblImage);
			panelUpper.add(new MyLabel("      ", GREEN, BLACK));
			panelUpper.add(panelAbout);

			JPanel panelButtons = new JPanel();
			panelButtons.setBackground(Color.black);
			panelButtons.setLayout(new FlowLayout());
			panelButtons.setBackground(Color.black);
			panelButtons.add(btnComments);
			panelButtons.add(new JLabel("     "));
			panelButtons.add(btnHelp);
			panelButtons.add(new JLabel("     "));
			panelButtons.add(btnClose);

			JPanel mainPanel = new JPanel();
			mainPanel.setBackground(Color.black);
			gbl = new GridBagLayout();
			mainPanel.setLayout(gbl);
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbl.setConstraints(panelUpper, gbc);
			mainPanel.add(panelUpper);
			gbc.gridy = 2;
			gbl.setConstraints(panelButtons, gbc);
			mainPanel.add(panelButtons);
			mainPanel = UtilityOperations.createBorderedPanel(mainPanel, 3, 2);
			setContentPane(mainPanel);

			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) (d.width * 2.1 / 3);
			int height = (int) (d.height * 1.9) / 3;
			setSize(width, height);
			setLocation((d.width - width) / 2, (d.height - height) / 2);
			setResizable(false);
		}

		public void setDisplayed(boolean choice) {
			setVisible(choice);
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnVisit)
				new HTMLFrame("http://mujeeb.addr.com", true);

			if (e.getSource() == btnHelp)
				new HTMLFrame(
						"file:" + System.getProperty("user.dir") + System.getProperty("file.separator") + "help.html",
						false);

			if (e.getSource() == btnComments)
				new HTMLFrame("http://mujeeb.addr.com/comments.html", true);

			if (e.getSource() == btnClose)
				setVisible(false);
		}
	} // End of Class AboutFrame
} // End of Class Steganograph
