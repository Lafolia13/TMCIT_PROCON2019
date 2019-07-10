package tmcit.yasu.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import tmcit.yasu.util.FileManager;

public class MainFrame extends JFrame{
	private FileManager fileManager;
	private JTabbedPane tabbedPane;

	public MainFrame() {
		init();
		initLayout();
	}

	private void init() {
		this.setTitle("本番用Unchicchi");
		this.setSize(500, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		tabbedPane = new JTabbedPane();
		
		fileManager = new FileManager();
	}

	private void initLayout() {
		tabbedPane.addTab("スタート画面", new MainPanel(fileManager));

		this.add(tabbedPane);
	}
}
