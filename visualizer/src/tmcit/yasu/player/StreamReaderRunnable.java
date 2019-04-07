package tmcit.yasu.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamReaderRunnable implements Runnable{
	private ExecPlayer parent;
	private boolean isStdout;

	private BufferedReader reader;

	public StreamReaderRunnable(ExecPlayer parent0, InputStream inStream, boolean isStdout0) {
		parent = parent0;
		isStdout = isStdout0;
		reader = new BufferedReader(new InputStreamReader(inStream));
	}

	@Override
	public void run() {
		String line;
		try {
			while((line = reader.readLine()) != null) {
				if(isStdout) {
					parent.addCommandQueue(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
