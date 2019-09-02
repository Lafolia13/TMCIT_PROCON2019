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
	
	// ����ping�𑗐M���鎞��
	private long nextPingUnixTime, nowUnixTime, gameStartUnixTime;

	public GameNetworkRunnable(ConnectSetting connectSetting0, MatchesData matchData0, GameStatusPanel gameStatusPanel0) {
		connectSetting = connectSetting0;
		matchData = matchData0;
		gameStatusPanel = gameStatusPanel0;
		
		nextPingUnixTime = 0;
		gameStartUnixTime = -1;
	}
	
	// �Q�[���̏�Ԃ��m�F
	private void checkGameStatus(Network net) {
		try {
			net.getMatcheStatus(matchData.id);
			gameStatusPanel.changeGameStatus("�Q�[����");
		} catch (InvalidTokenException e2) {
			gameStatusPanel.changeGameStatus("�g�[�N���G���[");
		} catch (InvalidMatchesException e2) {
			gameStatusPanel.changeGameStatus("�Q���ł��܂���");
		} catch (TooEarlyException e2) {
			gameStartUnixTime = e2.startUnixTime;
			nowUnixTime = System.currentTimeMillis() / 1000L;
			long lastTime = gameStartUnixTime - nowUnixTime;
			gameStatusPanel.changeGameStatus("�J�n�O(�c��" + String.valueOf(lastTime) + "�b)");
		}
	}
	
	// ping�ŃT�[�o�Ƃ̐ڑ���Ԃ��擾
	private void checkPing(Network net) {
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
	}

	@Override
	public void run() {
		Network net = new Network(connectSetting.url, connectSetting.port, connectSetting.token);

		// �ŏ��ɃQ�[�����J�n���Ă��邩�m�F
		checkGameStatus(net);

		while(true) {
			long nowUnixTime = System.currentTimeMillis() / 1000L;

			// ping�ŃT�[�o�Ƃ̐ڑ���Ԃ��擾
			if(nextPingUnixTime < nowUnixTime) {
				checkPing(net);
				nextPingUnixTime = nowUnixTime + connectSetting.interval;
			}

			// �Q�[�����J�n���Ă��Ȃ��ꍇ�A�b�����J�E���g�_�E��
			if(gameStartUnixTime - nowUnixTime > 0) {
				gameStatusPanel.changeGameStatus("�J�n�O(�c��" + String.valueOf(gameStartUnixTime - nowUnixTime) + "�b)");
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
