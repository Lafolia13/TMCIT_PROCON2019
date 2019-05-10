package tmcit.yasu.player;

import tmcit.yasu.listener.HumanPlayerKeyListener;
import tmcit.yasu.ui.game.GameMainPanel;
import tmcit.yasu.util.Constant;

public class HumanPlayer implements Player{
	private HumanPlayerKeyListener humanPlayerKeyListener;
	private GameMainPanel gameMainPanel;
	private boolean isMyPlayer;
	private int nowPlayerIndex, maxPlayer;
	
	public HumanPlayer(HumanPlayerKeyListener humanPlayerKeyListener0) {
		humanPlayerKeyListener = humanPlayerKeyListener0;
	}
	
	public void setGameMainPanelAndInfo(GameMainPanel gameMainPanel0, int maxPlayer0, boolean isMyPlayer0) {
		gameMainPanel = gameMainPanel0;
		maxPlayer = maxPlayer0;
		isMyPlayer = isMyPlayer0;
		nowPlayerIndex = 0;
	}
	
	private void paintArrow(int way) {
		gameMainPanel.paintNoneArrow(isMyPlayer, nowPlayerIndex, way);
	}

	@Override
	public String getAction() {
		String action;
		humanPlayerKeyListener.resetType();
		while(true) {
			action = humanPlayerKeyListener.getLastTypeAction();
			if(!action.isEmpty()) {
				paintArrow(4);
				break;
			}
			// –îˆó•`‰æ(Œˆ‚ß‚Ä‚È‚¢)
			int way = humanPlayerKeyListener.getLastTypeWay();
			paintArrow(way);
			
			try {
				Thread.sleep(Constant.KEY_GET_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		nowPlayerIndex++;
		nowPlayerIndex = nowPlayerIndex % maxPlayer;
		return action;
	}

	@Override
	public void input(String str) {
		
	}

}
