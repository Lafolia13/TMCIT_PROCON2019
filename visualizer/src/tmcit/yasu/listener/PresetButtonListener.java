package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tmcit.yasu.ui.AddPresetDialog;
import tmcit.yasu.util.FileManager;

public class PresetButtonListener implements ActionListener{
	private JFrame owner;
	private FileManager fileManager;
	private JComboBox<String> solverComboBox, presetComboBox;

	public PresetButtonListener(JFrame owner0, FileManager fileManager0, JComboBox<String> solverComboBox0, JComboBox<String> presetComboBox0) {
		owner = owner0;
		fileManager = fileManager0;
		solverComboBox = solverComboBox0;
		presetComboBox = presetComboBox0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "�v���Z�b�g�ǉ�") {
			AddPresetDialog addDialog = new AddPresetDialog(owner, "�v���Z�b�g�̒ǉ�");
			if(addDialog.isApplied()) {
				String newPresetName = addDialog.getName();
				int solverIndex = solverComboBox.getSelectedIndex();
				String solverName = solverComboBox.getItemAt(solverIndex);
				fileManager.makeNewPreset(solverName, newPresetName);
			}
		}else if(cmd == "�v���Z�b�g�폜") {
			int solverIndex = solverComboBox.getSelectedIndex();
			String solverName = solverComboBox.getItemAt(solverIndex);
			int presetIndex = presetComboBox.getSelectedIndex();
			String presetName = presetComboBox.getItemAt(presetIndex);
			if(presetName.equals("default.txt")) {
				JOptionPane.showMessageDialog(owner, "default�͍폜�ł��܂���B", "�G���[", JOptionPane.ERROR_MESSAGE);
			}else {
				fileManager.deletePreset(solverName, presetName);
			}
		}
	}

}
