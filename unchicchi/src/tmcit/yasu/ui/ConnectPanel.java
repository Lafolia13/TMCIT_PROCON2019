package tmcit.yasu.ui;

import javax.swing.JPanel;

public class ConnectPanel extends JPanel{
	
	private ConnectSettingPanel connectSettingPanel;
	
	public ConnectPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		connectSettingPanel = new ConnectSettingPanel();
	}
	
	private void initLayout() {
		setLayout(null);
		
		connectSettingPanel.setBounds(10, 10, 400, 400);
		
		add(connectSettingPanel);
	}
}
