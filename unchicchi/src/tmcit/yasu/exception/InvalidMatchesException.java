package tmcit.yasu.exception;

public class InvalidMatchesException extends Exception{
	public int startUnixTime;
	public InvalidMatchesException(int startUnixTime0, String msg) {
		super(msg);
		startUnixTime = startUnixTime0;
	}
}
