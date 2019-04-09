package tmcit.yasu.test;

import java.awt.Point;
import java.util.ArrayList;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.game.GameMaster;
import tmcit.yasu.player.ExecPlayer;
import tmcit.yasu.ui.game.GameFrame;

public class GameTester {

	private GameData makeGameData() {
		int[][] score = new int[10][8];
		for(int i = 0;i < 10;i++) {
			for(int j = 0;j < 8;j++) {
				score[i][j] = j;
			}
		}
		ArrayList<Point> myPlayers = new ArrayList<Point>();
		ArrayList<Point> rivalPlayers = new ArrayList<Point>();
		myPlayers.add(new Point(0, 0));
		myPlayers.add(new Point(5, 5));
		rivalPlayers.add(new Point(5, 0));
		rivalPlayers.add(new Point(0, 5));

		GameData gameData = new GameData(60, 10, 8, score, myPlayers, rivalPlayers);

		return gameData;
	}

	private ExecPlayer getTestExecPlayer() {
		String cmd = "E:\\\\Users\\\\yasu\\\\Desktop\\\\a.exe";
		ExecPlayer player = new ExecPlayer(cmd);
		return player;
	}

	public 	void startTest() {
		GameData gameData = makeGameData();
		ExecPlayer myPlayer = getTestExecPlayer();
		ExecPlayer rivalPlayer = getTestExecPlayer();

		GameFrame gameFrame = new GameFrame(new PaintGameData(gameData.getMapWidth(), gameData.getMapHeight()
				, gameData.getMapScore(), new int[gameData.getMapWidth()][gameData.getMapHeight()], gameData.getMyPlayers(), gameData.getRivalPlayers()));

		GameMaster master = new GameMaster(gameData, myPlayer, rivalPlayer, gameFrame);
		master.run();
	}
}
