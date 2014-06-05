package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;

public class JSONParser {

	public static String AUTH_KEY;

	public static void parseLoginData(String pData) {

		try {
			JSONObject jsonObj = new JSONObject(pData);
			JSONObject data = jsonObj.getJSONObject("data");
			String userId = data.getString("userId");
			AUTH_KEY = data.getString("auth_key");

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<Exam> parseExams(JSONObject json) {
		ArrayList<Exam> exams = null;
		try {
			JSONArray content = json.getJSONArray(Constants.KEY_DATA);
			exams = new ArrayList<Exam>();

			if (content.length() != 0) {
				for (int i = 0; i < content.length(); i++) {
					JSONObject obj = (JSONObject) content.get(i);
					Exam e = new Exam();
					e.setId(obj.getInt("examId"));
					e.setTitle(obj.getString("title"));
					e.setShortTitle("shrt_title");
					e.setCategoryType("categry_type");
					e.setStartDate("startdate");
					e.setDescription("description");
					exams.add(e);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		DataHolder.getInstance().setExams(exams);
		return exams;
	}

	public static Profile parseProfileData(JSONObject uJson) {
		Profile pData = new Profile();
		try {
			if (uJson.has(Constants.KEY_DATA)
					&& uJson.get(Constants.KEY_DATA) != null) {
				JSONObject obj = uJson.getJSONObject(Constants.KEY_DATA);

				HashMap<String, String> groups = new HashMap<String, String>();

				if (obj.length() != 0) {

					JSONArray groupArr = obj.getJSONArray("groups");

					for (int i = 0; i < groupArr.length(); i++) {
						JSONObject gObj = groupArr.getJSONObject(i);

						String gID = gObj.getString("groupId");
						String gName = gObj.getString("name");
						groups.put("GroudID", gID);
						groups.put("GroupName", gName);

					}

					pData.setEmailAdress(obj.getString("email"));
					pData.setUserName(obj.getString("username"));
					pData.setGroups(groups);
					pData.setId(obj.getInt("userId"));
					pData.setFirstName(obj.getString("firstname"));
					pData.setLastName(obj.getString("lastname"));

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return pData;

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

	public static Exercise parseExercises(JSONObject json) {

		Exercise exercise = new Exercise();
		ArrayList<Question> questionsList = new ArrayList<Question>();
		ArrayList<Integer> questionIds = new ArrayList<Integer>();

		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {
				questionsList = new ArrayList<Question>();

				JSONObject data = (JSONObject) json.getJSONObject("data");
				JSONObject exam = (JSONObject) data.getJSONObject("exam");
				JSONArray questions = (JSONArray) data
						.getJSONArray("questions");

				exercise.setId(exam.getInt("id"));
				exercise.setName(exam.getString("name"));
				exercise.setIntro(exam.getString("intro"));
				exercise.setTimeOpen(exam.getString("timeopen"));
				exercise.setTimeClose(exam.getString("timeclose"));
				exercise.setTimeLimit(exam.getString("timelimit"));
				exercise.setTimeModified(exam.getString("timemodified"));

				for (int k = 0; k < questions.length(); k++) {

					Question q = new Question();
					JSONObject qObj = (JSONObject) questions.get(k);
					q.setId(qObj.getInt("id"));
					q.setName(qObj.getString("name"));
					q.setType(qObj.getString("qtype"));
					q.setCreatedBy(qObj.getString("createdby"));
					q.setQuestionText(qObj.getString("questiontext"));
					if (qObj.has("multyple_choice_type")) {
						q.setMcType(qObj.getString("multyple_choice_type"));
					} else {
						q.setMcType("2");
					}

					JSONArray answers = (JSONArray) qObj
							.getJSONArray("answers");
					ArrayList<Answer> answersArrayList = new ArrayList<Answer>();
					for (int i = 0; i < answers.length(); i++) {
						JSONObject ans = (JSONObject) answers.get(i);
						Answer answer = new Answer();
						answer.setAnswer(ans.getString("answer"));
						answer.setAsnwerFormat(ans.getInt("answerformat"));
						answer.setFeedback(ans.getString("feedback"));
						answer.setFeedbackFormat(ans.getInt("feedbackformat"));
						answer.setFraction(ans.getDouble("fraction"));
						answer.setId(ans.getInt("id"));
						answer.setQuestionId(ans.getInt("question"));
						answersArrayList.add(answer);
					}

					q.setAnswers(answersArrayList);
					questionsList.add(q);
				}
				exercise.setQuestionIds(questionIds);
				exercise.setQuestions(questionsList);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		DataHolder.getInstance().getExercises().clear();
		DataHolder.getInstance().getExercises().add(exercise);
		return exercise;
	}

	public static void parseTrainings() {

	}

}
