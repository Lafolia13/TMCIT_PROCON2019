package tmcit.yasu.ui.game;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.util.Constant;

public class GameFrame extends JFrame{
	private PaintGameData gameData;
	private GamePaintPanel gamePaintPanel;

	public GameFrame(PaintGameData gameData0) {
		gameData = gameData0;

		initLayout();
		init();
	}

	public GameFrame(GameData gameData0) {
		int mapWidth = gameData0.getMapWidth();
		int mapHeight = gameData0.getMapHeight();
		int[][] mapScore = gameData0.getMapScore();
		ArrayList<Point> myPlayers = gameData0.getMyPlayers();
		ArrayList<Point> rivalPlayers = gameData0.getRivalPlayers();

		int[][] territoryMap = new int[mapWidth][mapHeight];
		for(int i = 0;i < mapHeight;i++) {
			for(int j = 0;j < mapWidth;j++) {
				territoryMap[j][i] = Constant.NONE_TERRITORY;
			}
		}
		myPlayers.stream().forEach(p -> territoryMap[p.x][p.y] = Constant.MY_TERRITORY);
		rivalPlayers.stream().forEach(p -> territoryMap[p.x][p.y] = Constant.RIVAL_TERRITORY);

		gameData = new PaintGameData(mapWidth, mapHeight, mapScore, territoryMap, myPlayers, rivalPlayers);

		initLayout();
		init();
	}

	private void init() {
		setSize(700, 700);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initLayout() {
		this.setLayout(null);

		gamePaintPanel = new GamePaintPanel(gameData, false);
		gamePaintPanel.setBounds(10, 10, Constant.MAP_SIZE+10, Constant.MAP_SIZE+10);

		this.add(gamePaintPanel);
	}
}
