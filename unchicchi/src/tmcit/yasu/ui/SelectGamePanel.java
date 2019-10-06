package tmcit.yasu.ui;

import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tmcit.yasu.data.ConnectSetting;
import tmcit.yasu.data.Field;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.exception.InvalidMatchesException;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.exception.TooEarlyException;
import tmcit.yasu.listener.SelectGameListener;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;
import tmcit.yasu.util.Network;

public class SelectGamePanel extends JPanel{
	private MainFrame mainFrame;
	private ConnectSetting connectSetting;

	private JButton refreshButton, startButton;
	private ArrayList<MatchesData> matchesData;
	private ArrayList<Integer> startTime;
	private GameListPanel gameListPanel;
	private AgentSelectPanel agentSelectPanel;

	private SelectGameListener listener;

	public SelectGamePanel(MainFrame mainFrame0, ConnectSetting connectSetting0) {
		mainFrame = mainFrame0;
		connectSetting = connectSetting0;

		init();
		initLayout();
	}

	private void getMatchesData() {
		Network net = new Network(connectSetting.url, connectSetting.port, connectSetting.token);
		startTime = new ArrayList<Integer>();

		try {
			matchesData = net.getMatches();
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		}

		for(MatchesData nowMatchData : matchesData) {
			try {
				Field nowField = net.getMatcheStatus(nowMatchData.id);
				startTime.add(nowField.startedAtUnixTime);
			} catch (InvalidTokenException e) {
				startTime.add(0);
			} catch (InvalidMatchesException e) {
				startTime.add(e.startUnixTime);
			} catch (TooEarlyException e) {
				startTime.add(e.startUnixTime);
			}
		}
	}

	public void refreshGameList() {
		gameListPanel.removeAll();
		getMatchesData();
		for(int i = 0;i < matchesData.size();i++) {
			GameInfoPanel nowInfoPanel = new GameInfoPanel(matchesData.get(i), startTime.get(i));
			gameListPanel.addGameInfoPanel(nowInfoPanel);
		}
		this.revalidate();
		this.repaint();
	}

	private void init() {
		refreshButton = new JButton("更新");

		gameListPanel = new GameListPanel();
		refreshGameList();

		agentSelectPanel = new AgentSelectPanel(new FileManager());

		startButton = new JButton("開始");
		startButton.setFont(Constant.DEFAULT_FONT);

		listener = new SelectGameListener(mainFrame, this, connectSetting);
		refreshButton.addActionListener(listener);
		startButton.addActionListener(listener);
	}

	private void initLayout() {
		setLayout(null);

		refreshButton.setBounds(10, 10, 100, 30);
		gameListPanel.setBounds(10, 50, 400, 400);
		agentSelectPanel.setBounds(420, 10, 300, 400);
		startButton.setBounds(10, 460, 200, 40);

		add(refreshButton);
		add(gameListPanel);
		add(agentSelectPanel);
		add(startButton);
	}

	public MatchesData getSelectedMatch() {
		return gameListPanel.getSelectedMatch();
	}
	
	public String getSolverCmd() {
		return agentSelectPanel.getSolverCmd();
	}
}
