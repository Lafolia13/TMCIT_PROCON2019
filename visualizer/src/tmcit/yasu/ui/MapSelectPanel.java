package tmcit.yasu.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import tmcit.yasu.util.Constant;

public class MapSelectPanel extends JPanel{
	private JLabel nameLabel;
	private JScrollPane selectMapScrollPanel;
	private JList<String> selectMapList;
	private JButton addFileButton, deleteFileButton;

	public MapSelectPanel() {
		init();
		initLayout();
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("マップ選択");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		selectMapList = new JList<String>();
		selectMapScrollPanel = new JScrollPane();
		selectMapScrollPanel.getViewport().setView(selectMapList);

		addFileButton = new JButton("追加");
		deleteFileButton = new JButton("削除");
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		selectMapScrollPanel.setBounds(10, 40, 300, 200);
		addFileButton.setBounds(10, 250, 100, 30);
		deleteFileButton.setBounds(130, 250, 100, 30);

		add(nameLabel);
		add(selectMapScrollPanel);
		add(addFileButton);
		add(deleteFileButton);
	}
}
