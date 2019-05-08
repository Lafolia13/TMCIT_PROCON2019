package tmcit.yasu.data;

public class GameManageData {
	private int runningGame, maxGame;

	public GameManageData(int maxGame0) {
		runningGame = 0;
		maxGame = maxGame0;
	}

	public void plusRunningGame() {
		runningGame++;
	}

	public void minusRunningGame() {
		runningGame--;
	}

	// getter
	public int getRunningGame() {
		return runningGame;
	}

	public int getMaxGame() {
		return maxGame;
	}
}
