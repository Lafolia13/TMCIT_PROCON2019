package tmcit.yasu.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddSolverDialog extends JDialog implements ActionListener{
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton okButton, cancelButton;
	private boolean applied;
	private String name;

	public AddSolverDialog(Frame owner, String title) {
		super(owner, title);
		init();
		initLayout();
		setVisible(true);
	}

	private void init() {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(250, 110);

		nameLabel = new JLabel("ソルバー名:");
		nameField = new JTextField();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("キャンセル");
		cancelButton.addActionListener(this);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 100, 20);
		nameField.setBounds(120, 10, 100, 20);
		okButton.setBounds(10, 40, 100, 20);
		cancelButton.setBounds(120, 40, 100, 20);

		add(nameLabel);
		add(nameField);
		add(okButton);
		add(cancelButton);
	}

	public boolean isApplied() {
		return applied;
	}

	public String getName() {
		return name;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "OK") {
			applied = true;
			name = nameField.getText();
			dispose();
		}else if(cmd == "キャンセル"){
			applied = false;
			dispose();
		}
	}
}
