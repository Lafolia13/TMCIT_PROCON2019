package tmcit.yasu.ui;

import java.awt.Frame;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SolverAddDialog extends JDialog implements ActionListener{
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton okButton, cancelButton;
	private boolean applied;
	private String name;

	public SolverAddDialog(Frame owner, String title, int bx, int by) {
		super(owner, title);
		init(bx, by);
		initLayout();
		setVisible(true);
	}

	private void init(int bx, int by) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(bx, by, 250, 110);

		nameLabel = new JLabel("�\���o�[��:");
		nameField = new JTextField();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("�L�����Z��");
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
		}else if(cmd == "�L�����Z��"){
			applied = false;
			dispose();
		}
	}
}
