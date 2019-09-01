package tmcit.yasu.ui;

import javax.swing.JPanel;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.data.MatchesData;

public class GamePanel extends JPanel {
	private ConnectSetting connectSetting;
	private MatchesData matchData;

	// UI
	private GameStatusPanel gameStatusPanel;

	public GamePanel(ConnectSetting connectSetting0, MatchesData matchData0) {
		connectSetting = connectSetting0;
		matchData = matchData0;
	}

	private void init() {
	}

	private void initLayout() {
	}
}
