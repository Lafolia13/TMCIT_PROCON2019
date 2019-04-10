package tmcit.yasu.ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import tmcit.yasu.data.PaintGameData;
import tmcit.yasu.game.GameData;
import tmcit.yasu.ui.game.GamePanel;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;
import tmcit.yasu.util.ReadMapData;

public class MapSelectPanel extends JPanel implements ActionListener, ListSelectionListener{
	private LookAndFeel defaultLookAndFeel;

	// common
	private FileManager fileManager;

	private JLabel nameLabel;
	private JScrollPane selectMapScrollPanel;
	private JList<String> selectMapList;
	private DefaultListModel<String> mapListModel;
	private JButton addFileButton, deleteFileButton;
	
	private JLabel previewLabel;
	private GamePanel previewPanel;
	private PaintGameData previewGameData;

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
	
	private void initPreviewGameData() {
		int[][] mapScore = new int[1][1];
		previewGameData = new PaintGameData(1, 1, mapScore, mapScore, new ArrayList<>(), new ArrayList<>());
	}

	private void init() {
		defaultLookAndFeel = UIManager.getLookAndFeel();
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("�}�b�v�I��");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		mapListModel = new DefaultListModel<String>();
		selectMapList = new JList<String>(mapListModel);
		selectMapScrollPanel = new JScrollPane();
		selectMapScrollPanel.getViewport().setView(selectMapList);
		selectMapList.addListSelectionListener(this);

		addFileButton = new JButton("�ǉ�");
		addFileButton.addActionListener(this);
		deleteFileButton = new JButton("�폜");
		deleteFileButton.addActionListener(this);
		
		previewLabel = new JLabel("�v���r���[");
		previewLabel.setFont(Constant.DEFAULT_FONT);
		initPreviewGameData();
		previewPanel = new GamePanel(previewGameData, true);
	}

	private void initLayout() {
		setLayout(null);

		nameLabel.setBounds(10, 10, 200, 20);
		selectMapScrollPanel.setBounds(10, 40, 300, 200);
		addFileButton.setBounds(10, 250, 100, 30);
		deleteFileButton.setBounds(130, 250, 100, 30);
		previewLabel.setBounds(10, 300, 200, 20);
		previewPanel.setBounds(10, 330, Constant.PREVIEW_MAP_SIZE + 10, Constant.PREVIEW_MAP_SIZE + 10);

		add(nameLabel);
		add(selectMapScrollPanel);
		add(addFileButton);
		add(deleteFileButton);
		add(previewLabel);
		add(previewPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "�ǉ�") {
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
		}else if(cmd == "�폜") {
			if(!selectMapList.isSelectionEmpty()) {
				int[] selectedIndices = selectMapList.getSelectedIndices();
				for(int i = 0;i < selectedIndices.length;i++) {
					int deleteIndex = selectedIndices[i] - i;
					mapListModel.remove(deleteIndex);
				}
			}
		}
		
	}

	private void reflectPreviewData(ReadMapData readMapData) {
		GameData readData = readMapData.getReadGameData();
		int w = readData.getMapWidth(), h = readData.getMapHeight();
		ArrayList<Point> myPlayers = readData.getMyPlayers();
		ArrayList<Point> rivalPlayers = readData.getRivalPlayers();
		
		int[][] territoryMap = new int[w][h];
		for(Point myPoint : myPlayers) {
			territoryMap[myPoint.x][myPoint.y] = Constant.MY_TERRITORY;
		}
		for(Point rivalPoint : rivalPlayers) {
			territoryMap[rivalPoint.x][rivalPoint.y] = Constant.RIVAL_TERRITORY;
		}
		
		previewGameData = new PaintGameData(w, h, readData.getMapScore(), territoryMap, myPlayers, rivalPlayers);
		previewPanel.reflectGameData(previewGameData);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!selectMapList.isSelectionEmpty()) {
			String selectedValue = selectMapList.getSelectedValue();
			ReadMapData readMapData = new ReadMapData(new File(selectedValue));
			reflectPreviewData(readMapData);
		}
	}
}