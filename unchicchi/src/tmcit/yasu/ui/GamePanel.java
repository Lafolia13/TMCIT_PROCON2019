package tmcit.yasu.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.GameNetworkRunnable;

public class GamePanel extends JPanel {
	private ConnectSetting connectSetting;
	private MatchesData matchData;
	private String solverCmd;

	// UI
	private GameStatusPanel gameStatusPanel;
	private GamePaintPanel gamePaintPanel;

	public GamePanel(ConnectSetting connectSetting0, MatchesData matchData0, String solverCmd0) {
		connectSetting = connectSetting0;
		matchData = matchData0;
		solverCmd = solverCmd0;
		init();
		initLayout();

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new GameNetworkRunnable(connectSetting, matchData, gameStatusPanel, gamePaintPanel, solverCmd));
	}

	private void init() {
		gameStatusPanel = new GameStatusPanel(matchData);
		gamePaintPanel = new GamePaintPanel(matchData);
	}

	private void initLayout() {
		setLayout(null);

		gameStatusPanel.setBounds(10, 10, 400, 300);
		gamePaintPanel.setBounds(420, 10, Constant.MAP_SIZE + 10, Constant.MAP_SIZE + 10);

		add(gameStatusPanel);
		add(gamePaintPanel);
	}
}
