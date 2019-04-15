package tmcit.yasu.data;

import java.awt.Point;
import java.util.ArrayList;

public class PaintGameData {
	private int mapWidth, mapHeight;
	private int[][] mapScore, territoryMap;
	private ArrayList<Point> myPlayers, rivalPlayers;

	public PaintGameData(int mapWidth0, int mapHeight0, int[][] mapScore0, int[][] territoryMap0
			,ArrayList<Point> myPlayers0, ArrayList<Point> rivalPlayers0) {
		mapWidth = mapWidth0;
		mapHeight = mapHeight0;
		mapScore = mapScore0;
		territoryMap = territoryMap0;
		myPlayers = myPlayers0;
		rivalPlayers = rivalPlayers0;
	}

	// getter
	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int[][] getMapScore(){
		return mapScore;
	}

	public int[][] getTerritoryMap(){
		return territoryMap;
	}

	public ArrayList<Point> getMyPlayers(){
		return myPlayers;
	}

	public ArrayList<Point> getRivalPlayers(){
		return rivalPlayers;
	}
}