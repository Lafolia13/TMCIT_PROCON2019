package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import tmcit.yasu.ui.SelectGamePanel;

public class SelectGameListener implements ActionListener {

	private SelectGamePanel selectGamePanel;

	public SelectGameListener(SelectGamePanel selectGamePanel0) {
		selectGamePanel = selectGamePanel0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("�X�V")) {
			selectGamePanel.refreshGameList();
		}else if(cmd.contentEquals("�J�n")) {
			int selectedId = selectGamePanel.getSelectedMatchId();
			if(selectedId == -1) {
				JOptionPane.showMessageDialog(selectGamePanel, "�ΐ킷��Q�[����I�����Ă��������B", "�G���[", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

}
