package tmcit.yasu.util;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import tmcit.yasu.game.GameData;

public class LogManager {
	private File logFile;
	private PrintWriter pw;

	public LogManager(FileManager fileManager) {
		logFile = fileManager.getLogFile();
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		if(!logFile.exists()) {
			logFile.createNewFile();
		}

		pw = new PrintWriter(logFile);
	}
	private void logMapOrTerritory(int[][] map) {
		int w = map.length;
		int h = map[0].length;
		for(int i = 0;i < h;i++) {
			String line = "";
			for(int j = 0;j < w;j++) {
				if(j < w-1) line += String.valueOf(map[j][i]) + " ";
				else line += String.valueOf(map[j][i]);
			}
			pw.println(line);
		}
	}

	private void logPlayers(ArrayList<Point> player) {
		for(Point np : player) {
			String str = String.valueOf(np.x) + " " + String.valueOf(np.y);
			pw.println(str);
		}
	}

	public void logGameData(GameData gameData) {
		pw.println(gameData.getMaxTurn());
		pw.println(gameData.getMapWidth());
		pw.println(gameData.getMapHeight());
		logMapOrTerritory(gameData.getMapScore());
		pw.println(gameData.getHowPlayer());
		logPlayers(gameData.getMyPlayers());
		logPlayers(gameData.getRivalPlayers());
	}

	public void logTurnAction(ArrayList<String> myActions, ArrayList<String> rivalActions) {
		for(String str : myActions) {
			pw.println(str);
		}
		for(String str : rivalActions) {
			pw.println(str);
		}
	}
}
