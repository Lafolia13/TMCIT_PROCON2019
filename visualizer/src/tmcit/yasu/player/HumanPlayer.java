package tmcit.yasu.player;

import tmcit.yasu.listener.HumanPlayerKeyListener;
import tmcit.yasu.util.Constant;

public class HumanPlayer implements Player{
	private HumanPlayerKeyListener humanPlayerKeyListener;
	
	public HumanPlayer(HumanPlayerKeyListener humanPlayerKeyListener0) {
		humanPlayerKeyListener = humanPlayerKeyListener0;
	}

	@Override
	public String getAction() {
		String action;
		humanPlayerKeyListener.resetType();
		while(true) {
			action = humanPlayerKeyListener.getLastTypeAction();
			if(!action.isEmpty()) {
				break;
			}
			
			try {
				Thread.sleep(Constant.KEY_GET_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return action;
	}

	@Override
	public void input(String str) {
		
	}

}
