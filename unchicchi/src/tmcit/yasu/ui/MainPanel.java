package tmcit.yasu.ui;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	private SettingPanel settingPanel;
	
	public MainPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		settingPanel = new SettingPanel();
	}
	
	private void initLayout() {
		setLayout(null);
		
		settingPanel.setBounds(10, 10, 300, 400);
		
		add(settingPanel);
	}
}
