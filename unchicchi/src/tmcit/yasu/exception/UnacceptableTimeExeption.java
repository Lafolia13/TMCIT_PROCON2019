package tmcit.yasu.exception;

public class UnacceptableTimeExeption extends Exception{
	public int startUnixTime;
	public UnacceptableTimeExeption(int startUnixTime0, String msg) {
		super(msg);
		startUnixTime = startUnixTime0;
	}

}
