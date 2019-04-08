package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayList;

public class GameData {
	private int maxTurn, mapWidth, mapHeight;
	private int[][] mapScore;
	private ArrayList<Point> myPlayers, rivalPlayers;
	
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
	
}
