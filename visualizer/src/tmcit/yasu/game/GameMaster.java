package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayList;

import tmcit.yasu.data.GameManageData;
import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.player.ExecPlayer;
import tmcit.yasu.player.Player;
import tmcit.yasu.ui.game.GameFrame;
import tmcit.yasu.ui.game.GameMainPanel;
import tmcit.yasu.ui.game.GamePaintPanel;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;
import tmcit.yasu.util.LogManager;

public class GameMaster implements Runnable{
	private GameManageData gameManageData;

	// UI
	private GameMainPanel gamePanel;

	private GameData gameData;
	private Player myPlayer, rivalPlayer;
	private Point highlightPoint;

	// now game data
	private TurnData nowTurnData;
	private ArrayList<String> myPlayerActions, rivalPlayerActions;

	// util
	private LogManager logManager;
	
	// setting
	private int sleepTime;
	private boolean showActionFlag;

	public GameMaster(GameData gameData0, Player myPlayer0, Player rivalPlayer0
			, GameMainPanel gamePanel0, GameManageData gameManageData0, FileManager fileManager,
			int sleepTime0, boolean showActionFlag0) {
		gameData = gameData0;
		myPlayer = myPlayer0;
		rivalPlayer = rivalPlayer0;
		gamePanel = gamePanel0;
		gameManageData = gameManageData0;
		sleepTime = sleepTime0;
		showActionFlag = showActionFlag0;

		init(fileManager);
	}

	private void init(FileManager fileManager) {
		myPlayerActions = new ArrayList<>();
		rivalPlayerActions = new ArrayList<>();
		highlightPoint = new Point(-1, -1);
		nowTurnData = new TurnData(gameData);
		logManager = new LogManager(fileManager);
		logManager.logGameData(gameData);
	}

	private void firstInput() {
		myPlayer.input(String.valueOf(gameData.getMaxTurn()));
		myPlayer.input(String.valueOf(gameData.getMapWidth()));
		myPlayer.input(String.valueOf(gameData.getMapHeight()));
		myPlayer.input(String.valueOf(gameData.getHowPlayer()));
		rivalPlayer.input(String.valueOf(gameData.getMaxTurn()));
		rivalPlayer.input(String.valueOf(gameData.getMapWidth()));
		rivalPlayer.input(String.valueOf(gameData.getMapHeight()));
		rivalPlayer.input(String.valueOf(gameData.getHowPlayer()));

		int[][] mapScore = gameData.getMapScore();

		for(int i = 0;i < gameData.getMapHeight();i++) {
			String line = "";
			for(int j = 0;j < gameData.getMapWidth();j++) {
				if(j != gameData.getMapWidth()-1) line += String.valueOf(mapScore[j][i]) + " ";
				else line += String.valueOf(mapScore[j][i]);
			}
			myPlayer.input(line);
			rivalPlayer.input(line);
		}
	}

	private void turnInput() {
		myPlayer.input(String.valueOf(nowTurnData.getNowTurn()));
		rivalPlayer.input(String.valueOf(nowTurnData.getNowTurn()));

		int[][] territoryMap = nowTurnData.getTerritoryMap();
		for(int i = 0;i < gameData.getMapHeight();i++) {
			String myLine = "", rivalLine = "";
			for(int j = 0;j < gameData.getMapWidth();j++) {
				myLine += String.valueOf(territoryMap[j][i]);

				if(territoryMap[j][i] == Constant.MY_TERRITORY) {
					rivalLine += String.valueOf(Constant.RIVAL_TERRITORY) + " ";
				}else if(territoryMap[j][i] == Constant.RIVAL_TERRITORY) {
					rivalLine += String.valueOf(Constant.MY_TERRITORY) + " ";
				}else if(territoryMap[j][i] == Constant.NONE_TERRITORY) {
					rivalLine += String.valueOf(Constant.NONE_TERRITORY) + " ";
				}

				if(j != gameData.getMapWidth()-1) {
					myLine += " ";
					rivalLine += " ";
				}
			}
			myPlayer.input(myLine);
			rivalPlayer.input(rivalLine);
		}

		ArrayList<Point> nowMyPlayers = nowTurnData.getMyPlayers();
		ArrayList<Point> nowRivalPlayer = nowTurnData.getRivalPlayers();
		int n = gameData.getHowPlayer();
		for(int i = 0;i < n;i++) {
			String myPosition = String.valueOf(nowMyPlayers.get(i).x) + " " + String.valueOf(nowMyPlayers.get(i).y);
			String rivalPosition = String.valueOf(nowRivalPlayer.get(i).x) + " " + String.valueOf(nowRivalPlayer.get(i).y);
			myPlayer.input(myPosition);
			rivalPlayer.input(rivalPosition);
		}
		for(int i = 0;i < n;i++) {
			String myPosition = String.valueOf(nowMyPlayers.get(i).x) + " " + String.valueOf(nowMyPlayers.get(i).y);
			String rivalPosition = String.valueOf(nowRivalPlayer.get(i).x) + " " + String.valueOf(nowRivalPlayer.get(i).y);
			myPlayer.input(rivalPosition);
			rivalPlayer.input(myPosition);
		}
	}
	
	private void resetPlayerActions() {
		myPlayerActions = new ArrayList<>();
		rivalPlayerActions = new ArrayList<>();
	}

	private void getPlayerActions(Player player, boolean isMyPlayer){
		for(int i = 0;i < gameData.getHowPlayer();i++) {
			ArrayList<Point> playersPoint;
			if(isMyPlayer) {
				playersPoint = nowTurnData.getMyPlayers();
			}else {
				playersPoint = nowTurnData.getRivalPlayers();
			}
			highlightPoint = playersPoint.get(i);
			paintTurnData(myPlayerActions, rivalPlayerActions, highlightPoint);
			String action = player.getAction();
			if(isMyPlayer) {
				myPlayerActions.add(action);
			}else {
				rivalPlayerActions.add(action);
			}
			highlightPoint = new Point(-1, -1);
			paintTurnData(myPlayerActions, rivalPlayerActions, highlightPoint);
		}
	}

	private void paintTurnData(ArrayList<String> myPlayerCmds, ArrayList<String> rivalPlayerCmds, Point hilightPoint) {
		gamePanel.reflectGameData(gameData, nowTurnData, myPlayerCmds, rivalPlayerCmds, hilightPoint);
	}

	private void endGameMaster() {
		if(myPlayer instanceof ExecPlayer) {
			((ExecPlayer)myPlayer).endProcess();
		}
		if(rivalPlayer instanceof ExecPlayer) {
			((ExecPlayer)rivalPlayer).endProcess();
		}
		gameManageData.minusRunningGame();
	}

	@Override
	public void run() {
		System.out.println("[SYSTEM]:Start GameMaster.");
		paintTurnData(new ArrayList<>(), new ArrayList<>(), highlightPoint);
		firstInput();
		System.out.println("[SYSTEM]:End first input.");

		while(true) {
			turnInput();
			// get players actions
			resetPlayerActions();
			System.out.println("[SYSTEM]:End input data.");
			getPlayerActions(myPlayer, true);
			System.out.println("[SYSTEM]:End my player action.");
			getPlayerActions(rivalPlayer, false);
			System.out.println("[SYSTEM]:End rival player action.");

			// reflect actions
			TurnData nextTurnData = nowTurnData.nextTurn(myPlayerActions, rivalPlayerActions);
			System.out.println("[SYSTEM]:End calc turn.");
			
			// write log and paint
			logManager.logTurnAction(myPlayerActions, rivalPlayerActions);
			
			if(showActionFlag) paintTurnData(myPlayerActions, rivalPlayerActions, highlightPoint);
      
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nowTurnData = nextTurnData;
			paintTurnData(new ArrayList<>(), new ArrayList<>(), highlightPoint);

			// exit
			System.out.println("[SYSTEM]:End Turn[" + nowTurnData.getNowTurn() + "]");
			if(nowTurnData.getNowTurn() > gameData.getMaxTurn()) {
				break;
			}
		}

		endGameMaster();
	}
}
