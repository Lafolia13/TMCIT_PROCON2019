package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayList;

import tmcit.yasu.util.Constant;

public class TurnData {
	private int nowTurn, mapWidth, mapHeight;
	private int[][] territoryMap;
	private ArrayList<Point> myPlayers, rivalPlayers;

	public TurnData(GameData startGameData) {
		myPlayers = new ArrayList<Point>(startGameData.getMyPlayers());
		rivalPlayers = new ArrayList<Point>(startGameData.getRivalPlayers());
		mapWidth = startGameData.getMapWidth();
		mapHeight = startGameData.getMapHeight();

		init();
	}

	public TurnData(ArrayList<Point> myPlayers0, ArrayList<Point> rivalPlayers0, int mapWidth0, int mapHeight0) {
		myPlayers = new ArrayList<Point>(myPlayers0);
		rivalPlayers = new ArrayList<Point>(rivalPlayers0);
		mapWidth = mapWidth0;
		mapHeight = mapHeight0;

		init();
	}

	void init() {
		nowTurn = 0;

		territoryMap = new int[mapWidth][mapHeight];
		for(Point np:myPlayers) {
			territoryMap[np.x][np.y] = Constant.MY_TERRITORY;
		}
		for(Point np:rivalPlayers) {
			territoryMap[np.x][np.y] = Constant.RIVAL_TERRITORY;
		}
	}

	public int getNowTurn() {
		return nowTurn;
	}

	public int[][] getTerritoryMap() {
		return territoryMap;
	}

	public ArrayList<Point> getMyPlayers() {
		return myPlayers;
	}

	public ArrayList<Point> getRivalPlayers() {
		return rivalPlayers;
	}


}
