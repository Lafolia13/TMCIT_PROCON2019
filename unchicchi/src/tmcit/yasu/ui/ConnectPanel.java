package tmcit.yasu.ui;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ConnectPanel extends JPanel{

	private ConnectSettingPanel connectSettingPanel;

	private JButton startConnectButton;

	public ConnectPanel() {
		init();
		initLayout();
	}

	private void init() {
		connectSettingPanel = new ConnectSettingPanel();

		startConnectButton = new JButton("ê⁄ë±");
	}

	private void initLayout() {
		setLayout(null);

		connectSettingPanel.setBounds(10, 10, 400, 200);

		startConnectButton.setBounds(10, 220, 200, 40);

		add(connectSettingPanel);
		add(startConnectButton);
	}
}
