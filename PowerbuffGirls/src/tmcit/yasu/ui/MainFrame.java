package tmcit.yasu.ui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import tmcit.yasu.util.FileManager;

public class MainFrame extends JFrame{
	private JTabbedPane tabbedPanel;
	private MainPanel mainPanel;
	
	private FileManager fileManager;
	
	public MainFrame() {
		setTitle("PowerbuffGirls");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		
		init();
		initLayout();
		
		setVisible(true);
	}
	
	private void init() {
		tabbedPanel = new JTabbedPane();
		mainPanel = new MainPanel(this);
		tabbedPanel.add("スタート画面", mainPanel);
		fileManager = new FileManager();
	}
	
	private void initLayout() {
		add(tabbedPanel);
	}
	
	public void addPanel(String title, Component comp) {
		int count = tabbedPanel.getTabCount();
		for(int i = 0;i < count;i++) {
			String nowName = tabbedPanel.getTitleAt(i);
			if(nowName.equals("ソルバーの追加")) {
				tabbedPanel.setSelectedIndex(i);
				return;
			}
		}
		tabbedPanel.add(title, comp);
		tabbedPanel.setSelectedComponent(comp);
	}
	
	public void removePanel(Component comp) {
		tabbedPanel.remove(comp);
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
}
