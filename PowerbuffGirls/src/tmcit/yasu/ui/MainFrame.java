package tmcit.yasu.ui;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	private MainPanel mainPanel;
	
	public MainFrame() {
		setTitle("PowerbuffGirls");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		
		init();
		initLayout();
		
		setVisible(true);
	}
	
	private void init() {
		mainPanel = new MainPanel();
	}
	
	private void initLayout() {
		add(mainPanel);
	}
}
