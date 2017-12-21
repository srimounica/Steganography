import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

class FilePreview extends JComponent implements PropertyChangeListener {
	ImageIcon thumbnail = null;

	public FilePreview(JFileChooser fc) {
		setPreferredSize(new Dimension(100, 50));
		fc.addPropertyChangeListener(this);
	}
	public void loadImage(File f) {
		if (f == null) {
			thumbnail = null;
		} else {
			ImageIcon tmpIcon = new ImageIcon(f.getPath());
			if (tmpIcon.getIconWidth() > 90) {
				thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
			} else {
				thumbnail = tmpIcon;
			}
		}
	}
	public void propertyChange(PropertyChangeEvent e)

	{
		String prop = e.getPropertyName();
		if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))

		{
			if (isShowing()) {
				loadImage((File) e.getNewValue());
				repaint();
			}
		}
	}

	public void paint(Graphics g) {
		if (thumbnail != null) {
			int xpos = (getWidth() - thumbnail.getIconWidth()) / 2;
			int ypos = (getHeight() - thumbnail.getIconHeight()) / 2;
			g.drawImage(thumbnail.getImage(), xpos, ypos, null);
		}
	}
}