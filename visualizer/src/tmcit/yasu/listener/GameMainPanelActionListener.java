package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.game.GameMainPanel;

public class GameMainPanelActionListener implements ActionListener{
	private MainFrame mainFrame;
	private GameMainPanel gameMainPanel;
	
	public GameMainPanelActionListener(MainFrame mainFrame0, GameMainPanel gameMainPanel0) {
		mainFrame = mainFrame0;
		gameMainPanel = gameMainPanel0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("•Â‚¶‚é")) {
			mainFrame.deleteTabbedPanel(gameMainPanel);
		}
	}

}
