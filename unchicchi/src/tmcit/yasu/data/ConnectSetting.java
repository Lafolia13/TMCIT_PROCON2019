package tmcit.yasu.data;

public class ConnectSetting {
	public String url, token;
	public int port, interval;

	public ConnectSetting(String url0, String token0, int port0, int interval0) {
		url = url0;
		token = token0;
		port = port0;
		interval = interval0;
	}
}
