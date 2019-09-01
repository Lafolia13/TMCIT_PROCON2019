package tmcit.yasu.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.util.GameNetworkRunnable;

public class GamePanel extends JPanel {
	private ConnectSetting connectSetting;
	private MatchesData matchData;

	// UI
	private GameStatusPanel gameStatusPanel;

	public GamePanel(ConnectSetting connectSetting0, MatchesData matchData0) {
		connectSetting = connectSetting0;
		matchData = matchData0;
		init();
		initLayout();

		ExecutorService exec = Executors.newSingleThreadExecutor();
		exec.execute(new GameNetworkRunnable(connectSetting, matchData, gameStatusPanel));
	}

	private void init() {
		gameStatusPanel = new GameStatusPanel(matchData);
	}

	private void initLayout() {
		setLayout(null);

		gameStatusPanel.setBounds(10, 10, 400, 200);

		add(gameStatusPanel);
	}
}
