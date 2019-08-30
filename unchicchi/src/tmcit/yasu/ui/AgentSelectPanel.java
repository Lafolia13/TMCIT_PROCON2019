package tmcit.yasu.ui;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import tmcit.yasu.data.PresetTableModel;
import tmcit.yasu.listener.PresetComboBoxListener;
import tmcit.yasu.listener.SolverComboBoxListener;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class AgentSelectPanel extends JPanel{
	private FileManager fileManager;
	
	// UI
	private JLabel nameLabel, presetLabel;
	private DefaultComboBoxModel<String> solverComboBoxModel, presetComboBoxModel;
	private JComboBox<String> solverComboBox, presetComboBox;
	private PresetTableModel paramTableModel;
	private JTable paramTable;
	private JScrollPane paramScrollPanel;
	
	// Listener
	private SolverComboBoxListener solverComboBoxListener;
	private PresetComboBoxListener presetComboBoxListener;

	public AgentSelectPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		
		init();
		initLayout();
		
		loadSolverComboBox();
	}

	private void init() {
		nameLabel = new JLabel("エージェント:");
		nameLabel.setFont(Constant.SMALL_FONT);
		presetLabel = new JLabel("プリセット:");
		presetLabel.setFont(Constant.SMALL_FONT);

		// box関係
		solverComboBoxModel = new DefaultComboBoxModel<String>();
		solverComboBox = new JComboBox<String>(solverComboBoxModel);
		presetComboBoxModel = new DefaultComboBoxModel<String>();
		presetComboBox = new JComboBox<String>(presetComboBoxModel);

		// table関係
		paramTableModel = new PresetTableModel(Constant.PRESET_PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		paramScrollPanel = new JScrollPane(paramTable);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);
		
		// listener関係
		solverComboBoxListener = new SolverComboBoxListener(this, solverComboBox);
		presetComboBoxListener = new PresetComboBoxListener(this, solverComboBox, presetComboBox);
		
		// listenerの紐づけ
		solverComboBox.addActionListener(solverComboBoxListener);
		presetComboBox.addActionListener(presetComboBoxListener);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(0, 0, 110, 20);
		solverComboBox.setBounds(120, 0, 150, 20);

		presetLabel.setBounds(0, 30, 110, 20);
		presetComboBox.setBounds(120, 30, 150, 20);

		paramScrollPanel.setBounds(0, 60, 270, 200);

		add(nameLabel);
		add(solverComboBox);
		add(presetLabel);
		add(presetComboBox);
		add(paramScrollPanel);
	}
	
	// ソルバーを読み込み
	public void loadSolverComboBox() {
		String[] solverList = fileManager.getSolverList();
		solverComboBoxModel.removeAllElements();
		for(String nowSolver : solverList) {
			solverComboBoxModel.addElement(nowSolver);
		}
	}
	
	// ソルバーのプリセットを読み込み
	public void refreshPresetComboBox(String solverName) {
		String[] presetList = fileManager.getSolverPresetList(solverName);
		presetComboBoxModel.removeAllElements();
		for(String nowPreset : presetList) {
			presetComboBoxModel.addElement(nowPreset);
		}
	}
	
	public void refreshParamTable(String solverName, String presetName) {
		if(presetName != null && presetName.equals("default.txt")) {
			paramTableModel.setDefaultFlag(true);
		}else {
			paramTableModel.setDefaultFlag(false);
		}

		// set preset
		ArrayList<String[]> paramList = fileManager.getSelectedSolverParameter(solverName, presetName);
		while(paramTable.getRowCount() > 0) {
			paramTableModel.removeRow(0);
		}
		for(String[] nowParam : paramList) {
			paramTableModel.addRow(nowParam);
		}
	}
	
	// Solverを取得
	public void getPlayer() {
		int solverIndex = solverComboBox.getSelectedIndex();
		String solverName = solverComboBox.getItemAt(solverIndex);

		int presetIndex = presetComboBox.getSelectedIndex();
		String presetName = presetComboBox.getItemAt(presetIndex);

		String exePath = fileManager.getSelectedSolverExePath(solverName);
		ArrayList<String[]> parameters = fileManager.getSelectedSolverParameter(solverName, presetName);

		String cmd = exePath;
		for(String[] nowParameter : parameters) {
			cmd += " " + nowParameter[0] + "=" + nowParameter[2];
		}
		
		System.out.println("[CMD] " + cmd);

//		ExecPlayer execPlayer = new ExecPlayer(cmd);
//		return execPlayer;

	}
}
