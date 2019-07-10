package tmcit.yasu.ui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import tmcit.yasu.data.PresetTableModel;
import tmcit.yasu.util.Constant;

public class AgentSelectPanel extends JPanel{
	// UI
	private JLabel nameLabel, presetLabel;
	private DefaultComboBoxModel<String> solverComboBoxModel, presetComboBoxModel;
	private JComboBox<String> solverComboBox, presetComboBox;
	private PresetTableModel paramTableModel;
	private JTable paramTable;
	private JScrollPane paramScrollPanel;

	public AgentSelectPanel() {
		init();
		initLayout();
	}

	private void init() {
		nameLabel = new JLabel("�G�[�W�F���g:");
		nameLabel.setFont(Constant.SMALL_FONT);
		presetLabel = new JLabel("�v���Z�b�g:");
		presetLabel.setFont(Constant.SMALL_FONT);

		// box�֌W
		solverComboBoxModel = new DefaultComboBoxModel<String>();
		solverComboBox = new JComboBox<String>(solverComboBoxModel);
		presetComboBoxModel = new DefaultComboBoxModel<String>();
		presetComboBox = new JComboBox<String>(presetComboBoxModel);

		// table�֌W
		paramTableModel = new PresetTableModel(Constant.PRESET_PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		paramScrollPanel = new JScrollPane(paramTable);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);
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
}
