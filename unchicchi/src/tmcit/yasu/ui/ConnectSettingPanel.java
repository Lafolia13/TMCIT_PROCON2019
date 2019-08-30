package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tmcit.yasu.util.Constant;

public class ConnectSettingPanel extends JPanel{
	private JLabel nameLabel, tokenLabel, urlLabel;
	private JTextField tokenField, urlField;
	
	public ConnectSettingPanel() {
		init();
		initLayout();
	}
	
	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);
		
		nameLabel = new JLabel("ê⁄ë±ê›íË");
		nameLabel.setFont(Constant.DEFAULT_FONT);
		tokenLabel = new JLabel("Token:");
		tokenLabel.setFont(Constant.SMALL_FONT);
		urlLabel = new JLabel("URL:");
		urlLabel.setFont(Constant.SMALL_FONT);
		
		tokenField = new JTextField();
		tokenField.setFont(Constant.SMALL_FONT);
		urlField = new JTextField();
	}
	
	private void initLayout() {
		setLayout(null);
		
		nameLabel.setBounds(10, 10, 100, 20);
		urlLabel.setBounds(10, 40, 60, 20);
		urlField.setBounds(70, 40, 200, 20);
		tokenLabel.setBounds(10, 70, 60, 20);
		tokenField.setBounds(70, 70, 200, 20);
		
		add(nameLabel);
		add(urlLabel);
		add(urlField);
		add(tokenLabel);
		add(tokenField);
	}
}
