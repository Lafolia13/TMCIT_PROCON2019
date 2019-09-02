package tmcit.yasu.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import tmcit.yasu.data.Field;
import tmcit.yasu.util.Constant;

public class GamePaintPanel extends JPanel {
	private Field field = null;
	
	public GamePaintPanel() {
	}
	
	public void drawField(Field field0) {
		field = field0;
		repaint();
	}
	
	private int calcDrawInterval() {
		int max = Math.max(field.height, field.width);
		return Constant.MAP_SIZE / max;
	}
	
	// paint
	private void paintGrid(Graphics2D g2) {
		int drawInterval = calcDrawInterval();

		int maxWidth = drawInterval * field.width;
		int maxHeight = drawInterval * field.height;

		for(int i = 0;i <= field.height;i++) {
			g2.drawLine(0, drawInterval*i, maxWidth, drawInterval*i);
		}
		for(int i = 0;i <= field.width;i++) {
			g2.drawLine(drawInterval*i, 0, drawInterval*i, maxHeight);
		}
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if(field == null) return;
		
		paintGrid(g2);
	}
}
