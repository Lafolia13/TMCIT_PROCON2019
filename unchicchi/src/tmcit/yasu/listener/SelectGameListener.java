package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.ui.GamePanel;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.SelectGamePanel;

public class SelectGameListener implements ActionListener {
	private MainFrame mainFrame;
	private SelectGamePanel selectGamePanel;
	private ConnectSetting connectSetting;

	public SelectGameListener(MainFrame mainFrame0, SelectGamePanel selectGamePanel0, ConnectSetting connectSetting0) {
		mainFrame = mainFrame0;
		selectGamePanel = selectGamePanel0;
		connectSetting = connectSetting0;
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
			mainFrame.addTab("�Q�[��[id=" + String.valueOf(selectedId) +"]", new GamePanel(connectSetting, selectedId));
		}
	}

}
