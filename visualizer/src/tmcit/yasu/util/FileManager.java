package tmcit.yasu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

	public String[] getSolverList() {
		String[] solverList = solverDirectory.list();
		return solverList;
	}

	public void makeSolverDir(String solverName) {
		File newFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName);
		if(!newFile.exists()) {
			newFile.mkdir();
		}
	}

	public void removeSolverDir(String solverName) {
		File removeFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName);
		removeFile.delete();
	}

	public String getSelectedSolverExePath(String solverName) {
		File readFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName + "\\default.txt");

		String ret = "";
		if(readFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(readFile));
				ret = br.readLine();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	public ArrayList<String[]> getSelectedSolverParameter(String solverName){
		File readFile = new File(solverDirectory.getAbsolutePath() + "\\" + solverName + "\\default.txt");

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

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}
}