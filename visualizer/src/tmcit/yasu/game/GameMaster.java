package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayList;

import tmcit.yasu.player.Player;

public class GameMaster implements Runnable{
	private GameData gameData;
	private Player myPlayer, rivalPlayer;

	// now game data
	private TurnData nowTurnData;

	public GameMaster(GameData gameData0, Player myPlayer0, Player rivalPlayer0) {
		gameData = gameData0;
		myPlayer = myPlayer0;
		rivalPlayer = rivalPlayer0;

		init();
	}

	private void init() {
		nowTurnData = new TurnData(gameData);
	}

	private void firstInput() {
		myPlayer.input(String.valueOf(gameData.getMaxTurn()));
		myPlayer.input(String.valueOf(gameData.getMapWidth()));
		myPlayer.input(String.valueOf(gameData.getMapHeight()));
		rivalPlayer.input(String.valueOf(gameData.getMaxTurn()));
		rivalPlayer.input(String.valueOf(gameData.getMapWidth()));
		rivalPlayer.input(String.valueOf(gameData.getMapHeight()));

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
			String line = "";
			for(int j = 0;j < gameData.getMapWidth();j++) {
				if(j != gameData.getMapWidth()-1) line += String.valueOf(territoryMap[j][i]) + " ";
				else line += String.valueOf(territoryMap[j][i]);
			}
			myPlayer.input(line);
			rivalPlayer.input(line);
		}

		ArrayList<Point> nowMyPlayers = nowTurnData.getMyPlayers();
		ArrayList<Point> nowRivalPlayer = nowTurnData.getRivalPlayers();
		int n = nowMyPlayers.size();
		myPlayer.input(String.valueOf(n));
		rivalPlayer.input(String.valueOf(n));
		for(int i = 0;i < n;i++) {
			String inMy = String.valueOf(nowMyPlayers.get(i).x) + " " + String.valueOf(nowMyPlayers.get(i).y);
			String inRival = String.valueOf(nowRivalPlayer.get(i).x) + " " + String.valueOf(nowRivalPlayer.get(i).y);
			myPlayer.input(inMy);
			rivalPlayer.input(inRival);
		}
	}

	@Override
	public void run() {
		firstInput();
		turnInput();
	}
}
