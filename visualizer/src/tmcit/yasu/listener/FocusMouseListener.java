package tmcit.yasu.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import tmcit.yasu.ui.game.GameMainPanel;

public class FocusMouseListener implements MouseListener{
	private GameMainPanel gameMainPanel;
	
	public FocusMouseListener(GameMainPanel gameMainPanel0) {
		gameMainPanel = gameMainPanel0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		boolean ret = gameMainPanel.requestFocusInWindow();
		System.out.println("[SYS] request focus in window : " + String.valueOf(ret));
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
