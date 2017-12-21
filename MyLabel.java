import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

class MyLabel extends JLabel {
	public MyLabel(String caption, Color foreground, Color background) {
		super(caption);
		setForeground(foreground);
		setBackground(background);
	}

	public MyLabel(String caption, Font font, Color foreground, Color background) {
		super(caption);
		setForeground(foreground);
		setBackground(background);
		setFont(font);
	}
}