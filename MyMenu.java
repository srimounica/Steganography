import javax.swing.JMenu;

class MyMenu extends JMenu {
	public MyMenu(String caption, int mnemonicIndex, int mnemonic) {
		super(caption);
		setDisplayedMnemonicIndex(mnemonicIndex);
		setMnemonic(mnemonic);
	}
}