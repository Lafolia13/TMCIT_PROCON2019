package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.util.Constant;

public class SolverSelectPanel extends JPanel{
	private JLabel mainLabel;
	
	public SolverSelectPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		setBorder(Constant.lineBorder);
		
		mainLabel = new JLabel("ƒ\ƒ‹ƒo[‚Ìİ’è");
		mainLabel.setFont(Constant.mainFont);
	}
	
	private void initLayout() {
		setLayout(null);
		
		mainLabel.setBounds(5, 5, 300, 30);
		
		add(mainLabel);
	}
}
