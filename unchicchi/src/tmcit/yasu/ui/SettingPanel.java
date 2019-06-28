package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;

public class SettingPanel extends JPanel{
	private JLabel nameLabel;
	
	public SettingPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);
		
		nameLabel = new JLabel("ê›íË");
		nameLabel.setFont(Constant.DEFAULT_FONT);
	}
	
	private void initLayout() {
		setLayout(null);
		
		nameLabel.setBounds(10, 5, 100, 30);
		
		add(nameLabel);
	}
}

