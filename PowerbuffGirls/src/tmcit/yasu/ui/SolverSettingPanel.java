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

	// ソルバー一覧
	private JLabel solverListLabel;
	private DefaultListModel<String> solverListModel;
	private JList<String> solverList;
	private JButton addSolverButton, deleteSolverButton;
	
	// ソルバーの設定関係
	// パラメータのテーブル関係
	private JLabel paramLabel;
	private JTable paramTable;
	private DefaultTableModel paramTableModel;
	private JScrollPane sp;
	
	// パラメータ関係
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

		// ソルバー一覧
		solverListLabel = new JLabel("ソルバー一覧");
		solverListLabel.setFont(Constant.MAIN_FONT);
		solverListModel = new DefaultListModel<String>();
		for(String nowName : fileManager.getSolverList()) {
			solverListModel.addElement(nowName);
		}
		solverList = new JList<>(solverListModel);
		addSolverButton = new JButton("追加");
		deleteSolverButton = new JButton("削除");
		
		
		// パラメータのテーブル関係
		paramLabel = new JLabel("パラメータ");
		paramLabel.setFont(Constant.MAIN_FONT);
		paramTableModel = new DefaultTableModel(Constant.PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);
		sp = new JScrollPane(paramTable);
		
		// パラメータ関係
		addParamButton = new JButton("パラメータ追加");
		deleteParamButton = new JButton("パラメータ削除");
		
		// exe
		exeLabel = new JLabel("exeパス");
		exeLabel.setFont(Constant.MAIN_FONT);
		exeField = new JTextField();
		exeButton = new JButton("参照");
		
		okButton = new JButton("完了");
		
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
		
		// ソルバー一覧
		solverListLabel.setBounds(10, 10, 100, 20);
		solverList.setBounds(10, 40, 150, 400);
		addSolverButton.setBounds(10, 450, 70, 30);
		deleteSolverButton.setBounds(90, 450, 70, 30);
		
		// パラメータ
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
