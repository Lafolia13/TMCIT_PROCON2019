package tmcit.yasu.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;

public class SettingPanel extends JPanel{
	private JLabel nameLabel;
	private JButton agentSettingButton;
	
	public SettingPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);
		
		nameLabel = new JLabel("設定");
		nameLabel.setFont(Constant.DEFAULT_FONT);
		agentSettingButton = new JButton("エージェントの設定");
	}
	
	private void initLayout() {
		setLayout(null);
		
		nameLabel.setBounds(10, 10, 200, 20);
		agentSettingButton.setBounds(10, 40, 150, 30);
		
		add(nameLabel);
		add(agentSettingButton);
	}
}
