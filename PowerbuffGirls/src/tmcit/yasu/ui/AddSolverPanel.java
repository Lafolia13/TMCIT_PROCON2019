package tmcit.yasu.ui;

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
	
	// ソルバー名
	private JLabel solverNameLabel;
	private JTextField solverNameField;
	
	// パラメータのテーブル関係
	private JLabel paramLabel;
	private JTable paramTable;
	private DefaultTableModel paramTableModel;
	private JScrollPane sp;
	
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
		// ソルバー名
		solverNameLabel = new JLabel("ソルバー名");
		solverNameLabel.setFont(Constant.MAIN_FONT);
		solverNameField = new JTextField();
		
		// パラメータのテーブル関係
		paramLabel = new JLabel("パラメータ");
		paramLabel.setFont(Constant.MAIN_FONT);
		paramTableModel = new DefaultTableModel(Constant.PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);
		sp = new JScrollPane(paramTable);
		
		// exe
		exeLabel = new JLabel("exeパス");
		exeLabel.setFont(Constant.MAIN_FONT);
		exeField = new JTextField();
		exeButton = new JButton("参照");
		
		okButton = new JButton("完了");
		
		// listener
		addSolverPanelListener = new AddSolverPanelListener(mainFrame, this);
		okButton.addActionListener(addSolverPanelListener);
	}
	
	private void initLayout() {
		setLayout(null);
		
		// ソルバー名
		solverNameLabel.setBounds(10, 10, 100, 30);
		solverNameField.setBounds(100, 19, 300, 20);
		// パラメータ
		paramLabel.setBounds(10, 45, 100, 30);
		sp.setBounds(10, 80, 500, 300);
		// exe
		exeLabel.setBounds(10, 392, 100, 30);
		exeField.setBounds(80, 400, 300, 20);
		exeButton.setBounds(400, 400, 100, 20);
		
		okButton.setBounds(10, 490, 100, 30);
		
		add(solverNameLabel);
		add(solverNameField);
		add(paramLabel);
		add(sp);
		add(exeLabel);
		add(exeField);
		add(exeButton);
		add(okButton);
	}
}
