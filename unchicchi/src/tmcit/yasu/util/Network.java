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
	
	public Network(String url0, String token0) {
		url = url0;
		token = token0;
		client = new OkHttpClient();
	}
	
	// GET /matches
	public ArrayList<MatchesData> getMatches() throws InvalidTokenException {
		Request request = new Request.Builder().url(url + "/matches").header("Authorization", token).build();

		try {
			Response response = client.newCall(request).execute();
			String json = response.body().string();
			if(response.code() == 401) {
				throw new InvalidTokenException("�g�[�N��������������܂���");
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
			Response response = client.newCall(request).execute();
			String json = response.body().string();
			
			if(response.code() == 401) {
				throw new InvalidTokenException("�g�[�N��������������܂���");
			} else if(response.code() == 400) {
				Exception401 exception401 = new ObjectMapper().readValue(json, Exception401.class);
				if(exception401.status.equals("TooEarly")) {
					throw new TooEarlyException(exception401.startAtUnixTime, "�����J�n�O�ł�");
				}else {
					throw new InvalidMatchesException(exception401.startAtUnixTime, "�Q�����Ă��Ȃ������ł�");
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
	
	// POT /matches/{id}/action
	public Actions postAction(int id, Actions actions) throws InvalidTokenException, InvalidMatchesException, TooEarlyException, UnacceptableTimeExeption {
		try {
			String json = new ObjectMapper().writeValueAsString(actions);

			MediaType MIMEType = MediaType.parse("application/json; charset=utf-8");
			RequestBody requestBody = RequestBody.create(MIMEType, json);
			Request request = new Request.Builder().url(url + "/matches/" + String.valueOf(id) +"/action").header("Authorization", token).post(requestBody).build();
			
			Response response = client.newCall(request).execute();
			
			if(response.code() == 401) {
				throw new InvalidTokenException("�g�[�N��������������܂���B");
			}else if(response.code() == 400) {
				String resJson = response.body().string();
				Exception401 exception401 = new ObjectMapper().readValue(resJson, Exception401.class);
				if(exception401.status.equals("InvalidMatches")) {
					throw new InvalidMatchesException(exception401.startAtUnixTime, "�Q�����Ă��Ȃ������ł��B");
				}else if(exception401.status.equals("TooEarly")) {
					throw new TooEarlyException(exception401.startAtUnixTime, "�����J�n�O�ł��B");
				}else {
					throw new UnacceptableTimeExeption(exception401.startAtUnixTime, "�s�����󂯕t���Ă��܂���B");
				}
			}
			
			Actions res = new ObjectMapper().readValue(response.body().string(), Actions.class);
			
			return res;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
