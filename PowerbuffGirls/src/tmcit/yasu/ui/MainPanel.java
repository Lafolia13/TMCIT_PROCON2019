package tmcit.yasu.ui;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	private MainFrame mainFrame;
	
	private SolverSelectPanel solverSelectPanel;
	private SettingPanel settingPanel;
	
	public MainPanel(MainFrame mainFrame0) {
		mainFrame = mainFrame0;
		init();
		initLayout();
	}
	
	private void init() {
		solverSelectPanel = new SolverSelectPanel();
		settingPanel = new SettingPanel(mainFrame);
	}
	
	private void initLayout() {
		setLayout(null);
		
		solverSelectPanel.setBounds(10, 10, 500, 300);
		settingPanel.setBounds(10, 320, 500, 200);
		
		add(solverSelectPanel);
		add(settingPanel);
	}
}
