package tmcit.yasu.ui;

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

import tmcit.yasu.ui.listener.AddSolverPanelListener;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class SolverSettingPanel extends JPanel{
	private MainFrame mainFrame;

	// �\���o�[�ꗗ
	private JLabel solverListLabel;
	private DefaultListModel<String> solverListModel;
	private JList<String> solverList;
	private JButton addSolverButton, deleteSolverButton;
	
	// �\���o�[�̐ݒ�֌W
	// �p�����[�^�̃e�[�u���֌W
	private JLabel paramLabel;
	private JTable paramTable;
	private DefaultTableModel paramTableModel;
	private JScrollPane sp;
	
	// �p�����[�^�֌W
	private JButton addParamButton, deleteParamButton;
	
	// exe
	private JLabel exeLabel;
	private JTextField exeField;
	private JButton exeButton;
	
	private JButton okButton;
	
	// listener
	private AddSolverPanelListener addSolverPanelListener;
	
	public SolverSettingPanel(MainFrame mainFrame0) {
		mainFrame = mainFrame0;
		init();
		initLayout();
	}
	
	private void init() {
		FileManager fileManager = mainFrame.getFileManager();

		// �\���o�[�ꗗ
		solverListLabel = new JLabel("�\���o�[�ꗗ");
		solverListLabel.setFont(Constant.MAIN_FONT);
		solverListModel = new DefaultListModel<String>();
		for(String nowName : fileManager.getSolverList()) {
			solverListModel.addElement(nowName);
		}
		solverList = new JList<>(solverListModel);
		addSolverButton = new JButton("�ǉ�");
		deleteSolverButton = new JButton("�폜");
		
		
		// �p�����[�^�̃e�[�u���֌W
		paramLabel = new JLabel("�p�����[�^");
		paramLabel.setFont(Constant.MAIN_FONT);
		paramTableModel = new DefaultTableModel(Constant.PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);
		sp = new JScrollPane(paramTable);
		
		// �p�����[�^�֌W
		addParamButton = new JButton("�p�����[�^�ǉ�");
		deleteParamButton = new JButton("�p�����[�^�폜");
		
		// exe
		exeLabel = new JLabel("exe�p�X");
		exeLabel.setFont(Constant.MAIN_FONT);
		exeField = new JTextField();
		exeButton = new JButton("�Q��");
		
		okButton = new JButton("����");
		
		// listener
		addSolverPanelListener = new AddSolverPanelListener(mainFrame, this);
		okButton.addActionListener(addSolverPanelListener);
		exeButton.addActionListener(addSolverPanelListener);
		addParamButton.addActionListener(addSolverPanelListener);
		deleteParamButton.addActionListener(addSolverPanelListener);
		addSolverButton.addActionListener(addSolverPanelListener);
		deleteSolverButton.addActionListener(addSolverPanelListener);
	}
	
	private void initLayout() {
		setLayout(null);
		
		// �\���o�[�ꗗ
		solverListLabel.setBounds(10, 10, 100, 20);
		solverList.setBounds(10, 40, 150, 400);
		addSolverButton.setBounds(10, 450, 70, 30);
		deleteSolverButton.setBounds(90, 450, 70, 30);
		
		// �p�����[�^
		paramLabel.setBounds(210, 45, 100, 30);
		sp.setBounds(210, 80, 500, 300);
		addParamButton.setBounds(210, 390, 150, 30);
		deleteParamButton.setBounds(370, 390, 150, 30);
		// exe
		exeLabel.setBounds(210, 12, 100, 30);
		exeField.setBounds(280, 20, 300, 20);
		exeButton.setBounds(600, 20, 100, 20);
		
		okButton.setBounds(10, 490, 100, 30);
		
		add(solverListLabel);
		add(solverList);
		add(addSolverButton);
		add(deleteSolverButton);
		add(paramLabel);
		add(sp);
		add(addParamButton);
		add(deleteParamButton);
		add(exeLabel);
		add(exeField);
		add(exeButton);
		add(okButton);
	}
	
	public void setExePath(String path) {
		exeField.setText(path);
	}
	
	public void addRow() {
		paramTableModel.addRow(new ArrayList().toArray());
	}
	
	public void deleteRow() {
		int[] selectedRows = paramTable.getSelectedRows();
		int rowCount = paramTable.getSelectedRowCount();
		for(int i = 0;i < rowCount;i++) {
			int nowRow = selectedRows[i];
			paramTableModel.removeRow(nowRow - i);
		}
	}
	
	public void addSolver(String solverName) {
		solverListModel.addElement(solverName);
		FileManager fileManager = mainFrame.getFileManager();
		fileManager.createNewSolver(solverName);
	}
	
	public void deleteSolver() {
		int[] selectedIndices = solverList.getSelectedIndices();
		for(int i = 0;i < selectedIndices.length;i++) {
			solverListModel.remove(selectedIndices[i] - i);
		}
	}
}
