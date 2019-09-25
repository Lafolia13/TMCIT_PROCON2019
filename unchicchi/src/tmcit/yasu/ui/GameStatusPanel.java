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
		nameLabel = new JLabel("�ΐ푊��:" + matchData.matchTo);
		nameLabel.setFont(Constant.SMALL_FONT);
		turnLabel = new JLabel("�^�[��:0/" + String.valueOf(matchData.turns));
		turnLabel.setFont(Constant.SMALL_FONT);
		serverStatus = new JLabel("�T�[�o�ڑ����:���擾");
		serverStatus.setFont(Constant.SMALL_FONT);
		gameStatus = new JLabel("�Q�[�����:���擾");
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
		serverStatus.setText("�T�[�o�ڑ����:" + str);
	}

	public void changeGameStatus(String str) {
		gameStatus.setText("�Q�[�����:" + str);
	}
	
	public void changeTurn(int nowTurn) {
		turnLabel.setText("�^�[��:" + String.valueOf(nowTurn) + "/" + String.valueOf(matchData.turns));
	}
}
