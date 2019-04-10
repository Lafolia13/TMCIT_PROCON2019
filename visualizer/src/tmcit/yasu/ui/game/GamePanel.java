package tmcit.yasu.ui.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;


import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.util.Constant;

public class GamePanel extends JPanel{
	private PaintGameData paintGameData;
	private boolean isPreviewMode;

	public GamePanel(PaintGameData paintGameData0, boolean isPreviewMode0) {
		paintGameData = paintGameData0;
		isPreviewMode = isPreviewMode0;
	}

	private int calcDrawInterval() {
		int max = Math.max(paintGameData.getMapHeight(), paintGameData.getMapWidth());
		if(isPreviewMode) {
			return Constant.PREVIEW_MAP_SIZE / max;
		}else {
			return Constant.MAP_SIZE / max;
		}
	}

	public void reflectGameData(PaintGameData newPaintGameData) {
		paintGameData = newPaintGameData;
		repaint();
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
		if(isPreviewMode) {
			g2.setFont(Constant.PREVIEW_MAP_SCORE_FONT);
		}else {
			g2.setFont(Constant.MAP_SCORE_FONT);
		}

		int drawInterval = calcDrawInterval();
		int[][] mapScore = paintGameData.getMapScore();

		for(int nowX = 0;nowX < paintGameData.getMapWidth();nowX++) {
			for(int nowY = 0;nowY < paintGameData.getMapHeight();nowY++) {
				int px = nowX*drawInterval;
				int py = (nowY+1)*drawInterval;

				String str = String.valueOf(mapScore[nowX][nowY]);
				if(isPreviewMode) g2.drawString(str, px+Constant.PREVIEW_MAP_SCORE_BIAS, py-Constant.PREVIEW_MAP_SCORE_BIAS);
				else g2.drawString(str, px+Constant.MAP_SCORE_BIAS, py-Constant.MAP_SCORE_BIAS);
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

	private void paintPlayer(Graphics2D g2, Point nowPlayer, Color c, String str) {
		int drawInterval = calcDrawInterval();
		int px = nowPlayer.x * drawInterval;
		int py = nowPlayer.y * drawInterval;

		int spaceBias = (int)(drawInterval*0.1);
		int spaceSize = (int)(drawInterval*0.8);
		int bias = 6;

		g2.setColor(c);
		g2.setFont(Constant.MAP_SCORE_FONT);
		g2.fillOval(px+spaceBias, py+spaceBias, spaceSize, spaceSize);
		g2.setColor(Color.BLACK);

		if(!isPreviewMode) g2.drawString(str, px+drawInterval/2-bias, py+drawInterval/2+bias);
	}

	private void paintPlayers(Graphics2D g2) {
		ArrayList<Point> myPlayers = paintGameData.getMyPlayers();
		ArrayList<Point> rivalPlayers = paintGameData.getRivalPlayers();

		for(int i = 0;i < myPlayers.size();i++) {
			String str = "" + (char)('a'+i);
			paintPlayer(g2, myPlayers.get(i), Constant.MY_COLOR, str);
		}
		for(int i = 0;i < rivalPlayers.size();i++) {
			String str = "" + (char)('A'+i);
			paintPlayer(g2, rivalPlayers.get(i), Constant.RIVAL_COLOR, str);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		//
		paintTerritory(g2);
		paintGrid(g2);
		paintScore(g2);
		paintPlayers(g2);
	}
}
