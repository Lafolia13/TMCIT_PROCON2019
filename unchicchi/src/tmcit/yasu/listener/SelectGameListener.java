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
		if(cmd.equals("更新")) {
			selectGamePanel.refreshGameList();
		}else if(cmd.contentEquals("開始")) {
			int selectedId = selectGamePanel.getSelectedMatchId();
			if(selectedId == -1) {
				JOptionPane.showMessageDialog(selectGamePanel, "対戦するゲームを選択してください。", "エラー", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

}
