package tmcit.yasu.ui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import tmcit.yasu.data.MatchesData;
import tmcit.yasu.util.Constant;

public class GameInfoPanel extends JPanel{
	private MatchesData matchesData;
	private int startUnixTime;

	private JLabel idLabel, rivalNameLabel, turnLabel, startTimeLabel;

	public GameInfoPanel(MatchesData matchesData0, int startUnixTime0) {
		matchesData = matchesData0;
		startUnixTime = startUnixTime0;

		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.NONE_SELECTED_LINE_BORDER);

		idLabel = new JLabel("ID:" + String.valueOf(matchesData.id));
		idLabel.setFont(Constant.SMALL_FONT);

		rivalNameLabel = new JLabel("対戦相手:" + matchesData.matchTo);
		rivalNameLabel.setFont(Constant.SMALL_FONT);

		turnLabel = new JLabel("ターン数:" + String.valueOf(matchesData.turns));
		turnLabel.setFont(Constant.SMALL_FONT);

		Date startDate = new Date((long)startUnixTime * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒");
		startTimeLabel = new JLabel("開始時刻:" + sdf.format(startDate));
		startTimeLabel.setFont(Constant.SMALL_FONT);

		long nowUnixTime = System.currentTimeMillis() / 1000L;
		if(startUnixTime < nowUnixTime) {
			startTimeLabel.setForeground(Color.red);
		}else {
			startTimeLabel.setForeground(Color.blue);
		}
	}

	private void initLayout() {
		setLayout(null);

		idLabel.setBounds(10, 10, 320, 20);
		rivalNameLabel.setBounds(10, 40, 320, 20);
		turnLabel.setBounds(10, 70, 320, 20);
		startTimeLabel.setBounds(10, 100, 320, 20);

		add(idLabel);
		add(rivalNameLabel);
		add(turnLabel);
		add(startTimeLabel);
	}

	public void setSelected(boolean selected) {
		if(selected) {
			setBorder(Constant.SELECTED_LINE_BORDER);
		}else {
			setBorder(Constant.NONE_SELECTED_LINE_BORDER);
		}
	}

	public MatchesData getMatchesData() {
		return matchesData;
	}
}
