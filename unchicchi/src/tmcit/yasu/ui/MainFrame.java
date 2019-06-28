package tmcit.yasu.ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame{
	private JTabbedPane tabbedPane;
	
	public MainFrame() {
		init();
		initLayout();
	}
	
	private void init() {
		this.setTitle("本番用Unchicchi");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		tabbedPane = new JTabbedPane();
	}
	
	private void initLayout() {
		tabbedPane.addTab("スタート画面", new MainPanel());
		
		this.add(tabbedPane);
	}
}
