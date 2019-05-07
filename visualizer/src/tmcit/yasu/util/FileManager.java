package tmcit.yasu.util;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tmcit.yasu.listener.SolverComboBoxListener;

public class FileManager {
	private LookAndFeel defaultLookAndFeel;
	private File procon30Directory, settingDirectory, mapDirectory, solverDirectory, logDirectory;

	public FileManager() {
		init();
		createFolder();
	}

	private void init() {
		defaultLookAndFeel = UIManager.getLookAndFeel();
		String procon30Path = System.getProperty("user.home") + "\\procon30";
		procon30Directory = new File(procon30Path);
		settingDirectory = new File(procon30Path.toString() + "\\setting");
		mapDirectory = new File(procon30Path.toString() + "\\map");
		solverDirectory = new File(procon30Path.toString() + "\\solver");
		logDirectory = new File(procon30Path.toString() + "\\log");
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
		if(!logDirectory.isDirectory()) {
			logDirectory.mkdir();
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

	public void makeSolverDir(String solverName) {
		File newFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName);
		if(!newFile.exists()) {
			newFile.mkdir();
		}
	}

	// 新しいプリセットを追加
	public void makeNewPreset(String solverName, String presetName) {
		File defaultFile = new File(solverDirectory.getAbsoluteFile() + "\\" + solverName + "\\default.txt");
		File newFile = new File(solverDirectory.getAbsoluteFile() + "\\" + solverName + "\\" + presetName + ".txt");
		if(newFile.exists()) return;
		try {
			Files.copy(defaultFile.toPath(), newFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// プリセットを削除
	public void deletePreset(String solverName, String presetName) {
		File deleteFile = new File(solverDirectory.getAbsoluteFile() + "\\" + solverName + "\\" + presetName);
		recursiveDeleteFile(deleteFile);
	}

	public void removeSolverDir(String solverName) {
		File removeFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName);
		System.out.println("[SYS]Delete File:" + removeFile.getAbsolutePath());
		recursiveDeleteFile(removeFile);
	}

	private String getTextExtension(String fileName) {
		if(fileName.endsWith(".txt")) return fileName;
		return fileName.concat(".txt");
	}

	// パラメータを新しく保存
	public void saveSolverParameter(String solverName, String presetName, ArrayList<String[]> parameters, String exePath) {
		presetName = getTextExtension(presetName);
		File writeFile = new File(solverDirectory.getAbsoluteFile() + "\\" + solverName + "\\" + presetName);
		if(!writeFile.exists()) {
			try {
				writeFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileWriter fw = new FileWriter(writeFile);
			fw.write(exePath + "\n");
			for(String[] nowParam : parameters) {
				String paramName = nowParam[0], paramText = nowParam[1], paramDefaultValue = nowParam[2];
				fw.write(paramName + " " + paramText + " " + paramDefaultValue + "\n");
			}
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// パラメータを上書き保存
	public void overwriteSolverParameter(String solverName, String presetName, ArrayList<String[]> parameters) {
		presetName = getTextExtension(presetName);
		File overwriteFile = new File(solverDirectory.getAbsoluteFile() + "\\" + solverName + "\\" + presetName);
		if(!overwriteFile.exists()) return;

		String exePath = getSelectedSolverExePath(solverName);
		saveSolverParameter(solverName, presetName, parameters, exePath);
	}
	
	// mapを選択したときmapDirectoryになかったらコピー
	public void mapCopyToMapDirectory(File mapFile, Component component) {
		File parent = mapFile.getParentFile();
		File newFile = new File(mapDirectory.getAbsoluteFile() + "//" + mapFile.getName());
		
		System.out.println(newFile.getAbsolutePath());
		
		if(!parent.equals(mapDirectory)) {
			
			if(newFile.exists()) {
				int optione = JOptionPane.showConfirmDialog(component, "同じファイル名が存在します。上書きますか？", "警告", JOptionPane.YES_NO_OPTION);
				if(optione == JOptionPane.NO_OPTION) {
					return;
				}
				newFile.delete();
			}
			
			try {
				Files.copy(mapFile.toPath(), newFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
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

	// util
	private String getDateString() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
		ZonedDateTime zonedDateTime = ZonedDateTime.now();
		String ret = zonedDateTime.format(f);
		return ret + ".txt";
	}


	// getter
	public File getMapDirectory() {
		return mapDirectory;
	}

	public File getProcon30Directory() {
		return procon30Directory;
	}

	public File getLogFile() {
		File logFile = logDirectory.getAbsoluteFile();
		// YYYY_MM_DD_hh_mm_ss.txt
		String fileName = getDateString();
		System.out.println(fileName);
		return new File(logFile.getAbsoluteFile() + "\\" + fileName);
	}

	public String[] getSolverList() {
		String[] solverList = solverDirectory.list();
		return solverList;
	}

	public String[] getSolverPresetList(String solverName) {
		File nowSolverDir = new File(solverDirectory.getAbsoluteFile() + "\\" + solverName);
		if(!nowSolverDir.exists()) return null;
		String[] presetList = nowSolverDir.list();
		return presetList;
	}

	public String getSelectedSolverExePath(String solverName) {
		File readFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName + "\\default.txt");

		String ret = "";
		if(readFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(readFile));
				ret = br.readLine();
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	public ArrayList<String[]> getSelectedSolverParameter(String solverName, String presetName){
		File readFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName + "\\" + presetName);

		if(!readFile.exists()) {
			return new ArrayList<String[]>();
		}

		ArrayList<String[]> ret = new ArrayList<String[]>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(readFile));

			br.readLine();

			String line;
			while((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				ret.add(split);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public ArrayList<String[]> getSelectedSolverDefaultParameter(String solverName){
		return getSelectedSolverParameter(solverName, "default.txt");
	}
}