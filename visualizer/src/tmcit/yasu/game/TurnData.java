package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import tmcit.yasu.data.ScoreData;
import tmcit.yasu.util.Constant;

public class TurnData {
	private int nowTurn, mapWidth, mapHeight;
	private int[][] territoryMap, scoreMap;
	private ArrayList<Point> myPlayers, rivalPlayers;

	public TurnData(GameData startGameData) {
		myPlayers = new ArrayList<Point>(startGameData.getMyPlayers());
		rivalPlayers = new ArrayList<Point>(startGameData.getRivalPlayers());
		mapWidth = startGameData.getMapWidth();
		mapHeight = startGameData.getMapHeight();
		scoreMap = startGameData.getMapScore();

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

	public ScoreData calcScore() {
		int myTerritoryScore = 0, myTileScore = 0, rivalTerritoryScore = 0, rivalTileScore = 0;

		// tile score
		for(int i = 0;i < mapHeight;i++) {
			for(int j = 0;j < mapWidth;j++) {
				if(territoryMap[j][i] == Constant.MY_TERRITORY) {
					myTileScore += scoreMap[j][i];
				}else if(territoryMap[j][i] == Constant.RIVAL_TERRITORY) {
					rivalTileScore += scoreMap[j][i];
				}
			}
		}

		// territory score
		boolean[][][] used = new boolean[21][21][3];
		for(int nowY = 0;nowY < mapHeight;nowY++){
			for(int nowX = 0;nowX < mapWidth;nowX++){
				for(int nowTeam = 1;nowTeam <= 2;nowTeam++){
					if(used[nowX][nowY][nowTeam]){
						continue;
					}
					int nowScore = 0;
					boolean noneScoreFlag = false;
					Queue<Point> que = new ArrayDeque<>();
					que.add(new Point(nowX, nowY));
					while(!que.isEmpty()){
						Point nowP = que.poll();

						if(nowP.x < 0 || nowP.y < 0 || nowP.x >= mapWidth || nowP.y >= mapHeight){
							nowScore = 0;
							noneScoreFlag = true;
							continue;
						}
						if(used[nowP.x][nowP.y][nowTeam]) continue;
						if(territoryMap[nowP.x][nowP.y] == nowTeam) continue;
						used[nowP.x][nowP.y][nowTeam] = true;
						nowScore += Math.abs(scoreMap[nowP.x][nowP.y]);

						for(int i = 0;i < 4;i++){
							int tx = nowP.x + Constant.DIR4_X[i], ty = nowP.y + Constant.DIR4_Y[i];
							if(tx < 0 || ty < 0 || tx >= mapWidth || ty >= mapHeight){
								nowScore = 0;
								noneScoreFlag = true;
								continue;
							}

							if(territoryMap[tx][ty] == nowTeam) continue;
							if(used[tx][ty][nowTeam] == true) continue;
							que.add(new Point(tx, ty));
						}
					}
					if(noneScoreFlag) nowScore = 0;
					if(nowTeam == 1){
						myTerritoryScore += nowScore;
					}else if(nowTeam == 2){
						rivalTerritoryScore += nowScore;
					}
				}
			}
		}

		return new ScoreData(myTerritoryScore, myTileScore, rivalTerritoryScore, rivalTileScore);
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
