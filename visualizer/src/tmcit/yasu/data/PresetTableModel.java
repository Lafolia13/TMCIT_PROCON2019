package tmcit.yasu.data;

import javax.swing.table.DefaultTableModel;

public class PresetTableModel extends DefaultTableModel{
	private boolean isDefault = true;


	public PresetTableModel(String[] columnsName, int rowCount) {
		super(columnsName, rowCount);
	}

	public void setDefaultFlag(boolean flag) {
		isDefault = flag;
	}

	@Override
	public boolean isCellEditable(int row, int colum) {
		if(isDefault) {
			return false;
		}else {
			if(colum < 2) return false;
			else return true;
		}
	}
}
