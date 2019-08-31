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
import tmcit.yasu.util.Network;

public class SelectGamePanel extends JPanel{
	private ConnectSetting connectSetting;
	private ArrayList<MatchesData> matchesData;
	private ArrayList<Integer> startTime;
	private GameListPanel gameListPanel;

	public SelectGamePanel(ConnectSetting connectSetting0) {
		connectSetting = connectSetting0;

		getMatchesData();
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

	private void init() {
		gameListPanel = new GameListPanel();

		for(int i = 0;i < matchesData.size();i++) {
			GameInfoPanel nowInfoPanel = new GameInfoPanel(matchesData.get(i), startTime.get(i));
			GameInfoPanel nowInfoPanel2 = new GameInfoPanel(matchesData.get(i), startTime.get(i));
			gameListPanel.addGameInfoPanel(nowInfoPanel);
			gameListPanel.addGameInfoPanel(nowInfoPanel2);
		}
	}

	private void initLayout() {
		setLayout(null);

		gameListPanel.setBounds(10, 10, 400, 400);

		add(gameListPanel);
	}
}
