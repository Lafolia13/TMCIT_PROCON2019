package tmcit.yasu.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import tmcit.yasu.util.FileManager;

public class MainFrame extends JFrame{
	// common
	private FileManager fileManager;

	private JTabbedPane tabbedPanel;

	public MainFrame() {
		init();
		initLayout();
	}

	private void init() {
		fileManager = new FileManager();

		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void initLayout() {
		tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("スタート画面", new StarterPanel(fileManager));

		this.add(tabbedPanel);
	}
}
