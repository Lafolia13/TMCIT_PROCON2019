package tmcit.yasu.ui;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	private SolverSelectPanel solverSelectPanel;
	private SettingPanel settingPanel;
	
	public MainPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		solverSelectPanel = new SolverSelectPanel();
		settingPanel = new SettingPanel();
	}
	
	private void initLayout() {
		setLayout(null);
		
		solverSelectPanel.setBounds(10, 10, 500, 300);
		settingPanel.setBounds(10, 320, 500, 200);
		
		add(solverSelectPanel);
		add(settingPanel);
	}
}
