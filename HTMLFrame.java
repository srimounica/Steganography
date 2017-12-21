import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
class HTMLFrame extends JFrame implements HyperlinkListener {
	JEditorPane editorPane;
	JScrollPane scrollPane;

	public HTMLFrame(String startURL, boolean isOnline) {
		super("Help - Steganograph ");

		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		if (isOnline) {
			editorPane.setBackground(Color.white);
			setTitle("Steganograph ");
		}
		editorPane.addHyperlinkListener(this);
		scrollPane = new JScrollPane(editorPane);

		try {
			editorPane.setPage(startURL);
			getContentPane().add(scrollPane);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Oops!! Error\n" + e, "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(d.width, d.height);
		setVisible(true);
	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				editorPane.setPage(e.getURL());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Oops!! Error\n" + ex, "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}