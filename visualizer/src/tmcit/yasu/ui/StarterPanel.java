package tmcit.yasu.ui;

import javax.swing.JPanel;

import tmcit.yasu.util.FileManager;

public class StarterPanel extends JPanel{
	private MainFrame mainFrame;
	private FileManager fileManager;

	private MapSelectPanel mapSelectPanel;
	private AgentSelectPanel myAgentSelectPanel, rivalAgentSelectPanel;
	private SettingPanel settingPanel;

	public StarterPanel(MainFrame mainFrame0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		mapSelectPanel = new MapSelectPanel(fileManager);
		myAgentSelectPanel = new AgentSelectPanel(true, fileManager);
		rivalAgentSelectPanel = new AgentSelectPanel(false, fileManager);
		settingPanel = new SettingPanel(mainFrame, fileManager);
	}

	private void initLayout() {
		setLayout(null);

		mapSelectPanel.setBounds(10, 10, 400, 700);
		myAgentSelectPanel.setBounds(420, 10, 300, 345);
		rivalAgentSelectPanel.setBounds(420, 365, 300, 345);
		settingPanel.setBounds(730, 10, 300, 700);

		add(mapSelectPanel);
		add(myAgentSelectPanel);
		add(rivalAgentSelectPanel);
		add(settingPanel);
	}
}
