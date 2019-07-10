package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tmcit.yasu.ui.AgentSelectPanel;

public class GameStartListener implements ActionListener{
	private AgentSelectPanel agentSelectPanel;
	
	public GameStartListener(AgentSelectPanel agentSelectPanel0) {
		agentSelectPanel = agentSelectPanel0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		agentSelectPanel.getPlayer();
		System.out.println("[!!!!!]");
	}

}
