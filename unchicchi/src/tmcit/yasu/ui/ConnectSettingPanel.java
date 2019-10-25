package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import tmcit.yasu.data.ConnectFileData;
import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class ConnectSettingPanel extends JPanel{
	private FileManager fileManager;
	
	private JLabel nameLabel, tokenLabel, urlLabel, portLabel, intervalLabel;
	private JTextField tokenField, urlField;
	private JSpinner portSpinner, intervalSpinner;
	private SpinnerModel portSpinnerModel, intervalSpinnerModel;

	public ConnectSettingPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);
		
		ConnectFileData cfd = fileManager.getConnectFileData();

		nameLabel = new JLabel("�ڑ��ݒ�");
		nameLabel.setFont(Constant.DEFAULT_FONT);
		tokenLabel = new JLabel("Token:");
		tokenLabel.setFont(Constant.SMALL_FONT);
		urlLabel = new JLabel("URL:");
		urlLabel.setFont(Constant.SMALL_FONT);
		portLabel = new JLabel("Port:");
		portLabel.setFont(Constant.SMALL_FONT);
		intervalLabel = new JLabel("�ڑ��Ԋu[ms]:");
		intervalLabel.setFont(Constant.SMALL_FONT);

		tokenField = new JTextField(cfd.getToken());
		urlField = new JTextField(cfd.getUrl());

		portSpinnerModel = new SpinnerNumberModel(cfd.getPort(), 0, 65535, 1);
		portSpinner = new JSpinner(portSpinnerModel);

		intervalSpinnerModel = new SpinnerNumberModel(1000, 10, 10000, 10);
		intervalSpinner = new JSpinner(intervalSpinnerModel);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 100, 20);

		urlLabel.setBounds(10, 40, 60, 20);
		urlField.setBounds(70, 40, 200, 20);

		portLabel.setBounds(10, 70, 60, 20);
		portSpinner.setBounds(70, 70, 60, 20);

		tokenLabel.setBounds(10, 100, 60, 20);
		tokenField.setBounds(70, 100, 200, 20);

		intervalLabel.setBounds(10, 130, 120, 20);
		intervalSpinner.setBounds(130, 130, 60, 20);

		add(nameLabel);
		add(urlLabel);
		add(urlField);
		add(portLabel);
		add(portSpinner);
		add(tokenLabel);
		add(tokenField);
		add(intervalLabel);
		add(intervalSpinner);
	}

	public ConnectSetting getSetting() {
		return new ConnectSetting(urlField.getText(), tokenField.getText(), (int)portSpinner.getValue(), (int)intervalSpinner.getValue());
	}
}
