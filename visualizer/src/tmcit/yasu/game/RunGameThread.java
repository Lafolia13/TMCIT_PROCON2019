package tmcit.yasu.game;

import java.awt.Point;
import java.util.ArrayList;

import tmcit.yasu.data.GameManageData;
import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.player.Player;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.SettingPanel;
import tmcit.yasu.ui.game.GameFrame;
import tmcit.yasu.ui.game.GameMainPanel;
import tmcit.yasu.ui.game.GamePaintPanel;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class RunGameThread extends Thread{
	private MainFrame mainFrame;
	private SettingPanel settingPanel;
	private Player myPlayer, rivalPlayer;
	private GameData gameData;
	private GameManageData gameManageData;
	private FileManager fileManager;

	public RunGameThread(MainFrame mainFrame0, Player myPlayer0, Player rivalPlayer0
			, GameData gameData0, GameManageData gameManageData0, FileManager fileManager0
			, SettingPanel settingPanel0) {
		mainFrame = mainFrame0;
		settingPanel = settingPanel0;
		myPlayer = myPlayer0;
		rivalPlayer = rivalPlayer0;
		gameData = gameData0;
		gameManageData = gameManageData0;
		fileManager = fileManager0;
	}

	private PaintGameData getInitPaintGameData() {
		int mapWidth = gameData.getMapWidth();
		int mapHeight = gameData.getMapHeight();
		int[][] mapScore = gameData.getMapScore();
		ArrayList<Point> myPlayers = gameData.getMyPlayers();
		ArrayList<Point> rivalPlayers = gameData.getRivalPlayers();

		int[][] territoryMap = new int[mapWidth][mapHeight];
		for(int i = 0;i < mapHeight;i++) {
			for(int j = 0;j < mapWidth;j++) {
				territoryMap[j][i] = Constant.NONE_TERRITORY;
			}
		}
		myPlayers.stream().forEach(p -> territoryMap[p.x][p.y] = Constant.MY_TERRITORY);
		rivalPlayers.stream().forEach(p -> territoryMap[p.x][p.y] = Constant.RIVAL_TERRITORY);

		PaintGameData paintGameData = new PaintGameData(mapWidth, mapHeight, mapScore, territoryMap, myPlayers, rivalPlayers);
		return paintGameData;
	}

	@Override
	public void run() {
		GameMainPanel gameMainPanel = new GameMainPanel(mainFrame, getInitPaintGameData(), myPlayer, rivalPlayer);

		mainFrame.addTabbedPanel("ÉQÅ[ÉÄ", gameMainPanel);

		GameMaster gameMaster = new GameMaster(gameData, myPlayer, rivalPlayer, gameMainPanel, gameManageData, fileManager
				, settingPanel.getSleepTime(), settingPanel.isSelectedShowActionRadioButton());
		gameMaster.run();
	}
}
