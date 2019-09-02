package tmcit.yasu.ui;

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
	
	public GamePaintPanel(MatchesData matchData0) {
		matchData = matchData0;
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
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		if(field == null) return;
		
		paintTerritory(g2);
		paintGrid(g2);
		paintScore(g2);
		paintPlayers(g2);
	}
}
