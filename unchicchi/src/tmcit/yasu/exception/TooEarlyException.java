package tmcit.yasu.exception;

public class TooEarlyException extends Exception{
	public int startUnixTime;
	public TooEarlyException(int startUnixTime0, String msg) {
		super(msg);
		startUnixTime = startUnixTime0;
	}

}
