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

		// 最初にゲームが開始しているか確認
		try {
			net.getMatcheStatus(matchData.id);
			gameStatusPanel.changeGameStatus("ゲーム中");
		} catch (InvalidTokenException e2) {
			gameStatusPanel.changeGameStatus("トークンエラー");
		} catch (InvalidMatchesException e2) {
			gameStatusPanel.changeGameStatus("参加できません");
		} catch (TooEarlyException e2) {
			gameStatusPanel.changeGameStatus("開始前");
		}

		while(true) {
			// pingでサーバとの接続状態を取得
			try {
				boolean pingResult = net.ping();
				if(pingResult) {
					gameStatusPanel.changeServerStatus("正常");
				}else {
					gameStatusPanel.changeServerStatus("失敗");
				}
			} catch (IOException e1) {
				gameStatusPanel.changeServerStatus("通信不能");
			} catch (InvalidTokenException e1) {
				gameStatusPanel.changeServerStatus("トークンエラー");
			}

			// TODO: 設定の値に合わせる
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
