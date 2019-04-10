package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;

public class AgentSelectPanel extends JPanel{
	private boolean isMyPlayer;
	
	// UI
	private JLabel nameLabel;
	
	public AgentSelectPanel(boolean isMyPlayer0) {
		isMyPlayer = isMyPlayer0;
		init();
		initLayout();
	}
	
	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);
		
		if(isMyPlayer) {
			nameLabel = new JLabel("�G�[�W�F���g1");
		}else {
			nameLabel = new JLabel("�G�[�W�F���g2");
		}
		nameLabel.setFont(Constant.DEFAULT_FONT);
	}
	
	private void initLayout() {
		setLayout(null);
		
		nameLabel.setBounds(10, 10, 200, 20);
		
		add(nameLabel);
	}
}
