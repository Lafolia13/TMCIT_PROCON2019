package tmcit.yasu.listener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class PresetParamTableModelListener implements TableModelListener {
	private boolean isListenMode = true;

	public void setListenMode(boolean flag) {
		isListenMode = flag;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(isListenMode) {
			System.out.println("changed");
		}
	}

}
