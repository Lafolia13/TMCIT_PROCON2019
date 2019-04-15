package tmcit.yasu.ui.game;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.game.GameData;
import tmcit.yasu.util.Constant;

public class GameInfoPanel extends JPanel{
	private JLabel myScoreLabel, rivalScoreLabel, turnLabel;

	public GameInfoPanel() {
		init();
		initLayout();
	}

	private void init() {
		myScoreLabel = new JLabel("MyAgent:-");
		myScoreLabel.setFont(Constant.DEFAULT_FONT);
		turnLabel = new JLabel("Turn:-/-");
		turnLabel.setFont(Constant.DEFAULT_FONT);
		rivalScoreLabel = new JLabel("RivalAgent:-");
		rivalScoreLabel.setFont(Constant.DEFAULT_FONT);
	}

	private void initLayout() {
		setLayout(null);

		myScoreLabel.setBounds(0, 10, 200, 30);
		turnLabel.setBounds(200, 10, 200, 30);
		rivalScoreLabel.setBounds(400, 10, 200, 30);

		add(myScoreLabel);
		add(turnLabel);
		add(rivalScoreLabel);
	}

	public void reflectGameData(int nowTurn, int maxTurn, int myScore, int rivalScore) {
		myScoreLabel.setText("MyAgent:" + String.valueOf(myScore));
		rivalScoreLabel.setText("RivalAgent:" + String.valueOf(rivalScore));
		turnLabel.setText("Turn:" + String.valueOf(nowTurn) + "/" + String.valueOf(maxTurn));
	}
}
