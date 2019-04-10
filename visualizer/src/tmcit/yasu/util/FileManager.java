package tmcit.yasu.util;

import java.io.File;

public class FileManager {
	private File procon30Directory, settingDirectory, mapDirectory, solverDirectory;

	public FileManager() {
		init();
		createFolder();
	}

	private void init() {
		String procon30Path = System.getProperty("user.home") + "\\procon30";
		procon30Directory = new File(procon30Path);
		settingDirectory = new File(procon30Path.toString() + "\\setting");
		mapDirectory = new File(procon30Path.toString() + "\\map");
		solverDirectory = new File(procon30Path.toString() + "\\solver");
	}

	private void createFolder() {
		if(!procon30Directory.isDirectory()) {
			procon30Directory.mkdir();
		}
		if(!settingDirectory.isDirectory()) {
			settingDirectory.mkdir();
		}
		if(!mapDirectory.isDirectory()) {
			mapDirectory.mkdir();
		}
		if(!solverDirectory.isDirectory()) {
			solverDirectory.mkdir();
		}
	}

	public File getMapDirectory() {
		return mapDirectory;
	}
}
