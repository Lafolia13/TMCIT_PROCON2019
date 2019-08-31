package tmcit.yasu.ui;

import java.util.ArrayList;

import javax.swing.JPanel;

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

	public SelectGamePanel(ConnectSetting connectSetting0) {
		connectSetting = connectSetting0;

		getMatchesData();
		init();
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
		GameInfoPanel gameInfoPanel = new GameInfoPanel(matchesData.get(0), startTime.get(0));

		setLayout(null);
		gameInfoPanel.setBounds(10, 10, 400, 300);
		add(gameInfoPanel);
	}
}
