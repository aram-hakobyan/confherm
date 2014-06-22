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
				// JSONObject obj = rJson.getJSONObject(Constants.KEY_DATA);
				JSONArray data = rJson.getJSONArray(Constants.KEY_DATA);
				if (data.length() != 0) {

					for (int i = 0; i < data.length(); i++) {
						JSONObject gObj = data.getJSONObject(i);

						Result r = new Result();

						r.setExamId(gObj.getInt("exam_id"));
						r.setExamName(gObj.getString("exam_name"));

						examsResult.add(r);
					}

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return examsResult;

	}

	public static ArrayList<NotesResult> parseExerciseResult(JSONObject exRJson) {
		ArrayList<NotesResult> exerciseResult = new ArrayList<NotesResult>();
		try {
			if (exRJson.has(Constants.KEY_DATA)
					&& exRJson.get(Constants.KEY_DATA) != null) {
				NotesResult exR = new NotesResult();

				JSONObject obj = exRJson.getJSONObject(Constants.KEY_DATA);
				
				 
				 exR.setMedianScore(obj.getInt("median_score"));
				 exR.setMonenneScore(obj.getInt("moyenne_score"));
				 
				 JSONArray data = obj.getJSONArray(Constants.KEY_RESULTS);

				if (data.length() != 0) {

					for (int i = 0; i < data.length(); i++) {
						JSONObject gObj = data.getJSONObject(i);

						
						exR.setStudentId(gObj.getInt("student_id"));
						exR.setRank(gObj.getInt("rank"));
						exR.setStudentName(gObj.getString("name"));
						exR.setScore(gObj.getInt("score"));

						exerciseResult.add(exR);
					}

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return exerciseResult;

	}

	public static ArrayList<ExamExercise> parseExamExercise(JSONObject exEJson) {
		ArrayList<ExamExercise> examExercise = new ArrayList<ExamExercise>();
		try {
			if (exEJson.has(Constants.KEY_DATA)
					&& exEJson.get(Constants.KEY_DATA) != null) {

				ExamExercise exR = new ExamExercise();
				JSONObject obj = exEJson.getJSONObject(Constants.KEY_DATA);

				exR.setCreatedBy(obj.getString("created_by"));
				exR.setExamName(obj.getString("exam_name"));

				JSONArray dataExercises = obj.getJSONArray("exercises");

				if (dataExercises.length() != 0) {

					for (int i = 0; i < dataExercises.length(); i++) {
						JSONObject gObj = dataExercises.getJSONObject(i);

						exR.setExerciseId(gObj.getInt("exercise_id"));
						exR.setExamName(gObj.getString("exercise_name"));
						exR.setPastExersice(gObj.getBoolean("past_exercise"));

						examExercise.add(exR);
					}

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return examExercise;

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
				exercise.setTeacher(data.getString("created_by"));

				HashMap<String, String> eFiles = new HashMap<String, String>();
				JSONObject filesExercise = (JSONObject) data
						.getJSONObject("exercise_files");
				JSONArray imgArrayExercise = (JSONArray) filesExercise
						.getJSONArray("image");
				if (imgArrayExercise.length() != 0) {
					JSONObject imgObjExercise = (JSONObject) imgArrayExercise
							.get(0);
					eFiles.put("image", imgObjExercise.getString("file"));
				}
				JSONArray soundArrayExercise = (JSONArray) filesExercise
						.getJSONArray("sound");
				if (soundArrayExercise.length() != 0) {
					JSONObject soundObjExercise = (JSONObject) soundArrayExercise
							.get(0);
					eFiles.put("sound", soundObjExercise.getString("file"));
				}
				JSONArray videoArrayExercise = (JSONArray) filesExercise
						.getJSONArray("image");
				if (videoArrayExercise.length() != 0) {
					JSONObject videoObjExercise = (JSONObject) videoArrayExercise
							.get(0);
					eFiles.put("video", videoObjExercise.getString("file"));
				}

				exercise.setFiles(eFiles);

				for (int k = 0; k < questions.length(); k++) {

					Question q = new Question();
					JSONObject qObj = (JSONObject) questions.get(k);
					q.setId(qObj.getInt("question_id"));
					q.setType(qObj.getString("question_type"));
					q.setQuestionText(qObj.getString("question_text"));
					q.setCorrection(qObj.getString("correction"));
					q.setInputCount(qObj.getString("input_count"));

					HashMap<String, String> qFiles = new HashMap<String, String>();
					JSONObject files = (JSONObject) qObj
							.getJSONObject("question_files");
					JSONArray imgArray = (JSONArray) files
							.getJSONArray("image");
					if (imgArray.length() != 0) {
						JSONObject imgObj = (JSONObject) imgArray.get(0);
						qFiles.put("image", imgObj.getString("file"));
					}
					JSONArray soundArray = (JSONArray) files
							.getJSONArray("sound");
					if (soundArray.length() != 0) {
						JSONObject soundObj = (JSONObject) soundArray.get(0);
						qFiles.put("sound", soundObj.getString("file"));
					}
					JSONArray videoArray = (JSONArray) files
							.getJSONArray("image");
					if (videoArray.length() != 0) {
						JSONObject videoObj = (JSONObject) videoArray.get(0);
						qFiles.put("video", videoObj.getString("file"));
					}

					q.setFiles(qFiles);

					HashMap<String, String> qFilesCorrection = new HashMap<String, String>();
					JSONObject correctionFiles = (JSONObject) qObj
							.getJSONObject("question_correction_files");
					JSONArray imgArrayCorrection = (JSONArray) correctionFiles
							.getJSONArray("image");
					if (imgArrayCorrection.length() != 0) {
						JSONObject imgObjCorrection = (JSONObject) imgArrayCorrection
								.get(0);
						qFiles.put("image", imgObjCorrection.getString("file"));
					}
					JSONArray soundArrayCorrection = (JSONArray) files
							.getJSONArray("sound");
					if (soundArrayCorrection.length() != 0) {
						JSONObject soundObjCorrection = (JSONObject) soundArrayCorrection
								.get(0);
						qFiles.put("sound",
								soundObjCorrection.getString("file"));
					}
					JSONArray videoArrayCorrection = (JSONArray) files
							.getJSONArray("image");
					if (videoArrayCorrection.length() != 0) {
						JSONObject videoObjCorrection = (JSONObject) videoArrayCorrection
								.get(0);
						qFiles.put("video",
								videoObjCorrection.getString("file"));
					}

					q.setCorrectionFiles(qFilesCorrection);

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
