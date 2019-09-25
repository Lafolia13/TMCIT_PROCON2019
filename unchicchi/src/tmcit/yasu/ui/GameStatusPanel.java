package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.data.MatchesData;
import tmcit.yasu.util.Constant;

public class GameStatusPanel extends JPanel{
	private MatchesData matchData;

	// UI
	private JLabel idLabel, nameLabel, turnLabel, serverStatus, gameStatus;

	public GameStatusPanel(MatchesData matchData0) {
		matchData = matchData0;
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		idLabel = new JLabel("ID:" + String.valueOf(matchData.id));
		idLabel.setFont(Constant.SMALL_FONT);
		nameLabel = new JLabel("対戦相手:" + matchData.matchTo);
		nameLabel.setFont(Constant.SMALL_FONT);
		turnLabel = new JLabel("ターン:0/" + String.valueOf(matchData.turns));
		turnLabel.setFont(Constant.SMALL_FONT);
		serverStatus = new JLabel("サーバ接続状態:未取得");
		serverStatus.setFont(Constant.SMALL_FONT);
		gameStatus = new JLabel("ゲーム状態:未取得");
		gameStatus.setFont(Constant.SMALL_FONT);
	}

	private void initLayout() {
		setLayout(null);

		idLabel.setBounds(10, 10, 200, 20);
		nameLabel.setBounds(10, 40, 200, 20);
		turnLabel.setBounds(10, 70, 200, 20);
		serverStatus.setBounds(10, 100, 200, 20);
		gameStatus.setBounds(10, 130, 300, 20);

		add(idLabel);
		add(nameLabel);
		add(turnLabel);
		add(serverStatus);
		add(gameStatus);
	}

	public void changeServerStatus(String str) {
		serverStatus.setText("サーバ接続状態:" + str);
	}

	public void changeGameStatus(String str) {
		gameStatus.setText("ゲーム状態:" + str);
	}
	
	public void changeTurn(int nowTurn) {
		turnLabel.setText("ターン:" + String.valueOf(nowTurn) + "/" + String.valueOf(matchData.turns));
	}
}
