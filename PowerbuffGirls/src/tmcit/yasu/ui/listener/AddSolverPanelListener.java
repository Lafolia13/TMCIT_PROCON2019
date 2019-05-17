package tmcit.yasu.ui.listener;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

import tmcit.yasu.ui.SolverSettingPanel;
import tmcit.yasu.ui.MainFrame;
import tmcit.yasu.ui.SolverAddDialog;
import tmcit.yasu.util.FileManager;

public class AddSolverPanelListener implements ActionListener {
	private MainFrame mainFrame;
	private SolverSettingPanel addSolverPanel;
	
	public AddSolverPanelListener(MainFrame mainFrame0, SolverSettingPanel addSolverPanel0) {
		mainFrame = mainFrame0;
		addSolverPanel = addSolverPanel0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("完了")) {
			mainFrame.removePanel(addSolverPanel);
		}else if(cmd.equals("参照")) {
			FileManager fileManager = mainFrame.getFileManager();
			fileManager.setWindowsLookAndFeel();
			JFileChooser fileChooser = new JFileChooser();
			int selected = fileChooser.showOpenDialog(mainFrame);
			if(selected == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				addSolverPanel.setExePath(file.getAbsolutePath());
			}
			fileManager.resetLookAndFeel();
		}else if(cmd.equals("パラメータ追加")) {
			addSolverPanel.addRow();
		}else if(cmd.equals("パラメータ削除")) {
			addSolverPanel.deleteRow();
		}else if(cmd.equals("追加")) {
			Rectangle mainFrameRectangle = mainFrame.getBounds();
			int bx = mainFrameRectangle.x + mainFrameRectangle.width/2 - 125;
			int by = mainFrameRectangle.y + mainFrameRectangle.height/2 - 55;
			SolverAddDialog addDialog = new SolverAddDialog(mainFrame, "ソルバー追加", bx, by);
			if(addDialog.isApplied()) {
				String solverName = addDialog.getName();
				addSolverPanel.addSolver(solverName);
			}
		}
	}

}
