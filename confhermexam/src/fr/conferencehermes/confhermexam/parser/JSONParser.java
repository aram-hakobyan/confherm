package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.conferencehermes.confhermexam.util.Constants;

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
			e.printStackTrace();
		}

	}

	public static ArrayList<Exam> parseExams(JSONObject json) {
		ArrayList<Exam> exams = null;
		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {
				JSONArray content = json.getJSONArray(Constants.KEY_DATA);
				exams = new ArrayList<Exam>();

				if (content.length() != 0) {
					for (int i = 0; i < content.length(); i++) {
						JSONObject obj = (JSONObject) content.get(i);
						Exam e = new Exam();
						e.setName(obj.getString("title"));
						e.setAvailableDate(obj.getString("availableDate"));
						e.setId(obj.getInt("examId"));
						e.setIsActive(obj.getInt("is_active"));
						e.setPassword(obj.getString("password"));
						e.setStatus(obj.getString("examStatus"));
						e.setTimeClose(obj.getString("timeopen"));
						e.setTimeClose(obj.getString("timeclose"));
						e.setTimeClose(obj.getString("timemodified"));
						exams.add(e);
					}

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return exams;
	}

	public static ArrayList<Profile> parseProfileData(JSONObject uJson) {

		ArrayList<Profile> uData = null;

		try {
			if (uJson.has(Constants.KEY_DATA)
					&& uJson.get(Constants.KEY_DATA) != null) {
				JSONObject obj = uJson.getJSONObject(Constants.KEY_DATA);
				uData = new ArrayList<Profile>();
				ArrayList<String> groups = new ArrayList<String>();

				if (obj.length() != 0) {

					Profile pData = new Profile();

					// JSONObject gObj = obj.getJSONObject("groups");
					// groups.add(gObj.getString(""));

					// pData.setGroups(groups);
					pData.setId(obj.getInt("userId"));
					pData.setFirstName(obj.getString("firstname"));
					pData.setLastName(obj.getString("lastname"));

					uData.add(pData);

					// Log.i("PDATA", pData + "");

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return uData;

	}

	public static void parseResults(JSONObject rJson) {

		try {
			if (rJson.has(Constants.KEY_DATA)
					&& rJson.get(Constants.KEY_DATA) != null) {
				JSONObject obj = rJson.getJSONObject(Constants.KEY_DATA);
				// rJson = new ArrayList<Profile>();
				ArrayList<String> groups = new ArrayList<String>();

				if (obj.length() != 0) {

					Profile pData = new Profile();

					// JSONObject gObj = obj.getJSONObject("groups");
					// groups.add(gObj.getString(""));

					// pData.setGroups(groups);
					int a = obj.getInt("examId");
					String a3 = obj.getString("title");
					String a1 = obj.getString("availableDate");
					String a2 = obj.getString("examStatus");
					// uData.add(pData);

					// Log.i("PDATA", pData + "");

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public static void parseExercises() {

	}

	public static void parseTrainings() {

	}

}
