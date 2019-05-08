package tmcit.yasu.ui.game;

import javax.swing.JButton;
import javax.swing.JPanel;

import tmcit.yasu.listener.LogControllActionListener;
import tmcit.yasu.ui.MainFrame;

public class LogControllPanel extends JPanel {
	private MainFrame mainFrame;
	private GameLogPanel gameLogPanel;
	
	private JButton nextButton, backButton, closeButton;
	
	private LogControllActionListener logControllActionListener;
	
	public LogControllPanel(MainFrame mainFrame0, GameLogPanel gameLogPanel0) {
		mainFrame = mainFrame0;
		gameLogPanel = gameLogPanel0;
		
		init();
		initLayout();
	}
	
	private void init() {
		logControllActionListener = new LogControllActionListener(mainFrame, gameLogPanel);

		nextButton = new JButton("次のターン");
		backButton = new JButton("前のターン");
		closeButton = new JButton("閉じる");
		
		nextButton.addActionListener(logControllActionListener);
		backButton.addActionListener(logControllActionListener);
		closeButton.addActionListener(logControllActionListener);
	}
	
	private void initLayout() {
		setLayout(null);
		
		backButton.setBounds(10, 10, 100, 30);
		nextButton.setBounds(120, 10, 100, 30);
		closeButton.setBounds(300, 10, 100, 30);
		
		add(nextButton);
		add(backButton);
		add(closeButton);
	}
}
