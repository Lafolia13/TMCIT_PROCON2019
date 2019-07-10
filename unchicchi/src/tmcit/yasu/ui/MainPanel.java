package tmcit.yasu.ui;

import javax.swing.JButton;
import javax.swing.JPanel;

import tmcit.yasu.listener.GameStartListener;
import tmcit.yasu.util.FileManager;

public class MainPanel extends JPanel{
	private FileManager fileManager;
	
	// UI
	private SettingPanel settingPanel;
	
	private JButton startButton;
	
	// Listener
	private GameStartListener gameStartListener;

	public MainPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		settingPanel = new SettingPanel(fileManager);
		
		startButton = new JButton("‚·‚½[‚ÆII");
		
		// Listener‚Ìì¬
		gameStartListener = new GameStartListener(settingPanel.getAgentSelectPanel());
		
		// Listener‚Ì•R‚Ã‚¯
		startButton.addActionListener(gameStartListener);
		
	}

	private void initLayout() {
		setLayout(null);

		settingPanel.setBounds(10, 10, 400, 500);
		
		startButton.setBounds(10, 520, 150, 30);

		add(settingPanel);
		add(startButton);
	}
}
