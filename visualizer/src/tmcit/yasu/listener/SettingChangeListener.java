package tmcit.yasu.listener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.yasu.ui.SettingPanel;
import tmcit.yasu.util.FileManager;

public class SettingChangeListener implements ChangeListener{
	private SettingPanel settingPanel;
	
	public SettingChangeListener(SettingPanel settingPanel0) {
		settingPanel = settingPanel0;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		System.out.println("[SYS] Setting Changed.");
	}

}
