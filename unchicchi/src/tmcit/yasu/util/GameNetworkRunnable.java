package tmcit.yasu.util;

import java.io.IOException;

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
	
	// ����ping�𑗐M���鎞��
	private long nextPingUnixTime, nowUnixTime, gameStartUnixTime;
	
	// solver�֌W
	private ExecPlayer execPlayer;

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
	
	// �Q�[���̏�Ԃ��m�F
	private Field checkGameStatus(Network net) {
		Field nowField = null;
		try {
			nowField = net.getMatcheStatus(matchData.id);
			gamePaintPanel.drawField(nowField);
			gameStatusPanel.changeGameStatus("�Q�[����");
			gameStatusPanel.changeTurn(nowField.turn);
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
		
		return nowField;
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
	
	// solver�Ƀ}�b�v���Ȃǂ̏����������
	private void inputInit(Field field) {
		System.out.println(field.width);
		System.out.println(field.height);
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
	
	// solver�Ƀ^�[�����̓���
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
	
	// solver����o�͂��擾���ăT�[�o�ɑ��M
	private void outputSolver(Network net, Field field) {
		for(int i = 0;i < field.teams.size();i++) {
			Team nowTeam = field.teams.get(i);
			if(nowTeam.teamID == matchData.teamID) {
				Actions actions = new Actions();
				for(int agentIndex = 0;agentIndex < nowTeam.agents.size();agentIndex++) {
					Agent nowAgent = nowTeam.agents.get(agentIndex);
					String solverAction = execPlayer.getAction();
					
					actions.actions.add(convertToAction(solverAction, nowAgent.agentID));
				}
				
				// post
				try {
					net.postAction(matchData.id, actions);
				} catch (InvalidTokenException | InvalidMatchesException | TooEarlyException
						| UnacceptableTimeExeption e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() {
		Network net = new Network(connectSetting.url, connectSetting.port, connectSetting.token);
		
		// ���̃^�[�����X�^�[�g���鎞��
		long nextTurnStartTime = 0;
		
		// flag
		boolean inputInitFlag = false;

		// 1�ŏ��ɃQ�[�����J�n���Ă��邩�m�F
		checkGameStatus(net);

		while(true) {
			long nowUnixTimeMillis = System.currentTimeMillis();
			long nowUnixTime = nowUnixTimeMillis / 1000L;

			// ping�ŃT�[�o�Ƃ̐ڑ���Ԃ��擾
			if(nextPingUnixTime < nowUnixTime) {
				checkPing(net);
				nextPingUnixTime = nowUnixTime + connectSetting.interval;
			}

			if(gameStartUnixTime - nowUnixTime >= 0) {
				// 1�Q�[�����J�n���Ă��Ȃ��ꍇ�A�b�����J�E���g�_�E��
				gameStatusPanel.changeGameStatus("�J�n�O(�c��" + String.valueOf((gameStartUnixTime*1000L - nowUnixTimeMillis) / 1000.0) + "�b)");
			}else {
				// 1�Q�[�����X�^�[�g���Ă���ꍇ
				if(!inputInitFlag) {
					// �ŏ��݂̂̓��o��
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
				}else if(nextTurnStartTime - nowUnixTime < 0){
					// �^�[�����̓��o��
					System.out.println("Turn Input:" + String.valueOf(nextTurnStartTime) + "/" + String.valueOf(nowUnixTime));
					Field nowField = checkGameStatus(net);
					if(nowField == null) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					inputTurn(nowField);
					outputSolver(net, nowField);
					nextTurnStartTime = gameStartUnixTime + nowField.turn * ((matchData.turnMillis + matchData.intervalMillis) / 1000L);
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
