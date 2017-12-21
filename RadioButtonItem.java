import javax.swing.JRadioButtonMenuItem;

class RadioButtonItem extends JRadioButtonMenuItem {
	public RadioButtonItem(String caption, int mnemonicIndex, int mnemonic) {
		super(caption);
		setDisplayedMnemonicIndex(mnemonicIndex);
		setMnemonic(mnemonic);
	}
}	