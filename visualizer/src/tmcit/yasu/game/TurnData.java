package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

import tmcit.yasu.data.ActionData;
import tmcit.yasu.data.ScoreData;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.Pair;

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

	public TurnData(ArrayList<Point> myPlayers0, ArrayList<Point> rivalPlayers0, int[][] newTerritoryMap, TurnData beforeTurnData) {
		myPlayers = new ArrayList<Point>(myPlayers0);
		rivalPlayers = new ArrayList<Point>(rivalPlayers0);
		mapWidth = beforeTurnData.mapWidth;
		mapHeight = beforeTurnData.mapHeight;
		scoreMap = beforeTurnData.scoreMap;
		territoryMap = newTerritoryMap;
		nowTurn = beforeTurnData.nowTurn+1;
	}

	void init() {
		nowTurn = 0;

		territoryMap = new int[mapWidth][mapHeight];
		for(int i = 0;i < mapHeight;i++) {
			for(int j = 0;j < mapWidth;j++) {
				territoryMap[j][i] = Constant.NONE_TERRITORY;
			}
		}
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
		int[] teams = new int[2];
		teams[0] = Constant.MY_TERRITORY;
		teams[1] = Constant.RIVAL_TERRITORY;
		for(int nowY = 0;nowY < mapHeight;nowY++){
			for(int nowX = 0;nowX < mapWidth;nowX++){
				for(int nowTeamIndex = 0;nowTeamIndex <= 1;nowTeamIndex++){
					if(used[nowX][nowY][nowTeamIndex]){
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
						if(used[nowP.x][nowP.y][nowTeamIndex]) continue;
						if(territoryMap[nowP.x][nowP.y] == teams[nowTeamIndex]) continue;
						used[nowP.x][nowP.y][nowTeamIndex] = true;
						nowScore += Math.abs(scoreMap[nowP.x][nowP.y]);

						for(int i = 0;i < 4;i++){
							int tx = nowP.x + Constant.DIR4_X[i], ty = nowP.y + Constant.DIR4_Y[i];
							if(tx < 0 || ty < 0 || tx >= mapWidth || ty >= mapHeight){
								nowScore = 0;
								noneScoreFlag = true;
								continue;
							}

							if(territoryMap[tx][ty] == teams[nowTeamIndex]) continue;
							if(used[tx][ty][nowTeamIndex] == true) continue;
							que.add(new Point(tx, ty));
						}
					}
					if(noneScoreFlag) nowScore = 0;
					if(teams[nowTeamIndex] == Constant.MY_TERRITORY){
						myTerritoryScore += nowScore;
					}else if(teams[nowTeamIndex] == Constant.RIVAL_TERRITORY){
						rivalTerritoryScore += nowScore;
					}
				}
			}
		}

		return new ScoreData(myTerritoryScore, myTileScore, rivalTerritoryScore, rivalTileScore);
	}

	private ActionData getActionData(int index, ArrayList<String> playerActions, boolean isMyPlayer) {
		String nowActionStr = playerActions.get(index);
		Point nowPoint;
		char command = nowActionStr.charAt(0), agentChar;
		int way, targetX, targetY, playerNum;

		if(isMyPlayer) {
			nowPoint = myPlayers.get(index);
			agentChar = (char)('a'+index);
			playerNum = Constant.MY_TERRITORY;
		}else {
			nowPoint = rivalPlayers.get(index);
			agentChar = (char)('A'+index);
			playerNum = Constant.RIVAL_TERRITORY;
		}

		if(command == 'n') {
			way = 4;
		}else {
			String wayStr = "" + nowActionStr.charAt(1);
			way = Integer.valueOf(wayStr);
		}
		targetX = nowPoint.x + Constant.DIR_X[way];
		targetY = nowPoint.y + Constant.DIR_Y[way];

		ActionData nowActionData = new ActionData(playerNum, way, targetX, targetY, command, agentChar, index);
		return nowActionData;
	}

	// 行動のリスト(文字列)をActionDataのリストに変換
	private ArrayList<ActionData> convertToActionDataList(ArrayList<String> myPlayerActions, ArrayList<String> rivalPlayerActions){
		ArrayList<ActionData> ret = new ArrayList<ActionData>();
		for(int i = 0;i < myPlayerActions.size();i++) {
			ret.add(getActionData(i, myPlayerActions, true));
		}
		for(int i = 0;i < rivalPlayerActions.size();i++) {
			ret.add(getActionData(i, rivalPlayerActions, false));
		}
		return ret;
	}

	// ActionDataを停留に変更
	private ActionData changeStay(int index, ActionData actionData) {
		Point stayPoint;
		if(actionData.playerNum == Constant.MY_TERRITORY) {
			stayPoint = new Point(myPlayers.get(actionData.agentIndex));
		}else {
			stayPoint = new Point(rivalPlayers.get(actionData.agentIndex));
		}
		ActionData ret = new ActionData(actionData.playerNum, 4, stayPoint.x, stayPoint.y, 'n', actionData.agentChar, actionData.agentIndex);
		return ret;
	}

	// 移動先に他の停留や除去をするエージェントがいるかチェック
	private boolean checkOtherAgent(ArrayList<ActionData> allActionDataList, int checkIndex) {
		ActionData checkActionData = allActionDataList.get(checkIndex);
		int playerSize = myPlayers.size();

		if(checkActionData.playerNum == Constant.MY_TERRITORY) {
			for(int i = 0;i < playerSize;i++) {
				Point nowRivalP = rivalPlayers.get(i);
				ActionData nowRivalActionData = allActionDataList.get(playerSize+i);
				if(checkActionData.target.x == nowRivalP.x && checkActionData.target.y == nowRivalP.y) {
					if(nowRivalActionData.command == 'e' || nowRivalActionData.command == 'n') {
						// 移動先が仲間じゃなく、除去か停留をしている。
						return true;
					}
				}
			}

			for(int i = 0;i < playerSize;i++) {
				Point nowMyP = myPlayers.get(i);
				ActionData nowMyActionData = allActionDataList.get(i);
				if(checkActionData.agentChar == nowMyActionData.agentChar) continue;
				if(checkActionData.target.x == nowMyP.x && checkActionData.target.y == nowMyP.y) {
					if(nowMyActionData.command == 'e' || nowMyActionData.command == 'n') {
						// 移動先が仲間じゃなく、除去か停留をしている。
						return true;
					}
				}
			}
		}else {
			for(int i = 0;i < playerSize;i++) {
				Point nowMyP = myPlayers.get(i);
				ActionData nowMyActionData = allActionDataList.get(i);
				if(checkActionData.target.x == nowMyP.x && checkActionData.target.y == nowMyP.y) {
					if(nowMyActionData.command == 'e' || nowMyActionData.command == 'n') {
						// 移動先が仲間じゃなく、除去か停留をしている。
						return true;
					}
				}
			}

			for(int i = 0;i < playerSize;i++) {
				Point nowRivalP = rivalPlayers.get(i);
				ActionData nowRivalActionData = allActionDataList.get(playerSize+i);
				if(checkActionData.agentChar == nowRivalActionData.agentChar) continue;
				if(checkActionData.target.x == nowRivalP.x && checkActionData.target.y == nowRivalP.y) {
					if(nowRivalActionData.command == 'e' || nowRivalActionData.command == 'n') {
						// 移動先が仲間じゃなく、除去か停留をしている。
						return true;
					}
				}
			}
		}
//		for(int i = 0;i < allActionDataList.size();i++) {
//			if(i == checkIndex) continue;
//
//			ActionData nowData = allActionDataList.get(i);
//			if(checkActionData.target.x == nowData.target.x && checkActionData.target.y == nowData.target.y) {
//				return true;
//			}
//		}
		return false;
	}

	// 移動(除去)できないものをチェックして停留に変換(相手の領域or範囲外or対象にエージェントがいる)
	private Pair<ArrayList<ActionData>, Boolean> changeBadActionToStay(ArrayList<ActionData> allActionDataList){
		boolean outFlag = false;
		for(int i = 0;i < allActionDataList.size();i++) {
			ActionData nowActionData = allActionDataList.get(i);

			if(nowActionData.command == 'w' || nowActionData.command == 'e') {
				// 範囲外
				if(nowActionData.target.x < 0 || nowActionData.target.y < 0 || nowActionData.target.x >= mapWidth || nowActionData.target.y >= mapHeight) {
					allActionDataList.set(i, changeStay(i, nowActionData));
					outFlag = true;
					continue;
				}
				// 相手の領域
				int targetTerritory = territoryMap[nowActionData.target.x][nowActionData.target.y];
				if(nowActionData.command == 'w' && targetTerritory != Constant.NONE_TERRITORY && targetTerritory != nowActionData.playerNum) {
					allActionDataList.set(i, changeStay(i, nowActionData));
					outFlag = true;
					continue;
				}
			}
			// 移動する先にエージェントがいて衝突するか判定
			// 移動先のエージェントが、移動コマンドじゃない場合のみ
			if(nowActionData.command == 'w') {
				boolean checkFlag = checkOtherAgent(allActionDataList, i);
				if(checkFlag) {
					allActionDataList.set(i, changeStay(i, nowActionData));
					outFlag = true;
					continue;
				}
			}
		}
		return new Pair<ArrayList<ActionData>, Boolean>(allActionDataList, outFlag);
	}

	// 2回以上選ばれた場所をターゲットにしているエージェントを停留に変更
	private Pair<ArrayList<ActionData>, Boolean> changeCollisionAgentToStay(int[][] howSelect, ArrayList<ActionData> allActionDataList){
		boolean outFlag = false;
		for(int i = 0;i < allActionDataList.size();i++) {
			ActionData nowActionData = allActionDataList.get(i);
			if(howSelect[nowActionData.target.x][nowActionData.target.y] >= 2) {
				allActionDataList.set(i, changeStay(i, nowActionData));
				outFlag = true;
			}
		}
		return new Pair<ArrayList<ActionData>, Boolean>(allActionDataList, outFlag);
	}

	// 移動先や除去先にされている場所のカウント
	private int[][] countSelect(ArrayList<ActionData> allActionDataList){
		int[][] howSelect = new int[mapWidth][mapHeight];
		for(int i = 0;i < allActionDataList.size();i++) {
			ActionData nowActionData = allActionDataList.get(i);
			howSelect[nowActionData.target.x][nowActionData.target.y]++;
		}
		return howSelect;
	}

	// ルールに則ってコマンドを変更
	private ArrayList<ActionData> checkAction(ArrayList<ActionData> allActionDataList){
		int[][] howSelect = new int[mapWidth][mapHeight];

		howSelect = countSelect(allActionDataList);
		Pair<ArrayList<ActionData>, Boolean> ret = changeCollisionAgentToStay(howSelect, allActionDataList);
		ret = changeBadActionToStay(allActionDataList);
		allActionDataList = ret.first;
		howSelect = countSelect(allActionDataList);

		boolean changeFlag = true;
		while(changeFlag) {
			changeFlag = false;

			ret = changeCollisionAgentToStay(howSelect, allActionDataList);
			allActionDataList = ret.first;
			if(ret.second) changeFlag = true;
			ret = changeBadActionToStay(allActionDataList);
			allActionDataList = ret.first;
			if(ret.second) changeFlag = true;
			howSelect = countSelect(allActionDataList);
		}

		return allActionDataList;
	}

	// コマンドを反映
	private TurnData doCommand(ArrayList<ActionData> allActionDataList) {
		// copy data
		int[][] newTerritoryMap = new int[mapWidth][mapHeight];
		for(int i = 0;i < mapHeight;i++) {
			for(int j = 0;j < mapWidth;j++) {
				newTerritoryMap[j][i] = territoryMap[j][i];
			}
		}
		ArrayList<Point> newMyPlayers = new ArrayList<Point>(myPlayers);
		ArrayList<Point> newRivalPlayers = new ArrayList<Point>(rivalPlayers);

		// make new turn data
		// 移動と除去を反映
		for(int i = 0;i < allActionDataList.size();i++) {
			ActionData nowActionData = allActionDataList.get(i);
			if(nowActionData.command == 'w') {
				if(i < allActionDataList.size()/2) {
					newMyPlayers.set(nowActionData.agentIndex, new Point(nowActionData.target.x, nowActionData.target.y));
				}else {
					newRivalPlayers.set(nowActionData.agentIndex, new Point(nowActionData.target.x, nowActionData.target.y));
				}
			}else if(nowActionData.command == 'e') {
				newTerritoryMap[nowActionData.target.x][nowActionData.target.y] = Constant.NONE_TERRITORY;
			}
		}
		// エージェントのいる場所の領域を変更
		for(Point nowMyPoint:newMyPlayers) {
			newTerritoryMap[nowMyPoint.x][nowMyPoint.y] = Constant.MY_TERRITORY;
		}for(Point nowRivalPoint:newRivalPlayers) {
			newTerritoryMap[nowRivalPoint.x][nowRivalPoint.y] = Constant.RIVAL_TERRITORY;
		}

		return new TurnData(newMyPlayers, newRivalPlayers, newTerritoryMap, this);
	}

	// コマンドをマップに反映
	public TurnData nextTurn(ArrayList<String> myPlayerActions, ArrayList<String> rivalPlayerActions) {
		// コマンドをチェック
		ArrayList<ActionData> allActionDataList = convertToActionDataList(myPlayerActions, rivalPlayerActions);
		allActionDataList = checkAction(allActionDataList);
		System.out.println("[SYSTEM]:End checkAction();");

		// コマンドを実行
		TurnData nextTurnData = doCommand(allActionDataList);
		System.out.println("[SYSTEM]:End doCommand();");
		return nextTurnData;
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


	// debug用
	private void printSelectMap(int[][] howSelect) {
		System.out.println("----- print select map -----");
		for(int i = 0;i < mapHeight;i++) {
			for(int j = 0;j < mapWidth;j++) {
				System.out.print(String.valueOf(howSelect[j][i]) + " ");
			}
			System.out.println();
		}
		System.out.println("----------------------------");
	}
}
