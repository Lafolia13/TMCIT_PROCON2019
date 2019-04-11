package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import tmcit.yasu.util.Constant;

public class AgentSettingPanel extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	
	private JLabel nameLabel, solverLabel, paramLabel, exeLabel;
	private JList<String> solverList;
	private JButton addSolverButton, deleteSolverButton, selectExeButton;
	private JTextField exePathField;
	private JTable paramTabel;

	public AgentSettingPanel(MainFrame mainFrame0) {
		mainFrame = mainFrame0;
		init();
		initLayout();
	}

	private void init() {
		nameLabel = new JLabel("�G�[�W�F���g�ݒ�");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		solverLabel = new JLabel("�\���o�[�ꗗ");
		solverLabel.setFont(Constant.SMALL_FONT);
		solverList = new JList<String>();
		addSolverButton = new JButton("�ǉ�");
		addSolverButton.addActionListener(this);
		deleteSolverButton = new JButton("�폜");

		paramLabel = new JLabel("�p�����[�^");
		paramLabel.setFont(Constant.SMALL_FONT);
		paramTabel = new JTable(3, 3);

		exeLabel = new JLabel("���s�R�}���h");
		exeLabel.setFont(Constant.SMALL_FONT);
		exePathField = new JTextField();
		selectExeButton = new JButton("�Q��");
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		solverLabel.setBounds(10, 40, 200, 20);
		solverList.setBounds(10, 70, 200, 300);
		addSolverButton.setBounds(10, 380, 80, 20);
		deleteSolverButton.setBounds(100, 380, 80, 20);

		paramLabel.setBounds(250, 40, 200, 20);
		paramTabel.setBounds(250, 70, 400, 300);

		exeLabel.setBounds(690, 40, 200, 20);
		exePathField.setBounds(690, 70, 300, 20);
		selectExeButton.setBounds(690, 100, 80, 20);

		add(nameLabel);
		add(solverLabel);
		add(solverList);
		add(addSolverButton);
		add(deleteSolverButton);
		add(paramLabel);
		add(paramTabel);
		add(exeLabel);
		add(exePathField);
		add(selectExeButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "�ǉ�") {
			new AddSolverDialog(mainFrame, "�ǉ�");
		}
	}
}