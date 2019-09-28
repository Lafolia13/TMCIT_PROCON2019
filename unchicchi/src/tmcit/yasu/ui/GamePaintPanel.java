package tmcit.yasu.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

import tmcit.yasu.data.Agent;
import tmcit.yasu.data.Field;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.data.Team;
import tmcit.yasu.util.Constant;

public class GamePaintPanel extends JPanel {
	private Field field = null;
	private MatchesData matchData;
	
	private ArrayList<String> cmds;
	private ArrayList<Integer> ids;

	public GamePaintPanel(MatchesData matchData0) {
		matchData = matchData0;
	}

	public void drawField(Field field0) {
		field = field0;
		repaint();
	}
	
	public void drawActions(ArrayList<String> cmds0, ArrayList<Integer> ids0) {
		cmds = cmds0;
		ids = ids0;
		repaint();
	}
	
	public void resetActions() {
		cmds = new ArrayList<>();
		ids = new ArrayList<>();
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

	private void paintTerritory(Graphics2D g2) {
		int drawInterval = calcDrawInterval();

		for(int nowX = 0;nowX < field.width;nowX++) {
			for(int nowY = 0;nowY < field.height;nowY++) {
				int px = nowX*drawInterval;
				int py = nowY*drawInterval;
				int nowTerritory = field.tiled.get(nowY).get(nowX);

				if(nowTerritory == 0) {
					g2.setColor(Constant.NONE_BACK_COLOR);
				}else if(nowTerritory == matchData.teamID) {
					g2.setColor(Constant.MY_BACK_COLOR);
				}else if(nowTerritory != matchData.teamID) {
					g2.setColor(Constant.RIVAL_BACK_COLOR);
				}
				g2.fillRect(px+1, py+1, drawInterval, drawInterval);
			}
		}

		g2.setColor(Color.BLACK);
	}

	private void paintScore(Graphics2D g2) {
		g2.setFont(Constant.MAP_SCORE_FONT);

		int drawInterval = calcDrawInterval();

		for(int nowX = 0;nowX < field.width;nowX++) {
			for(int nowY = 0;nowY < field.height;nowY++) {
				int px = nowX*drawInterval;
				int py = (nowY+1)*drawInterval;

				int nowScore = field.points.get(nowY).get(nowX);
				String str = String.valueOf(nowScore);
				g2.drawString(str, px+Constant.MAP_SCORE_BIAS, py-Constant.MAP_SCORE_BIAS);
			}
		}
	}

	private void paintPlayer(Graphics2D g2, int nowPlayerX, int nowPlayerY, Color c, String str) {
		int drawInterval = calcDrawInterval();
		int px = nowPlayerX * drawInterval;
		int py = nowPlayerY * drawInterval;

		int spaceBias = (int)(drawInterval*0.1);
		int spaceSize = (int)(drawInterval*0.8);
		int bias = 6;

		g2.setColor(c);
		g2.setFont(Constant.MAP_SCORE_FONT);
		g2.fillOval(px+spaceBias, py+spaceBias, spaceSize, spaceSize);
		g2.setColor(Color.BLACK);
		g2.drawString(str, px+drawInterval/2-bias, py+drawInterval/2+bias);
	}

	private void paintPlayers(Graphics2D g2) {
		for(int teamIndex = 0;teamIndex < field.teams.size();teamIndex++) {
			Team nowTeam = field.teams.get(teamIndex);
			for(int agentIndex = 0;agentIndex < nowTeam.agents.size();agentIndex++) {
				Agent nowAgent = nowTeam.agents.get(agentIndex);

				if(nowTeam.teamID == matchData.teamID) {
					// 自分のチーム
					String str = String.valueOf(nowAgent.agentID);
					paintPlayer(g2, nowAgent.x-1, nowAgent.y-1, Constant.MY_COLOR, str);
				}else {
					// 相手のチーム
					String str = String.valueOf(nowAgent.agentID);
					paintPlayer(g2, nowAgent.x-1, nowAgent.y-1, Constant.RIVAL_COLOR, str);
				}
			}
		}
	}

	// startからendへの矢印の描画(Pointはマスの座標)
	private void paintArrow(Graphics2D g2, Point start, Point end, char action) {

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
		if(action == 'w') {
			g2.setColor(Constant.WALK_COLOR);
		}else if(action == 'e'){
			g2.setColor(Constant.ERASE_COLOR);
		}else if(action == 'n') {
			g2.setColor(Color.WHITE);
		}
		g2.fillPolygon(arrowX, arrowY, 7);
		// 枠
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2.0f));
		g2.drawPolygon(arrowX, arrowY, 7);

		g2.setColor(Color.BLACK);
	}

	private void paintActions(Graphics2D g2, ArrayList<String> playerCmds, ArrayList<Integer> agentIds) {
		if(playerCmds == null || playerCmds.size() == 0) return;
		
		for(int i = 0;i < playerCmds.size();i++) {
			String nowCmd = playerCmds.get(i);
			int nowAgentId = agentIds.get(i);
			int way = nowCmd.charAt(1) - '0';
			if(way >= 4) way--;
			Point fromP = null, toP;
			
			for(int teamIndex = 0;teamIndex < field.teams.size();teamIndex++) {
				Team nowTeam = field.teams.get(teamIndex);
				for(int agentIndex = 0;agentIndex < nowTeam.agents.size();agentIndex++) {
					Agent nowAgent = nowTeam.agents.get(agentIndex);
					if(nowTeam.teamID == matchData.teamID && nowAgent.agentID == nowAgentId) {
						fromP = new Point(nowAgent.x-1, nowAgent.y-1);
					}
				}
			}
			
			toP = new Point(fromP.x + Constant.DIR_X[way], fromP.y + Constant.DIR_Y[way]);

			if(nowCmd.charAt(0) == 'n') continue;
			paintArrow(g2, fromP, toP, nowCmd.charAt(0));
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		if(field == null) return;

		paintTerritory(g2);
		paintGrid(g2);
		paintScore(g2);
		paintPlayers(g2);
		paintActions(g2, cmds, ids);
	}
}
