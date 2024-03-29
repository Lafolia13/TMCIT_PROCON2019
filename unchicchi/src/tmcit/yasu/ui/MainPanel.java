package tmcit.yasu.ui;

import javax.swing.JButton;
import javax.swing.JPanel;

import tmcit.yasu.listener.GameStartListener;
import tmcit.yasu.util.FileManager;

public class MainPanel extends JPanel{
	private MainFrame mainFrame;
	private FileManager fileManager;
	
	// UI
	private SettingPanel settingPanel;
	
	private JButton startButton;
	
	// Listener
	private GameStartListener gameStartListener;

	public MainPanel(MainFrame mainFrame0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		settingPanel = new SettingPanel(fileManager);
		
		startButton = new JButton("すたーと！！");
		
		// Listenerの作成
		gameStartListener = new GameStartListener(mainFrame, settingPanel.getAgentSelectPanel());
		
		// Listenerの紐づけ
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
