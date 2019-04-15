package tmcit.yasu.ui.game;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tmcit.yasu.util.Constant;

public class ExeStreamPanel extends JPanel{
	private String agentName;
	private JLabel agentNameLabel, outLabel, errLabel;
	private JScrollPane outScrollPanel, errScrollPanel;
	private JTextArea outTextArea, errTextArea;
	
	public ExeStreamPanel(String agentName0) {
		agentName = agentName0;
		init();
		initLayout();
	}
	
	private void init() {
		agentNameLabel = new JLabel(agentName);
		agentNameLabel.setFont(Constant.DEFAULT_FONT);
		
		outLabel = new JLabel("標準出力");
		outLabel.setFont(Constant.DEFAULT_FONT);
		outTextArea = new JTextArea();
		outScrollPanel = new JScrollPane(outTextArea);
		
		errLabel = new JLabel("エラー出力");
		errLabel.setFont(Constant.DEFAULT_FONT);
		errTextArea = new JTextArea();
		errScrollPanel = new JScrollPane(errTextArea);
	}
	
	private void initLayout() {
		setLayout(null);
		
		agentNameLabel.setBounds(0, 10, 200, 30);
		
		outLabel.setBounds(0, 50, 200, 30);
		outScrollPanel.setBounds(0, 90, 200, 260);
		
		errLabel.setBounds(0, 360, 200, 30);
		errScrollPanel.setBounds(0, 400, 200, 260);
		
		add(agentNameLabel);
		add(outLabel);
		add(outScrollPanel);
		add(errLabel);
		add(errScrollPanel);
	}
	
	public void addStream(String str, boolean isStdout) {
		if(isStdout) {
			outTextArea.append(str + "\n");
		}else {
			errTextArea.append(str + "\n");
		}
	}
}
