package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import tmcit.yasu.data.MatchesData;
import tmcit.yasu.util.Constant;

public class GameStatusPanel extends JPanel{
	private MatchesData matchData;

	// UI
	private JLabel idLabel, nameLabel, turnLabel, serverStatus, gameStatus;
	private JLabel pointLabel, myPointLabel, myTerritoryPointLabel, rivalPointLabel, rivalTerritoryPointLabel;

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
		pointLabel = new JLabel("�|�C���g:");
		pointLabel.setFont(Constant.SMALL_FONT);
		myPointLabel = new JLabel("����:-");
		myPointLabel.setFont(Constant.SMALL_FONT);
		rivalPointLabel = new JLabel("����:-");
		rivalPointLabel.setFont(Constant.SMALL_FONT);
	}

	private void initLayout() {
		setLayout(null);

		idLabel.setBounds(10, 10, 300, 20);
		nameLabel.setBounds(10, 40, 300, 20);
		turnLabel.setBounds(10, 70, 300, 20);
		serverStatus.setBounds(10, 100, 300, 20);
		gameStatus.setBounds(10, 130, 300, 20);
		pointLabel.setBounds(10, 160, 300, 20);
		myPointLabel.setBounds(40, 190, 300, 20);
		rivalPointLabel.setBounds(40, 220, 300, 20);

		add(idLabel);
		add(nameLabel);
		add(turnLabel);
		add(serverStatus);
		add(gameStatus);
		add(pointLabel);
		add(myPointLabel);
		add(rivalPointLabel);
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
	
	public void changePoint(int tilePoint, int territoryPoint, boolean isMy) {
		if(isMy) {
			myPointLabel.setText(String.format("����:%d (�^�C��=%d, �̈�=%d)", tilePoint+territoryPoint, tilePoint, territoryPoint));
		}else {
			rivalPointLabel.setText(String.format("����:%d (�^�C��=%d, �̈�=%d)", tilePoint+territoryPoint, tilePoint, territoryPoint));
		}
	}
}
