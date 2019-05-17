package tmcit.yasu.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import tmcit.yasu.ui.AddSolverPanel;
import tmcit.yasu.ui.MainFrame;

public class AddSolverPanelListener implements ActionListener {
	private MainFrame mainFrame;
	private AddSolverPanel addSolverPanel;
	
	public AddSolverPanelListener(MainFrame mainFrame0, AddSolverPanel addSolverPanel0) {
		mainFrame = mainFrame0;
		addSolverPanel = addSolverPanel0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Š®—¹")) {
			mainFrame.removePanel(addSolverPanel);
		}
	}

}
