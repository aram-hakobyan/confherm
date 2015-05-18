package fr.conferencehermes.confhermexam;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.text.Html;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.androidquery.AQuery;

import fr.conferencehermes.confhermexam.adapters.QuestionsAdapter;
import fr.conferencehermes.confhermexam.correction.QuestionAnswer;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.lifecycle.ScreenReceiver;
import fr.conferencehermes.confhermexam.parser.Answer;
import fr.conferencehermes.confhermexam.parser.Event;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.ExerciseAnswer;
import fr.conferencehermes.confhermexam.parser.Question;
import fr.conferencehermes.confhermexam.util.DataHolder;
import fr.conferencehermes.confhermexam.util.StringUtils;
import fr.conferencehermes.confhermexam.util.Utilities;

@SuppressLint("DefaultLocale")
public class ExaminationActivity extends Activity implements OnClickListener {
	private LayoutInflater inflater;
	private ListView listview;
	private QuestionsAdapter adapter;
	private Exercise exercise;
	private ArrayList<Question> questions;
	private LinearLayout answersLayout, correctionsLayout;
	private Button btnImage, btnAudio, btnVideo, abandonner, ennouncer,
			valider;
	private int exercise_id, exam_id, event_id;
	private TextView temps1;
	private TextView temps2;
	private TextView teacher;
	private TextView examName;
	long updatedTime = 0L, timeSwapBuff = 0L, timeInMilliseconds = 0L,
			startTime;
	Question currentQuestion;
	ArrayList<Answer> currentQuestionAnswers;
	int currentQuestionId = 0;
	HashMap<String, String> currentQuestionFiles;
	HashMap<String, String> exerciseFiles;

	AQuery aq;
	JSONArray answersArray;
	private RadioGroup mRadioGroup;
	private ArrayList<Integer> multipleAnswers;

	int ANSWERED_QUESTIONS_COUNT = 0;
	ArrayList<EditText> editTextsArray;

	private boolean onPaused = false;
	private boolean SEND_DATA = false;
	private boolean CONFERENCE = false;

	private SparseBooleanArray validAnswers;
	private ArrayList<QuestionAnswer> questionAnswers;
	private LinearLayout checkBoxLayout;
	MediaPlayer mediaPlayer;
	private int resumPlaying = 0;
	private int resumPlayingSound = 0;
	private int resumPlayingVideo = 0;
	private CounterClass timer;
	private CounterClassForExercise exerciseTimer;
	private int maxPosition = 0;
	private boolean DIALOG_IS_OPEN = false;
	private long pauseTime = 0;
	private boolean isActive = false;
	private ArrayList<ExerciseAnswer> exerciseAnswers;
	private ExerciseAnswer currentExerciseAnswer;
	private ToggleButton toggleTimer;
	private boolean TIMER_PAUSED = false;
	private long timeToResume = 0;
	private long timeToResumeExercise = 0;

	int POS = 1;
	final int TYPE_IMAGE = 0;
	final int TYPE_SOUND = 1;
	final int TYPE_VIDEO = 2;
	int MEDIA_TYPE = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question_response);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aq = new AQuery(ExaminationActivity.this);
		answersArray = new JSONArray();
		multipleAnswers = new ArrayList<Integer>();
		validAnswers = new SparseBooleanArray();
		questionAnswers = new ArrayList<QuestionAnswer>();

		toggleTimer = (ToggleButton) findViewById(R.id.timerToggle);
		if (getIntent().getBooleanExtra("conference", false)) {
			toggleTimer.setVisibility(View.VISIBLE);
			toggleTimer
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								timer.cancel();
								exerciseTimer.cancel();
								TIMER_PAUSED = true;
							} else {
								timer = new CounterClass(timeToResume, 1000);
								exerciseTimer = new CounterClassForExercise(
										timeToResumeExercise, 1000);
								timer.start();
								exerciseTimer.start();
								TIMER_PAUSED = false;
							}
						}
					});
		}

		editTextsArray = new ArrayList<EditText>();
		temps1 = (TextView) findViewById(R.id.temps1);
		temps2 = (TextView) findViewById(R.id.temps2);
		teacher = (TextView) findViewById(R.id.teacher);
		examName = (TextView) findViewById(R.id.examName);
		btnImage = (Button) findViewById(R.id.btnImage);
		btnAudio = (Button) findViewById(R.id.btnAudio);
		btnVideo = (Button) findViewById(R.id.btnVideo);
		btnImage.setOnClickListener(this);
		btnAudio.setOnClickListener(this);
		btnVideo.setOnClickListener(this);
		btnImage.bringToFront();
		btnAudio.bringToFront();
		btnVideo.bringToFront();

		abandonner = (Button) findViewById(R.id.abandonner);
		ennouncer = (Button) findViewById(R.id.ennouncer);
		valider = (Button) findViewById(R.id.validerBtn);
		abandonner.setOnClickListener(this);
		ennouncer.setOnClickListener(this);
		valider.setOnClickListener(this);

		DatabaseHelper db = new DatabaseHelper(ExaminationActivity.this);

		exercise_id = getIntent().getIntExtra("exercise_id", 1);
		exam_id = getIntent().getIntExtra("exam_id", 1);
		event_id = getIntent().getIntExtra("event_id", 1);
		answersLayout = (LinearLayout) findViewById(R.id.answersLayout);
		correctionsLayout = (LinearLayout) findViewById(R.id.correctionsLayout);
		listview = (ListView) findViewById(R.id.questionsListView);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (questionAnswers.get(position) != null
						|| position == maxPosition || isActive)
					try {
						selectQuestion(questions.get(position), position);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		});

		exercise = db.getExercise(exercise_id);
		exerciseFiles = db.getExerciseFile(exercise_id);

		timer = new CounterClass(DataHolder.getInstance()
				.getMillisUntilFinished(), 1000);
		timer.start();
		exerciseTimer = new CounterClassForExercise(
				exercise.getDuration() * 1000, 1000);
		exerciseTimer.start();

		startTime = SystemClock.uptimeMillis();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		if (exercise.getType().equalsIgnoreCase("2"))
			CONFERENCE = true;
		else
			CONFERENCE = false;
		if (CONFERENCE) {
			ennouncer.setVisibility(View.GONE);
		}

		int qCount = exercise.getQuestions().size();
		SparseBooleanArray selectedQuestions = new SparseBooleanArray(qCount);
		for (int i = 0; i < selectedQuestions.size(); i++) {
			selectedQuestions.put(i, false);
		}
		DataHolder.getInstance().setSelectedQuestions(selectedQuestions);

		exercise.setExerciseIsAlreadyPassed(1);
		db.updateExercise(exercise);

		String key = "exercise_passed" + String.valueOf(event_id)
				+ String.valueOf(exam_id) + String.valueOf(exercise_id);
		Utilities.writeBoolean(ExaminationActivity.this, key, true);

		questions = db.getAllQuestionsByExerciseId(exercise_id);
		adapter = new QuestionsAdapter(ExaminationActivity.this, questions);
		listview.setAdapter(adapter);
		examName.setText(exercise.getName());
		teacher.setText("Teacher " + exercise.getTeacher());
		db.close();

		ViewTreeObserver vto = listview.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				for (int i = 0; i < listview.getChildCount(); i++) {
					if (i == 0)
						listview.getChildAt(i).setBackgroundColor(
								getResources().getColor(
										R.color.app_main_color_dark));
					else
						listview.getChildAt(i)
								.setBackgroundColor(
										getResources().getColor(
												R.color.app_main_color));
				}

				listview.getViewTreeObserver().removeGlobalOnLayoutListener(
						this);
			}
		});

		isActive = getIntent().getBooleanExtra("isActive", false);
		if (isActive) {
			valider.setEnabled(false);
			valider.setAlpha(0.5f);

			exerciseAnswers = db.getAllExerciseAnswers();
			int n = exerciseAnswers.size();
			for (int i = 0; i < n; i++) {
				if (exerciseAnswers.get(i).getExerciseId() == exercise_id) {
					{
						currentExerciseAnswer = exerciseAnswers.get(i);
						break;
					}
				}
			}
		}

		if (!questions.isEmpty()) {
			for (int i = 0; i < questions.size(); i++) {
				questionAnswers.add(null);
			}
			try {
				selectQuestion(questions.get(0), 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void selectQuestion(Question q, int position) {
		DataHolder.getInstance().getSelectedQuestions().put(position, true);
		adapter.notifyDataSetChanged();

		if (position > maxPosition)
			maxPosition = position;

		DatabaseHelper db = new DatabaseHelper(ExaminationActivity.this);
		currentQuestionId = position;
		currentQuestion = q;
		currentQuestionFiles = db.getQuestionFile(currentQuestion.getId());

		answersLayout.removeAllViews();
		correctionsLayout.removeAllViews();
		TextView title = (TextView) findViewById(R.id.questionTitle);
		TextView txt = (TextView) findViewById(R.id.question);
		title.setText("QUESTION " + String.valueOf(position + 1));
		txt.setText(Html.fromHtml(q.getQuestionText()));

		try {
			if (currentQuestionFiles != null)
				setFileIcons(currentQuestionFiles);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		currentQuestionAnswers = db.getAllAnswersByQuestionId(currentQuestion
				.getId());
		int answersCount = currentQuestionAnswers.size();
		JSONObject answersJson = null;

		System.out.println(currentExerciseAnswer);
		if (currentExerciseAnswer != null)
			try {
				JSONObject obj = new JSONObject(
						currentExerciseAnswer.getJsonString());
				JSONObject data = obj.getJSONObject("data");
				JSONArray arr = data.getJSONArray("question_answers");
				if (arr.length() > position)
					answersJson = (JSONObject) arr.get(position);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

		// Single choice answer
		if (q.getType().equalsIgnoreCase("2")) {
			mRadioGroup = new RadioGroup(ExaminationActivity.this);
			mRadioGroup.setOrientation(RadioGroup.VERTICAL);
			for (int i = answersCount - 1; i >= 0; i--) {
				RadioButton newRadioButton = new RadioButton(this);
				newRadioButton.setText(currentQuestionAnswers.get(i)
						.getAnswer());

				try {
					if (answersJson != null
							&& currentQuestionAnswers.get(i).getId() == answersJson
									.getJSONArray("answers").getInt(0))
						newRadioButton.setChecked(true);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				newRadioButton.setTextSize(16);
				newRadioButton.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.WRAP_CONTENT,
						RadioGroup.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(0, 0, 0, 5);
				mRadioGroup.addView(newRadioButton, 0, layoutParams);

			}
			answersLayout.addView(mRadioGroup);

			if (questionAnswers.get(currentQuestionId) != null) {
				int pos = questionAnswers.get(currentQuestionId)
						.getSingleAnswerPosition();
				View v = mRadioGroup.getChildAt(pos);
				mRadioGroup.check(v.getId());
			}

			if (isActive) {
				for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
					mRadioGroup.getChildAt(i).setEnabled(false);
				}
			}

		} else // Multichoice answer
		if (q.getType().equalsIgnoreCase("1")) {
			ArrayList<String> answers = null;

			for (int i = 0; i < answersCount; i++) {
				checkBoxLayout = new LinearLayout(ExaminationActivity.this);
				checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
				final CheckBox checkBox = new CheckBox(ExaminationActivity.this);
				checkBox.setGravity(Gravity.CENTER_VERTICAL);

				try {
					if (answersJson != null)
						for (int j = 0; j < answersJson.getJSONArray("answers")
								.length(); j++) {
							if (currentQuestionAnswers.get(i).getId() == answersJson
									.getJSONArray("answers").getInt(j)) {
								checkBox.setChecked(true);
								break;
							}
						}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				TextView text = new TextView(ExaminationActivity.this);
				text.setText(currentQuestionAnswers.get(i).getAnswer());
				text.setTextSize(16);
				text.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(0, 0, 0, 5);
				checkBoxLayout.addView(checkBox, layoutParams);
				checkBoxLayout.addView(text, layoutParams);
				answersLayout.addView(checkBoxLayout, layoutParams);

				checkBox.setTag(currentQuestionAnswers.get(i).getId());
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int id = (Integer) checkBox.getTag();
						if (isChecked) {
							multipleAnswers.add(id);
						} else {
							for (int j = 0; j < multipleAnswers.size(); j++) {
								if (multipleAnswers.get(j) == id)
									multipleAnswers.remove(j);
							}

						}

					}
				});

				if (isActive) {
					for (int k = 0; k < answersLayout.getChildCount(); k++) {
						LinearLayout layout = (LinearLayout) answersLayout
								.getChildAt(k);
						for (int i1 = 0; i1 < layout.getChildCount(); i1++) {
							layout.getChildAt(i1).setEnabled(false);
						}
					}
				}

			}

			if (questionAnswers.get(currentQuestionId) != null) {
				ArrayList<Integer> answerIds = new ArrayList<Integer>();
				answerIds.addAll(questionAnswers.get(currentQuestionId)
						.getMultiAnswerPositions());
				for (int j = 0; j < answersLayout.getChildCount(); j++) {
					try {
						LinearLayout layout = (LinearLayout) answersLayout
								.getChildAt(j);
						CheckBox box = (CheckBox) layout.getChildAt(0);
						int currentId = currentQuestionAnswers.get(j).getId();
						for (int k = 0; k < answerIds.size(); k++) {
							if (answerIds.get(k) == currentId)
								box.setChecked(true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}

		} else if (q.getType().equalsIgnoreCase("3")) {
			editTextsArray.clear();
			int count = Integer.valueOf(q.getInputCount());
			for (int i = 0; i < count; i++) {
				EditText editText = new EditText(ExaminationActivity.this);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setInputType(InputType.TYPE_CLASS_TEXT);
				editText.setTextSize(16);
				editText.clearFocus();
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 50);
				answersLayout.addView(editText, layoutParams);
				editTextsArray.add(editText);

				if (i == 0) {
					editText.setFocusableInTouchMode(true);
					editText.requestFocus();
				}

				try {
					if (answersJson != null)
						editText.setText(answersJson.getJSONArray("answers")
								.getString(i));
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (questionAnswers.get(currentQuestionId) != null) {
				ArrayList<String> answers = questionAnswers.get(
						currentQuestionId).getTextAnswers();
				for (int c = 0; c < editTextsArray.size(); c++) {
					editTextsArray.get(c).setText(answers.get(c));
				}
			}

			if (isActive) {
				for (int c = 0; c < editTextsArray.size(); c++) {
					editTextsArray.get(c).setEnabled(false);
				}
			}
		}

		db.close();
	}

	private void setFileIcons(HashMap<String, String> files) {
		if (files.get("image") != null && !files.get("image").isEmpty()) {
			btnImage.setBackgroundResource(R.drawable.ic_camera);
			btnImage.setClickable(true);
			btnImage.setAlpha(1f);
		} else {
			btnImage.setBackgroundResource(R.drawable.ic_camera_gray);
			btnImage.setClickable(false);
			btnImage.setAlpha(0.5f);
		}
		if (files.get("sound") != null && !files.get("sound").isEmpty()) {
			btnAudio.setBackgroundResource(R.drawable.ic_sound);
			btnAudio.setClickable(true);
			btnAudio.setAlpha(1f);
		} else {
			btnAudio.setBackgroundResource(R.drawable.ic_sound_gray);
			btnAudio.setClickable(false);
			btnAudio.setAlpha(0.5f);
		}
		if (files.get("video") != null && !files.get("video").isEmpty()) {
			btnVideo.setBackgroundResource(R.drawable.ic_video);
			btnVideo.setClickable(true);
			btnVideo.setAlpha(1f);
		} else {
			btnVideo.setBackgroundResource(R.drawable.ic_video_gray);
			btnVideo.setClickable(false);
			btnVideo.setAlpha(0.5f);
		}
	}

	public void sendAnswers() throws JSONException {
		fr.conferencehermes.confhermexam.util.Constants.calledFromExam = true;

		if (isActive)
			return;

		JSONObject object = new JSONObject();
		JSONObject data = new JSONObject();

		DatabaseHelper db = new DatabaseHelper(ExaminationActivity.this);
		Event event = db.getEvent(event_id);

		data.put("event_id", event_id);
		data.put("exam_id", event.getTestId());
		data.put("exercise_id", exercise_id);
		data.put("is_send", 0);
		data.put("type", exercise.getType());

		JSONArray answers = answersArray;
		data.put("question_answers", answers);
		object.put("auth_key",
				Utilities.readString(ExaminationActivity.this, "auth_key", ""));
		object.put("device_id", Utilities.getDeviceId(ExaminationActivity.this));
		object.put("device_time", System.currentTimeMillis() / 1000);
		object.put("data", data);

		Utilities.writeBoolean(ExaminationActivity.this,
				String.valueOf("exercise" + exercise_id), false);

		ExerciseAnswer exerciseAnswer = new ExerciseAnswer();
		exerciseAnswer.setExerciseId(exercise.getId());
		exerciseAnswer.setExamId(exam_id);
		exerciseAnswer.setEventId(event_id);
		exerciseAnswer.setJsonString(object.toString());
		createOrUpdateExerciseAnswer(db, exerciseAnswer);

		showAlertDialog(ExaminationActivity.this, "Attention", getResources()
				.getString(R.string.connection_alert));

	}

	private void createOrUpdateExerciseAnswer(DatabaseHelper db,
			ExerciseAnswer ea) {

		if (db.getExerciseAnswer(exercise.getId()).getJsonString() == null) {
			db.createExerciseAnswer(ea);
		} else {
			db.updateExerciseAnswer(ea);
		}

		db.closeDB();

	}

	public void showAlertDialog(Context context, String title, String message) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setCancelable(true);
			builder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					finish();
				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAlertDialogWhenExerciseTimeIsOver(Context context,
			String title, String message)
			throws android.view.WindowManager.BadTokenException {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(true);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				try {
					sendAnswers();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
			}
		});
		dialog.show();
	}

	private void saveQuestionAnswers() throws JSONException {
		JSONObject questionAnswer = new JSONObject();
		questionAnswer.put("question_id", currentQuestion.getId());
		questionAnswer.put("question_type", currentQuestion.getType());

		JSONArray answers = new JSONArray();

		try {
			if (currentQuestion.getType().equalsIgnoreCase("2")) {

				int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
				View radioButton = mRadioGroup.findViewById(radioButtonID);
				int idx = mRadioGroup.indexOfChild(radioButton);
				int answerId = currentQuestionAnswers.get(idx).getId();
				answers.put(answerId);

			} else if (currentQuestion.getType().equalsIgnoreCase("1")) {
				for (int i = 0; i < multipleAnswers.size(); i++) {
					answers.put(multipleAnswers.get(i));
				}
				multipleAnswers.clear();
			} else if (currentQuestion.getType().equalsIgnoreCase("3")) {
				for (EditText editText : editTextsArray) {
					if (editText.getText().length() != 0) {
						String text = editText.getText().toString();
						try {
							String answer = new String(text.getBytes("UTF-8"),
									"ISO-8859-1");
							answers.put(answer);
						} catch (UnsupportedEncodingException e) {
							answers.put(text);
							e.printStackTrace();
						}
					} else {
						answers.put("");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		questionAnswer.put("answers", answers);
		answersArray.put(questionAnswer);
	}

	private void saveValidation() {
		QuestionAnswer qa = new QuestionAnswer();
		if (currentQuestion.getType().equalsIgnoreCase("2")) {
			qa.setType(2);
			int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
			View radioButton = mRadioGroup.findViewById(radioButtonID);
			int idx = mRadioGroup.indexOfChild(radioButton);
			qa.setSingleAnswerPosition(idx);
		} else if (currentQuestion.getType().equalsIgnoreCase("1")) {
			qa.setType(1);
			ArrayList<Integer> mAnswers = new ArrayList<Integer>(
					multipleAnswers);
			qa.setMultiAnswerPositions(mAnswers);
		} else if (currentQuestion.getType().equalsIgnoreCase("3")) {
			qa.setType(3);
			for (int e = 0; e < editTextsArray.size(); e++) {
				qa.getTextAnswers().add(e,
						editTextsArray.get(e).getText().toString());
			}
		}

		questionAnswers.set(currentQuestionId, qa);
	}

	private boolean isValidAnswer() {
		if (currentQuestion.getType().equalsIgnoreCase("2")) {
			return mRadioGroup.getCheckedRadioButtonId() != -1;
		} else if (currentQuestion.getType().equalsIgnoreCase("1")) {
			return !multipleAnswers.isEmpty();
		} else if (currentQuestion.getType().equalsIgnoreCase("3")) {
			for (EditText editText : editTextsArray) {
				if (editText.getText().length() != 0) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean areAllAnswersValidated() {
		int count = 0;
		for (int i = 0; i < questionAnswers.size(); i++) {
			if (questionAnswers.get(i) != null)
				count++;
		}

		return count == questions.size();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.validerBtn:
			try {
				{
					saveValidation();
					saveQuestionAnswers();
					ANSWERED_QUESTIONS_COUNT++;
					if (areAllAnswersValidated()) {
						abandonner.setText("ENVOYER");
						valider.setVisibility(View.GONE);
						SEND_DATA = true;
					}

					if (currentQuestionId < questions.size() - 1) {
						currentQuestionId++;
						try {
							selectQuestion(questions.get(currentQuestionId),
									currentQuestionId);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			validAnswers.put(currentQuestionId, true);
			break;
		case R.id.abandonner:
			if (isActive) {
				finish();
			} else {
				if (SEND_DATA)
					showAlertDialogWhenFinishPressed("Attention",
							getResources()
									.getString(R.string.finish_alert_text));
				else
					showAlertDialogWhenFinishPressed(
							"Attention",
							getResources().getString(
									R.string.finish_alert_text_abandonner));
			}

			break;
		case R.id.ennouncer:
			if (exerciseFiles != null && !DIALOG_IS_OPEN)
				try {
					openDialog(exerciseFiles, 0);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			break;
		case R.id.btnImage:
			if (currentQuestionFiles != null && !DIALOG_IS_OPEN)
				try {
					openDialog(currentQuestionFiles, 1);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			break;
		case R.id.btnAudio:
			if (currentQuestionFiles != null && !DIALOG_IS_OPEN)
				try {
					openDialog(currentQuestionFiles, 2);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			break;
		case R.id.btnVideo:
			if (currentQuestionFiles != null && !DIALOG_IS_OPEN)
				try {
					openDialog(currentQuestionFiles, 3);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			break;

		default:
			break;
		}

	}

	private Dialog dialog = null;

	private void setCount(TextView text, int pos, int count) {
		String s = String.valueOf(pos) + " sur " + String.valueOf(count);
		text.setText(s);
	}

	private void setSlideButtons(int pos, int count, ImageButton prev,
			ImageButton next) {
		if (count <= 1) {
			disableSlideButtons(next, prev);
		} else {
			enableSlideButtons(next, prev);
		}

		if (pos == 1 & count > 1) {
			disablePrevSlideButton(next, prev);
		} else if (pos == count & count > 1) {
			disableNextSlideButton(next, prev);
		} else if (count > 1) {
			enableSlideButtons(next, prev);
		}

	}

	private void enableSlideButtons(ImageButton next, ImageButton prev) {
		next.setBackgroundResource(R.drawable.arrow_right);
		prev.setBackgroundResource(R.drawable.arrow_left);
	}

	private void disableSlideButtons(ImageButton next, ImageButton prev) {
		next.setBackgroundResource(R.drawable.arrow_right_alpha);
		prev.setBackgroundResource(R.drawable.arrow_left_alpha);
	}

	private void disablePrevSlideButton(ImageButton next, ImageButton prev) {
		next.setBackgroundResource(R.drawable.arrow_right);
		prev.setBackgroundResource(R.drawable.arrow_left_alpha);
	}

	private void disableNextSlideButton(ImageButton next, ImageButton prev) {
		next.setBackgroundResource(R.drawable.arrow_right_alpha);
		prev.setBackgroundResource(R.drawable.arrow_left);
	}

	private String getMediaURL(String[] array) {
		if (POS < 1)
			return array[0];
		if (POS > array.length)
			return array[array.length - 1];
		return array[POS - 1];
	}

	private void prepareImage(final ImageView img, final String url) {
		if (url != null)
			if (!url.isEmpty()) {
				Bitmap bitmap = BitmapFactory.decodeFile(url);
				if (bitmap != null)
					img.setImageBitmap(bitmap);
				else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							final Bitmap mbitmap = Utilities
									.getBitmapFromURL(url);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									img.setImageBitmap(mbitmap);
								}
							});
						}
					}).start();

				}

			}
	}

	private void prepareAudio(String url) {
		if (url != null)
			if (!url.isEmpty()) {
				try {
					mediaPlayer.setDataSource(url);
					mediaPlayer.prepare();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	private void prepareVideo(VideoView video, String url) {
		if (url != null)
			if (!url.isEmpty()) {
				Uri videoURI = Uri.parse(url);
				video.setVideoURI(videoURI);
				video.setZOrderOnTop(true);

			}
	}

	public void openDialog(HashMap<String, String> files, int from)
			throws NullPointerException {
		DIALOG_IS_OPEN = true;
		dialog = new Dialog(ExaminationActivity.this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.new_dialog);

		Button button1 = (Button) dialog.findViewById(R.id.button1);
		Button button2 = (Button) dialog.findViewById(R.id.button2);
		Button button3 = (Button) dialog.findViewById(R.id.button3);
		Button close = (Button) dialog.findViewById(R.id.buttonClose2);
		final TextView text = (TextView) dialog
				.findViewById(R.id.ennouncerText);
		text.setMovementMethod(new ScrollingMovementMethod());

		final ImageButton prev = (ImageButton) dialog.findViewById(R.id.prev);
		final ImageButton next = (ImageButton) dialog.findViewById(R.id.next);
		final TextView countText = (TextView) dialog
				.findViewById(R.id.countText);

		if (files.get("image") != null && !files.get("image").isEmpty()) {
			button1.setBackgroundResource(R.drawable.ic_camera);
			button1.setEnabled(true);
			button1.setAlpha(1.0f);
		} else {
			button1.setBackgroundResource(R.drawable.ic_camera_gray);
			button1.setEnabled(false);
			button1.setAlpha(0.5f);
		}
		if (files.get("sound") != null && !files.get("sound").isEmpty()) {
			button2.setBackgroundResource(R.drawable.ic_sound);
			button2.setEnabled(true);
			button2.setAlpha(1f);
		} else {
			button2.setBackgroundResource(R.drawable.ic_sound_gray);
			button2.setEnabled(false);
			button2.setAlpha(0.5f);
		}
		if (files.get("video") != null && !files.get("video").isEmpty()) {
			button3.setBackgroundResource(R.drawable.ic_video);
			button3.setEnabled(true);
			button3.setAlpha(1f);
		} else {
			button3.setBackgroundResource(R.drawable.ic_video_gray);
			button3.setEnabled(false);
			button3.setAlpha(0.5f);
		}

		final String[] images = StringUtils.convertStringToArray(files
				.get("image"));
		final String[] sounds = StringUtils.convertStringToArray(files
				.get("sound"));
		final String[] videos = StringUtils.convertStringToArray(files
				.get("video"));

		POS = 1;
		final String IMAGE_URL = getMediaURL(images);
		final String AUDIO_URL = getMediaURL(sounds);
		final String VIDEO_URL = getMediaURL(videos);
		final int IMAGE_COUNT = images.length;
		final int SOUND_COUNT = sounds.length;
		final int VIDEO_COUNT = videos.length;

		for (int i = 0; i < VIDEO_COUNT; i++) {
			videos[i] = videos[i].replaceAll(" ", "%20");
		}

		final ImageView img = (ImageView) dialog.findViewById(R.id.imageView1);
		final VideoView video = (VideoView) dialog
				.findViewById(R.id.videoView1);

		final LinearLayout soundControlLayout = (LinearLayout) dialog
				.findViewById(R.id.sound_control_layout);
		final ImageView soundPlay = (ImageView) dialog
				.findViewById(R.id.sound_play);
		final ImageView soundPause = (ImageView) dialog
				.findViewById(R.id.sound_pause);
		final ImageView soundReplay = (ImageView) dialog
				.findViewById(R.id.sound_replay);

		final LinearLayout videoControlLayout = (LinearLayout) dialog
				.findViewById(R.id.video_control_layout);
		final ImageView videoPlay = (ImageView) dialog
				.findViewById(R.id.video_play);
		final ImageView videoPause = (ImageView) dialog
				.findViewById(R.id.video_pause);
		final ImageView videoReplay = (ImageView) dialog
				.findViewById(R.id.video_replay);

		soundPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
						resumPlayingSound = mediaPlayer.getCurrentPosition();
						soundPlay.setVisibility(View.VISIBLE);
						soundPause.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		soundReplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mediaPlayer.seekTo(0);
					mediaPlayer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (mediaPlayer.isPlaying()) {
					soundPlay.setVisibility(View.GONE);
					soundPause.setVisibility(View.VISIBLE);
				}
			}
		});

		soundPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					mediaPlayer.seekTo(resumPlayingSound);
					mediaPlayer.start();
					soundPlay.setVisibility(View.GONE);
					soundPause.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		videoPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {

					if (video.isPlaying()) {
						video.pause();

						resumPlayingVideo = video.getCurrentPosition();
						videoPlay.setVisibility(View.VISIBLE);
						videoPause.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		videoReplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					video.seekTo(0);
					video.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (video.isPlaying()) {
					videoPlay.setVisibility(View.GONE);
					videoPause.setVisibility(View.VISIBLE);
				}
			}
		});

		videoPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					video.seekTo(resumPlayingVideo);
					video.start();
					videoPlay.setVisibility(View.GONE);
					videoPause.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		prepareAudio(AUDIO_URL);
		prepareVideo(video, VIDEO_URL);
		prepareImage(img, IMAGE_URL);

		switch (from) {
		case 0:
			text.setText(exercise.getText());
			text.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
			video.setVisibility(View.GONE);
			videoControlLayout.setVisibility(View.GONE);
			soundControlLayout.setVisibility(View.GONE);
			countText.setVisibility(View.GONE);
			prev.setVisibility(View.GONE);
			next.setVisibility(View.GONE);
			break;
		case 1:
			MEDIA_TYPE = TYPE_IMAGE;
			if (mediaPlayer != null) {
				try {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.pause();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (video.isPlaying()) {
				video.stopPlayback();
			}
			img.setVisibility(View.VISIBLE);
			video.setVisibility(View.GONE);
			videoControlLayout.setVisibility(View.GONE);
			text.setVisibility(View.GONE);
			soundControlLayout.setVisibility(View.GONE);
			countText.setVisibility(View.VISIBLE);
			prev.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
			setCount(countText, POS, IMAGE_COUNT);
			setSlideButtons(POS, IMAGE_COUNT, prev, next);
			break;
		case 2:
			MEDIA_TYPE = TYPE_SOUND;
			img.setVisibility(View.GONE);
			video.setVisibility(View.GONE);
			videoControlLayout.setVisibility(View.GONE);
			text.setVisibility(View.GONE);
			soundControlLayout.setVisibility(View.VISIBLE);
			countText.setVisibility(View.VISIBLE);
			prev.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
			setCount(countText, POS, SOUND_COUNT);
			setSlideButtons(POS, SOUND_COUNT, prev, next);
			if (video.isPlaying()) {
				video.stopPlayback();
			}
			try {
				mediaPlayer.seekTo(0);
				mediaPlayer.start();
				soundPlay.setVisibility(View.GONE);
				soundPause.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case 3:
			MEDIA_TYPE = TYPE_VIDEO;
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			img.setVisibility(View.GONE);
			video.setVisibility(View.VISIBLE);
			videoControlLayout.setVisibility(View.VISIBLE);
			text.setVisibility(View.GONE);
			soundControlLayout.setVisibility(View.GONE);
			countText.setVisibility(View.VISIBLE);
			prev.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
			setCount(countText, POS, VIDEO_COUNT);
			setSlideButtons(POS, VIDEO_COUNT, prev, next);
			try {
				video.start();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}

		dialog.findViewById(R.id.button1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						MEDIA_TYPE = TYPE_IMAGE;
						if (mediaPlayer != null) {
							try {
								if (mediaPlayer.isPlaying()) {
									mediaPlayer.pause();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (video.isPlaying()) {
							video.stopPlayback();
						}
						img.setVisibility(View.VISIBLE);
						video.setVisibility(View.GONE);
						videoControlLayout.setVisibility(View.GONE);
						text.setVisibility(View.GONE);
						soundControlLayout.setVisibility(View.GONE);
						countText.setVisibility(View.VISIBLE);
						prev.setVisibility(View.VISIBLE);
						next.setVisibility(View.VISIBLE);
						setCount(countText, POS, IMAGE_COUNT);
						setSlideButtons(POS, IMAGE_COUNT, prev, next);
					}
				});

		dialog.findViewById(R.id.button2).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						MEDIA_TYPE = TYPE_SOUND;
						img.setVisibility(View.GONE);
						video.setVisibility(View.GONE);
						videoControlLayout.setVisibility(View.GONE);
						text.setVisibility(View.GONE);
						countText.setVisibility(View.VISIBLE);
						prev.setVisibility(View.VISIBLE);
						next.setVisibility(View.VISIBLE);
						soundControlLayout.setVisibility(View.VISIBLE);
						if (video.isPlaying()) {
							video.stopPlayback();
						}
						try {
							mediaPlayer.seekTo(0);
							mediaPlayer.start();
							soundPlay.setVisibility(View.GONE);
							soundPause.setVisibility(View.VISIBLE);
						} catch (Exception e) {
							e.printStackTrace();
						}
						setCount(countText, POS, SOUND_COUNT);
						setSlideButtons(POS, SOUND_COUNT, prev, next);
					}
				});

		dialog.findViewById(R.id.button3).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						MEDIA_TYPE = TYPE_VIDEO;
						try {
							if (mediaPlayer.isPlaying()) {
								mediaPlayer.pause();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						img.setVisibility(View.GONE);
						video.setVisibility(View.VISIBLE);
						videoControlLayout.setVisibility(View.VISIBLE);
						countText.setVisibility(View.VISIBLE);
						prev.setVisibility(View.VISIBLE);
						next.setVisibility(View.VISIBLE);
						text.setVisibility(View.GONE);
						soundControlLayout.setVisibility(View.GONE);
						setCount(countText, POS, VIDEO_COUNT);
						setSlideButtons(POS, VIDEO_COUNT, prev, next);

						try {
							video.start();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});

		prev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (POS > 1)
					POS--;
				else
					return;
				switch (MEDIA_TYPE) {
				case TYPE_IMAGE:
					prepareImage(img, getMediaURL(images));
					setCount(countText, POS, IMAGE_COUNT);
					setSlideButtons(POS, IMAGE_COUNT, prev, next);
					break;
				case TYPE_SOUND:
					prepareAudio(getMediaURL(sounds));
					setCount(countText, POS, SOUND_COUNT);
					setSlideButtons(POS, SOUND_COUNT, prev, next);
					break;
				case TYPE_VIDEO:
					prepareVideo(video, getMediaURL(videos));
					setCount(countText, POS, VIDEO_COUNT);
					setSlideButtons(POS, VIDEO_COUNT, prev, next);
					break;
				default:
					break;
				}
			}
		});

		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				switch (MEDIA_TYPE) {
				case TYPE_IMAGE:
					if (POS < IMAGE_COUNT)
						POS++;
					else
						return;
					prepareImage(img, getMediaURL(images));
					setCount(countText, POS, IMAGE_COUNT);
					setSlideButtons(POS, IMAGE_COUNT, prev, next);
					break;
				case TYPE_SOUND:
					if (POS < SOUND_COUNT)
						POS++;
					else
						return;
					prepareAudio(getMediaURL(sounds));
					setCount(countText, POS, SOUND_COUNT);
					setSlideButtons(POS, SOUND_COUNT, prev, next);
					break;
				case TYPE_VIDEO:
					if (POS < VIDEO_COUNT)
						POS++;
					else
						return;
					prepareVideo(video, getMediaURL(videos));
					setCount(countText, POS, VIDEO_COUNT);
					setSlideButtons(POS, VIDEO_COUNT, prev, next);
					break;
				default:
					break;
				}
			}
		});

		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				try {
					if (mediaPlayer.isPlaying())
						mediaPlayer.pause();
					if (video.isPlaying()) {
						video.stopPlayback();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					DIALOG_IS_OPEN = false;
				}
			}
		});

		int x = Utilities.getSizeX(this);
		if (!dialog.isShowing()) {
			dialog.show();
			dialog.getWindow().setLayout(x, x - 100);
		}

	}

	@Override
	protected void onDestroy() {
		mediaPlayer.stop();
		mediaPlayer.release();
		super.onDestroy();
	}

	private void showAlertDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ExaminationActivity.this);

		// set dialog message
		alertDialogBuilder
				.setMessage(getResources().getString(R.string.drop_out_text))
				.setCancelable(false)
				.setPositiveButton(
						getResources().getString(R.string.drop_out_text_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								try {
									sendAnswers();
								} catch (JSONException e) {
									e.printStackTrace();
								}

								Intent intent = new Intent(
										ExaminationActivity.this,
										MyFragmentActivity.class);
								intent.putExtra("PAGE_ID", 2);
								startActivity(intent);

								ExaminationActivity.this.finish();
								onPaused = false;
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// ONLY WHEN SCREEN TURNS ON
		if (!ScreenReceiver.wasScreenOn) {
			// THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT
			// CHANGED
		}

		boolean twoMinutesPassed = System.currentTimeMillis() - pauseTime >= 3 * 60 * 1000;

		if (onPaused && twoMinutesPassed) {
			showAlertDialog();
		} else {
			pauseTime = System.currentTimeMillis();
		}

	}

	@Override
	protected void onPause() {
		// WHEN THE SCREEN IS ABOUT TO TURN OFF
		if (!ScreenReceiver.wasScreenOn) {
			// THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A
			// SCREEN STATE CHANGE
			System.out.println("SCREEN TURNED OFF");
		} else {
			// THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT
			// CHANGED
			pauseTime = System.currentTimeMillis();
			onPaused = true;
		}
		super.onPause();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (v instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			Log.d("Activity",
					"Touch event " + event.getRawX() + "," + event.getRawY()
							+ " " + x + "," + y + " rect " + w.getLeft() + ","
							+ w.getTop() + "," + w.getRight() + ","
							+ w.getBottom() + " coords " + scrcoords[0] + ","
							+ scrcoords[1]);
			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	public class CounterClass extends CountDownTimer {

		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			temps1.setText("00:00:00");
			try {
				sendAnswers();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long millis = millisUntilFinished;
			DataHolder.getInstance()
					.setMillisUntilFinished(millisUntilFinished);
			String hms = String.format(
					"%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
			temps1.setText("Temps epreuve - " + hms);
			timeToResume = millis;
		}
	}

	public class CounterClassForExercise extends CountDownTimer {

		public CounterClassForExercise(long millisInFuture,
				long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			temps2.setText("00:00:00");

			if (!getIntent().getBooleanExtra("conference", false)) {
				try {
					showAlertDialogWhenExerciseTimeIsOver(
							ExaminationActivity.this,
							"Attention",
							getResources().getString(
									R.string.exercise_finished_alert));
				} catch (BadTokenException e) {
					e.printStackTrace();
					finish();
				} catch (NotFoundException e) {
					e.printStackTrace();
					finish();
				}
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long millis = millisUntilFinished;
			timeToResumeExercise = millisUntilFinished;
			String hms = String.format(
					"%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
			temps2.setText("Temps exercise - " + hms);
		}
	}

	public void showAlertDialogWhenFinishPressed(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ExaminationActivity.this);

		alertDialogBuilder.setTitle(title);
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							sendAnswers();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finish();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	protected void onStop() {
		if (timer != null)
			timer.cancel();
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		if (SEND_DATA)
			showAlertDialogWhenFinishPressed("Attention", getResources()
					.getString(R.string.finish_alert_text));
		else
			showAlertDialogWhenFinishPressed("Attention", getResources()
					.getString(R.string.finish_alert_text_abandonner));

	}

}
