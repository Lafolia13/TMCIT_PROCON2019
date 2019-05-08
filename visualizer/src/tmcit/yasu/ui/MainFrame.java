package tmcit.yasu.ui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
		tabbedPanel.addTab("スタート画面", new StarterPanel(this, fileManager));

		this.add(tabbedPanel);
	}

	public void addTabbedPanel(String title, JPanel addPanel) {
		tabbedPanel.addTab(title, addPanel);
		int index = tabbedPanel.indexOfTab(title);
		tabbedPanel.setSelectedIndex(index);
	}

	public void switchOrAddTabbedPanel(String tilte, JPanel addPanel) {
		int index = tabbedPanel.indexOfTab(tilte);
		if(index == -1) {
			addTabbedPanel(tilte, addPanel);
		}else {
			tabbedPanel.setSelectedIndex(index);
		}
	}

	public void deleteTabbedPanel(Component comp) {
		int index = tabbedPanel.indexOfComponent(comp);
		if(index == -1) {
			System.err.println("[SYSTEM]deteleTabbedPanel:index = -1");
			return;
		}
		tabbedPanel.remove(index);
	}
}
