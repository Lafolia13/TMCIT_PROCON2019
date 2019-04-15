package tmcit.yasu.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import tmcit.yasu.ui.game.ExeStreamPanel;
import tmcit.yasu.ui.game.GameInfoPanel;

public class StreamReaderRunnable implements Runnable{
	private ExeStreamPanel exeStreamPanel = null;
	private ExecPlayer parent;
	private boolean isStdout;

	private BufferedReader reader;

	public StreamReaderRunnable(ExecPlayer parent0, InputStream inStream, boolean isStdout0) {
		parent = parent0;
		isStdout = isStdout0;
		reader = new BufferedReader(new InputStreamReader(inStream));
	}
	
	public void setExeStreamPanel(ExeStreamPanel exeStreamPanel0) {
		exeStreamPanel = exeStreamPanel0;
	}

	@Override
	public void run() {
		String line;
		try {
			while((line = reader.readLine()) != null) {
				if(exeStreamPanel != null) {
					exeStreamPanel.addStream(line, isStdout);
				}
				if(isStdout) {
					parent.addCommandQueue(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
