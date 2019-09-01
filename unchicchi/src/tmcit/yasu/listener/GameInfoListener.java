package tmcit.yasu.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tmcit.yasu.ui.GameInfoPanel;
import tmcit.yasu.ui.GameListPanel;

public class GameInfoListener implements MouseListener{
	private GameListPanel gameListPanel;
	private int index;

	public GameInfoListener(GameListPanel gameListPanel0, int index0) {
		gameListPanel = gameListPanel0;
		index = index0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		gameListPanel.reflectSelectedItem(index);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
