package tmcit.yasu.ui.game;

import javax.swing.JPanel;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.data.ScoreData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.game.TurnData;
import tmcit.yasu.util.Constant;

public class GameMainPanel extends JPanel{
	// data
	private PaintGameData paintGameData;
	
	// UI
	private GameInfoPanel gameInfoPanel;
	private GamePaintPanel gamePaintPanel;
	
	public GameMainPanel(PaintGameData paintGameData0) {
		paintGameData = paintGameData0;
		init();
		initLayout();
	}
	
	private void init() {
		gameInfoPanel = new GameInfoPanel();
		gamePaintPanel = new GamePaintPanel(paintGameData, false);
	}
	
	private void initLayout() {
		setLayout(null);
		
		gameInfoPanel.setBounds(200, 10, 610, 40);
		gamePaintPanel.setBounds(200, 60, Constant.MAP_SIZE+10, Constant.MAP_SIZE+10);
		
		add(gameInfoPanel);
		add(gamePaintPanel);
	}
	
	public void reflectGameData(GameData gameData, TurnData nowTurnData) {
		PaintGameData newPaintGameData = new PaintGameData(gameData.getMapWidth(), gameData.getMapHeight(), gameData.getMapScore(), nowTurnData.getTerritoryMap(), nowTurnData.getMyPlayers(), nowTurnData.getRivalPlayers());
		ScoreData nowScore = nowTurnData.calcScore();

		gameInfoPanel.reflectGameData(nowTurnData.getNowTurn(), gameData.getMaxTurn(), nowScore.myTerritoryScore + nowScore.myTileScore, nowScore.rivalTerritoryScore + nowScore.rivalTileScore);
		gamePaintPanel.reflectGameData(newPaintGameData);
	}
	
	// getter
	public GamePaintPanel getGamePaintPanel() {
		return gamePaintPanel;
	}
}
