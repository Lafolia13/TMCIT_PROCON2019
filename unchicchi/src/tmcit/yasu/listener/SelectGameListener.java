package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tmcit.yasu.ui.SelectGamePanel;

public class SelectGameListener implements ActionListener {

	private SelectGamePanel selectGamePanel;

	public SelectGameListener(SelectGamePanel selectGamePanel0) {
		selectGamePanel = selectGamePanel0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("çXêV")) {
			selectGamePanel.refreshGameList();
		}
	}

}
