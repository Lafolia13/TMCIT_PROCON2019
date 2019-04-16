package tmcit.yasu.data;

public class GameManageData {
	private int runningGame;

	public GameManageData() {
		runningGame = 0;
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
}
