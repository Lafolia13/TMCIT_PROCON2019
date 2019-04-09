package tmcit.yasu.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame{
	private JTabbedPane tabbedPanel;

	public MainFrame() {
		init();
		initLayout();
	}

	private void init() {
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void initLayout() {
		tabbedPanel = new JTabbedPane();
		tabbedPanel.addTab("スタート画面", new StarterPanel());

		this.add(tabbedPanel);
	}
}
