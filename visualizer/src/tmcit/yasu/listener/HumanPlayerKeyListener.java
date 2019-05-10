package tmcit.yasu.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HumanPlayerKeyListener implements KeyListener{

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println(e.getKeyChar());
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
