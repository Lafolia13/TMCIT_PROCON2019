package tmcit.yasu.game;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;import javax.swing.plaf.basic.BasicBorders.SplitPaneBorder;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.util.Constant;

public class GameLogMaster {
	private File logFile;
	
	// data
	private int maxTurn, width, height, howPlayer;
	private int[][] scoreMap;
	private ArrayList<Point> myPlayers, rivalPlayers;
	private ArrayList<String> myPlayerCmds, rivalPlayerCmds;
	
	// logData
	private ArrayList<PaintGameData> logPaintDataList;
	
	public GameLogMaster(File logFile0) {
		logFile = logFile0;
		init();
		try {
			readFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		makeLogTurnData();
	}
	
	private void init() {
		myPlayers = new ArrayList<>();
		rivalPlayers = new ArrayList<>();
		myPlayerCmds = new ArrayList<>();
		rivalPlayerCmds = new ArrayList<>();
		logPaintDataList = new ArrayList<>();
	}
	
	private void readFile() throws IOException {
		List<String> lines = Files.readAllLines(logFile.toPath());
		
		for(int i = 0;i < lines.size();i++) {
			String line = lines.get(i);
			if(i == 0) {
				maxTurn = Integer.valueOf(line);
			}else if(i == 1) {
				width = Integer.valueOf(line);
			}else if(i == 2) {
				height = Integer.valueOf(line);
				scoreMap = new int[width][height];
			}else if(i <= 2 + height) {
				// マップスコアの読み込み
				String[] splits = line.split(" ");
				for(int j = 0;j < width;j++) {
					scoreMap[j][i-3] = Integer.valueOf(splits[j]);
				}
			}else if(i == 3 + height) {
				howPlayer = Integer.valueOf(line);
			}else if(i <= 3 + height + howPlayer) {
				// MyPlayerの初期位置の読み込み
				String[] splits = line.split(" ");
				myPlayers.add(new Point(Integer.valueOf(splits[0]), Integer.valueOf(splits[1])));
			}else if(i <= 3 + height + howPlayer*2) {
				// RivalPlayerの初期位置の読み込み
				String[] splits = line.split(" ");
				rivalPlayers.add(new Point(Integer.valueOf(splits[0]), Integer.valueOf(splits[1])));
			}else {
				int index = i - (3 + height + howPlayer*2);
				if(index % 8 < 4) {
					myPlayerCmds.add(line);
				}else {
					rivalPlayerCmds.add(line);
				}
			}
		}
	}
	
	private TurnData makeFirstTurnData() {
		TurnData firstTurnData = new TurnData(new GameData(maxTurn, width, height, scoreMap, myPlayers, rivalPlayers));
		return firstTurnData;
	}
	
	private void makeLogTurnData() {
		TurnData nowTurnData = makeFirstTurnData();
		logPaintDataList.add(new PaintGameData(width, height, scoreMap, nowTurnData.getTerritoryMap(), nowTurnData.getMyPlayers(), nowTurnData.getRivalPlayers()));
		
		for(int nowTurn = 0;nowTurn < maxTurn;nowTurn++) {
			ArrayList<String> nowMyCmds = new ArrayList<>();
			ArrayList<String> nowRivalCmds = new ArrayList<>();
			
			for(int i = 0;i < howPlayer;i++) {
				nowMyCmds.add(myPlayerCmds.get(nowTurn*howPlayer + i));
				nowRivalCmds.add(rivalPlayerCmds.get(nowTurn*howPlayer + i));
			}
			
			TurnData nextTurnData = nowTurnData.nextTurn(nowMyCmds, nowRivalCmds);
			nowTurnData = nextTurnData;
			logPaintDataList.add(new PaintGameData(width, height, scoreMap, nowTurnData.getTerritoryMap(), nowTurnData.getMyPlayers(), nowTurnData.getRivalPlayers()));
		}
	}
	
	// getter
	public ArrayList<PaintGameData> getPaintGameDataList(){
		return logPaintDataList;
	}
}
