package tmcit.yasu.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class SettingPanel extends JPanel{
	private FileManager fileManager;
	private JLabel nameLabel;

//	private GameInfoPanel gameInfoPanel;
	private AgentSelectPanel agentSelectPanel;


	public SettingPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("ê›íË");
		nameLabel.setFont(Constant.DEFAULT_FONT);

//		gameInfoPanel = new GameInfoPanel();
		agentSelectPanel = new AgentSelectPanel(fileManager);

	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 5, 100, 30);
//		gameInfoPanel.setBounds(10, 40, 300, 120);
		agentSelectPanel.setBounds(10, 170, 300, 300);

//		add(gameInfoPanel);
		add(nameLabel);
		add(agentSelectPanel);
	}

	// getter
	public AgentSelectPanel getAgentSelectPanel() {
		return agentSelectPanel;
	}
}

