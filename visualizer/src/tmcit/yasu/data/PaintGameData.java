package tmcit.yasu.data;

public class PaintGameData {
	private int mapWidth, mapHeight;
	private int[][] mapScore, territoryMap;

	public PaintGameData(int mapWidth0, int mapHeight0, int[][] mapScore0, int[][] territoryMap0) {
		mapWidth = mapWidth0;
		mapHeight = mapHeight0;
		mapScore = mapScore0;
		territoryMap = territoryMap0;
	}

	// getter
	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public int[][] getMapScore(){
		return mapScore;
	}

	public int[][] getTerritoryMap(){
		return territoryMap;
	}
}