package tmcit.yasu.ui.game;

import java.util.ArrayList;

import javax.swing.JPanel;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.util.Constant;

public class GameLogPanel extends JPanel{
	// data
	private ArrayList<PaintGameData> paintGameDataList;
	
	// UI
	private GamePaintPanel gamePaintPanel;
	
	public GameLogPanel(ArrayList<PaintGameData> paintGameDataList0) {
		paintGameDataList = paintGameDataList0;
		init();
		initLayout();
	}
	
	private void init() {
		gamePaintPanel = new GamePaintPanel(paintGameDataList.get(0), false);
	}
	
	private void initLayout() {
		setLayout(null);
		
		gamePaintPanel.setBounds(230, 60, Constant.MAP_SIZE+10, Constant.MAP_SIZE+10);

		add(gamePaintPanel);
	}
}
