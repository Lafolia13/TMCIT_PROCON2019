package tmcit.yasu.ui.game;

import java.awt.Graphics;

import javax.swing.JPanel;


import tmcit.yasu.data.GameData;
import tmcit.yasu.util.Constant;

public class GamePanel extends JPanel{
	private GameData gameData;
	
	public GamePanel(GameData gameData0) {
		gameData = gameData0;
	}
	
	
	private void paintGrid(Graphics g) {
		int max = Math.max(gameData.getMapHeight(), gameData.getMapWidth());
		
		int drawInterval = (Constant.MAP_SIZE / max);
		
		int maxWidth = drawInterval * gameData.getMapWidth();
		int maxHeight = drawInterval * gameData.getMapHeight();
		
		for(int i = 0;i <= gameData.getMapHeight();i++) {
			g.drawLine(0, drawInterval*i, maxWidth, drawInterval*i);
		}
		for(int i = 0;i <= gameData.getMapWidth();i++) {
			g.drawLine(drawInterval*i, 0, drawInterval*i, maxHeight);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//
		paintGrid(g);
	}
}
