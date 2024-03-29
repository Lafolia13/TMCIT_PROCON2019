package tmcit.yasu.ui.game;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.data.ScoreData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.game.TurnData;
import tmcit.yasu.listener.FocusMouseListener;
import tmcit.yasu.listener.GameMainPanelActionListener;
import tmcit.yasu.listener.HumanPlayerKeyListener;
import tmcit.yasu.player.ExecPlayer;
import tmcit.yasu.player.Player;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.util.Constant;

public class GameMainPanel extends JPanel{
	// data
	private MainFrame mainFrame;
	private PaintGameData paintGameData;
	
	// UI
	private GameInfoPanel gameInfoPanel;
	private GamePaintPanel gamePaintPanel;
	private ExeStreamPanel myAgentStreamPanel, rivalAgentStreamPanel;
	private JButton exitButton;

	// Player
	private Player myPlayer, rivalPlayer;
	
	// Listener
	private FocusMouseListener focusMouseListener;
	private HumanPlayerKeyListener humanPlayerKeyListener;

	public GameMainPanel(MainFrame mainFrame0, PaintGameData paintGameData0, Player myPlayer0, Player rivalPlayer0,
			HumanPlayerKeyListener humanPlayerKeyListener0) {
		mainFrame = mainFrame0;
		myPlayer = myPlayer0;
		rivalPlayer = rivalPlayer0;
		paintGameData = paintGameData0;
		humanPlayerKeyListener = humanPlayerKeyListener0;
		init();
		initLayout();
		initExecPlayerStream();
	}

	private void init() {
		gameInfoPanel = new GameInfoPanel();
		gamePaintPanel = new GamePaintPanel(paintGameData, false);
		myAgentStreamPanel = new ExeStreamPanel("MyAgent");
		rivalAgentStreamPanel = new ExeStreamPanel("RivalAgent");
		exitButton = new JButton("����");
		exitButton.setFont(Constant.DEFAULT_FONT);
		
		// listener
		focusMouseListener = new FocusMouseListener(this);

		exitButton.addActionListener(new GameMainPanelActionListener(mainFrame, this));
		addKeyListener(humanPlayerKeyListener);
		addMouseListener(focusMouseListener);
	}

	private void initLayout() {
		setLayout(null);

		myAgentStreamPanel.setBounds(10, 10, 200, 800);
		gameInfoPanel.setBounds(230, 10, 610, 40);
		gamePaintPanel.setBounds(230, 60, Constant.MAP_SIZE+10, Constant.MAP_SIZE+10);
		rivalAgentStreamPanel.setBounds(860, 10, 200, 800);
		exitButton.setBounds(230, 680, 100, 30);

		add(myAgentStreamPanel);
		add(gameInfoPanel);
		add(gamePaintPanel);
		add(rivalAgentStreamPanel);
		add(exitButton);
	}

	private void initExecPlayerStream() {
		if(myPlayer instanceof ExecPlayer) {
			((ExecPlayer) myPlayer).setExeStreamPanel(myAgentStreamPanel);
		}
		if(rivalPlayer instanceof ExecPlayer) {
			((ExecPlayer) rivalPlayer).setExeStreamPanel(rivalAgentStreamPanel);
		}
	}

	public void reflectGameData(GameData gameData, TurnData nowTurnData, ArrayList<String> myPlayerCmds, ArrayList<String> rivalPlayerCmds, Point hilightPoint) {
		PaintGameData newPaintGameData = new PaintGameData(gameData.getMapWidth(), gameData.getMapHeight(), gameData.getMapScore(), nowTurnData.getTerritoryMap()
				, nowTurnData.getMyPlayers(), nowTurnData.getRivalPlayers(), myPlayerCmds, rivalPlayerCmds, hilightPoint);
		ScoreData nowScore = nowTurnData.calcScore();

		gameInfoPanel.reflectGameData(nowTurnData.getNowTurn(), gameData.getMaxTurn(), nowScore.myTerritoryScore + nowScore.myTileScore, nowScore.rivalTerritoryScore + nowScore.rivalTileScore);
		gamePaintPanel.reflectGameData(newPaintGameData);
	}
	
	public void paintNoneArrow(boolean isMyPlayer, int playerIndex, int way) {
		gamePaintPanel.paintNoneArrow(isMyPlayer, playerIndex, way);
	}

	// getter
	public GamePaintPanel getGamePaintPanel() {
		return gamePaintPanel;
	}
	
	// 
	public void endExec() {
		if(myPlayer instanceof ExecPlayer) {
			((ExecPlayer) myPlayer).endProcess();
		}
		if(rivalPlayer instanceof ExecPlayer) {
			((ExecPlayer) rivalPlayer).endProcess();
		}
	}
}
