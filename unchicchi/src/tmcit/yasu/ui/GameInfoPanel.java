package tmcit.yasu.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import tmcit.yasu.util.Constant;

public class GameInfoPanel extends JPanel{
	private JLabel urlLabel, teamIdLabel, agentIdLabel, maxTurnLabel;
	private JTextField urlField;
	private JSpinner teamIdSpinner, agentIdSpinner, maxTurnSpinner;
	private SpinnerModel teamIdSpinnerModel, agentSpinnerModel, maxTurnSpinnerModel;


	public GameInfoPanel() {
		init();
		initLayout();
	}

	private void init() {
		urlLabel = new JLabel("URL:");
		urlLabel.setFont(Constant.SMALL_FONT);
		teamIdLabel = new JLabel("TeamID:");
		teamIdLabel.setFont(Constant.SMALL_FONT);
		agentIdLabel = new JLabel("AgnetID:");
		agentIdLabel.setFont(Constant.SMALL_FONT);
		maxTurnLabel = new JLabel("MaxTurn:");
		maxTurnLabel.setFont(Constant.SMALL_FONT);

		urlField = new JTextField();

		// spinner
		teamIdSpinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
		agentSpinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
		maxTurnSpinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
		teamIdSpinner = new JSpinner(teamIdSpinnerModel);
		agentIdSpinner = new JSpinner(agentSpinnerModel);
		maxTurnSpinner = new JSpinner(maxTurnSpinnerModel);
	}

	private void initLayout() {
		setLayout(null);

		urlLabel.setBounds(0, 0, 40, 20);
		urlField.setBounds(50, 0, 200, 20);
		teamIdLabel.setBounds(0, 30, 80, 20);
		teamIdSpinner.setBounds(90, 30, 50, 20);
		agentIdLabel.setBounds(0, 60, 80, 20);
		agentIdSpinner.setBounds(90, 60, 50, 20);
		maxTurnLabel.setBounds(0, 90, 80, 20);
		maxTurnSpinner.setBounds(90, 90, 50, 20);

		add(urlLabel);
		add(urlField);
		add(teamIdLabel);
		add(teamIdSpinner);
		add(agentIdLabel);
		add(agentIdSpinner);
		add(maxTurnLabel);
		add(maxTurnSpinner);
	}
}
