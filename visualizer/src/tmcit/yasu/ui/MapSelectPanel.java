package tmcit.yasu.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;

public class MapSelectPanel extends JPanel implements ActionListener{
	private LookAndFeel defaultLookAndFeel;

	// common
	private FileManager fileManager;

	private JLabel nameLabel;
	private JScrollPane selectMapScrollPanel;
	private JList<String> selectMapList;
	private DefaultListModel<String> mapListModel;
	private JButton addFileButton, deleteFileButton;

	public MapSelectPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		init();
		initLayout();
	}

	// LookAndFeel
	private void setWindowsLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void resetLookAndFeel() {
		try {
			UIManager.setLookAndFeel(defaultLookAndFeel);
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		defaultLookAndFeel = UIManager.getLookAndFeel();
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("マップ選択");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		mapListModel = new DefaultListModel<String>();
		selectMapList = new JList<String>(mapListModel);
		selectMapScrollPanel = new JScrollPane();
		selectMapScrollPanel.getViewport().setView(selectMapList);

		addFileButton = new JButton("追加");
		addFileButton.addActionListener(this);
		deleteFileButton = new JButton("削除");
		deleteFileButton.addActionListener(this);
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "追加") {
			setWindowsLookAndFeel();
			JFileChooser filechooser = new JFileChooser(fileManager.getMapDirectory());
			filechooser.setMultiSelectionEnabled(true);
			int selected = filechooser.showOpenDialog(this);
			resetLookAndFeel();

			if(selected == JFileChooser.APPROVE_OPTION) {
				File[] selectedFiles = filechooser.getSelectedFiles();
				for(File nowFile : selectedFiles) {
					mapListModel.addElement(nowFile.getAbsolutePath());
				}
			}
		}else if(cmd == "削除") {
			if(!selectMapList.isSelectionEmpty()) {
				int[] selectedIndices = selectMapList.getSelectedIndices();
				for(int i = 0;i < selectedIndices.length;i++) {
					int deleteIndex = selectedIndices[i] - i;
					mapListModel.remove(deleteIndex);
				}
			}
		}
	}
}
