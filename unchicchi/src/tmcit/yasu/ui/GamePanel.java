package tmcit.yasu.ui;

import javax.swing.JPanel;

import tmcit.yasu.data.ConnectSetting;

public class GamePanel extends JPanel {
	private ConnectSetting connectSetting;
	private int id;

	public GamePanel(ConnectSetting connectSetting0, int id0) {
		connectSetting = connectSetting0;
		id = id0;
	}
}
