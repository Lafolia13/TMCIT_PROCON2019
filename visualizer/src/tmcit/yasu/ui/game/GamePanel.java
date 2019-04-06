package tmcit.yasu.ui.game;

import java.awt.Color;
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

	private void paintGrid(Graphics2D g2) {
		int drawInterval = calcDrawInterval();

		int maxWidth = drawInterval * paintGameData.getMapWidth();
		int maxHeight = drawInterval * paintGameData.getMapHeight();

		for(int i = 0;i <= paintGameData.getMapHeight();i++) {
			g2.drawLine(0, drawInterval*i, maxWidth, drawInterval*i);
		}
		for(int i = 0;i <= paintGameData.getMapWidth();i++) {
			g2.drawLine(drawInterval*i, 0, drawInterval*i, maxHeight);
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

	private void paintTerritory(Graphics2D g2) {
		int drawInterval = calcDrawInterval();
		int[][] territoryMap = paintGameData.getTerritoryMap();

		for(int nowX = 0;nowX < paintGameData.getMapWidth();nowX++) {
			for(int nowY = 0;nowY < paintGameData.getMapHeight();nowY++) {
				int px = nowX*drawInterval;
				int py = nowY*drawInterval;

				if(territoryMap[nowX][nowY] == Constant.NONE_TERRITORY) {
					g2.setColor(Constant.NONE_BACK_COLOR);
				}else if(territoryMap[nowX][nowY] == Constant.MY_TERRITORY) {
					g2.setColor(Constant.MY_BACK_COLOR);
				}else if(territoryMap[nowX][nowY] == Constant.RIVAL_TERRITORY) {
					g2.setColor(Constant.RIVAL_BACK_COLOR);
				}
				g2.fillRect(px, py, drawInterval, drawInterval);
			}
		}

		g2.setColor(Color.BLACK);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		//
		paintTerritory(g2);
		paintGrid(g2);
		paintScore(g2);
	}
}
