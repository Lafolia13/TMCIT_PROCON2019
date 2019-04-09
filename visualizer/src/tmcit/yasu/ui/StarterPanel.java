package tmcit.yasu.ui;

import javax.swing.JPanel;

public class StarterPanel extends JPanel{
	private MapSelectPanel mapSelectPanel;

	public StarterPanel() {
		init();
		initLayout();
	}

	private void init() {
		mapSelectPanel = new MapSelectPanel();
	}

	private void initLayout() {
		setLayout(null);

		mapSelectPanel.setBounds(10, 10, 400, 500);

		add(mapSelectPanel);
	}
}
