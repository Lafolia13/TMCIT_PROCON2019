package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.game.GameMaster;
import tmcit.yasu.game.RunGameThread;
import tmcit.yasu.player.Player;
import tmcit.yasu.ui.AgentSelectPanel;
import tmcit.yasu.ui.MapSelectPanel;
import tmcit.yasu.ui.game.GameFrame;

public class StartButtonListener implements ActionListener{
	private MapSelectPanel mapSelectPanel;
	private AgentSelectPanel myAgentSelectPanel, rivalAgentSelectPanel;

	public StartButtonListener(MapSelectPanel mapSelectPanel0, AgentSelectPanel myAgentSelectPanel0, AgentSelectPanel rivalAgentSelectPanel0) {
		mapSelectPanel = mapSelectPanel0;
		myAgentSelectPanel = myAgentSelectPanel0;
		rivalAgentSelectPanel = rivalAgentSelectPanel0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<GameData> gameDataList = mapSelectPanel.getGameDataList();

		for(GameData nowGameData : gameDataList) {
			Player myPlayer = myAgentSelectPanel.getPlayer();
			Player rivalPlayer = rivalAgentSelectPanel.getPlayer();

			RunGameThread runGameThread = new RunGameThread(myPlayer, rivalPlayer, nowGameData);
			ExecutorService exec = Executors.newSingleThreadExecutor();
			exec.submit(runGameThread);
		}
	}

}
