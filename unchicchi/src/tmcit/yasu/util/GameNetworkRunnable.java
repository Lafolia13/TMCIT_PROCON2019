package tmcit.yasu.util;

import java.io.IOException;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.exception.InvalidMatchesException;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.exception.TooEarlyException;
import tmcit.yasu.ui.GameStatusPanel;

public class GameNetworkRunnable implements Runnable{
	private ConnectSetting connectSetting;
	private MatchesData matchData;
	private GameStatusPanel gameStatusPanel;

	public GameNetworkRunnable(ConnectSetting connectSetting0, MatchesData matchData0, GameStatusPanel gameStatusPanel0) {
		connectSetting = connectSetting0;
		matchData = matchData0;
		gameStatusPanel = gameStatusPanel0;
	}

	@Override
	public void run() {
		Network net = new Network(connectSetting.url, connectSetting.port, connectSetting.token);

		// �ŏ��ɃQ�[�����J�n���Ă��邩�m�F
		long startUnixTime = -1;
		try {
			net.getMatcheStatus(matchData.id);
			gameStatusPanel.changeGameStatus("�Q�[����");
		} catch (InvalidTokenException e2) {
			gameStatusPanel.changeGameStatus("�g�[�N���G���[");
		} catch (InvalidMatchesException e2) {
			gameStatusPanel.changeGameStatus("�Q���ł��܂���");
		} catch (TooEarlyException e2) {
			startUnixTime = e2.startUnixTime;
			long nowUnixTime = System.currentTimeMillis() / 1000L;
			long lastTime = startUnixTime - nowUnixTime;
			gameStatusPanel.changeGameStatus("�J�n�O(�c��" + String.valueOf(lastTime) + "�b)");
		}

		while(true) {
			// ping�ŃT�[�o�Ƃ̐ڑ���Ԃ��擾
			try {
				boolean pingResult = net.ping();
				if(pingResult) {
					gameStatusPanel.changeServerStatus("����");
				}else {
					gameStatusPanel.changeServerStatus("���s");
				}
			} catch (IOException e1) {
				gameStatusPanel.changeServerStatus("�ʐM�s�\");
			} catch (InvalidTokenException e1) {
				gameStatusPanel.changeServerStatus("�g�[�N���G���[");
			}

			// �Q�[�����J�n���Ă��Ȃ��ꍇ
			long nowUnixTime = System.currentTimeMillis() / 1000L;
			if(startUnixTime - nowUnixTime > 0) {
				gameStatusPanel.changeGameStatus("�J�n�O(�c��" + String.valueOf(startUnixTime - nowUnixTime) + "�b)");
			}

			// TODO: �ݒ�̒l�ɍ��킹��
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
