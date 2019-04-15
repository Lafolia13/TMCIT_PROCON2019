package tmcit.yasu.player;

public class ShutdownExecPlayer extends Thread{
	private ExecPlayer execPlayer;
	
	public ShutdownExecPlayer(ExecPlayer execPlayer0) {
		execPlayer = execPlayer0;
	}
	
	@Override
	public void run() {
		super.run();
		
		execPlayer.endProcess();
	}
}
