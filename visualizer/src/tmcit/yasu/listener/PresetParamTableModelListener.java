package tmcit.yasu.listener;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import tmcit.yasu.util.FileManager;

public class PresetParamTableModelListener implements TableModelListener {
	private boolean isListenMode = true;
	private FileManager fileManager;
	private JComboBox<String> solverComboBox, presetComboBox;
	private DefaultTableModel paramTableModel;

	public PresetParamTableModelListener(FileManager fileManager0, JComboBox<String> solverComboBox0, JComboBox<String> presetComboBox0, DefaultTableModel paramTableModel0) {
		fileManager = fileManager0;
		solverComboBox = solverComboBox0;
		presetComboBox = presetComboBox0;
		paramTableModel = paramTableModel0;
	}

	public void setListenMode(boolean flag) {
		isListenMode = flag;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(isListenMode) {
			System.out.println("changed");
			int solverIndex = solverComboBox.getSelectedIndex();
			String solverName = solverComboBox.getItemAt(solverIndex);
			int presetIndex = presetComboBox.getSelectedIndex();
			String presetName = presetComboBox.getItemAt(presetIndex);

			// read parameters
			ArrayList<String[]> parameters = new ArrayList<String[]>();
			Vector<Vector<String>> ret = paramTableModel.getDataVector();
			for(Vector<String> now : ret) {
				String[] nowParam = {now.get(0), now.get(1), now.get(2)};
				parameters.add(nowParam);
			}

			fileManager.overwriteSolverParameter(solverName, presetName, parameters);
		}
	}

}
