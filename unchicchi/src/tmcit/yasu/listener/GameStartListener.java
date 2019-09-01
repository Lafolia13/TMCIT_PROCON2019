package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tmcit.yasu.ui.AgentSelectPanel;
import tmcit.yasu.ui.GamePanel;
import tmcit.yasu.ui.MainFrame;

public class GameStartListener implements ActionListener{
	private MainFrame mainFrame;
	private AgentSelectPanel agentSelectPanel;

	public GameStartListener(MainFrame mainFrame0, AgentSelectPanel agentSelectPanel0) {
		mainFrame = mainFrame0;
		agentSelectPanel = agentSelectPanel0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		agentSelectPanel.getPlayer();
		System.out.println("[!!!!!]");
	}

}
