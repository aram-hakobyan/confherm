package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.conferencehermes.confhermexam.util.DataHolder;

public class ExamJsonParser {
	private static String directory = "";

	public static Event parseExamData(JSONObject json, String destDirectory)
			throws JSONException {
		directory = destDirectory;

		JSONObject eventObj = json.getJSONObject("event");
		JSONArray examsArr = json.getJSONArray("exams");

		Event event = parseEvent(eventObj);
		ArrayList<Exam> exams = parseExams(examsArr);
		event.setExams(exams);
		System.out.println(event);
		return event;
	}

	public static Event parseEvent(JSONObject obj) throws JSONException {
		Event event = new Event();
		event.setAccess_notes(obj.getString("access_notes"));
		event.setAuthor(obj.getString("author"));
		event.setId(obj.getInt("event_id"));
		event.setName(obj.getString("event_name"));
		event.setTestId(obj.getInt("test_id"));
		event.setValidation(obj.getInt("validation"));
		event.setLastEditTime(obj.getLong("last_edit_time"));
		return event;
	}

	public static ArrayList<Exam> parseExams(JSONArray json)
			throws JSONException {
		ArrayList<Exam> exams = new ArrayList<Exam>();

		if (json.length() != 0) {
			for (int i = 0; i < json.length(); i++) {
				JSONObject obj = (JSONObject) json.get(i);
				Exam e = new Exam();
				e.setId(obj.getInt("exam_id"));
				e.setEventId(obj.getInt("event_id"));
				e.setTitle(obj.getString("exam_name"));

				e.setEvent_name(obj.getString("event_name"));

				e.setStartDate(obj.getLong("start_date"));
				e.setEndDate(obj.getLong("end_date"));
				e.setStatus(1);
				e.setCategoryType(obj.getString("type"));
				e.setLastEditTime(System.currentTimeMillis() / 1000);
				e.setPassword(obj.getString("password"));

				ArrayList<Exercise> exercises = new ArrayList<Exercise>();
				JSONArray exercisesArr = obj.getJSONArray("exercises");
				for (int j = 0; j < exercisesArr.length(); j++) {
					Exercise ex = parseExercise(exercisesArr.getJSONObject(j),
							e.getId());
					exercises.add(ex);
				}

				e.setExercises(exercises);
				exams.add(e);
			}
		}

		DataHolder.getInstance().setExams(exams);
		return exams;
	}

	public static Exercise parseExercise(JSONObject data, int examId)
			throws JSONException {

		Exercise exercise = new Exercise();
		ArrayList<Question> questionsList = new ArrayList<Question>();
		ArrayList<Integer> questionIds = new ArrayList<Integer>();

		exercise.setId(data.getInt("exercise_id"));
		exercise.setExamId(examId);
		exercise.setName(data.getString("name"));
		exercise.setType(data.getString("type"));
		// exercise.setExerciseType(data.getInt("exercise_type"));
		exercise.setText(data.getString("text"));
		exercise.setTeacher(data.getString("created_by"));

		HashMap<String, String> eFiles = new HashMap<String, String>();
		JSONObject filesExercise = (JSONObject) data
				.getJSONObject("exercise_files");
		JSONArray imgArrayExercise = (JSONArray) filesExercise
				.getJSONArray("image");
		if (imgArrayExercise.length() != 0) {
			JSONObject imgObjExercise = (JSONObject) imgArrayExercise.get(0);
			eFiles.put("image",
					imgObjExercise.getString("file") != null ? directory
							+ imgObjExercise.getString("file") : "");
		} else
			eFiles.put("image", "");
		JSONArray soundArrayExercise = (JSONArray) filesExercise
				.getJSONArray("sound");
		if (soundArrayExercise.length() != 0) {
			JSONObject soundObjExercise = (JSONObject) soundArrayExercise
					.get(0);
			eFiles.put("sound",
					soundObjExercise.getString("file") != null ? directory
							+ soundObjExercise.getString("file") : "");
		} else
			eFiles.put("sound", "");
		JSONArray videoArrayExercise = (JSONArray) filesExercise
				.getJSONArray("video");
		if (videoArrayExercise.length() != 0) {
			JSONObject videoObjExercise = (JSONObject) videoArrayExercise
					.get(0);
			eFiles.put("video",
					videoObjExercise.getString("file") != null ? directory
							+ videoObjExercise.getString("file") : "");
		} else
			eFiles.put("video", "");

		exercise.setFiles(eFiles);

		questionsList = new ArrayList<Question>();
		JSONArray questions = (JSONArray) data.getJSONArray("questions");

		for (int k = 0; k < questions.length(); k++) {
			Question q = new Question();
			JSONObject qObj = (JSONObject) questions.get(k);
			q.setId(qObj.getInt("question_id"));
			q.setType(qObj.getString("question_type"));
			q.setQuestionText(qObj.getString("question_text"));
			q.setInputCount(qObj.getString("input_count"));
			q.setExerciseId(exercise.getId());

			System.out.println("QUESTION " + q.getId() + " exercise id "
					+ q.getExerciseId());

			HashMap<String, String> qFiles = new HashMap<String, String>();
			JSONObject files = (JSONObject) qObj
					.getJSONObject("question_files");
			JSONArray imgArray = (JSONArray) files.getJSONArray("image");
			if (imgArray.length() != 0) {
				JSONObject imgObj = (JSONObject) imgArray.get(0);
				qFiles.put(
						"image",
						imgObj.getString("file") != null ? directory
								+ imgObj.getString("file") : "");
			} else
				qFiles.put("image", "");
			JSONArray soundArray = (JSONArray) files.getJSONArray("sound");
			if (soundArray.length() != 0) {
				JSONObject soundObj = (JSONObject) soundArray.get(0);
				qFiles.put("sound",
						soundObj.getString("file") != null ? directory
								+ soundObj.getString("file") : "");
			} else
				qFiles.put("sound", "");
			JSONArray videoArray = (JSONArray) files.getJSONArray("video");
			if (videoArray.length() != 0) {
				JSONObject videoObj = (JSONObject) videoArray.get(0);
				qFiles.put("video",
						videoObj.getString("file") != null ? directory
								+ videoObj.getString("file") : "");
			} else
				qFiles.put("video", "");

			q.setFiles(qFiles);

			JSONArray answers = (JSONArray) qObj.getJSONArray("answers");
			ArrayList<Answer> answersArrayList = new ArrayList<Answer>();
			for (int i = 0; i < answers.length(); i++) {
				JSONObject ans = (JSONObject) answers.get(i);
				Answer answer = new Answer();
				answer.setId(ans.getInt("answer_id"));
				answer.setAnswer(ans.getString("name"));
				answersArrayList.add(answer);
				answer.setQuestionId(q.getId());
			}

			q.setAnswers(answersArrayList);
			questionsList.add(q);
		}
		exercise.setQuestionIds(questionIds);
		exercise.setQuestions(questionsList);

		return exercise;
	}

}
