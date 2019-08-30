package tmcit.yasu.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tmcit.yasu.data.Exception401;
import tmcit.yasu.data.Field;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.exception.InvalidMatchesException;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.exception.TooEarlyException;

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
				throw new InvalidTokenException("トークンが正しくありません");
			}else {
				ArrayList<MatchesData> data = new ObjectMapper().readValue(json, new TypeReference<ArrayList<MatchesData>>() {});
				return data;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	// GET /matches/{id}
	public Field getMatcheStatus(int id) throws InvalidTokenException, InvalidMatchesException, TooEarlyException {
		Request request = new Request.Builder().url(url + "/matches/" + String.valueOf(id)).header("Authorization", token).build();
		
		try {
			Response respones = client.newCall(request).execute();
			String json = respones.body().string();
			
			if(respones.code() == 401) {
				throw new InvalidTokenException("トークンが正しくありません");
			} else if(respones.code() == 400) {
				Exception401 exception401 = new ObjectMapper().readValue(json, Exception401.class);
				if(exception401.status.equals("TooEarly")) {
					throw new TooEarlyException(exception401.startAtUnixTime, "試合開始前です");
				}else {
					throw new InvalidMatchesException(exception401.startAtUnixTime, "参加していない試合です");
				}
			}else {
				Field field = new ObjectMapper().readValue(json, Field.class);
				return field;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
