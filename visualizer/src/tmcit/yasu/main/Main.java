package tmcit.yasu.main;

import tmcit.yasu.data.GameData;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.game.GameFrame;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello World");
		
		GameData gameData = new GameData(10, 8, null);
		
		new GameFrame(gameData);
	}

}
