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
import tmcit.yasu.ui.game.GamePaintPanel;
import tmcit.yasu.util.Constant;
import tmcit.yasu.util.FileManager;
import tmcit.yasu.util.ReadMapData;

public class MapSelectPanel extends JPanel implements ActionListener, ListSelectionListener{
	// common
	private FileManager fileManager;

	private JLabel nameLabel;
	private JScrollPane selectMapScrollPanel;
	private JList<String> selectMapList;
	private DefaultListModel<String> mapListModel;
	private JButton addFileButton, deleteFileButton;

	private JLabel previewLabel;
	private GamePaintPanel previewPanel;
	private PaintGameData previewGameData;

	public MapSelectPanel(FileManager fileManager0) {
		fileManager = fileManager0;
		init();
		initLayout();
	}

	private void initPreviewGameData() {
		int[][] mapScore = new int[1][1];
		int[][] territoryMap = new int[1][1];
		territoryMap[0][0] = Constant.NONE_TERRITORY;
		previewGameData = new PaintGameData(1, 1, mapScore, territoryMap, new ArrayList<>(), new ArrayList<>());
	}

	private void init() {
		setBorder(Constant.DEFAULT_LINE_BORDER);

		nameLabel = new JLabel("マップ選択");
		nameLabel.setFont(Constant.DEFAULT_FONT);

		mapListModel = new DefaultListModel<String>();
		selectMapList = new JList<String>(mapListModel);
		selectMapScrollPanel = new JScrollPane();
		selectMapScrollPanel.getViewport().setView(selectMapList);
		selectMapList.addListSelectionListener(this);

		addFileButton = new JButton("追加");
		addFileButton.addActionListener(this);
		deleteFileButton = new JButton("削除");
		deleteFileButton.addActionListener(this);

		previewLabel = new JLabel("プレビュー");
		previewLabel.setFont(Constant.DEFAULT_FONT);
		initPreviewGameData();
		previewPanel = new GamePaintPanel(previewGameData, true);
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

	public ArrayList<GameData> getGameDataList(){
		ArrayList<GameData> gameDataList = new ArrayList<GameData>();

		int mapListSize = mapListModel.getSize();
		for(int index = 0;index < mapListSize;index++) {
			String nowMapPath = mapListModel.getElementAt(index);
			ReadMapData readMap = new ReadMapData(new File(nowMapPath));
			gameDataList.add(readMap.getReadGameData());
		}

		return gameDataList;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd == "追加") {
			fileManager.setWindowsLookAndFeel();
			JFileChooser filechooser = new JFileChooser(fileManager.getMapDirectory());
			filechooser.setMultiSelectionEnabled(true);
			int selected = filechooser.showOpenDialog(this);
			fileManager.resetLookAndFeel();

			if(selected == JFileChooser.APPROVE_OPTION) {
				File[] selectedFiles = filechooser.getSelectedFiles();
				for(File nowFile : selectedFiles) {
					fileManager.mapCopyToMapDirectory(nowFile, this);
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

	private void reflectPreviewData(ReadMapData readMapData) {
		GameData readData = readMapData.getReadGameData();
		int w = readData.getMapWidth(), h = readData.getMapHeight();
		ArrayList<Point> myPlayers = readData.getMyPlayers();
		ArrayList<Point> rivalPlayers = readData.getRivalPlayers();

		int[][] territoryMap = new int[w][h];
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				territoryMap[j][i] = Constant.NONE_TERRITORY;
			}
		}
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
