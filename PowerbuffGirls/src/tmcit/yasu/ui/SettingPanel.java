package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;

public class SettingPanel extends JPanel{
	private JLabel mainLabel;
	
	public SettingPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		setBorder(Constant.lineBorder);
		
		mainLabel = new JLabel("ê›íË");
		mainLabel.setFont(Constant.mainFont);
	}
	
	private void initLayout() {
		setLayout(null);
		
		mainLabel.setBounds(5, 5, 300, 30);
		
		add(mainLabel);
	}
}
