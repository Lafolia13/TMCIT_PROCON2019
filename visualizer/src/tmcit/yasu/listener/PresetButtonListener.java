package tmcit.yasu.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import tmcit.yasu.ui.AddPresetDialog;

public class PresetButtonListener implements ActionListener{
	private JFrame owner;

	public PresetButtonListener(JFrame owner0) {
		owner = owner0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "プリセット追加") {
			AddPresetDialog addDialog = new AddPresetDialog(owner, "プリセットの追加");
		}else if(cmd == "プリセット削除") {

		}
	}

}
