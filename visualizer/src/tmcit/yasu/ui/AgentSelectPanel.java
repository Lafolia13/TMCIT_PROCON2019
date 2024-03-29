package tmcit.yasu.ui;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tmcit.yasu.data.PresetTableModel;
import tmcit.yasu.listener.HumanPlayerKeyListener;
import tmcit.yasu.listener.PresetButtonListener;
import tmcit.yasu.listener.PresetComboBoxListener;
import tmcit.yasu.listener.PresetParamTableModelListener;
import tmcit.yasu.listener.SolverComboBoxListener;
import tmcit.yasu.player.ExecPlayer;
import tmcit.yasu.player.HumanPlayer;
import tmcit.yasu.player.Player;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class AgentSelectPanel extends JPanel{
	private boolean isMyPlayer;
	private FileManager filemanager;
	private JFrame mainFrame;

	// UI
	private JLabel nameLabel, presetLabel;
	private ButtonGroup modeGroup;
	private JRadioButton humanRadio, programRadio;
	private DefaultComboBoxModel<String> solverComboBoxModel, presetComboBoxModel;
	private JComboBox<String> solverComboBox, presetComboBox;
	private PresetTableModel paramTableModel;
	private JTable paramTable;
	private JScrollPane paramScrollPanel;
	private JButton addPresetButton, deletePresetButton;

	// listener
	private SolverComboBoxListener solverComboBoxListener;
	private PresetComboBoxListener presetComboBoxListener;
	private PresetButtonListener presetButtonListener;
	private PresetParamTableModelListener presetParamTableModelListener;

	public AgentSelectPanel(JFrame mainFrame0, boolean isMyPlayer0, FileManager filemanager0) {
		mainFrame = mainFrame0;
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

		paramTableModel = new PresetTableModel(Constant.PRESET_PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		paramScrollPanel = new JScrollPane(paramTable);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);

		addPresetButton = new JButton("プリセット追加");
		deletePresetButton = new JButton("プリセット削除");

		// listener
		solverComboBoxListener = new SolverComboBoxListener(this, solverComboBox);
		solverComboBox.addActionListener(solverComboBoxListener);
		presetComboBoxListener = new PresetComboBoxListener(this, solverComboBox, presetComboBox);
		presetComboBox.addActionListener(presetComboBoxListener);
		presetButtonListener = new PresetButtonListener(mainFrame, filemanager, solverComboBox, presetComboBox);
		addPresetButton.addActionListener(presetButtonListener);
		deletePresetButton.addActionListener(presetButtonListener);
		presetParamTableModelListener = new PresetParamTableModelListener(filemanager, solverComboBox, presetComboBox, paramTableModel);
		paramTableModel.addTableModelListener(presetParamTableModelListener);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		humanRadio.setBounds(10, 40, 150, 20);
		programRadio.setBounds(10, 70, 150, 20);
		solverComboBox.setBounds(160, 70, 120, 20);
		presetLabel.setBounds(10, 100, 150, 20);
		presetComboBox.setBounds(100, 100, 180, 20);
		paramScrollPanel.setBounds(10, 130, 270, 150);
		addPresetButton.setBounds(10, 290, 130, 30);
		deletePresetButton.setBounds(150, 290, 130, 30);

		add(nameLabel);
		add(humanRadio);
		add(programRadio);
		add(solverComboBox);
		add(presetLabel);
		add(presetComboBox);
		add(paramScrollPanel);
		add(addPresetButton);
		add(deletePresetButton);
	}

	// getter
	public Player getPlayer(HumanPlayerKeyListener humanPlayerKeyListener) {
		if(programRadio.isSelected()) {
			int solverIndex = solverComboBox.getSelectedIndex();
			String solverName = solverComboBox.getItemAt(solverIndex);

			int presetIndex = presetComboBox.getSelectedIndex();
			String presetName = presetComboBox.getItemAt(presetIndex);

			String exePath = filemanager.getSelectedSolverExePath(solverName);
			ArrayList<String[]> parameters = filemanager.getSelectedSolverParameter(solverName, presetName);

			String cmd = exePath;
			for(String[] nowParameter : parameters) {
				cmd += " " + nowParameter[0] + "=" + nowParameter[2];
			}

			ExecPlayer execPlayer = new ExecPlayer(cmd);
			return execPlayer;
		}else if(humanRadio.isSelected()) {
			HumanPlayer humanPlayer = new HumanPlayer(humanPlayerKeyListener);
			return humanPlayer;
		}
		return null;
	}

	// refresh

	public void refreshSolverComboBox() {
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

	public void refreshParamTable(String solverName, String presetName) {
		presetParamTableModelListener.setListenMode(false);

		if(presetName != null && presetName.equals("default.txt")) {
			paramTableModel.setDefaultFlag(true);
		}else {
			paramTableModel.setDefaultFlag(false);
		}

		// set preset
		ArrayList<String[]> paramList = filemanager.getSelectedSolverParameter(solverName, presetName);
		while(paramTable.getRowCount() > 0) {
			paramTableModel.removeRow(0);
		}
		for(String[] nowParam : paramList) {
			paramTableModel.addRow(nowParam);
		}
		presetParamTableModelListener.setListenMode(true);
	}
}
