package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class SettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private FileManager fileManager;

	private JLabel nameLabel, maxGameLabel;
	private JButton agentSettingButton;
	private SpinnerNumberModel maxGameSpinnerModel;
	private JSpinner maxGameSpinner;

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

		// 最大同時ゲーム数
		maxGameLabel = new JLabel("最大同時ゲーム数:");
		maxGameLabel.setFont(Constant.SMALL_FONT);
		maxGameSpinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
		maxGameSpinner = new JSpinner(maxGameSpinnerModel);

		// listener
		agentSettingButton.addActionListener(this);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 30);
		agentSettingButton.setBounds(10, 40, 160, 30);
		maxGameLabel.setBounds(10, 80, 150, 20);
		maxGameSpinner.setBounds(180, 80, 100, 20);

		add(nameLabel);
		add(agentSettingButton);
		add(maxGameLabel);
		add(maxGameSpinner);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "エージェントの設定") {
			mainFrame.switchOrAddTabbedPanel("エージェントの設定", new AgentSettingPanel(mainFrame, fileManager));
		}
	}
}
