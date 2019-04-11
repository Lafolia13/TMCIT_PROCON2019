package tmcit.yasu.ui;

import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddSolverDialog extends JDialog{
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton okButton, cancelButton;
	
	public AddSolverDialog(Frame owner, String title) {
		super(owner, title);
		init();
		initLayout();
	}
	
	private void init() {
		this.setSize(250, 110);
		this.setVisible(true);
		
		nameLabel = new JLabel("ソルバー名:");
		nameField = new JTextField();
		okButton = new JButton("OK");
		cancelButton = new JButton("キャンセル");
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
}
