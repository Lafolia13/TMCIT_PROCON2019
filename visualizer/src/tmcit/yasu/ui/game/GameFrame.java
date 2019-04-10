package tmcit.yasu.ui.game;

import javax.swing.JFrame;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.util.Constant;

public class GameFrame extends JFrame{
	private PaintGameData gameData;
	private GamePanel gamePanel;

	public GameFrame(PaintGameData gameData0) {
		gameData = gameData0;

		initLayout();
		init();
	}

	private void init() {
		setSize(700, 700);
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initLayout() {
		this.setLayout(null);

		gamePanel = new GamePanel(gameData, false);
		gamePanel.setBounds(10, 10, Constant.MAP_SIZE+10, Constant.MAP_SIZE+10);

		this.add(gamePanel);
	}

	public void reflectGameData(PaintGameData newPaintGameData) {
		gamePanel.reflectGameData(newPaintGameData);
	}
}
