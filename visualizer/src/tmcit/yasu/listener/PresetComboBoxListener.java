package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import tmcit.yasu.ui.AgentSelectPanel;

public class PresetComboBoxListener implements ActionListener{
	private AgentSelectPanel agentSelectPanel;
	private JComboBox<String> solverComboBox, presetComboBox;

	public PresetComboBoxListener(AgentSelectPanel agentSelectPanel0, JComboBox<String> solverComboBox0, JComboBox<String> presetComboBox0) {
		agentSelectPanel = agentSelectPanel0;
		solverComboBox = solverComboBox0;
		presetComboBox = presetComboBox0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int solverIndex = solverComboBox.getSelectedIndex();
		String selectedSolver = solverComboBox.getItemAt(solverIndex);

		int presetIndex = presetComboBox.getSelectedIndex();
		String selectedPreset = presetComboBox.getItemAt(presetIndex);

		agentSelectPanel.refreshParamTable(selectedSolver, selectedPreset);
	}

}
