package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayList;

import tmcit.yasu.player.Player;

public class GameMaster {
	private GameData gameData;
	private Player myPlayer, rivalPlayer;
	
	// start game data
	private int mapWidth, mapHeight, maxTurn;
	private int[][] mapScore;
	private ArrayList<Point> startMyPlayers, startRivalPlayers;
	
	// now game data
	
	
	public GameMaster(GameData gameData0, Player myPlayer0, Player rivalPlayer0) {
		gameData = gameData0;
		myPlayer = myPlayer0;
		rivalPlayer = rivalPlayer0;
	}
	
	private void init() {
		mapWidth = gameData.getMapWidth();
		mapHeight = gameData.getMapHeight();
		maxTurn = gameData.getMaxTurn();
		mapScore = gameData.getMapScore();
		startMyPlayers = gameData.getMyPlayers();
		startRivalPlayers = gameData.getRivalPlayers();
	}
}
