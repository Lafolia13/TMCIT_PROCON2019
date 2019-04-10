package tmcit.yasu.util;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import tmcit.yasu.game.GameData;

public class ReadMapData {
	private File mapFile;
	private BufferedReader br;
	private GameData readGameData;
	
	public ReadMapData(File mapFile0) {
		mapFile = mapFile0;
		try {
			br = new BufferedReader(new FileReader(mapFile));
			readData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readData() throws IOException {
		String line;
		int maxTurn, w, h, n;
		int[][] scoreMap;
		ArrayList<Point> myPlayers = new ArrayList<>();
		ArrayList<Point> rivalPlayers = new ArrayList<>();
		
		maxTurn = Integer.valueOf(br.readLine());
		w = Integer.valueOf(br.readLine());
		h = Integer.valueOf(br.readLine());
		
		scoreMap = new int[w][h];
		
		for(int i = 0;i < h;i++) {
			line = br.readLine();
			String[] split = line.split(" ");
			for(int j = 0;j < w;j++) {
				scoreMap[j][i] = Integer.valueOf(split[j]);
			}
		}
		
		n = Integer.valueOf(br.readLine());
		for(int i = 0;i < n;i++) {
			line = br.readLine();
			String[] split = line.split(" ");
			myPlayers.add(new Point(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
		}
		for(int i = 0;i < n;i++) {
			line = br.readLine();
			String[] split = line.split(" ");
			rivalPlayers.add(new Point(Integer.valueOf(split[0]), Integer.valueOf(split[1])));
		}
		
		readGameData = new GameData(maxTurn, w, h, scoreMap, myPlayers, rivalPlayers);
	}
	
	public GameData getReadGameData() {
		return readGameData;
	}
}
