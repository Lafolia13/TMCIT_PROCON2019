package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.ui.ConnectSettingPanel;

public class ConnectionListener implements ActionListener{

	private ConnectSettingPanel connectSettingPanel;

	public ConnectionListener(ConnectSettingPanel connectSettingPanel0) {
		connectSettingPanel = connectSettingPanel0;
	}

	private boolean checkSetting(ConnectSetting connectSetting) {
		String msg = "";
		boolean flag = false;
		if(connectSetting.url.isEmpty() && connectSetting.token.isEmpty()) {
			msg = "URL��Token�����͂���Ă��܂���B";
			flag = true;
		}else if(connectSetting.url.isEmpty()) {
			msg = "URL�����͂���Ă��܂���B";
			flag = true;
		}else if(connectSetting.token.isEmpty()) {
			msg = "Token�����͂���Ă��܂���B";
			flag = true;
		}

		if(flag) {
			JOptionPane.showMessageDialog(connectSettingPanel, msg, "�ݒ�G���[", JOptionPane.ERROR_MESSAGE);
		}

		return flag;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConnectSetting setting = connectSettingPanel.getSetting();

		// ���͊m�F
		if(checkSetting(setting)) {
			return;
		}

	}

}
