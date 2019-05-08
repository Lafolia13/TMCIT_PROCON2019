package tmcit.yasu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import tmcit.yasu.ui.SettingPanel;

public class SettingManager {
	private File settingFile;
	private SettingPanel settingPanel;
	
	public SettingManager(FileManager fileManager, SettingPanel settingPanel0) {
		settingFile = fileManager.getSettingFile();
		settingPanel = settingPanel0;
	}
	
	public void saveSetting() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(settingFile);
		pw.println(settingPanel.getMaxGame());
		pw.println(settingPanel.getSleepTime());
		pw.println(settingPanel.isSelectedShowActionRadioButton());
		pw.flush();
		pw.close();
	}
}
