package tmcit.yasu.util;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.exception.InvalidTokenException;

public class Network {
	private String url = null, token = null;
	private OkHttpClient client;
	
	public Network(String url0, String token0) {
		url = url0;
		token = token0;
		client = new OkHttpClient();
	}
	
	// GET /matches
	public ArrayList<MatchesData> getMatches() throws InvalidTokenException {
		Request request = new Request.Builder().url(url + "/matches").header("Authorization", token).build();

		try {
			Response respones = client.newCall(request).execute();
			String json = respones.body().string();
			if(respones.code() == 401) {
				throw new InvalidTokenException("ÉgÅ[ÉNÉìÇ™ê≥ÇµÇ≠Ç†ÇËÇ‹ÇπÇÒ");
			}else {
				ArrayList<MatchesData> data = new ObjectMapper().readValue(json, new TypeReference<ArrayList<MatchesData>>() {});
				return data;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
