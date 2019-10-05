package tmcit.yasu.util;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

import tmcit.yasu.data.Action;
import tmcit.yasu.data.Actions;
import tmcit.yasu.data.Agent;
import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.data.Field;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.data.Team;
import tmcit.yasu.exception.InvalidMatchesException;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.exception.TooEarlyException;
import tmcit.yasu.exception.UnacceptableTimeExeption;
import tmcit.yasu.player.ExecPlayer;
import tmcit.yasu.ui.GamePaintPanel;
import tmcit.yasu.ui.GameStatusPanel;

public class GameNetworkRunnable implements Runnable{
	private ConnectSetting connectSetting;
	private MatchesData matchData;
	private GameStatusPanel gameStatusPanel;
	private GamePaintPanel gamePaintPanel;
	
	// 次にpingを送信する時間
	private long nextPingUnixTime, nowUnixTime, gameStartUnixTime;
	
	// solver関係
	private ExecPlayer execPlayer;
	
	// 
	private int nowTurn;

	public GameNetworkRunnable(ConnectSetting connectSetting0, MatchesData matchData0, GameStatusPanel gameStatusPanel0, GamePaintPanel gamePaintPanel0, String cmd0) {
		connectSetting = connectSetting0;
		matchData = matchData0;
		gameStatusPanel = gameStatusPanel0;
		gamePaintPanel = gamePaintPanel0;
		
		nextPingUnixTime = 0;
		gameStartUnixTime = -1;
		initExecPlayer(cmd0);
	}
	
	private void initExecPlayer(String cmd) {
		execPlayer = new ExecPlayer(cmd);
	}
	
	// 残り時間の表示
	private void printLastTime(boolean afterStartTimeFlag) {
		long nowUnixTimeMillis = System.currentTimeMillis();
		String statusText = "";
		if(afterStartTimeFlag) {
			statusText = "開始前にアクセスしてます(残り" + String.valueOf((gameStartUnixTime*1000L - nowUnixTimeMillis) / 1000.0) + "秒)";
		}else {
			statusText = "開始前(残り" + String.valueOf((gameStartUnixTime*1000L - nowUnixTimeMillis) / 1000.0) + "秒)";
		}
		gameStatusPanel.changeGameStatus(statusText);
	}
	
	// 作戦ステップ時の表示
	private void printStrategyStep(long strategyStepUnixTime) {
		long nowUnixTimeMillis = System.currentTimeMillis();
		gameStatusPanel.changeGameStatus("作戦ステップ(残り" + String.valueOf((strategyStepUnixTime*1000L - nowUnixTimeMillis) / 1000.0) + "秒)");
	}
	
	// インターバル時の表示
	private void printIntervalStep(long intervalStepUnixTime) {
		long nowUnixTimeMillis = System.currentTimeMillis();
		gameStatusPanel.changeGameStatus("インターバル(残り" + String.valueOf((intervalStepUnixTime*1000L - nowUnixTimeMillis) / 1000.0) + "秒)");
	}
	
	// ゲームの状態を確認
	private Field checkGameStatus(Network net) {
		Field nowField = null;
		try {
			nowField = net.getMatcheStatus(matchData.id);
			gamePaintPanel.drawField(nowField);
			gameStatusPanel.changeGameStatus("ゲーム中");
			gameStatusPanel.changeTurn(nowField.turn);
			nowTurn = nowField.turn;
		} catch (InvalidTokenException e2) {
			gameStatusPanel.changeGameStatus("トークンエラー");
		} catch (InvalidMatchesException e2) {
			gameStatusPanel.changeGameStatus("参加できません");
		} catch (TooEarlyException e2) {
			gameStartUnixTime = e2.startUnixTime;
			printLastTime(true);
		}
		
		return nowField;
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
	
	// solverにマップ情報などの初期化を入力
	private void inputInit(Field field) {
		execPlayer.input(String.valueOf(matchData.turns));
		execPlayer.input(String.valueOf(field.width));
		execPlayer.input(String.valueOf(field.height));
		execPlayer.input(String.valueOf(field.teams.get(0).agents.size()));
		for(int nowY = 0;nowY < field.height;nowY++) {
			String line = "";
			for(int nowX = 0;nowX < field.width;nowX++) {
				line += String.valueOf(field.points.get(nowY).get(nowX));
				if(nowX != field.width-1) line += " ";
			}
			execPlayer.input(line);
		}
	}
	
	// solverにターン毎の入力
	private void inputTurn(Field field) {
		execPlayer.input(String.valueOf(field.turn));
		for(int nowY = 0;nowY < field.height;nowY++) {
			String line = "";
			for(int nowX = 0;nowX < field.width;nowX++) {
				int nowTerritory = field.tiled.get(nowY).get(nowX);
				
				if(nowTerritory == 0) {
					line += "0";
				}else if(nowTerritory == matchData.teamID) {
					line += "2";
				}else {
					line += "1";
				}
				
				if(nowX != field.width-1) line += " ";
			}
			execPlayer.input(line);
		}
		
		// my agent
		for(int i = 0;i < field.teams.size();i++) {
			Team nowTeam = field.teams.get(i);
			if(nowTeam.teamID == matchData.teamID) {
				for(int agentIndex = 0;agentIndex < nowTeam.agents.size();agentIndex++) {
					Agent nowAgent = nowTeam.agents.get(agentIndex);
					String line = String.valueOf(nowAgent.x-1) + " " + String.valueOf(nowAgent.y-1);
					execPlayer.input(line);
				}
			}
		}
		
		// rival agent
		for(int i = 0;i < field.teams.size();i++) {
			Team nowTeam = field.teams.get(i);
			if(nowTeam.teamID != matchData.teamID) {
				for(int agentIndex = 0;agentIndex < nowTeam.agents.size();agentIndex++) {
					Agent nowAgent = nowTeam.agents.get(agentIndex);
					String line = String.valueOf(nowAgent.x-1) + " " + String.valueOf(nowAgent.y-1);
					execPlayer.input(line);
				}
			}
		}
	}
	
	private Action convertToAction(String str, int agentId) {
		Action ret = new Action();
		ret.agentID = agentId;
		
		System.out.println("[SOLVER] " + str);
		
		if(str.charAt(0) == 'w') {
			ret.type = "move";
			int way = Integer.valueOf("" + str.charAt(1));
			if(way >= 4) way--;
			ret.dx = Constant.DIR_X[way];
			ret.dy = Constant.DIR_Y[way];
		}else if(str.charAt(0) == 'e') {
			ret.type = "remove";
			int way = Integer.valueOf("" + str.charAt(1));
			if(way >= 4) way--;
			ret.dx = Constant.DIR_X[way];
			ret.dy = Constant.DIR_Y[way];
		}else {
			ret.type = "stay";
			ret.dx = 0;
			ret.dy = 0;
		}
		return ret;
	}
	
	// solverから出力を取得してサーバに送信
	private void outputSolver(Network net, Field field) {
		ArrayList<String> myCmd = new ArrayList<>();
		ArrayList<Integer> agentIds = new ArrayList<>();
		for(int i = 0;i < field.teams.size();i++) {
			Team nowTeam = field.teams.get(i);
			if(nowTeam.teamID == matchData.teamID) {
				Actions actions = new Actions();
				for(int agentIndex = 0;agentIndex < nowTeam.agents.size();agentIndex++) {
					Agent nowAgent = nowTeam.agents.get(agentIndex);
					String solverAction = execPlayer.getAction();
					
					myCmd.add(solverAction);
					agentIds.add(nowAgent.agentID);
					
					actions.actions.add(convertToAction(solverAction, nowAgent.agentID));
				}
				
				// post
				try {
					net.postAction(matchData.id, actions);
				} catch (InvalidTokenException | InvalidMatchesException | TooEarlyException
						| UnacceptableTimeExeption e) {
					e.printStackTrace();
				}
				
				// 描画
				gamePaintPanel.drawActions(myCmd, agentIds);
			}
		}
	}

	@Override
	public void run() {
		Network net = new Network(connectSetting.url, connectSetting.port, connectSetting.token);
		
		// 次のターンがスタートする時間
		long nextTurnStartTime = 0;
		
		// flag
		boolean inputInitFlag = false;
		int beforeTurn = -1;

		// 1最初にゲームが開始しているか確認
		checkGameStatus(net);

		while(true) {
			long nowUnixTimeMillis = System.currentTimeMillis();
			long nowUnixTime = nowUnixTimeMillis / 1000L;

			// pingでサーバとの接続状態を取得
			if(nextPingUnixTime < nowUnixTime) {
				checkPing(net);
				nextPingUnixTime = nowUnixTime + connectSetting.interval;
			}

			if(gameStartUnixTime > nowUnixTime) {
				// 1ゲームが開始していない場合、秒数をカウントダウン
				printLastTime(false);
			}else {
				// 1ゲームがスタートしている場合
				if(!inputInitFlag) {
					// 最初のみの入出力
					Field nowField = checkGameStatus(net);
					if(nowField == null) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					inputInit(nowField);
					inputTurn(nowField);
					outputSolver(net, nowField);
					inputInitFlag = true;
					nextTurnStartTime = gameStartUnixTime + (matchData.turnMillis + matchData.intervalMillis) / 1000L;
					beforeTurn = nowField.turn;
				}else if(nextTurnStartTime <= nowUnixTime && nowTurn == matchData.turns) {
					checkGameStatus(net);
					gameStatusPanel.changeGameStatus("ゲーム終了");
					break;
				}else if(nextTurnStartTime <= nowUnixTime){
					// ターン毎の入出力
					Field nowField = checkGameStatus(net);
					if(nowField == null || nowField.turn == beforeTurn) {
						System.out.println("[WARN]continued beforeTurn == nowTurn");
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					System.out.println("Turn Input:" + String.valueOf(nowField.turn));
					gamePaintPanel.resetActions();
					inputTurn(nowField);
					outputSolver(net, nowField);
					nextTurnStartTime = gameStartUnixTime + (nowField.turn)* ((matchData.turnMillis + matchData.intervalMillis) / 1000L);
					beforeTurn = nowField.turn;
				}else if(nowUnixTime < nextTurnStartTime - matchData.intervalMillis/1000L) {
					// 作戦ステップの間
					printStrategyStep(nextTurnStartTime - matchData.intervalMillis/1000L);
				}else if(nowUnixTime < nextTurnStartTime) {
					// インターバルの間
					printIntervalStep(nextTurnStartTime);
				}
				
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
