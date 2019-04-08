package tmcit.yasu.player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tmcit.yasu.util.Constant;

public class ExecPlayer implements Player{
	private ArrayList<String> cmdAndArgs;
	private Process process;

	private Queue<String> cmdQue;

	private PrintWriter stdinPrint;

	public ExecPlayer(String cmd) {
		cmdAndArgs = new ArrayList<String>(Arrays.asList(cmd.split(" ")));
		cmdQue = new ArrayDeque<String>();

		try {
			initProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initProcess() throws IOException {
		process = new ProcessBuilder(cmdAndArgs).start();

		// stream
		InputStream stdout = process.getInputStream();
		InputStream stderr = process.getErrorStream();
		OutputStream stdin = process.getOutputStream();

		// run thread
		ExecutorService execStdout = Executors.newSingleThreadExecutor();
		execStdout.submit(new StreamReaderRunnable(this, stdout, true));

		//TODO: STD ERROR

		stdinPrint = new PrintWriter(new PrintStream(stdin));

		Runtime.getRuntime().addShutdownHook(new ShutdownExecPlayer(this));
	}

	public void addCommandQueue(String str) {
		cmdQue.add(str);
	}

	public void endProcess() {
		System.out.println("Exec Destroy");
		process.destroy();
	}


	@Override
	public String getAction() {
		while(cmdQue.isEmpty()) {
			try {
				Thread.sleep(Constant.SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return cmdQue.poll();
	}

	@Override
	public void input(String str) {
		stdinPrint.println(str);
		stdinPrint.flush();
	}
}
