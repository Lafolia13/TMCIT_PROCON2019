package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import tmcit.yasu.ui.AddPresetDialog;
import tmcit.yasu.util.FileManager;

public class PresetButtonListener implements ActionListener{
	private JFrame owner;
	private FileManager fileManager;
	private JComboBox<String> solverComboBox;

	public PresetButtonListener(JFrame owner0, FileManager fileManager0, JComboBox<String> solverComboBox0) {
		owner = owner0;
		fileManager = fileManager0;
		solverComboBox = solverComboBox0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "プリセット追加") {
			AddPresetDialog addDialog = new AddPresetDialog(owner, "プリセットの追加");
			if(addDialog.isApplied()) {
				String newPresetName = addDialog.getName();
				int solverIndex = solverComboBox.getSelectedIndex();
				String solverName = solverComboBox.getItemAt(solverIndex);
				fileManager.makeNewPreset(solverName, newPresetName);
			}
		}else if(cmd == "プリセット削除") {

		}
	}

}
