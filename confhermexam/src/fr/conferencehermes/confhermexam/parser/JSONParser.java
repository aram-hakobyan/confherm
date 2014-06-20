package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;

public class JSONParser {

	public static String AUTH_KEY;

	public static void parseLoginData(String pData) {

		try {
			JSONObject jsonObj = new JSONObject(pData);
			JSONObject data = jsonObj.getJSONObject("data");
			String userId = data.getString("user_id");
			AUTH_KEY = data.getString("auth_key");

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public static ArrayList<Training> parseTrainings(JSONObject json) {
		ArrayList<Training> trainings = null;
		try {
			JSONArray content = json.getJSONArray(Constants.KEY_DATA);
			trainings = new ArrayList<Training>();

			if (content.length() != 0) {
				for (int i = 0; i < content.length(); i++) {
					JSONObject obj = (JSONObject) content.get(i);
					Training t = new Training();
					t.setId(obj.getInt("training_id"));
					t.setTitle(obj.getString("title"));
					t.setCategoryType(obj.getString("category_type"));
					trainings.add(t);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		DataHolder.getInstance().setTrainings(trainings);
		return trainings;
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

						String gID = gObj.getString("group_id");
						String gName = gObj.getString("name");
						groups.put("GroudID", gID);
						groups.put("GroupName", gName);

					}

					pData.setEmailAdress(obj.getString("email"));
					pData.setUserName(obj.getString("username"));
					pData.setAuthKey(obj.getString("auth_key"));
					pData.setGroups(groups);
					pData.setId(obj.getInt("user_id"));
					pData.setFirstName(obj.getString("firstname"));
					pData.setLastName(obj.getString("lastname"));

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return pData;

	}

	public static ArrayList<Result> parseResults(JSONObject rJson) {
		ArrayList<Result> examsResult = new ArrayList<Result>();
		try {
			if (rJson.has(Constants.KEY_DATA)
					&& rJson.get(Constants.KEY_DATA) != null) {
				JSONObject obj = rJson.getJSONObject(Constants.KEY_DATA);

				if (obj.length() != 0) {

					JSONArray data = rJson.getJSONArray("data");

					for (int i = 0; i < data.length(); i++) {
						Result r = new Result();
						// JSONObject obj = data.getJSONObject(i);

						r.setExamId(obj.getInt("exam_id"));
						r.setExamName(obj.getString("exam_name"));

						examsResult.add(r);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return examsResult;

	}

	public static ArrayList<TrainingExercise> parseTrainingExercises(
			JSONObject json) {
		ArrayList<TrainingExercise> exercises = new ArrayList<TrainingExercise>();

		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {

				JSONArray data = json.getJSONArray("data");

				for (int i = 0; i < data.length(); i++) {
					TrainingExercise t = new TrainingExercise();
					JSONObject obj = data.getJSONObject(i);

					t.setExercise_id(obj.getInt("exercise_id"));
					t.setTitle(obj.getString("title"));

					exercises.add(t);

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		DataHolder.getInstance().setTrainingExercises(exercises);
		return exercises;
	}

	public static Exercise parseExercise(JSONObject json) {

		Exercise exercise = new Exercise();
		ArrayList<Question> questionsList = new ArrayList<Question>();
		ArrayList<Integer> questionIds = new ArrayList<Integer>();

		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {
				questionsList = new ArrayList<Question>();

				JSONObject data = (JSONObject) json.getJSONObject("data");
				JSONArray questions = (JSONArray) data
						.getJSONArray("questions");

				exercise.setId(data.getInt("exercise_id"));
				exercise.setName(data.getString("name"));
				exercise.setType(data.getString("type"));
				exercise.setText(data.getString("text"));

				for (int k = 0; k < questions.length(); k++) {

					Question q = new Question();
					JSONObject qObj = (JSONObject) questions.get(k);
					q.setId(qObj.getInt("question_id"));
					q.setType(qObj.getString("question_type"));
					q.setQuestionText(qObj.getString("question_text"));
					q.setMcType(qObj.getString("correction"));

					JSONArray answers = (JSONArray) qObj
							.getJSONArray("answers");
					ArrayList<Answer> answersArrayList = new ArrayList<Answer>();
					for (int i = 0; i < answers.length(); i++) {
						JSONObject ans = (JSONObject) answers.get(i);
						Answer answer = new Answer();
						answer.setId(ans.getInt("answer_id"));
						answer.setAnswer(ans.getString("name"));
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
