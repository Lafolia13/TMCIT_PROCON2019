package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.ui.ConnectPanel;
import tmcit.yasu.ui.ConnectSettingPanel;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.SelectGamePanel;
import tmcit.yasu.util.Network;

public class ConnectionListener implements ActionListener{
	private MainFrame mainFrame;
	private ConnectPanel connectPanel;
	private ConnectSettingPanel connectSettingPanel;

	public ConnectionListener(MainFrame mainFrame0, ConnectPanel connectPanel0, ConnectSettingPanel connectSettingPanel0) {
		mainFrame = mainFrame0;
		connectPanel = connectPanel0;
		connectSettingPanel = connectSettingPanel0;
	}

	private boolean urlValidator(String url) {
		String URL_REGEX =
				"^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
				"(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
				"([).!';/?:,][[:blank:]])?$";

		Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

		if(url == null) return false;

		Matcher matcher = URL_PATTERN.matcher(url);
		return matcher.matches();
	}

	private boolean checkSetting(ConnectSetting connectSetting) {
		String msg = "";
		boolean flag = true;
		if(connectSetting.url.isEmpty() && connectSetting.token.isEmpty()) {
			msg = "URL��Token�����͂���Ă��܂���B";
			flag = false;
		}else if(connectSetting.url.isEmpty()) {
			msg = "URL�����͂���Ă��܂���B";
			flag = false;
		}else if(connectSetting.token.isEmpty()) {
			msg = "Token�����͂���Ă��܂���B";
			flag = false;
		}else if(!urlValidator(connectSetting.url)) {
			msg = "URL������������܂���B";
			flag = false;
		}


		if(!flag) {
			JOptionPane.showMessageDialog(connectSettingPanel, msg, "�ݒ�G���[", JOptionPane.ERROR_MESSAGE);
		}

		return flag;
	}

	private boolean checkConnect(ConnectSetting connectSetting) {
		Network net = new Network(connectSetting.url, connectSetting.port, connectSetting.token);

		try {
			net.ping();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(connectSettingPanel, "�T�[�o�Ɛڑ��ł��܂���B", "�ڑ��G���[", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (InvalidTokenException e) {
			JOptionPane.showMessageDialog(connectSettingPanel, "�g�[�N��������������܂���B", "�ڑ��G���[", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConnectSetting setting = connectSettingPanel.getSetting();

		// ���͊m�F
		if(!checkSetting(setting)) {
			return;
		}

		// �ڑ��m�F
		if(!checkConnect(setting)) {
			return;
		}

		mainFrame.addTab("�Q�[���ꗗ", new SelectGamePanel(setting));
	}

}
