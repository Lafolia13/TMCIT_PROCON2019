package tmcit.yasu.ui;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tmcit.yasu.ui.listener.AddSolverPanelListener;
import tmcit.yasu.util.Constant;

public class AddSolverPanel extends JPanel{
	private MainFrame mainFrame;
	
	// �\���o�[��
	private JLabel solverNameLabel;
	private JTextField solverNameField;
	
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
	
	public AddSolverPanel(MainFrame mainFrame0) {
		mainFrame = mainFrame0;
		init();
		initLayout();
	}
	
	private void init() {
		// �\���o�[��
		solverNameLabel = new JLabel("�\���o�[��");
		solverNameLabel.setFont(Constant.MAIN_FONT);
		solverNameField = new JTextField();
		
		// �p�����[�^�̃e�[�u���֌W
		paramLabel = new JLabel("�p�����[�^");
		paramLabel.setFont(Constant.MAIN_FONT);
		paramTableModel = new DefaultTableModel(Constant.PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);
		sp = new JScrollPane(paramTable);
		
		// �p�����[�^�֌W
		addParamButton = new JButton("�ǉ�");
		deleteParamButton = new JButton("�폜");
		
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
	}
	
	private void initLayout() {
		setLayout(null);
		
		// �\���o�[��
		solverNameLabel.setBounds(10, 10, 100, 30);
		solverNameField.setBounds(100, 19, 300, 20);
		// �p�����[�^
		paramLabel.setBounds(10, 45, 100, 30);
		sp.setBounds(10, 80, 500, 300);
		addParamButton.setBounds(10, 390, 100, 30);
		deleteParamButton.setBounds(120, 390, 100, 30);
		// exe
		exeLabel.setBounds(10, 422, 100, 30);
		exeField.setBounds(80, 430, 300, 20);
		exeButton.setBounds(400, 430, 100, 20);
		
		okButton.setBounds(10, 490, 100, 30);
		
		add(solverNameLabel);
		add(solverNameField);
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
}
