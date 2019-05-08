package tmcit.yasu.listener;

import java.io.FileNotFoundException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tmcit.yasu.ui.SettingPanel;
import tmcit.yasu.util.FileManager;
import tmcit.yasu.util.SettingManager;

public class SettingChangeListener implements ChangeListener{
	private SettingManager settingManager;
	
	public SettingChangeListener(SettingManager settingManager0) {
		settingManager = settingManager0;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		try {
			settingManager.saveSetting();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

}
