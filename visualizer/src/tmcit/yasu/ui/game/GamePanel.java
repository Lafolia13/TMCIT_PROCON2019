package tmcit.yasu.ui.game;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;


import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.util.Constant;

public class GamePanel extends JPanel{
	private PaintGameData paintGameData;

	public GamePanel(PaintGameData paintGameData0) {
		paintGameData = paintGameData0;
	}

	private int calcDrawInterval() {
		int max = Math.max(paintGameData.getMapHeight(), paintGameData.getMapWidth());
		return Constant.MAP_SIZE / max;
	}

	// paint

	private void paintGrid(Graphics g) {
		int drawInterval = calcDrawInterval();

		int maxWidth = drawInterval * paintGameData.getMapWidth();
		int maxHeight = drawInterval * paintGameData.getMapHeight();

		for(int i = 0;i <= paintGameData.getMapHeight();i++) {
			g.drawLine(0, drawInterval*i, maxWidth, drawInterval*i);
		}
		for(int i = 0;i <= paintGameData.getMapWidth();i++) {
			g.drawLine(drawInterval*i, 0, drawInterval*i, maxHeight);
		}
	}

	private void paintScore(Graphics2D g2) {
		g2.setFont(Constant.MAP_SCORE_FONT);

		int drawInterval = calcDrawInterval();
		int[][] mapScore = paintGameData.getMapScore();

		for(int nowX = 0;nowX < paintGameData.getMapWidth();nowX++) {
			for(int nowY = 0;nowY < paintGameData.getMapHeight();nowY++) {
				int px = nowX*drawInterval;
				int py = (nowY+1)*drawInterval;

				String str = String.valueOf(mapScore[nowX][nowY]);
				g2.drawString(str, px+Constant.MAP_SCORE_BIAS, py-Constant.MAP_SCORE_BIAS);
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		//
		paintGrid(g);
		paintScore(g2);
	}
}
