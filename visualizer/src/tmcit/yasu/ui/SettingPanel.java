package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.game.GameLogMaster;
import tmcit.yasu.game.TurnData;
import tmcit.yasu.ui.game.GameLogPanel;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class SettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private StarterPanel starterPanel;
	private FileManager fileManager;

	private JLabel nameLabel, maxGameLabel;
	private JButton agentSettingButton;
	private SpinnerNumberModel maxGameSpinnerModel;
	private JSpinner maxGameSpinner;
	
	// ���O
	private JButton loadLogButton;

	public SettingPanel(MainFrame mainFrame0, StarterPanel starterPanel0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		starterPanel = starterPanel0;
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("�ݒ�");
		nameLabel.setFont(Constant.DEFAULT_FONT);
		agentSettingButton = new JButton("�G�[�W�F���g�̐ݒ�");

		// �ő哯���Q�[����
		maxGameLabel = new JLabel("�ő哯���Q�[����:");
		maxGameLabel.setFont(Constant.SMALL_FONT);
		maxGameSpinnerModel = new SpinnerNumberModel(1, 1, 10, 1);
		maxGameSpinner = new JSpinner(maxGameSpinnerModel);
		
		// ���O
		loadLogButton = new JButton("���O�̓ǂݍ���");

		// listener
		agentSettingButton.addActionListener(this);
		loadLogButton.addActionListener(this);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 30);
		agentSettingButton.setBounds(10, 40, 160, 30);
		maxGameLabel.setBounds(10, 80, 150, 20);
		maxGameSpinner.setBounds(180, 80, 100, 20);
		loadLogButton.setBounds(10, 110, 150, 30);

		add(nameLabel);
		add(agentSettingButton);
		add(maxGameLabel);
		add(maxGameSpinner);
		add(loadLogButton);
	}
	
	private void showLogGames(File[] logFiles) {
		for(File nowLog : logFiles) {
			System.out.println(nowLog.getName());
			GameLogMaster logMaster = new GameLogMaster(nowLog);
			mainFrame.addTabbedPanel("���O", new GameLogPanel(logMaster.getTurnDataList(), logMaster.getGameData()));
		}
	}

	// getter
	public int getMaxGame() {
		return (int) maxGameSpinner.getValue();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("�G�[�W�F���g�̐ݒ�")) {
			mainFrame.switchOrAddTabbedPanel("�G�[�W�F���g�̐ݒ�", new AgentSettingPanel(mainFrame, starterPanel, fileManager));
		}else if(cmd.equals("���O�̓ǂݍ���")) {
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
