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
		setBorder(Constant.LINE_BORDER);
		
		mainLabel = new JLabel("É\ÉãÉoÅ[ÇÃê›íË");
		mainLabel.setFont(Constant.MAIN_FONT);
	}
	
	private void initLayout() {
		setLayout(null);
		
		mainLabel.setBounds(5, 5, 300, 30);
		
		add(mainLabel);
	}
}
