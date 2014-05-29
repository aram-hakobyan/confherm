package fr.conferencehermes.confhermexam.parser;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	public static String AUTH_KEY;

	public static void parseLoginData(String pData) {

		try {
			JSONObject jsonObj = new JSONObject(pData);

			JSONObject data = jsonObj.getJSONObject("data");
			String userId = data.getString("userId");
			AUTH_KEY = data.getString("auth_key");
			Log.d("AUTH_KEY", AUTH_KEY + "");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void parseProfileData() {

	}

	public static void parseResults() {

	}

	public static void parseExams() {

	}

	public static void parseExercises() {

	}

	public static void parseTrainings() {

	}

}
