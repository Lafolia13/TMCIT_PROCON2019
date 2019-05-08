package tmcit.yasu.ui.game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;


import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.util.Constant;

public class GamePaintPanel extends JPanel{
	private PaintGameData paintGameData;
	private boolean isPreviewMode;

	public GamePaintPanel(PaintGameData paintGameData0, boolean isPreviewMode0) {
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
	
	// startからendへの矢印の描画(Pointはマスの座標)
	private void paintArrow(Graphics2D g2, Point start, Point end, boolean isWalk) {
		
		int drawInterval = calcDrawInterval();
		int plusInterval = (int)(0.5 * drawInterval);
		int sx = start.x * drawInterval + plusInterval;
		int sy = start.y * drawInterval + plusInterval;
		int ex = end.x * drawInterval + plusInterval;
		int ey = end.y * drawInterval + plusInterval;
		
		double lineLength = Math.sqrt((ex-sx)*(ex-sx) + (ey-sy)*(ey-sy)) * 0.9;
		
		double h = drawInterval / 2; // 先端の長さ
		double w = drawInterval / 2; // 先端の幅

		// 使用する変数の準備
		double vx = ex - sx;
		double vy = ey - sy;
		double v = Math.sqrt(vx*vx + vy*vy);
		double ux = vx / v;
		double uy = vy / v;
		
		// 直線部分を求める
		double llx1 = ex - uy*w*0.5 - ux*lineLength;
		double lly1 = ey + ux*w*0.5 - uy*lineLength; 
		double rrx1 = ex + uy*w*0.5 - ux*lineLength;
		double rry1 = ey - ux*w*0.5 - uy*lineLength;
		
		double llx2 = ex - uy*w*0.5 - ux*h;
		double lly2 = ey + ux*w*0.5 - uy*h; 
		double rrx2 = ex + uy*w*0.5 - ux*h;
		double rry2 = ey - ux*w*0.5 - uy*h;
		
		// 矢尻の位置を求める
		double lx = ex - uy*w - ux*h;
		double ly = ey + ux*w - uy*h;
		double rx = ex + uy*w - ux*h;
		double ry = ey - ux*w - uy*h;
		
		int[] arrowX = {(int)llx1, (int)rrx1, (int)rrx2, (int)rx, ex, (int)lx, (int)llx2};
		int[] arrowY = {(int)lly1, (int)rry1, (int)rry2, (int)ry, ey, (int)ly, (int)lly2};
		
		// 中身
		if(isWalk) {
			g2.setColor(Constant.WALK_COLOR);
		}else {
			g2.setColor(Constant.ERASE_COLOR);
		}
		g2.fillPolygon(arrowX, arrowY, 7);
		// 枠
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2.0f));
		g2.drawPolygon(arrowX, arrowY, 7);
		
		g2.setColor(Color.BLACK);
	}
	
	private void paintActions(Graphics2D g2, ArrayList<String> playerCmds, boolean isMyPlayer) {
		for(int i = 0;i < playerCmds.size();i++) {
			String nowCmd = playerCmds.get(i);
			int way = nowCmd.charAt(1) - '0';
			
			Point fromP, toP;
			if(isMyPlayer) {
				fromP = paintGameData.getMyPlayers().get(i);
			}else {
				fromP = paintGameData.getRivalPlayers().get(i);
			}
			toP = new Point(fromP.x + Constant.DIR_X[way], fromP.y + Constant.DIR_Y[way]);
			
			if(nowCmd.charAt(0) == 'w') {
				paintArrow(g2, fromP, toP, true);
			}else if(nowCmd.charAt(0) == 'e') {
				paintArrow(g2, fromP, toP, false);
			}
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
		
		paintActions(g2, paintGameData.getMyPlayerCmds(), true);
		paintActions(g2, paintGameData.getRivalPlayerCmds(), false);
	}
}
