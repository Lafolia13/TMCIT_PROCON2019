package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;

public class SettingPanel extends JPanel{
	private JLabel nameLabel;

	private GameInfoPanel gameInfoPanel;
	private AgentSelectPanel agentSelectPanel;

	public SettingPanel() {
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("ê›íË");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		gameInfoPanel = new GameInfoPanel();
		agentSelectPanel = new AgentSelectPanel();
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 5, 100, 30);
		gameInfoPanel.setBounds(10, 40, 300, 120);
		agentSelectPanel.setBounds(10, 170, 300, 300);

		add(gameInfoPanel);
		add(nameLabel);
		add(agentSelectPanel);
	}
}

