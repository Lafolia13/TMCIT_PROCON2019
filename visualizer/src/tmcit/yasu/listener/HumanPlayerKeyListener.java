package tmcit.yasu.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import tmcit.yasu.util.Constant;

public class HumanPlayerKeyListener implements KeyListener{
	private boolean up, down, left, right;
	private int lastTypeArrow;
	private char lastTypeAction;

	public HumanPlayerKeyListener() {
		init();
	}
	
	private void init() {
		up = false;
		down = false;
		left = false;
		right = false;
		lastTypeArrow = Constant.KEY_NONE;
		lastTypeAction = '-';
	}
	
	public void resetType() {
		lastTypeAction = '-';
		lastTypeArrow = Constant.KEY_NONE;
		up = false;
		down = false;
		left = false;
		right = false;
	}
	
	// getter
	public String getLastTypeAction() {
		if(lastTypeArrow != Constant.KEY_NONE && lastTypeAction != '-') {
			return String.valueOf(lastTypeAction) + String.valueOf(lastTypeArrow);
		}
		return "";
	}
	
	public int getLastTypeWay() {
		return lastTypeArrow;
	}
	
	// key
	private void checkLastTypeArrow() {
		if(up & !down & left & !right) {
			lastTypeArrow = Constant.KEY_LEFT_UP;
		}else if(up & !down & !left & !right) {
			lastTypeArrow = Constant.KEY_UP;
		}else if(up & !down & !left & right) {
			lastTypeArrow = Constant.KEY_RIGHT_UP;
		}else if(!up & !down & left & !right) {
			lastTypeArrow = Constant.KEY_LEFT;
		}else if(!up & !down & !left & right) {
			lastTypeArrow = Constant.KEY_RIGHT;
		}else if(!up & down & left & !right) {
			lastTypeArrow = Constant.KEY_LEFT_DOWN;
		}else if(!up & down & !left & !right) {
			lastTypeArrow = Constant.KEY_DOWN;
		}else if(!up & down & !left & right) {
			lastTypeArrow = Constant.KEY_RIGHT_DOWN;
		}else {
			lastTypeArrow = Constant.KEY_NONE;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		char typeChar = e.getKeyChar();
		if(typeChar == 'w') {
			lastTypeAction = 'w';
		}else if(typeChar == 'e') {
			lastTypeAction = 'e';
		}else {
			lastTypeAction = '-';
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();

		if(id == KeyEvent.VK_UP) {
			up = true;
		}else if(id == KeyEvent.VK_DOWN) {
			down = true;
		}else if(id == KeyEvent.VK_LEFT) {
			left = true;
		}else if(id == KeyEvent.VK_RIGHT) {
			right = true;
		}
		checkLastTypeArrow();
		System.out.println("lastTypeArrow:" + lastTypeArrow);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();

		if(id == KeyEvent.VK_UP) {
			up = false;
		}else if(id == KeyEvent.VK_DOWN) {
			down = false;
		}else if(id == KeyEvent.VK_LEFT) {
			left = false;
		}else if(id == KeyEvent.VK_RIGHT) {
			right = false;
		}
		checkLastTypeArrow();
	}

}
