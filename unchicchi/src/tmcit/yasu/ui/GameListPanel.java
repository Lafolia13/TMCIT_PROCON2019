package tmcit.yasu.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tmcit.yasu.listener.GameInfoListener;

public class GameListPanel extends JScrollPane{
	private JPanel listPanel;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	private int nowY;
	private ArrayList<GameInfoPanel> listItem;
	private int selectedIndex;

	public GameListPanel() {
		init();
		initLayout();
	}

	private void init() {
		listPanel = new JPanel();
		listItem = new ArrayList<GameInfoPanel>();
		selectedIndex = -1;

		// layout
		nowY = 0;
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.ipadx = 320;
		gbc.ipady = 130;
		gbc.insets = new Insets(5, 10, 5, 10);

		getVerticalScrollBar().setUnitIncrement(10);
	}

	private void initLayout() {
		this.setViewportView(listPanel);

		listPanel.setLayout(gbl);
	}

	public void removeAll() {
		while(!listItem.isEmpty()) {
			GameInfoPanel nowPanel = listItem.get(0);
			listItem.remove(0);
			listPanel.remove(nowPanel);
		}
		nowY = 0;
		selectedIndex = -1;
	}

	public void reflectSelectedItem(int newIndex) {
		if(selectedIndex != -1) {
			listItem.get(selectedIndex).setSelected(false);
		}
		listItem.get(newIndex).setSelected(true);
		selectedIndex = newIndex;
	}

	public void addGameInfoPanel(GameInfoPanel gameInfoPanel) {
		gameInfoPanel.addMouseListener(new GameInfoListener(this, nowY));
		gbc.gridy = nowY;
		gbl.setConstraints(gameInfoPanel, gbc);
		listPanel.add(gameInfoPanel);
		listItem.add(gameInfoPanel);
		nowY++;
	}
}
