package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;

public class SettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	
	private JLabel mainLabel;
	private JButton addSolverButton;
	
	public SettingPanel(MainFrame mainFrame0) {
		mainFrame = mainFrame0;
		init();
		initLayout();
	}
	
	private void init() {
		setBorder(Constant.LINE_BORDER);
		
		mainLabel = new JLabel("設定");
		mainLabel.setFont(Constant.MAIN_FONT);
		addSolverButton = new JButton("ソルバーの管理");
		
		// listener
		addSolverButton.addActionListener(this);
	}
	
	private void initLayout() {
		setLayout(null);
		
		mainLabel.setBounds(5, 5, 300, 30);
		addSolverButton.setBounds(5, 40, 150, 30);
		
		add(mainLabel);
		add(addSolverButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("ソルバーの管理")) {
			mainFrame.addPanel("ソルバーの管理", new SolverSettingPanel(mainFrame));
		}
	}
}
