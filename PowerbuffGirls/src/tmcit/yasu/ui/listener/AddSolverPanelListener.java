package tmcit.yasu.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import tmcit.yasu.ui.AddSolverPanel;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.util.FileManager;

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
		if(cmd.equals("äÆóπ")) {
			mainFrame.removePanel(addSolverPanel);
		}else if(cmd.equals("éQè∆")) {
			FileManager fileManager = mainFrame.getFileManager();
			fileManager.setWindowsLookAndFeel();
			JFileChooser fileChooser = new JFileChooser();
			int selected = fileChooser.showOpenDialog(mainFrame);
			if(selected == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				addSolverPanel.setExePath(file.getAbsolutePath());
			}
			fileManager.resetLookAndFeel();
		}
	}

}
