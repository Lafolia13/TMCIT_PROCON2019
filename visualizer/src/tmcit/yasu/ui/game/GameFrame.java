package tmcit.yasu.ui.game;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.util.Constant;

public class GameFrame extends JFrame{
	private PaintGameData gameData;
	private GamePanel gamePanel;

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

		gamePanel = new GamePanel(gameData, false);
		gamePanel.setBounds(10, 10, Constant.MAP_SIZE+10, Constant.MAP_SIZE+10);

		this.add(gamePanel);
	}

	public void reflectGameData(PaintGameData newPaintGameData) {
		gamePanel.reflectGameData(newPaintGameData);
	}
}
