package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.game.GameLogPanel;

public class LogControllActionListener implements ActionListener{
	private MainFrame mainFrame;
	private GameLogPanel gameLogPanel;
	
	public LogControllActionListener(MainFrame mainFrame0, GameLogPanel gameLogPanel0) {
		mainFrame = mainFrame0;
		gameLogPanel = gameLogPanel0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("���̃^�[��")) {
			gameLogPanel.paintNextTurn();
		}else if(cmd.equals("�O�̃^�[��")) {
			gameLogPanel.paintBackTurn();
		}else if(cmd.equals("����")) {
			mainFrame.deleteTabbedPanel(gameLogPanel);
		}
	}

}
