import javax.swing.JMenuItem;

class MenuItem extends JMenuItem {
	public MenuItem(String caption, int mnemonicIndex, int mnemonic) {
		super(caption);
		setDisplayedMnemonicIndex(mnemonicIndex);
		setMnemonic(mnemonic);
	}
}