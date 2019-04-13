package tmcit.yasu.listener;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import tmcit.yasu.util.FileManager;

public class SolverListSelectionListener implements ListSelectionListener{
	private FileManager fileManager;
	private DefaultListModel<String> solverListModel;
	private JList<String> solverList;
	private DefaultTableModel paramTableModel;
	private JTextField exePathField;
	private ParamTableModelListener paramTableModelListener;

	public SolverListSelectionListener(FileManager fileManager0, JList<String> solverList0,
			DefaultListModel<String> solverListModel0, DefaultTableModel paramTableModel0, JTextField exePathField0,
			ParamTableModelListener paramTableModelListener0) {
		fileManager = fileManager0;
		solverList = solverList0;
		solverListModel = solverListModel0;
		paramTableModel = paramTableModel0;
		exePathField = exePathField0;
		paramTableModelListener = paramTableModelListener0;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		paramTableModelListener.setSaveFlag(false);

		while(paramTableModel.getRowCount() > 0) {
			paramTableModel.removeRow(0);
		}

		if(solverList.isSelectionEmpty()) return;
		int index = solverList.getSelectedIndex();
		String selectedName = solverListModel.getElementAt(index);


		ArrayList<String[]> paramList = fileManager.getSelectedSolverParameter(selectedName);
		for(String[] nowParam : paramList) {
			paramTableModel.addRow(nowParam);
		}

		String exePath = fileManager.getSelectedSolverExePath(selectedName);
		exePathField.setText(exePath);

		paramTableModelListener.setSaveFlag(true);
	}

}

