package tmcit.yasu.data;

public class ConnectFileData {
	private String url, token;
	private int port;
	
	public ConnectFileData(String url0, String token0, int port0) {
		url = url0;
		token = token0;
		port = port0;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getToken() {
		return token;
	}
	
	public int getPort() {
		return port;
	}
}
