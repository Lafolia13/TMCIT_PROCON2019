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
		if(cmd.equals("更新")) {
			selectGamePanel.refreshGameList();
		}else if(cmd.contentEquals("開始")) {
			int selectedId = selectGamePanel.getSelectedMatchId();
			if(selectedId == -1) {
				JOptionPane.showMessageDialog(selectGamePanel, "対戦するゲームを選択してください。", "エラー", JOptionPane.ERROR_MESSAGE);
				return;
			}
			mainFrame.addTab("ゲーム[id=" + String.valueOf(selectedId) +"]", new GamePanel(connectSetting, selectedId));
		}
	}

}
