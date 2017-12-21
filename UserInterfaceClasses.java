
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

class MyJButton extends JButton {
	public MyJButton(ImageIcon icon) {
		super(icon);
		setBackground(Color.white);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}

	public MyJButton(ImageIcon icon, ImageIcon rollOverIcon) {
		super(icon);
		setRolloverIcon(rollOverIcon);
		setPressedIcon(icon);
		setBackground(Color.white);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}

	public MyJButton(String icon) {
		super(new ImageIcon(icon));
		setBackground(Color.white);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}

	public MyJButton(String icon, String rollOverIcon) {
		super(new ImageIcon(icon));
		setRolloverIcon(new ImageIcon(rollOverIcon));
		setPressedIcon(new ImageIcon(icon));
		setBackground(Color.white);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}

	public MyJButton(String caption, Color foreground, Color background) {
		super(caption);
		setForeground(foreground);
		setBackground(background);
	}
}

