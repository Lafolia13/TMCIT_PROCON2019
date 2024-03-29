package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import tmcit.yasu.listener.ParamTableModelListener;
import tmcit.yasu.listener.SolverListSelectionListener;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class AgentSettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private StarterPanel starterPanel;
	private FileManager fileManager;

	private JLabel nameLabel, solverLabel, paramLabel, exeLabel;
	private JScrollPane paramScrollPanel;
	private DefaultListModel<String> solverListModel;
	private JList<String> solverList;
	private JButton addSolverButton, deleteSolverButton, selectExeButton, addParamButton, deleteParamButton;
	private JTextField exePathField;
	private DefaultTableModel paramTableModel;
	private JTable paramTable;
	private JButton okButton;

	// listener
	private ParamTableModelListener paramTableModelListener;

	public AgentSettingPanel(MainFrame mainFrame0, StarterPanel starterPanel0, FileManager fileManager0) {
		mainFrame = mainFrame0;
		starterPanel = starterPanel0;
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void init() {
		nameLabel = new JLabel("エージェント設定");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		solverLabel = new JLabel("ソルバー一覧");
		solverLabel.setFont(Constant.SMALL_FONT);
		solverListModel = new DefaultListModel<String>();
		String[] solverNameList = fileManager.getSolverList();
		for(String nowSolverName : solverNameList) {
			solverListModel.addElement(nowSolverName);
		}
		solverList = new JList<String>(solverListModel);
		addSolverButton = new JButton("追加");
		deleteSolverButton = new JButton("削除");

		paramLabel = new JLabel("パラメータ");
		paramLabel.setFont(Constant.SMALL_FONT);
		paramTableModel = new DefaultTableModel(Constant.PARAM_COLUMN_NAMES, 0);
		paramTable = new JTable(paramTableModel);
		addParamButton = new JButton("パラメータ追加");
		deleteParamButton = new JButton("パラメータ削除");
		paramScrollPanel = new JScrollPane(paramTable);
		JTableHeader paramTableHeader = paramTable.getTableHeader();
		paramTableHeader.setReorderingAllowed(false);

		exeLabel = new JLabel("実行コマンド");
		exeLabel.setFont(Constant.SMALL_FONT);
		exePathField = new JTextField();
		selectExeButton = new JButton("参照");

		okButton = new JButton("完了");

		// listener
		paramTableModelListener = new ParamTableModelListener(solverList, exePathField, paramTableModel, fileManager);

		// set listener
		addSolverButton.addActionListener(this);
		deleteSolverButton.addActionListener(this);
		addParamButton.addActionListener(this);
		deleteParamButton.addActionListener(this);
		selectExeButton.addActionListener(this);
		okButton.addActionListener(this);
		solverList.addListSelectionListener(new SolverListSelectionListener(fileManager, solverList, solverListModel, paramTableModel, exePathField, paramTableModelListener));
		paramTableModel.addTableModelListener(paramTableModelListener);
		exePathField.getDocument().addDocumentListener(paramTableModelListener);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		solverLabel.setBounds(10, 40, 200, 20);
		solverList.setBounds(10, 70, 200, 300);
		addSolverButton.setBounds(10, 380, 80, 30);
		deleteSolverButton.setBounds(100, 380, 80, 30);

		paramLabel.setBounds(250, 40, 200, 20);
		paramScrollPanel.setBounds(250, 70, 400, 300);
		addParamButton.setBounds(250, 380, 150, 30);
		deleteParamButton.setBounds(410, 380, 150, 30);

		exeLabel.setBounds(690, 40, 200, 20);
		exePathField.setBounds(690, 70, 300, 20);
		selectExeButton.setBounds(690, 100, 80, 30);

		okButton.setBounds(690, 380, 80, 30);

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
		add(okButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "追加") {
			AddSolverDialog solverDialog = new AddSolverDialog(mainFrame, "追加");
			if(solverDialog.isApplied()) {
				String addName = solverDialog.getName();
				solverListModel.addElement(addName);
				fileManager.makeSolverDir(addName);
			}
		}else if(cmd == "削除") {
			if(!solverList.isSelectionEmpty()) {
				int index = solverList.getSelectedIndex();
				String selectedSolverName = solverList.getSelectedValue();
				fileManager.removeSolverDir(selectedSolverName);
				solverListModel.remove(index);
			}
		}else if(cmd == "パラメータ追加") {
			paramTableModel.addRow(new ArrayList<String>().toArray());
		}else if(cmd == "パラメータ削除") {
			int[] selectedRows = paramTable.getSelectedRows();
			int rowCount = paramTable.getSelectedRowCount();
			for(int i = 0;i < rowCount;i++) {
				int nowRow = selectedRows[i];
				paramTableModel.removeRow(nowRow - i);
			}
		}else if(cmd == "参照") {
			fileManager.setWindowsLookAndFeel();
			JFileChooser filechooser = new JFileChooser(fileManager.getProcon30Directory());
			filechooser.setMultiSelectionEnabled(false);
			int selected = filechooser.showOpenDialog(this);
			fileManager.resetLookAndFeel();

			if(selected == JFileChooser.APPROVE_OPTION) {
				File selectedFile = filechooser.getSelectedFile();
				exePathField.setText(selectedFile.getAbsolutePath());
			}
		}else if(cmd == "完了") {
			starterPanel.refreshAgent();
			mainFrame.deleteTabbedPanel(this);
		}
	}
}
