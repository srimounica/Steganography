import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class RetrieveGUI extends JFrame implements ActionListener
{
	public static boolean RETRIEVEMSG= true;
	public static boolean RETRIEVEFILE= false;
	private JPanel panel;
	private JLabel lblMaster, lblMasterFile, lblVersion, lblVersionUsed;
	private JLabel lblContains, lblContainsWhat, lblCompressed, lblCompressedStatus;
	private JLabel lblCompression, lblCompressionRatio;
	private JLabel lblEncrypted, lblEncryptedStatus, lblRequested, lblRequestedOperation;
	private JLabel lblFiller[];
	private JButton buttonGo, buttonCancel;

	private boolean operation;
	private SteganoInformation steganoInformation;

	public RetrieveGUI(SteganoInformation info, boolean operation)
	{
		super("Master file information");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.operation= operation;
		this.steganoInformation= info;
		Font comicFont= new Font("Comic sans MS", Font.PLAIN, 11);
		Font arialFont= new Font("Arial", Font.PLAIN, 11);

		lblMaster= new MyLabel("Master file", arialFont, Color.black, Color.gray);
		lblMasterFile= new MyLabel(info.getFile().getName(), comicFont, Color.blue, Color.gray);
		lblVersion= new MyLabel("Steganograph Used", arialFont, Color.black, Color.gray);
		lblVersionUsed= new MyLabel(info.getVersion(), comicFont, Color.blue, Color.gray);
		lblContains= new MyLabel("Contains", arialFont, Color.black, Color.gray);

		byte features= info.getFeatures();
		if(features == SteganographService.UUM || features== SteganographService.UEM
				|| features == SteganographService.CUM || features== SteganographService.CEM)
			lblContainsWhat= new MyLabel("Embedded message", comicFont, Color.blue, Color.gray);
		else
			lblContainsWhat= new MyLabel("Embedded file", comicFont, Color.blue, Color.gray);

		lblCompressed= new MyLabel("Compressed", arialFont, Color.black, Color.gray);
		lblCompression= new MyLabel("Compression ratio", arialFont, Color.black, Color.gray);
		lblCompressionRatio= new MyLabel(""+ info.getCompressionRatio()+ " %", comicFont, Color.blue, Color.gray);
		if(features == SteganographService.CUM || features== SteganographService.CUF
				|| features == SteganographService.CEM || features== SteganographService.CEF)
			lblCompressedStatus= new MyLabel("YES", comicFont, Color.blue, Color.gray);
		else
		{
			lblCompressedStatus= new MyLabel("NO", comicFont, Color.blue, Color.gray);
			lblCompression.setEnabled(false);
			lblCompressionRatio.setEnabled(false);
		}
		
		lblEncrypted= new MyLabel("Encrypted", arialFont, Color.black, Color.gray);
		if(features== SteganographService.UEM || features== SteganographService.CEM
				|| features== SteganographService.UEF || features== SteganographService.CEF)
			lblEncryptedStatus= new MyLabel("YES", comicFont, Color.blue, Color.gray);
		else
			lblEncryptedStatus= new MyLabel("NO", comicFont, Color.blue, Color.gray);

		lblRequested= new MyLabel("Requested operation", arialFont, Color.black, Color.gray);

		if(operation== RETRIEVEMSG)
			lblRequestedOperation= new MyLabel("Retrieve message", comicFont, Color.blue, Color.gray);
		else
			lblRequestedOperation= new MyLabel("Retrieve File", comicFont, Color.blue, Color.gray);

		lblFiller= new JLabel[2];
		for(int i=0; i<2; i++)	lblFiller[i]= new JLabel("       ");
		buttonGo= new JButton("Go");
		buttonCancel= new JButton("Cancel");
		buttonGo.addActionListener(this);
		buttonCancel.addActionListener(this);

		JPanel panelUpper= new JPanel();
		GridBagLayout gbl= new GridBagLayout();
		GridBagConstraints gbc= new GridBagConstraints();
		panelUpper.setLayout(gbl);

		gbc.anchor= gbc.WEST;
		gbc.gridx= 1; gbc.gridy= 1;	gbl.setConstraints(lblMaster, gbc);
		panelUpper.add(lblMaster);
		gbc.gridx= 3; gbl.setConstraints(lblMasterFile, gbc);
		panelUpper.add(lblMasterFile);
		gbc.gridx= 1; gbc.gridy= 2;	gbl.setConstraints(lblVersion, gbc);
		panelUpper.add(lblVersion);
		gbc.gridx= 3; gbl.setConstraints(lblVersionUsed, gbc);
		panelUpper.add(lblVersionUsed);
		gbc.gridx= 1; gbc.gridy= 3;	gbl.setConstraints(lblContains, gbc);
		panelUpper.add(lblContains);
		gbc.gridx= 3; gbl.setConstraints(lblContainsWhat, gbc);
		panelUpper.add(lblContainsWhat);
		gbc.gridx= 1; gbc.gridy= 4;	gbl.setConstraints(lblCompressed, gbc);
		//panelUpper.add(lblCompressed);
		gbc.gridx= 3; gbl.setConstraints(lblCompressedStatus, gbc);
		//panelUpper.add(lblCompressedStatus);
		gbc.gridx= 1; gbc.gridy= 5;	gbl.setConstraints(lblCompression, gbc);
		//panelUpper.add(lblCompression);
		gbc.gridx= 3; gbl.setConstraints(lblCompressionRatio, gbc);
		//panelUpper.add(lblCompressionRatio);
		gbc.gridx= 1; gbc.gridy= 6;	gbl.setConstraints(lblEncrypted, gbc);
		panelUpper.add(lblEncrypted);
		gbc.gridx= 3; gbl.setConstraints(lblEncryptedStatus, gbc);
		panelUpper.add(lblEncryptedStatus);

		gbc.gridx= 2; gbc.gridy= 7;	gbl.setConstraints(lblFiller[0], gbc);
		panelUpper.add(lblFiller[0]);
		gbc.gridx= 1; gbc.gridy= 8;	gbl.setConstraints(lblRequested, gbc);
		panelUpper.add(lblRequested);
		gbc.gridx= 3; gbl.setConstraints(lblRequestedOperation, gbc);
		panelUpper.add(lblRequestedOperation);

		JPanel panelLower= new JPanel();
		new BoxLayout(panelLower, BoxLayout.X_AXIS);
		panelLower.add(buttonGo);
		panelLower.add(new JLabel("     "));
		panelLower.add(buttonCancel);

		panelUpper= UtilityOperations.createBorderedPanel(panelUpper, "File information", 7, 7);

		JPanel mainPanel= (JPanel) getContentPane();
		gbl= new GridBagLayout();
		mainPanel.setLayout(gbl);
		gbc.anchor= gbc.CENTER;
		gbc.gridx= 1; gbc.gridy= 1; gbl.setConstraints(panelUpper, gbc);
		mainPanel.add(panelUpper);
		gbc.gridy= 2; gbl.setConstraints(lblFiller[1], gbc);
		mainPanel.add(lblFiller[1]);
		gbc.gridy= 3; gbl.setConstraints(panelLower, gbc);
		mainPanel.add(panelLower);

		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		int width= (int)(0.4* d.width);
		int height= (int)(0.5* d.height);
		setSize(width, height);
		setLocation((d.width- width)/2, (d.height- height)/2);
		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()== buttonCancel)
			dispose();
		else
		{
			int result;
			String password= null;

			if(lblEncryptedStatus.getText().equals("YES"))
			{
				Object message[]= new Object[3];
				message[0]= new MyLabel("This is an encrypted zone.", Color.red, Color.gray);
				message[1]= new JLabel("Please enter password to continue.");
				JPasswordField pass= new JPasswordField(10);
				message[2]= pass;

				String options[]= {"Retrieve now", "Cancel"};

				do
				{
					result= JOptionPane.showOptionDialog(null, message, "Encrypted zone"
						, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

					if(result== 1)
						return;
					password= new String(pass.getPassword());
					if(password.length()<8)
						JOptionPane.showMessageDialog(null, "Length of password should be atleast 8 Characters", "Invalid password", JOptionPane.OK_OPTION);
				}while(password.length()<8);				
			}

			if(operation== RETRIEVEMSG	&&
					lblContainsWhat.getText().equals("Embedded file"))
			{
				result= JOptionPane.showConfirmDialog(null, "This file contains an embedded file\nWhereas you have requested to retrieve a message.\n\nWould you like to retrieve the file instead?", "Incorrect request!", JOptionPane.YES_NO_OPTION);
				if(result== JOptionPane.NO_OPTION)
					dispose();
			}

			if(operation== RETRIEVEFILE &&
					lblContainsWhat.getText().equals("Embedded message"))
			{
				result= JOptionPane.showConfirmDialog(null, "This file contains an embedded message\nWhereas you have requested to retrieve a file.\n\nWould you like to retrieve the message instead?", "Incorrect request!", JOptionPane.YES_NO_OPTION);
				if(result== JOptionPane.NO_OPTION)
				{
					dispose();
					return;
				}
			}

			if(lblContainsWhat.getText().equals("Embedded message"))
			{
				String message= SteganographService.retrieveMessage(steganoInformation, password);
				if(message!=null && !message.equals("#FAILED#"))
					new MessageDisplay(message, steganoInformation.getFile().getName());
				else
				{
					message= SteganographService.getMessage();
					if(message!= null && message.equals("Incorrent Password"))
						JOptionPane.showMessageDialog(null, "Good Morning!", "HAI", JOptionPane.WARNING_MESSAGE);
					else 
						JOptionPane.showMessageDialog(null, "Error!\n"+ SteganographService.getMessage(), "Oops, Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
			{
					showFile(steganoInformation);
			}
		}
	}

	private void showFile(SteganoInformation info)
	{
		int result= JOptionPane.showConfirmDialog(null, "The data file '"+ info.getDataFile().getName()+ "' has been successfully retrieved as\n"+ info.getDataFile().getPath()+ "\n\nWould you like to open it now?", "Operation successful", JOptionPane.YES_NO_OPTION);
		if(result== JOptionPane.YES_OPTION)
		{
			String osName= System.getProperty("os.name");
			if(osName.length()>=7)
				osName= osName.substring(0, 7);
			if(osName.compareToIgnoreCase("windows")== 0)
				try
				{
					Runtime.getRuntime().exec("explorer "+ info.getDataFile().getPath());
				}
				catch(Exception ex)
				{
					JOptionPane.showMessageDialog(null, "Oops!!  Error!\n"+ ex, "Error!", JOptionPane.WARNING_MESSAGE);
				}
			else
				JOptionPane.showMessageDialog(null, "Sorry!\nI just discovered that you are not running a Windows operating system.\nI will not be able to open this file for you at this time.", "Non windows OS!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
