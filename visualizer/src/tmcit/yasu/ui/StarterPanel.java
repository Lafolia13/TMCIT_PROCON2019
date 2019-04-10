package tmcit.yasu.ui;

import javax.swing.JPanel;

import tmcit.yasu.util.FileManager;

public class StarterPanel extends JPanel{
	private FileManager fileManager;

	private MapSelectPanel mapSelectPanel;

	public StarterPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		mapSelectPanel = new MapSelectPanel(fileManager);
	}

	private void initLayout() {
		setLayout(null);

		mapSelectPanel.setBounds(10, 10, 400, 700);

		add(mapSelectPanel);
	}
}
