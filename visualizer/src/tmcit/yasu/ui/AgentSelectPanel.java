package tmcit.yasu.ui;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;

import tmcit.yasu.listener.SolverComboBoxListener;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class AgentSelectPanel extends JPanel{
	private boolean isMyPlayer;
	private FileManager filemanager;

	// UI
	private JLabel nameLabel, presetLabel;
	private ButtonGroup modeGroup;
	private JRadioButton humanRadio, programRadio;
	private DefaultComboBoxModel<String> solverComboBoxModel, presetComboBoxModel;
	private JComboBox<String> solverComboBox, presetComboBox;
	private JTable paramTable;

	// listener
	private SolverComboBoxListener solverComboBoxListener;

	public AgentSelectPanel(boolean isMyPlayer0, FileManager filemanager0) {
		isMyPlayer = isMyPlayer0;
		filemanager = filemanager0;
		init();
		initLayout();

		refreshSolverComboBox();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		if(isMyPlayer) {
			nameLabel = new JLabel("エージェント1");
		}else {
			nameLabel = new JLabel("エージェント2");
		}
		nameLabel.setFont(Constant.DEFAULT_FONT);

		humanRadio = new JRadioButton("操作");
		humanRadio.setFont(Constant.DEFAULT_FONT);
		programRadio = new JRadioButton("プログラム");
		programRadio.setFont(Constant.DEFAULT_FONT);
		modeGroup = new ButtonGroup();
		modeGroup.add(humanRadio);
		modeGroup.add(programRadio);

		solverComboBoxModel = new DefaultComboBoxModel<String>();
		solverComboBox = new JComboBox<String>(solverComboBoxModel);
		presetComboBoxModel = new DefaultComboBoxModel<String>();
		presetComboBox = new JComboBox<String>(presetComboBoxModel);

		presetLabel = new JLabel("プリセット:");
		presetLabel.setFont(new Font("MS ゴシック", Font.BOLD, 15));

		paramTable = new JTable(3, 3);

		// listener
		solverComboBoxListener = new SolverComboBoxListener(this, solverComboBox);
		solverComboBox.addActionListener(solverComboBoxListener);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		humanRadio.setBounds(10, 40, 150, 20);
		programRadio.setBounds(10, 70, 150, 20);
		solverComboBox.setBounds(160, 70, 120, 20);
		presetLabel.setBounds(10, 100, 150, 20);
		presetComboBox.setBounds(100, 100, 180, 20);
		paramTable.setBounds(10, 130, 270, 150);

		add(nameLabel);
		add(humanRadio);
		add(programRadio);
		add(solverComboBox);
		add(presetLabel);
		add(presetComboBox);
		add(paramTable);
	}

	private void refreshSolverComboBox() {
		String[] solverList = filemanager.getSolverList();
		solverComboBoxModel.removeAllElements();
		for(String nowSolver : solverList) {
			solverComboBoxModel.addElement(nowSolver);
		}
	}

	public void refreshPresetComboBox(String solverName) {
		String[] presetList = filemanager.getSolverPresetList(solverName);
		presetComboBoxModel.removeAllElements();
		for(String nowPreset : presetList) {
			presetComboBoxModel.addElement(nowPreset);
		}
	}
}
