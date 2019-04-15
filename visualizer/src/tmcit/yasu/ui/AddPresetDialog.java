package tmcit.yasu.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddPresetDialog extends JDialog implements ActionListener{
	private JFrame mainFrame;

	// UI
	private JLabel presetNameLabel;
	private JTextField presetNameField;
	private JButton okButton, cancelButton;
	private boolean applied;
	private String presetName;

	public AddPresetDialog(Frame owner, String title) {
		super(owner, title);
		if(owner instanceof JFrame) {
			mainFrame = (JFrame)owner;
		}
		init();
		initLayout();
		setVisible(true);
	}

	private void init() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(250, 110);
		int mainFrameX = mainFrame.getX(), mainFrameY = mainFrame.getY();
		int mainFrameWidth = mainFrame.getWidth(), mainFrameHeight = mainFrame.getHeight();
		setBounds(mainFrameX + (mainFrameWidth - 250) / 2, mainFrameY + (mainFrameHeight - 110) / 2, 250, 110);

		presetNameLabel = new JLabel("プリセット名:");
		presetNameField = new JTextField();
		okButton = new JButton("OK");
		cancelButton = new JButton("キャンセル");

		// listener
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}

	private void initLayout() {
		setLayout(null);

		presetNameLabel.setBounds(10, 10, 100, 20);
		presetNameField.setBounds(120, 10, 100, 20);
		okButton.setBounds(10, 40, 100, 20);
		cancelButton.setBounds(120, 40, 100, 20);

		add(presetNameLabel);
		add(presetNameField);
		add(okButton);
		add(cancelButton);
	}

	public boolean isApplied() {
		return applied;
	}

	public String getName() {
		return presetName;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "OK") {
			applied = true;
			presetName = presetNameField.getText();
			dispose();
		}else if(cmd == "キャンセル") {
			applied = false;
			dispose();
		}
	}

}
