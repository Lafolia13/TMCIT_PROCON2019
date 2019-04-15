package tmcit.yasu.ui;

import javax.swing.JButton;
import javax.swing.JPanel;

import tmcit.yasu.listener.StartButtonListener;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class StarterPanel extends JPanel{
	private MainFrame mainFrame;
	private FileManager fileManager;

	// UI
	private MapSelectPanel mapSelectPanel;
	private AgentSelectPanel myAgentSelectPanel, rivalAgentSelectPanel;
	private SettingPanel settingPanel;
	private JButton startButton;

	// listener
	private StartButtonListener startButtonListener;

	public StarterPanel(MainFrame mainFrame0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		mapSelectPanel = new MapSelectPanel(fileManager);
		myAgentSelectPanel = new AgentSelectPanel(mainFrame, true, fileManager);
		rivalAgentSelectPanel = new AgentSelectPanel(mainFrame, false, fileManager);
		settingPanel = new SettingPanel(mainFrame, fileManager);
		startButton = new JButton("ŠJŽn");
		startButton.setFont(Constant.DEFAULT_FONT);

		// listener
		startButtonListener = new StartButtonListener(mainFrame, mapSelectPanel, myAgentSelectPanel, rivalAgentSelectPanel);
		startButton.addActionListener(startButtonListener);
	}

	private void initLayout() {
		setLayout(null);

		mapSelectPanel.setBounds(10, 10, 400, 700);
		myAgentSelectPanel.setBounds(420, 10, 300, 345);
		rivalAgentSelectPanel.setBounds(420, 365, 300, 345);
		settingPanel.setBounds(730, 10, 300, 650);
		startButton.setBounds(730, 670, 300, 40);

		add(mapSelectPanel);
		add(myAgentSelectPanel);
		add(rivalAgentSelectPanel);
		add(settingPanel);
		add(startButton);
	}
}
