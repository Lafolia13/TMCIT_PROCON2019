package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class SettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private FileManager fileManager;

	private JLabel nameLabel;
	private JButton agentSettingButton;

	public SettingPanel(MainFrame mainFrame0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("設定");
		nameLabel.setFont(Constant.DEFAULT_FONT);
		agentSettingButton = new JButton("エージェントの設定");
		agentSettingButton.addActionListener(this);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		agentSettingButton.setBounds(10, 40, 160, 30);

		add(nameLabel);
		add(agentSettingButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "エージェントの設定") {
			mainFrame.switchOrAddTabbedPanel("エージェントの設定", new AgentSettingPanel(mainFrame, fileManager));
		}
	}
}
