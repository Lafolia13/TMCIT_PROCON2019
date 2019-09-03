package tmcit.yasu.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tmcit.yasu.data.Action;
import tmcit.yasu.data.Actions;
import tmcit.yasu.data.Exception401;
import tmcit.yasu.data.Field;
import tmcit.yasu.data.MatchesData;
import tmcit.yasu.exception.InvalidMatchesException;
import tmcit.yasu.exception.InvalidTokenException;
import tmcit.yasu.exception.TooEarlyException;
import tmcit.yasu.exception.UnacceptableTimeExeption;

public class Network {
	private String url = null, token = null;
	private OkHttpClient client;

	public Network(String url0, int port0, String token0) {
		url = getURL(url0, port0);
		System.out.println(url);
		token = token0;
		client = new OkHttpClient();
	}
	
	private String getURL(String url, int port) {
		int searchStartIndex = url.indexOf("//");
		int slashIndex = url.indexOf('/', searchStartIndex+2);
		if(slashIndex == -1) {
			return url + ":" + String.valueOf(port);
		}else {
			String firstStr = url.substring(0, slashIndex);
			String endStr = url.substring(slashIndex);
			String ret = firstStr + ":" + String.valueOf(port) + endStr;
			return ret;
		}
	}

	// GET /matches
	public ArrayList<MatchesData> getMatches() throws InvalidTokenException {
		Request request = new Request.Builder().url(url + "/matches").header("Authorization", token).build();

		try {
			Response response = client.newCall(request).execute();
			String json = response.body().string();
			if(response.code() == 401) {
				response.close();
				throw new InvalidTokenException("トークンが正しくありません");
			}else {
				ArrayList<MatchesData> data = new ObjectMapper().readValue(json, new TypeReference<ArrayList<MatchesData>>() {});
				response.close();
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
			Response response = client.newCall(request).execute();
			String json = response.body().string();

			if(response.code() == 401) {
				response.close();
				throw new InvalidTokenException("トークンが正しくありません");
			} else if(response.code() == 400) {
				Exception401 exception401 = new ObjectMapper().readValue(json, Exception401.class);
				if(exception401.status.equals("TooEarly")) {
					response.close();
					throw new TooEarlyException(exception401.startAtUnixTime, "試合開始前です");
				}else {
					response.close();
					throw new InvalidMatchesException(exception401.startAtUnixTime, "参加していない試合です");
				}
			}else {
				Field field = new ObjectMapper().readValue(json, Field.class);
				response.close();
				return field;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// POST /matches/{id}/action
	public Actions postAction(int id, Actions actions) throws InvalidTokenException, InvalidMatchesException, TooEarlyException, UnacceptableTimeExeption {
		try {
			String json = new ObjectMapper().writeValueAsString(actions);

			MediaType MIMEType = MediaType.parse("application/json; charset=utf-8");
			RequestBody requestBody = RequestBody.create(MIMEType, json);
			Request request = new Request.Builder().url(url + "/matches/" + String.valueOf(id) +"/action").header("Authorization", token).post(requestBody).build();

			Response response = client.newCall(request).execute();

			if(response.code() == 401) {
				response.close();
				throw new InvalidTokenException("トークンが正しくありません。");
			}else if(response.code() == 400) {
				String resJson = response.body().string();
				Exception401 exception401 = new ObjectMapper().readValue(resJson, Exception401.class);
				if(exception401.status.equals("InvalidMatches")) {
					response.close();
					throw new InvalidMatchesException(exception401.startAtUnixTime, "参加していない試合です。");
				}else if(exception401.status.equals("TooEarly")) {
					response.close();
					throw new TooEarlyException(exception401.startAtUnixTime, "試合開始前です。");
				}else {
					response.close();
					throw new UnacceptableTimeExeption(exception401.startAtUnixTime, "行動を受け付けていません。");
				}
			}

			Actions res = new ObjectMapper().readValue(response.body().string(), Actions.class);

			response.close();
			return res;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// GET /ping
	public boolean ping() throws IOException, InvalidTokenException {
		Request request = new Request.Builder().url(url + "/ping").header("Authorization", token).build();

		Response response = client.newCall(request).execute();

		if(response.code() == 200) {
			response.close();
			return true;
		}else if(response.code() == 401) {
			response.close();
			throw new InvalidTokenException("トークンが正しくありません。");
		}

		return false;
	}
}
