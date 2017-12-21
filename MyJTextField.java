import java.awt.Color;

import javax.swing.JTextField;

class MyJTextField extends JTextField {
	public MyJTextField(String caption, int size, Color foreground, Color background) {
		super(caption, size);
		setForeground(foreground);
		setBackground(background);
	}
}