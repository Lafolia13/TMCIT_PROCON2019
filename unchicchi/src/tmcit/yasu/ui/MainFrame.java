package tmcit.yasu.ui;

import java.awt.Component;

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
		this.setTitle("�{�ԗpUnchicchi");
		this.setSize(1000, 650);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		tabbedPane = new JTabbedPane();

		fileManager = new FileManager();
	}

	private void initLayout() {
//		tabbedPane.addTab("�X�^�[�g���", new MainPanel(this, fileManager));

		tabbedPane.addTab("�ڑ��ݒ�", new ConnectPanel(this, fileManager));

		this.add(tabbedPane);
	}

	public void addTab(String title, Component component) {
		int index = tabbedPane.indexOfTab(title);
		if(index != -1) {
			tabbedPane.setSelectedIndex(index);
			return;
		}
		
		tabbedPane.addTab(title, component);
		tabbedPane.setSelectedComponent(component);
	}
}
