package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayList;

public class GameData {
	private int maxTurn, mapWidth, mapHeight;
	private int[][] mapScore;
	private ArrayList<Point> myPlayers, rivalPlayers;

	public GameData(int maxTurn0, int mapWidth0, int mapHeight0, int[][] mapScore0,
			ArrayList<Point> myPlayers0, ArrayList<Point> rivalPlayers0) {
		maxTurn = maxTurn0;
		mapWidth = mapWidth0;
		mapHeight = mapHeight0;
		mapScore = mapScore0;
		myPlayers = myPlayers0;
		rivalPlayers = rivalPlayers0;
	}

	public int getMaxTurn() {
		return maxTurn;
	}
	public int getMapWidth() {
		return mapWidth;
	}
	public int getMapHeight() {
		return mapHeight;
	}
	public int[][] getMapScore() {
		return mapScore;
	}
	public ArrayList<Point> getMyPlayers() {
		return myPlayers;
	}
	public ArrayList<Point> getRivalPlayers() {
		return rivalPlayers;
	}
	public int getHowPlayer() {
		return myPlayers.size();
	}
}
