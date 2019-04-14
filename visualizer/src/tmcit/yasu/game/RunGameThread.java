package tmcit.yasu.game;

import tmcit.yasu.player.Player;
import tmcit.yasu.ui.game.GameFrame;

public class RunGameThread extends Thread{
	private Player myPlayer, rivalPlayer;
	private GameData gameData;

	public RunGameThread(Player myPlayer0, Player rivalPlayer0, GameData gameData0) {
		myPlayer = myPlayer0;
		rivalPlayer = rivalPlayer0;
		gameData = gameData0;
	}

	@Override
	public void run() {
		GameFrame gameFrame = new GameFrame(gameData);
		GameMaster gameMaster = new GameMaster(gameData, myPlayer, rivalPlayer, gameFrame);
		gameMaster.run();
	}
}
