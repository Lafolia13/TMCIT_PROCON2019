package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.game.GameLogMaster;
import tmcit.yasu.game.TurnData;
import tmcit.yasu.listener.SettingChangeListener;
import tmcit.yasu.ui.game.GameLogPanel;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;
import tmcit.yasu.util.SettingManager;

public class SettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private StarterPanel starterPanel;
	private FileManager fileManager;
	private SettingManager settingManager;

	private JLabel nameLabel, maxGameLabel, sleepTimeLabel;
	private JButton agentSettingButton;
	private SpinnerNumberModel maxGameSpinnerModel, sleepTimeSpinnerModel;
	private JSpinner maxGameSpinner, sleepTimeSpinner;
	private JRadioButton showActionRadioButton;
	
	// ログ
	private JButton loadLogButton;
	
	// listener
	private SettingChangeListener settingChangeListener;

	public SettingPanel(MainFrame mainFrame0, StarterPanel starterPanel0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		starterPanel = starterPanel0;
		fileManager = fileManager0;
		init();
		initLayout();
		try {
			settingManager.loadSetting();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		settingManager = new SettingManager(fileManager, this);
		
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("設定");
		nameLabel.setFont(Constant.DEFAULT_FONT);
		agentSettingButton = new JButton("エージェントの設定");

		// 最大同時ゲーム数
		maxGameLabel = new JLabel("最大同時ゲーム数:");
		maxGameLabel.setFont(Constant.SMALL_FONT);
		maxGameSpinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
		maxGameSpinner = new JSpinner(maxGameSpinnerModel);
		
		// 時間
		sleepTimeLabel = new JLabel("ターン間の時間[ms]:");
		sleepTimeLabel.setFont(Constant.SMALL_FONT);
		sleepTimeSpinnerModel = new SpinnerNumberModel(100, 0, 100000, 100);
		sleepTimeSpinner = new JSpinner(sleepTimeSpinnerModel);
		
		// 行動の描画
		showActionRadioButton = new JRadioButton("行動の描画");
		showActionRadioButton.setFont(Constant.SMALL_FONT);
		showActionRadioButton.setSelected(true);
		
		// ログ
		loadLogButton = new JButton("ログの読み込み");

		// action listener
		agentSettingButton.addActionListener(this);
		loadLogButton.addActionListener(this);
		// change listener
		settingChangeListener = new SettingChangeListener(settingManager);
		maxGameSpinner.addChangeListener(settingChangeListener);
		sleepTimeSpinner.addChangeListener(settingChangeListener);
		showActionRadioButton.addChangeListener(settingChangeListener);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 30);
		agentSettingButton.setBounds(10, 40, 160, 30);
		maxGameLabel.setBounds(10, 80, 150, 20);
		maxGameSpinner.setBounds(180, 80, 100, 20);
		sleepTimeLabel.setBounds(10, 110, 170, 20);
		sleepTimeSpinner.setBounds(180, 110, 100, 20);
		showActionRadioButton.setBounds(10, 140, 200, 20);
		loadLogButton.setBounds(10, 170, 150, 30);

		add(nameLabel);
		add(agentSettingButton);
		add(maxGameLabel);
		add(maxGameSpinner);
		add(loadLogButton);
		add(sleepTimeLabel);
		add(sleepTimeSpinner);
		add(showActionRadioButton);
	}
	
	private void showLogGames(File[] logFiles) {
		for(File nowLog : logFiles) {
			System.out.println(nowLog.getName());
			GameLogMaster logMaster = new GameLogMaster(nowLog);
			mainFrame.addTabbedPanel("ログ", new GameLogPanel(mainFrame, logMaster.getTurnDataList(), logMaster.getGameData()));
		}
	}

	// getter
	public int getMaxGame() {
		return (int) maxGameSpinner.getValue();
	}
	
	public int getSleepTime() {
		return (int) sleepTimeSpinner.getValue();
	}
	
	public boolean isSelectedShowActionRadioButton() {
		return showActionRadioButton.isSelected();
	}
	
	// setter
	public void setMaxGame(int value) {
		maxGameSpinner.setValue(value);
	}
	
	public void setSleepTime(int value) {
		sleepTimeSpinner.setValue(value);
	}
	
	public void setSelectedShowActionRadioButton(boolean value) {
		showActionRadioButton.setSelected(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("エージェントの設定")) {
			mainFrame.switchOrAddTabbedPanel("エージェントの設定", new AgentSettingPanel(mainFrame, starterPanel, fileManager));
		}else if(cmd.equals("ログの読み込み")) {
			fileManager.setWindowsLookAndFeel();
			JFileChooser filechooser = new JFileChooser(fileManager.getLogDirectory());
			filechooser.setMultiSelectionEnabled(true);
			int selected = filechooser.showOpenDialog(this);
			fileManager.resetLookAndFeel();

			if(selected == JFileChooser.APPROVE_OPTION) {
				File[] selectedFiles = filechooser.getSelectedFiles();
				showLogGames(selectedFiles);
			}
		}
	}
}
