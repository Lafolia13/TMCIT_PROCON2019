package tmcit.yasu.main;

import java.awt.Point;
import java.util.ArrayList;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.player.ExecPlayer;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.game.GameFrame;

public class Main {
	
	private static void testExe() {
		String str = "C:\\Users\\yasu\\Desktop\\out.exe";
		ExecPlayer player = new ExecPlayer(str);
		System.out.println(player.getAction());
		player.input("123");
		String ret = player.getAction();
		System.out.println(ret);
	}

	public static void main(String[] args) {
		System.out.println("Hello World");

		int[][] score = new int[10][8];
		int[][] territory = new int[10][8];
		for(int i = 0;i < 10;i++) {
			for(int j = 0;j < 8;j++) {
				score[i][j] = j;
				territory[i][j] = (i+j)%3;
			}
		}
		ArrayList<Point> myPlayers = new ArrayList<Point>();
		ArrayList<Point> rivalPlayers = new ArrayList<Point>();
		myPlayers.add(new Point(0, 0));
		myPlayers.add(new Point(5, 5));
		rivalPlayers.add(new Point(5, 0));
		rivalPlayers.add(new Point(0, 5));

		PaintGameData gameData = new PaintGameData(10, 8, score, territory, myPlayers, rivalPlayers);

		new GameFrame(gameData);
		
		testExe();
	}

}
