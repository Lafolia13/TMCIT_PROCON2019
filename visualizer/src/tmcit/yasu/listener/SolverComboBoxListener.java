package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import tmcit.yasu.ui.AgentSelectPanel;

public class SolverComboBoxListener implements ActionListener{
	private AgentSelectPanel agentSelectPanel;
	private JComboBox<String> solverComboBox;

	public SolverComboBoxListener(AgentSelectPanel agentSelectPanel0, JComboBox<String> solverComboBox0) {
		agentSelectPanel = agentSelectPanel0;
		solverComboBox = solverComboBox0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int index = solverComboBox.getSelectedIndex();
		if(index == -1) return;

		String selectedSolver = solverComboBox.getItemAt(index);

		System.out.println("[SYSTEM]SolverComboBoxListener:index=" + index + ", selectedSolver=" + selectedSolver);

		agentSelectPanel.refreshPresetComboBox(selectedSolver);
	}

}
