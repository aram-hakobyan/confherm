package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
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

		DataHolder.getInstance().setExams(exams);
		return exams;
	}

	public static void parseProfileData() {

	}

	public static void parseResults() {

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
				JSONObject questions = (JSONObject) data
						.getJSONObject("questions");

				exercise.setId(exam.getInt("id"));
				exercise.setName(exam.getString("name"));
				exercise.setIntro(exam.getString("intro"));
				exercise.setTimeOpen(exam.getString("timeopen"));
				exercise.setTimeClose(exam.getString("timeclose"));
				exercise.setTimeLimit(exam.getString("timelimit"));
				exercise.setTimeModified(exam.getString("timemodified"));
				exercise.setReviewoverallfeedback(exam
						.getInt("reviewoverallfeedback"));

				for (int k = 1; k <= 3; k++) {
					Question q = new Question();
					JSONObject qObj = (JSONObject) questions
							.getJSONObject(String.valueOf(k));
					q.setId(qObj.getInt("id"));
					q.setName(qObj.getString("name"));
					q.setType(qObj.getString("qtype"));
					q.setCreatedBy(qObj.getString("createdby"));
					q.setQuestionText(qObj.getString("questiontext"));

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

		return exercise;
	}

	public static void parseTrainings() {

	}

}
