package tmcit.yasu.ui.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.data.ScoreData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.game.TurnData;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.util.Constant;

public class GameLogPanel extends JPanel{
	private MainFrame mainFrame;
	
	// data
	private int nowTurn;
	private GameData gameData;
	private ArrayList<TurnData> turnDataList;
	
	// UI
	private GameInfoPanel gameInfoPanel;
	private GamePaintPanel gamePaintPanel;
	private LogControllPanel logControllPanel;
	
	public GameLogPanel(MainFrame mainFrame0, ArrayList<TurnData> turnDataList0, GameData gameData0) {
		mainFrame = mainFrame0;
		gameData = gameData0;
		turnDataList = turnDataList0;
		init();
		initLayout();
		reflectGameData();
	}
	
	private void init() {
		nowTurn = 0;
		gameInfoPanel = new GameInfoPanel();
		gamePaintPanel = new GamePaintPanel(getNowPaintGameData(), false);
		logControllPanel = new LogControllPanel(mainFrame, this);
	}
	
	private void initLayout() {
		setLayout(null);

		gameInfoPanel.setBounds(230, 10, 610, 40);
		gamePaintPanel.setBounds(230, 60, Constant.MAP_SIZE+10, Constant.MAP_SIZE+10);
		logControllPanel.setBounds(230, Constant.MAP_SIZE+80, 600, 50);
		
		add(gameInfoPanel);
		add(gamePaintPanel);
		add(logControllPanel);
	}
	
	private PaintGameData getNowPaintGameData() {
		TurnData nowTurnData = turnDataList.get(nowTurn);
		return new PaintGameData(gameData.getMapWidth(), gameData.getMapHeight(), gameData.getMapScore()
				, nowTurnData.getTerritoryMap(), nowTurnData.getMyPlayers(), nowTurnData.getRivalPlayers());
	}
	
	private void reflectGameData() {
		TurnData nowTurnData = turnDataList.get(nowTurn);
		ScoreData score = nowTurnData.calcScore();
		gameInfoPanel.reflectGameData(nowTurn, gameData.getMaxTurn(), score.myTileScore + score.myTerritoryScore, score.rivalTileScore + score.rivalTerritoryScore);
		gamePaintPanel.reflectGameData(getNowPaintGameData());
	}
	
	// •`‰æ
	public void paintNextTurn() {
		if(nowTurn + 1 < turnDataList.size()) {
			nowTurn++;
		}
		reflectGameData();
	}
	
	public void paintBackTurn() {
		if(nowTurn - 1 >= 0) {
			nowTurn--;
		}
		reflectGameData();
	}
}
