package tmcit.yasu.game;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tmcit.yasu.data.GameManageData;
import tmcit.yasu.listener.HumanPlayerKeyListener;
import tmcit.yasu.player.Player;
import tmcit.yasu.ui.AgentSelectPanel;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.SettingPanel;
import tmcit.yasu.util.FileManager;

public class GameStarter implements Runnable{
	private MainFrame mainFrame;
	private SettingPanel settingPanel;
	private FileManager fileManager;
	private AgentSelectPanel myAgentSelectPanel, rivalAgentSelectPanel;
	private ArrayList<GameData> gameDataList;
	private GameManageData gameManageData;


	public GameStarter(MainFrame mainFrame0, AgentSelectPanel myAgentSelectPanel0, AgentSelectPanel rivalAgentSelectPanel0
			, ArrayList<GameData> gameDataList0, GameManageData gameManageData0, FileManager fileManager0
			, SettingPanel settingPanel0) {
		mainFrame = mainFrame0;
		settingPanel = settingPanel0;
		myAgentSelectPanel = myAgentSelectPanel0;
		rivalAgentSelectPanel = rivalAgentSelectPanel0;
		gameDataList = gameDataList0;
		gameManageData = gameManageData0;
		fileManager = fileManager0;
	}

	@Override
	public void run() {
		for(GameData nowGameData : gameDataList) {
			while(true) {
				if(gameManageData.getRunningGame() < gameManageData.getMaxGame()) break;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			gameManageData.plusRunningGame();

			HumanPlayerKeyListener humanPlayerKeyListener = new HumanPlayerKeyListener();
			Player myPlayer = myAgentSelectPanel.getPlayer(humanPlayerKeyListener);
			Player rivalPlayer = rivalAgentSelectPanel.getPlayer(humanPlayerKeyListener);

			RunGameThread runGameThread = new RunGameThread(mainFrame, myPlayer, rivalPlayer, nowGameData, gameManageData, fileManager, settingPanel, humanPlayerKeyListener);
			ExecutorService exec = Executors.newSingleThreadExecutor();
			exec.submit(runGameThread);
		}
	}

}
