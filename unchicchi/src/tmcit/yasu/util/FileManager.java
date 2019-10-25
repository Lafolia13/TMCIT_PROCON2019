package tmcit.yasu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.UIManager;

import tmcit.yasu.data.ConnectFileData;

public class FileManager {
	private File procon30Directory, settingDirectory, solverDirectory, logDirectory;

	public FileManager() {
		init();
		createFolder();
	}

	private void init() {
		String procon30Path = System.getProperty("user.home") + "\\procon30";
		procon30Directory = new File(procon30Path);
		settingDirectory = new File(procon30Path.toString() + "\\setting");
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
	
	// ソルバー一覧
	public String[] getSolverList() {
		String[] solverList = solverDirectory.list();
		return solverList;
	}

	// パラメータ一覧
	public String[] getSolverPresetList(String solverName) {
		File nowSolverDir = new File(solverDirectory.getAbsoluteFile() + "\\" + solverName);
		if(!nowSolverDir.exists()) return null;
		String[] presetList = nowSolverDir.list();
		return presetList;
	}

	// パラメータの値一覧
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
	
	// exeのパスを取得
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

	// URLやport, tokenの設定を取得
	public ConnectFileData getConnectFileData() {
		File readFile = new File(settingDirectory.getAbsolutePath() + "\\connectSetting.txt");
		
		String url = "example.com", token = "PROCON30_TOKEN";
		int port = 80;
		if(readFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(readFile));
				url = br.readLine();
				token = br.readLine();
				port = Integer.valueOf(br.readLine());
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				readFile.createNewFile();
				PrintWriter pw = new PrintWriter(readFile);
				pw.println(url);
				pw.println(token);
				pw.println(port);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return new ConnectFileData(url, token, port);
	}
}
