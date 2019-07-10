package tmcit.yasu.ui;

import javax.swing.JPanel;

import tmcit.yasu.util.FileManager;

public class MainPanel extends JPanel{
	private FileManager fileManager;
	private SettingPanel settingPanel;

	public MainPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		settingPanel = new SettingPanel(fileManager);
	}

	private void initLayout() {
		setLayout(null);

		settingPanel.setBounds(10, 10, 400, 500);

		add(settingPanel);
	}
}
