package tmcit.yasu.util;

import java.io.IOException;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.data.Field;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.exception.InvalidMatchesException;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.exception.TooEarlyException;
import tmcit.yasu.ui.GamePaintPanel;
import tmcit.yasu.ui.GameStatusPanel;

public class GameNetworkRunnable implements Runnable{
	private ConnectSetting connectSetting;
	private MatchesData matchData;
	private GameStatusPanel gameStatusPanel;
	private GamePaintPanel gamePaintPanel;
	
	// 次にpingを送信する時間
	private long nextPingUnixTime, nowUnixTime, gameStartUnixTime;

	public GameNetworkRunnable(ConnectSetting connectSetting0, MatchesData matchData0, GameStatusPanel gameStatusPanel0, GamePaintPanel gamePaintPanel0) {
		connectSetting = connectSetting0;
		matchData = matchData0;
		gameStatusPanel = gameStatusPanel0;
		gamePaintPanel = gamePaintPanel0;
		
		nextPingUnixTime = 0;
		gameStartUnixTime = -1;
	}
	
	// ゲームの状態を確認
	private void checkGameStatus(Network net) {
		try {
			Field nowField = net.getMatcheStatus(matchData.id);
			gamePaintPanel.drawField(nowField);
			gameStatusPanel.changeGameStatus("ゲーム中");
		} catch (InvalidTokenException e2) {
			gameStatusPanel.changeGameStatus("トークンエラー");
		} catch (InvalidMatchesException e2) {
			gameStatusPanel.changeGameStatus("参加できません");
		} catch (TooEarlyException e2) {
			gameStartUnixTime = e2.startUnixTime;
			nowUnixTime = System.currentTimeMillis() / 1000L;
			long lastTime = gameStartUnixTime - nowUnixTime;
			gameStatusPanel.changeGameStatus("開始前(残り" + String.valueOf(lastTime) + "秒)");
		}
	}
	
	// pingでサーバとの接続状態を取得
	private void checkPing(Network net) {
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
	}

	@Override
	public void run() {
		Network net = new Network(connectSetting.url, connectSetting.port, connectSetting.token);

		// 最初にゲームが開始しているか確認
		checkGameStatus(net);

		while(true) {
			long nowUnixTime = System.currentTimeMillis() / 1000L;

			// pingでサーバとの接続状態を取得
			if(nextPingUnixTime < nowUnixTime) {
				checkPing(net);
				nextPingUnixTime = nowUnixTime + connectSetting.interval;
			}

			if(gameStartUnixTime - nowUnixTime > 0) {
				// ゲームが開始していない場合、秒数をカウントダウン
				gameStatusPanel.changeGameStatus("開始前(残り" + String.valueOf(gameStartUnixTime - nowUnixTime) + "秒)");
			}else {
				// ゲームがスタートしている場合
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
