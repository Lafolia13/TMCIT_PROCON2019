package tmcit.yasu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;

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
	
	public void loadSetting() throws IOException {
		List<String> lines = Files.readAllLines(settingFile.toPath());
		for(int i = 0;i < lines.size();i++) {
			String line = lines.get(i);
			if(i == 0) {
				settingPanel.setMaxGame(Integer.valueOf(line));
			}else if(i == 1) {
				settingPanel.setSleepTime(Integer.valueOf(line));
			}else if(i == 2) {
				settingPanel.setSelectedShowActionRadioButton(Boolean.valueOf(line));
			}
		}
	}
}
