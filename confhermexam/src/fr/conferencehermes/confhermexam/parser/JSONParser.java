package fr.conferencehermes.confhermexam.parser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;
import fr.conferencehermes.confhermexam.util.StringUtils;

public class JSONParser {

	public static String AUTH_KEY;
	public static String USER_ID;

	public static void parseLoginData(String pData) {

		try {
			if (pData != null) {
				JSONObject jsonObj = new JSONObject(pData);
				JSONObject data = jsonObj.getJSONObject(Constants.KEY_DATA);

				USER_ID = data.getString("user_id");
				AUTH_KEY = data.getString("auth_key");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public static String parseLoginInfo(JSONObject json) {
		String loginInfo = null;
		try {
			if (json != null) {
				JSONObject data = json.getJSONObject(Constants.KEY_DATA);
				loginInfo = data.getString("text");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return loginInfo;
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
					e.setId(obj.getInt("exam_id"));
					e.setEventId(obj.getInt("event_id"));
					e.setTitle(obj.getString("exam_name"));
					e.setEvent_name(obj.getString("event_name"));
					e.setCategoryType(obj.getString("category_type"));
					e.setStartDate(obj.getLong("start_date"));
					e.setEndDate(obj.getLong("end_date"));
					e.setStatus(obj.getInt("status"));
					e.setLastEditTime(obj.getLong("last_edit_time"));
					exams.add(e);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		DataHolder.getInstance().setExams(exams);
		return exams;
	}

	public static ArrayList<DownloadInstance> parseDownloads(JSONObject json) {
		ArrayList<DownloadInstance> downloads = null;
		try {
			JSONArray content = json.getJSONArray(Constants.KEY_DATA);
			downloads = new ArrayList<DownloadInstance>();

			if (content.length() != 0) {
				for (int i = 0; i < content.length(); i++) {
					JSONObject obj = (JSONObject) content.get(i);
					DownloadInstance d = new DownloadInstance();
					d.setEventId(obj.getInt("event_id"));
					d.setName(obj.getString("exam_name"));
					d.setDownloadUrl(obj.getString("download_url"));
					d.setRemoveUrl(obj.getString("remove_url"));
					d.setStatus(obj.getInt("status"));
					d.setLastEditTime(obj.getLong("last_edit_time"));
					downloads.add(d);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		DataHolder.getInstance().setDownloads(downloads);
		return downloads;
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
						groups.put(gID, gName);

					}

					pData.setInformation(obj.getString("profile_information"));
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

				JSONObject obj = exRJson.getJSONObject(Constants.KEY_DATA);

				JSONArray data = obj.getJSONArray(Constants.KEY_RESULTS);

				if (data.length() != 0) {

					for (int i = 0; i < data.length(); i++) {
						JSONObject gObj = data.getJSONObject(i);
						NotesResult exR = new NotesResult();
						exR.setStudentId(gObj.getString("student_id"));
						exR.setRank(gObj.getString("rank"));
						exR.setStudentName(gObj.getString("name"));
						exR.setScore(gObj.getInt("score"));
						exR.setMedianScore(obj.getInt("median_score"));
						exR.setMonenneScore(obj.getInt("moyenne_score"));
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

				JSONObject obj = exEJson.getJSONObject(Constants.KEY_DATA);

				JSONArray dataExercises = obj.getJSONArray("exercises");

				if (dataExercises.length() != 0) {

					for (int i = 0; i < dataExercises.length(); i++) {
						JSONObject gObj = dataExercises.getJSONObject(i);
						ExamExercise exR = new ExamExercise();
						exR.setCreatedBy(obj.getString("created_by"));
						exR.setExamName(obj.getString("exam_name"));
						exR.setExerciseId(gObj.getInt("exercise_id"));
						exR.setExersiceName(gObj.getString("exercise_name"));
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

				JSONObject data = json.getJSONObject("data");
				int duration = data.getInt("duration");

				JSONArray exObj = data.getJSONArray("exercises");
				for (int i = 0; i < exObj.length(); i++) {
					TrainingExercise t = new TrainingExercise();
					JSONObject obj = exObj.getJSONObject(i);

					t.setExercise_id(obj.getInt("exercise_id"));
					t.setTitle(obj.getString("title"));
					t.setDuration(duration * 1000);

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
				exercise.setExerciseType(data.getInt("type"));
				exercise.setText(data.getString("text"));
				exercise.setTeacher(data.getString("created_by"));

				HashMap<String, String> eFiles = new HashMap<String, String>();
				JSONObject filesExercise = (JSONObject) data
						.getJSONObject("exercise_files");
				JSONArray imgArrayExercise = (JSONArray) filesExercise
						.getJSONArray("image");
				String[] images = new String[imgArrayExercise.length()];
				if (imgArrayExercise.length() != 0) {
					for (int i = 0; i < imgArrayExercise.length(); i++) {
						JSONObject imgObjExercise = (JSONObject) imgArrayExercise
								.get(i);
						images[i] = imgObjExercise.getString("file") != null ? imgObjExercise
								.getString("file") : "";
					}
					eFiles.put("image",
							StringUtils.convertArrayToString(images));
				} else
					eFiles.put("image", "");

				JSONArray soundArrayExercise = (JSONArray) filesExercise
						.getJSONArray("sound");
				String[] sounds = new String[soundArrayExercise.length()];
				if (soundArrayExercise.length() != 0) {
					for (int i = 0; i < soundArrayExercise.length(); i++) {
						JSONObject soundObjExercise = (JSONObject) soundArrayExercise
								.get(0);
						sounds[i] = soundObjExercise.getString("file") != null ? soundObjExercise
								.getString("file") : "";
					}
					eFiles.put("sound",
							StringUtils.convertArrayToString(sounds));
				} else
					eFiles.put("sound", "");

				JSONArray videoArrayExercise = (JSONArray) filesExercise
						.getJSONArray("video");
				String[] videos = new String[videoArrayExercise.length()];
				if (videoArrayExercise.length() != 0) {
					for (int i = 0; i < videoArrayExercise.length(); i++) {
						JSONObject videoObjExercise = (JSONObject) videoArrayExercise
								.get(0);
						videos[i] = videoObjExercise.getString("file") != null ? videoObjExercise
								.getString("file") : "";
					}
					eFiles.put("video",
							StringUtils.convertArrayToString(videos));
				} else
					eFiles.put("video", "");

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
					String[] imagesQ = new String[imgArray.length()];
					if (imgArray.length() != 0) {
						for (int i = 0; i < imgArray.length(); i++) {
							JSONObject imgObj = (JSONObject) imgArray.get(i);
							imagesQ[i] = imgObj.getString("file") != null ? imgObj
									.getString("file") : "";
						}
						qFiles.put("image",
								StringUtils.convertArrayToString(imagesQ));

					} else
						qFiles.put("image", "");

					JSONArray soundArray = (JSONArray) files
							.getJSONArray("sound");
					String[] soundsQ = new String[soundArray.length()];
					if (soundArray.length() != 0) {
						for (int i = 0; i < soundArray.length(); i++) {
							JSONObject soundObj = (JSONObject) soundArray
									.get(0);
							soundsQ[i] = soundObj.getString("file") != null ? soundObj
									.getString("file") : "";
						}
						qFiles.put("sound",
								StringUtils.convertArrayToString(soundsQ));
					} else
						qFiles.put("sound", "");

					JSONArray videoArray = (JSONArray) filesExercise
							.getJSONArray("video");
					String[] videosQ = new String[videoArray.length()];
					if (videoArray.length() != 0) {
						for (int i = 0; i < videoArray.length(); i++) {
							JSONObject videoObj = (JSONObject) videoArray
									.get(0);
							videosQ[i] = videoObj.getString("file") != null ? videoObj
									.getString("file") : "";
						}
						qFiles.put("video",
								StringUtils.convertArrayToString(videosQ));
					} else
						qFiles.put("video", "");

					q.setFiles(qFiles);

					HashMap<String, String> qFilesCorrection = new HashMap<String, String>();
					JSONObject correctionFiles = (JSONObject) qObj
							.getJSONObject("question_correction_files");

					JSONArray imgArrayCorrection = (JSONArray) files
							.getJSONArray("image");
					String[] imagesCorrectionQ = new String[imgArrayCorrection
							.length()];
					if (imgArrayCorrection.length() != 0) {
						for (int i = 0; i < imgArrayCorrection.length(); i++) {
							JSONObject imgObjCorrection = (JSONObject) imgArrayCorrection
									.get(i);
							imagesCorrectionQ[i] = imgObjCorrection
									.getString("file") != null ? imgObjCorrection
									.getString("file") : "";
						}
						qFilesCorrection.put("image", StringUtils
								.convertArrayToString(imagesCorrectionQ));

					} else
						qFilesCorrection.put("image", "");

					JSONArray soundArrayCorrection = (JSONArray) files
							.getJSONArray("sound");
					String[] soundCorrectionQ = new String[soundArrayCorrection
							.length()];
					if (soundArrayCorrection.length() != 0) {
						for (int i = 0; i < soundArrayCorrection.length(); i++) {
							JSONObject soundObjCorrection = (JSONObject) soundArrayCorrection
									.get(i);
							soundCorrectionQ[i] = soundObjCorrection
									.getString("file") != null ? soundObjCorrection
									.getString("file") : "";
						}
						qFilesCorrection.put("sound", StringUtils
								.convertArrayToString(soundCorrectionQ));

					} else
						qFilesCorrection.put("sound", "");

					JSONArray videoArrayCorrection = (JSONArray) files
							.getJSONArray("video");
					String[] videoCorrectionQ = new String[videoArrayCorrection
							.length()];
					if (videoArrayCorrection.length() != 0) {
						for (int i = 0; i < videoArrayCorrection.length(); i++) {
							JSONObject videoObjCorrection = (JSONObject) videoArrayCorrection
									.get(i);
							videoCorrectionQ[i] = videoObjCorrection
									.getString("file") != null ? videoObjCorrection
									.getString("file") : "";
						}
						qFilesCorrection.put("video", StringUtils
								.convertArrayToString(videoCorrectionQ));

					} else
						qFilesCorrection.put("video", "");

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

	public static String parseCorrections(JSONObject json) {
		ArrayList<Correction> correctionsList = new ArrayList<Correction>();
		String score = "";
		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {

				JSONObject data = json.getJSONObject("data");
				score = data.getString("score_message");
				JSONArray corrections = data
						.getJSONArray("question_corrections");

				for (int i = 0; i < corrections.length(); i++) {
					Correction c = new Correction();
					JSONObject obj = corrections.getJSONObject(i);
					c.setQuestionId(obj.getString("question_id"));
					c.setText(obj.getString("correction_text"));
					c.setQuestionPoint(obj.getString("question_point"));
					ArrayList<String> answersArray = new ArrayList<String>();
					JSONArray answers = obj.getJSONArray("answers");
					for (int j = 0; j < answers.length(); j++) {
						answersArray.add(String.valueOf(answers.get(j)));
					}
					c.setAnswersArray(answersArray);
					correctionsList.add(c);
				}
				DataHolder.getInstance().setCorrections(correctionsList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return score;
	}

	public static ArrayList<Correction> parseResultCorrections(JSONObject json,
			String directory) {
		ArrayList<Correction> correctionsList = new ArrayList<Correction>();

		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {

				JSONObject data = json.optJSONObject("data");
				if (data == null)
					return correctionsList;

				JSONArray corrections = data
						.getJSONArray("question_corrections");

				for (int i = 0; i < corrections.length(); i++) {
					Correction c = new Correction();
					JSONObject obj = corrections.getJSONObject(i);
					c.setQuestionId(obj.getString("question_id"));
					c.setText(obj.getString("correction_text"));
					c.setQuestionPoint(obj.getString("question_point"));
					ArrayList<String> answersArray = new ArrayList<String>();
					JSONArray answers = obj.getJSONArray("answers");
					for (int j = 0; j < answers.length(); j++) {
						answersArray.add(String.valueOf(answers.get(j)));
					}

					if (obj.has("question_correction_files"))
						try {
							HashMap<String, String> qFilesCorrection = new HashMap<String, String>();
							JSONObject files = (JSONObject) obj
									.getJSONObject("question_correction_files");

							JSONArray imgArrayCorrection = (JSONArray) files
									.getJSONArray("image");
							String[] imagesCorrectionQ = new String[imgArrayCorrection
									.length()];
							if (imgArrayCorrection.length() != 0) {
								for (int j = 0; j < imgArrayCorrection.length(); j++) {
									JSONObject imgObjCorrection = (JSONObject) imgArrayCorrection
											.get(j);
									imagesCorrectionQ[j] = imgObjCorrection
											.getString("file") != null ? imgObjCorrection
											.getString("file") : "";
								}
								qFilesCorrection
										.put("image",
												StringUtils
														.convertArrayToString(imagesCorrectionQ));

							} else
								qFilesCorrection.put("image", "");

							JSONArray soundArrayCorrection = (JSONArray) files
									.getJSONArray("sound");
							String[] soundCorrectionQ = new String[soundArrayCorrection
									.length()];
							if (soundArrayCorrection.length() != 0) {
								for (int j = 0; j < soundArrayCorrection
										.length(); j++) {
									JSONObject soundObjCorrection = (JSONObject) soundArrayCorrection
											.get(j);
									soundCorrectionQ[j] = soundObjCorrection
											.getString("file") != null ? soundObjCorrection
											.getString("file") : "";
								}
								qFilesCorrection
										.put("sound",
												StringUtils
														.convertArrayToString(soundCorrectionQ));

							} else
								qFilesCorrection.put("sound", "");

							JSONArray videoArrayCorrection = (JSONArray) files
									.getJSONArray("video");
							String[] videoCorrectionQ = new String[videoArrayCorrection
									.length()];
							if (videoArrayCorrection.length() != 0) {
								for (int j = 0; j < videoArrayCorrection
										.length(); j++) {
									JSONObject videoObjCorrection = (JSONObject) videoArrayCorrection
											.get(j);
									videoCorrectionQ[j] = videoObjCorrection
											.getString("file") != null ? videoObjCorrection
											.getString("file") : "";
								}
								qFilesCorrection
										.put("video",
												StringUtils
														.convertArrayToString(videoCorrectionQ));

							} else
								qFilesCorrection.put("video", "");

							c.setFiles(qFilesCorrection);
						} catch (Exception e) {
							e.printStackTrace();
						}

					c.setAnswersArray(answersArray);
					correctionsList.add(c);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return correctionsList;
	}

	public static ArrayList<Correction> parseResultAsnwers(JSONObject json) {
		ArrayList<Correction> correctionsList = new ArrayList<Correction>();

		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {

				JSONObject data = json.getJSONObject("data");

				JSONArray corrections = data.getJSONArray("question_answers");

				for (int i = 0; i < corrections.length(); i++) {
					Correction c = new Correction();
					JSONObject obj = corrections.getJSONObject(i);
					c.setQuestionId(obj.getString("question_id"));
					c.setText(obj.getString("correction_text"));
					ArrayList<String> answersArray = new ArrayList<String>();
					JSONArray answers = obj.getJSONArray("answers");
					for (int j = 0; j < answers.length(); j++) {
						answersArray.add(String.valueOf(answers.get(j)));
					}
					c.setAnswersArray(answersArray);
					correctionsList.add(c);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return correctionsList;
	}

	public static ArrayList<TimeSlot> parsePlannig(JSONObject planJson) {
		ArrayList<TimeSlot> planningResult = new ArrayList<TimeSlot>();
		try {
			if (planJson.has(Constants.KEY_DATA)
					&& planJson.get(Constants.KEY_DATA) != null) {
				JSONArray data = planJson.getJSONArray(Constants.KEY_DATA);
				if (data.length() != 0) {
					for (int i = 0; i < data.length(); i++) {
						JSONObject gObj = data.getJSONObject(i);
						TimeSlot r = new TimeSlot();
						r.setTimeslot_id(gObj.getInt("timeslot_id"));
						r.setTest_id(gObj.getInt("test_id"));
						r.setEvent_id(gObj.getInt("event_id"));
						r.setTest_name(gObj.getString("test_name"));
						r.setEvent_name(gObj.getString("event_name"));
						r.setAcademy(gObj.getString("academy"));
						r.setStart_date(gObj.getLong("start_date"));
						r.setEnd_date(gObj.getLong("end_date"));
						r.setPlace(gObj.getString("place"));
						r.setRoom(gObj.getString("room"));
						r.setStatus(gObj.getInt("status"));
						r.setLast_edit_time(gObj.getLong("last_edit_time"));
						planningResult.add(r);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return planningResult;

	}

	public static ArrayList<CorrectionsExercise> parseCorrectionsExercises(
			JSONObject json) {
		ArrayList<CorrectionsExercise> corExercises = new ArrayList<CorrectionsExercise>();

		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {

				JSONArray exObj = json.getJSONArray("data");
				for (int i = 0; i < exObj.length(); i++) {
					CorrectionsExercise t = new CorrectionsExercise();
					JSONObject obj = exObj.getJSONObject(i);

					t.setExam_id(obj.getInt("exam_id"));
					t.setEvent_id(obj.getInt("event_id"));
					t.setExercise_id(obj.getInt("exercise_id"));
					t.setName(obj.getString("name"));

					corExercises.add(t);

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return corExercises;
	}

	public static ArrayList<CorrectionAnswer> parseCorrectionAnswers(
			JSONObject json) {
		ArrayList<CorrectionAnswer> correctionsList = new ArrayList<CorrectionAnswer>();

		try {
			if (json.has(Constants.KEY_DATA)
					&& json.get(Constants.KEY_DATA) != null) {

				JSONObject data = json.optJSONObject("data");
				if (data == null)
					return correctionsList;

				JSONArray corrections = data.getJSONArray("question_answers");

				for (int i = 0; i < corrections.length(); i++) {
					CorrectionAnswer c = new CorrectionAnswer();
					JSONObject obj = corrections.getJSONObject(i);
					c.setQuestionId(obj.getInt("question_id"));
					c.setQuestionType(obj.getInt("question_type"));
					ArrayList<String> answersArray = new ArrayList<String>();
					JSONArray answers = obj.getJSONArray("answers");
					for (int j = 0; j < answers.length(); j++) {
						answersArray.add(String.valueOf(answers.get(j)));
					}
					c.setAnswers(answersArray);
					correctionsList.add(c);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return correctionsList;
	}

}
