package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class AgentSettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private FileManager fileManager;

	private JLabel nameLabel, solverLabel, paramLabel, exeLabel;
	private JScrollPane paramScrollPanel;
	private DefaultListModel<String> solverListModel;
	private JList<String> solverList;
	private JButton addSolverButton, deleteSolverButton, selectExeButton, addParamButton, deleteParamButton;
	private JTextField exePathField;
	private DefaultTableModel paramTableModel;
	private JTable paramTabel;

	public AgentSettingPanel(MainFrame mainFrame0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		nameLabel = new JLabel("�G�[�W�F���g�ݒ�");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		solverLabel = new JLabel("�\���o�[�ꗗ");
		solverLabel.setFont(Constant.SMALL_FONT);
		solverListModel = new DefaultListModel<String>();
		String[] solverNameList = fileManager.getSolverList();
		for(String nowSolverName : solverNameList) {
			solverListModel.addElement(nowSolverName);
		}
		solverList = new JList<String>(solverListModel);
		addSolverButton = new JButton("�ǉ�");
		addSolverButton.addActionListener(this);
		deleteSolverButton = new JButton("�폜");
		deleteSolverButton.addActionListener(this);

		paramLabel = new JLabel("�p�����[�^");
		paramLabel.setFont(Constant.SMALL_FONT);
		paramTableModel = new DefaultTableModel(Constant.PARAM_COLUMN_NAMES, 0);
		paramTabel = new JTable(paramTableModel);
		addParamButton = new JButton("�p�����[�^�ǉ�");
		addParamButton.addActionListener(this);
		deleteParamButton = new JButton("�p�����[�^�폜");
		deleteParamButton.addActionListener(this);
		paramScrollPanel = new JScrollPane(paramTabel);
		JTableHeader paramTableHeader = paramTabel.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);

		exeLabel = new JLabel("���s�R�}���h");
		exeLabel.setFont(Constant.SMALL_FONT);
		exePathField = new JTextField();
		selectExeButton = new JButton("�Q��");
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		solverLabel.setBounds(10, 40, 200, 20);
		solverList.setBounds(10, 70, 200, 300);
		addSolverButton.setBounds(10, 380, 80, 20);
		deleteSolverButton.setBounds(100, 380, 80, 20);

		paramLabel.setBounds(250, 40, 200, 20);
		paramScrollPanel.setBounds(250, 70, 400, 300);
		addParamButton.setBounds(250, 380, 150, 20);
		deleteParamButton.setBounds(410, 380, 150, 20);

		exeLabel.setBounds(690, 40, 200, 20);
		exePathField.setBounds(690, 70, 300, 20);
		selectExeButton.setBounds(690, 100, 80, 20);

		add(nameLabel);
		add(solverLabel);
		add(solverList);
		add(addSolverButton);
		add(deleteSolverButton);
		add(paramLabel);
		add(paramScrollPanel);
		add(addParamButton);
		add(deleteParamButton);
		add(exeLabel);
		add(exePathField);
		add(selectExeButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "�ǉ�") {
			AddSolverDialog solverDialog = new AddSolverDialog(mainFrame, "�ǉ�");
			if(solverDialog.isApplied()) {
				String addName = solverDialog.getName();
				solverListModel.addElement(addName);
				fileManager.makeSolverDir(addName);
			}
		}else if(cmd == "�폜") {
			if(!solverList.isSelectionEmpty()) {
				int index = solverList.getSelectedIndex();
				String selectedSolverName = solverList.getSelectedValue();
				fileManager.removeSolverDir(selectedSolverName);
				solverListModel.remove(index);
			}
		}else if(cmd == "�p�����[�^�ǉ�") {
			paramTableModel.addRow(new ArrayList<String>().toArray());
		}else if(cmd == "�p�����[�^�폜") {
			int[] selectedRows = paramTabel.getSelectedRows();
			int rowCount = paramTabel.getSelectedRowCount();
			for(int i = 0;i < rowCount;i++) {
				int nowRow = selectedRows[i];
				paramTableModel.removeRow(nowRow - i);
			}
		}
	}
}
