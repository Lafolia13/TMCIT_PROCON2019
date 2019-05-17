package tmcit.yasu.util;

import java.io.File;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class FileManager {
	private LookAndFeel defaultLookAndFeel;
	private File procon30Directory, powerbuffGirlsDirectory;

	public FileManager() {
		init();
		createFolder();
	}

	private void init() {
		defaultLookAndFeel = UIManager.getLookAndFeel();
		String procon30Path = System.getProperty("user.home") + "\\procon30";
		procon30Directory = new File(procon30Path);
		String powerbuffGirldPath = procon30Path + "\\powerbuffGirls";
		powerbuffGirlsDirectory = new File(powerbuffGirldPath);
	}

	private void createFolder() {
		if(!procon30Directory.isDirectory()) {
			procon30Directory.mkdir();
		}
		if(!powerbuffGirlsDirectory.isDirectory()) {
			powerbuffGirlsDirectory.mkdir();
		}
	}

	// LookAndFeel
	public void setWindowsLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public void resetLookAndFeel() {
		try {
			UIManager.setLookAndFeel(defaultLookAndFeel);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	// 再帰的にファイルを削除
	private void recursiveDeleteFile(File file) {
		if(!file.exists()) {
			return;
		}
		if(file.isDirectory()) {
			for(File child : file.listFiles()) {
				recursiveDeleteFile(child);
			}
		}
		boolean res = file.delete();
		System.out.println(res);
	}
}