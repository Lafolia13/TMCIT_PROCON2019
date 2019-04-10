package tmcit.yasu.ui;

import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;

import tmcit.yasu.util.Constant;

public class AgentSelectPanel extends JPanel{
	private boolean isMyPlayer;

	// UI
	private JLabel nameLabel, presetLabel;
	private ButtonGroup modeGroup;
	private JRadioButton humanRadio, programRadio;
	private JComboBox<String> solverComboBox, presetComboBox;
	private JTable paramTable;

	public AgentSelectPanel(boolean isMyPlayer0) {
		isMyPlayer = isMyPlayer0;
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		if(isMyPlayer) {
			nameLabel = new JLabel("エージェント1");
		}else {
			nameLabel = new JLabel("エージェント2");
		}
		nameLabel.setFont(Constant.DEFAULT_FONT);

		humanRadio = new JRadioButton("操作");
		humanRadio.setFont(Constant.DEFAULT_FONT);
		programRadio = new JRadioButton("プログラム");
		programRadio.setFont(Constant.DEFAULT_FONT);
		modeGroup = new ButtonGroup();
		modeGroup.add(humanRadio);
		modeGroup.add(programRadio);

		solverComboBox = new JComboBox<String>();
		presetComboBox = new JComboBox<String>();

		presetLabel = new JLabel("プリセット:");
		presetLabel.setFont(new Font("MS ゴシック", Font.BOLD, 15));

		paramTable = new JTable(3, 3);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		humanRadio.setBounds(10, 40, 150, 20);
		programRadio.setBounds(10, 70, 150, 20);
		solverComboBox.setBounds(160, 70, 120, 20);
		presetLabel.setBounds(10, 100, 150, 20);
		presetComboBox.setBounds(100, 100, 180, 20);
		paramTable.setBounds(10, 130, 270, 150);

		add(nameLabel);
		add(humanRadio);
		add(programRadio);
		add(solverComboBox);
		add(presetLabel);
		add(presetComboBox);
		add(paramTable);
	}
}
