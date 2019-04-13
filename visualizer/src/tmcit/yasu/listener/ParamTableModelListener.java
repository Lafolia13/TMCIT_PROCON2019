package tmcit.yasu.listener;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import tmcit.yasu.util.FileManager;

public class ParamTableModelListener implements TableModelListener{
	private JList<String> solverList;
	private DefaultTableModel paramTableModel;
	private FileManager fileManager;
	private boolean saveFlag;
	
	public ParamTableModelListener(JList<String> solverList0, DefaultTableModel tableModel0, FileManager fileManager0) {
		solverList = solverList0;
		paramTableModel = tableModel0;
		fileManager = fileManager0;
		saveFlag = false;
	}
	
	public void setSaveFlag(boolean flag) {
		saveFlag = flag;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(!saveFlag) {
			return;
		}
		System.out.println("change");
		// get solver name
		String solverName = solverList.getSelectedValue();
		
		// get parameters
		Vector<Vector> paramDataVector = paramTableModel.getDataVector();
		ArrayList<String[]> parameters = new ArrayList<>();
		
		for(Vector nowRow : paramDataVector) {
			String[] paramData = new String[3];
			paramData[0] = (String) nowRow.get(0);
			paramData[1] = (String) nowRow.get(1);
			paramData[2] = (String) nowRow.get(2);
			
			parameters.add(paramData);
		}
		
		for(String[] now : parameters) {
			System.out.println(now[0] + " " + now[1] + " " + now[2]);
		}
		
		// save parameters
		fileManager.saveSolverParameter(solverName, "default", parameters, "");
	}

}
