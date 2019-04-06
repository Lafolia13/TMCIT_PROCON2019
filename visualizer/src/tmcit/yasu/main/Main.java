package tmcit.yasu.main;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.game.GameFrame;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World");

		int[][] score = new int[10][8];
		for(int i = 0;i < 10;i++) {
			for(int j = 0;j < 8;j++) {
				score[i][j] = j;
			}
		}

		PaintGameData gameData = new PaintGameData(10, 8, score);

		new GameFrame(gameData);
	}

}
