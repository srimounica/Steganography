import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class MessageDisplay extends JFrame
{
	public MessageDisplay(String message, String fileName)
	{
		super("Retrieved message from file '"+ fileName+ "' - Steganography with Audio, Video & Image");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		getContentPane().add(new JScrollPane(new JTextArea(message, 14, 50)));

		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		int width= (int)(0.75* d.width);
		int height= (int)(0.75* d.height);
		setSize(width, height);
		setLocation((d.width- width)/2, (d.height- height)/2);
		setVisible(true);
	}
}