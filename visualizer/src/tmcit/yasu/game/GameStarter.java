package tmcit.yasu.game;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tmcit.yasu.data.GameManageData;
import tmcit.yasu.player.Player;
import tmcit.yasu.ui.AgentSelectPanel;
import tmcit.yasu.ui.MainFrame;

public class GameStarter implements Runnable{
	private MainFrame mainFrame;
	private AgentSelectPanel myAgentSelectPanel, rivalAgentSelectPanel;
	private ArrayList<GameData> gameDataList;
	private GameManageData gameManageData;


	public GameStarter(MainFrame mainFrame0, AgentSelectPanel myAgentSelectPanel0, AgentSelectPanel rivalAgentSelectPanel0
			, ArrayList<GameData> gameDataList0, GameManageData gameManageData0) {
		mainFrame = mainFrame0;
		myAgentSelectPanel = myAgentSelectPanel0;
		rivalAgentSelectPanel = rivalAgentSelectPanel0;
		gameDataList = gameDataList0;
		gameManageData = gameManageData0;
	}

	@Override
	public void run() {
		for(GameData nowGameData : gameDataList) {
			while(true) {
				if(gameManageData.getRunningGame() < 2) break;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			gameManageData.plusRunningGame();

			Player myPlayer = myAgentSelectPanel.getPlayer();
			Player rivalPlayer = rivalAgentSelectPanel.getPlayer();

			RunGameThread runGameThread = new RunGameThread(mainFrame, myPlayer, rivalPlayer, nowGameData, gameManageData);
			ExecutorService exec = Executors.newSingleThreadExecutor();
			exec.submit(runGameThread);
		}
	}

}
