package tmcit.yasu.ui;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tmcit.yasu.listener.PresetComboBoxListener;
import tmcit.yasu.listener.SolverComboBoxListener;
import tmcit.yasu.player.ExecPlayer;
import tmcit.yasu.player.Player;
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
	private DefaultTableModel paramTableModel;
	private JTable paramTable;
	private JScrollPane paramScrollPanel;
	private JButton addPresetButton, deletePresetButton;

	// listener
	private SolverComboBoxListener solverComboBoxListener;
	private PresetComboBoxListener presetComboBoxListener;

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

		paramTableModel = new DefaultTableModel(Constant.PARAM_COLUMN_NAMES, 0);
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
	public Player getPlayer() {
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
		}
		return null;
	}

	// refresh

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

	public void refreshParamTable(String solverName, String presetName) {
		ArrayList<String[]> paramList = filemanager.getSelectedSolverParameter(solverName, presetName);
		while(paramTable.getRowCount() > 0) {
			paramTableModel.removeRow(0);
		}
		for(String[] nowParam : paramList) {
			paramTableModel.addRow(nowParam);
		}
	}
}
