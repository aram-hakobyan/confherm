package fr.conferencehermes.confhermexam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.adapters.QuestionsAdapter;
import fr.conferencehermes.confhermexam.correction.QuestionAnswer;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.model.LineEditText;
import fr.conferencehermes.confhermexam.parser.Answer;
import fr.conferencehermes.confhermexam.parser.Correction;
import fr.conferencehermes.confhermexam.parser.CorrectionAnswer;
import fr.conferencehermes.confhermexam.parser.Event;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Note;
import fr.conferencehermes.confhermexam.parser.Question;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;
import fr.conferencehermes.confhermexam.util.StringUtils;
import fr.conferencehermes.confhermexam.util.Utilities;

public class CorrectionActivity extends Activity implements OnClickListener {
	private LayoutInflater inflater;
	private ListView listview;
	private QuestionsAdapter adapter;
	private Exercise exercise;
	private ArrayList<Question> questions;
	private LinearLayout answersLayout, correctionsLayout;
	private Button btnImage, btnAudio, btnVideo, abandonner, ennouncer;
	private Button btnImageCorrection;
	private Button btnAudioCorrection;
	private Button btnVideoCorrection;
	private int exercise_id, exam_id, event_id;
	private TextView teacher;
	private TextView examName;
	Question currentQuestion;
	ArrayList<Answer> currentQuestionAnswers;
	int currentPosition = 0;
	int currentQuestionId = 0;
	HashMap<String, String> currentQuestionFiles;
	HashMap<String, String> currentQuestionCorrectionFiles;
	HashMap<String, String> exerciseFiles;
	AQuery aq;
	JSONArray answersArray;
	private RadioGroup mRadioGroup;
	private ArrayList<Integer> multipleAnswers;
	ArrayList<EditText> editTextsArray;
	private ArrayList<QuestionAnswer> questionAnswers;
	private LinearLayout checkBoxLayout;
	MediaPlayer mediaPlayer;
	DatabaseHelper db;
	private int resumPlayingSound = 0;
	private int resumPlayingVideo = 0;
	private ArrayList<Correction> corrections;
	private ArrayList<CorrectionAnswer> answers;
	private boolean CONFERENCE = false;
	int padding = 10, imgSize;
	int POS = 1;
	final int TYPE_IMAGE = 0;
	final int TYPE_SOUND = 1;
	final int TYPE_VIDEO = 2;
	int MEDIA_TYPE = -1;

	Button noteBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_correction_response);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aq = new AQuery(CorrectionActivity.this);
		answersArray = new JSONArray();
		multipleAnswers = new ArrayList<Integer>();
		questionAnswers = new ArrayList<QuestionAnswer>();

		imgSize = getResources().getDimensionPixelSize(
				R.dimen.correction_image_size);

		editTextsArray = new ArrayList<EditText>();

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
		noteBtn = (Button) findViewById(R.id.noteBtn);

		abandonner = (Button) findViewById(R.id.abandonner);
		ennouncer = (Button) findViewById(R.id.ennouncer);

		abandonner.setOnClickListener(this);
		ennouncer.setOnClickListener(this);
		noteBtn.setOnClickListener(this);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

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
				try {
					selectQuestion(questions.get(position), position);
				} catch (IndexOutOfBoundsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

		db = new DatabaseHelper(CorrectionActivity.this);

		exercise = db.getExercise(exercise_id);
		exerciseFiles = db.getExerciseFile(exercise_id);

		if (exercise == null)
			return;

		if (exercise.getExerciseType() == 2) {
			ennouncer.setVisibility(View.GONE);
		}
		questions = db.getAllQuestionsByExerciseId(exercise_id);
		int qCount = exercise.getQuestions().size();
		SparseBooleanArray selectedQuestions = new SparseBooleanArray(qCount);
		for (int i = 0; i < selectedQuestions.size(); i++) {
			selectedQuestions.put(i, false);
		}
		DataHolder.getInstance().setSelectedQuestions(selectedQuestions);
		if (exercise.getType().equalsIgnoreCase("2"))
			CONFERENCE = true;
		else
			CONFERENCE = false;
		if (CONFERENCE) {
			ennouncer.setVisibility(View.GONE);
		}

		adapter = new QuestionsAdapter(CorrectionActivity.this, questions);
		listview.setAdapter(adapter);
		examName.setText(exercise.getName());
		teacher.setText("Teacher " + exercise.getTeacher());

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

		if (!questions.isEmpty()) {
			for (int i = 0; i < questions.size(); i++) {
				questionAnswers.add(null);
			}

			getCorrections();
		}

	}

	private void selectQuestion(Question q, int position)
			throws IndexOutOfBoundsException, NullPointerException {
		currentPosition = position;
		DataHolder.getInstance().getSelectedQuestions().put(position, true);
		adapter.notifyDataSetChanged();

		currentQuestionId = q.getId();
		currentQuestion = q;

		currentQuestionFiles = db.getQuestionFile(currentQuestion.getId());
		if (position < corrections.size())
			currentQuestionCorrectionFiles = corrections.get(position)
					.getFiles();

		answersLayout.removeAllViews();
		correctionsLayout.removeAllViews();
		TextView title = (TextView) findViewById(R.id.questionTitle);
		TextView txt = (TextView) findViewById(R.id.question);
		title.setText("QUESTION " + String.valueOf(position + 1));
		txt.setText(Html.fromHtml(q.getQuestionText()));

		try {
			if (currentQuestionFiles != null)
				if (!currentQuestionFiles.isEmpty())
					setFileIcons(currentQuestionFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Note n = db.getNote(currentQuestionId);
		if (n != null && !n.getText().isEmpty()) {
			noteBtn.setBackgroundResource(R.drawable.note_icon);
		} else {
			noteBtn.setBackgroundResource(R.drawable.note_icon_gray);
		}

		currentQuestionAnswers = db.getAllAnswersByQuestionId(currentQuestion
				.getId());
		int answersCount = currentQuestionAnswers.size();

		// Single choice answer
		if (q.getType().equalsIgnoreCase("2")) {
			mRadioGroup = new RadioGroup(CorrectionActivity.this);
			mRadioGroup.setOrientation(RadioGroup.VERTICAL);
			for (int i = answersCount - 1; i >= 0; i--) {
				RadioButton newRadioButton = new RadioButton(this);
				newRadioButton.setText(currentQuestionAnswers.get(i)
						.getAnswer());
				newRadioButton.setTextSize(16);
				newRadioButton.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.WRAP_CONTENT, imgSize);
				layoutParams.gravity = Gravity.CENTER_VERTICAL;
				layoutParams.setMargins(padding, padding, padding, padding);
				mRadioGroup.addView(newRadioButton, 0, layoutParams);
				newRadioButton.setEnabled(false);

			}
			answersLayout.addView(mRadioGroup);

		} else // Multichoice answer
		if (q.getType().equalsIgnoreCase("1")) {
			ArrayList<String> answers = null;

			for (int i = 0; i < answersCount; i++) {
				checkBoxLayout = new LinearLayout(CorrectionActivity.this);
				checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
				final CheckBox checkBox = new CheckBox(CorrectionActivity.this);
				checkBox.setGravity(Gravity.CENTER_VERTICAL);
				checkBox.setEnabled(false);
				TextView text = new TextView(CorrectionActivity.this);
				text.setText(currentQuestionAnswers.get(i).getAnswer());
				text.setGravity(Gravity.CENTER_VERTICAL);
				text.setTextSize(16);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, imgSize);
				layoutParams.setMargins(padding, padding, padding, padding);
				layoutParams.gravity = Gravity.CENTER_VERTICAL;
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
			}

		} else if (q.getType().equalsIgnoreCase("3")) {
			editTextsArray.clear();
			int count = Integer.valueOf(q.getInputCount());
			for (int i = 0; i < count; i++) {
				EditText editText = new EditText(CorrectionActivity.this);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setInputType(InputType.TYPE_CLASS_TEXT);
				editText.setEnabled(false);
				editText.setTextSize(20);
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, imgSize);
				layoutParams.setMargins(padding, padding, padding, padding);
				answersLayout.addView(editText, layoutParams);
				editTextsArray.add(editText);
			}
		}

		if (corrections != null && answers != null)
			try {
				drawCorrections();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}

	}

	private void drawCorrections() throws IndexOutOfBoundsException {
		correctionsLayout.removeAllViews();

		ArrayList<String> allAnswerIDs = new ArrayList<String>();
		ArrayList<Answer> allAnswers = db
				.getAllAnswersByQuestionId(currentQuestionId);
		ArrayList<Answer> allAnswersDB = db.getAllAnswers();
		for (int i = 0; i < allAnswers.size(); i++) {
			allAnswerIDs.add(String.valueOf(allAnswers.get(i).getId()));
		}

		int answerCount = 0;
		if (currentQuestion.getType().equalsIgnoreCase("3")) {
			answerCount = Integer.valueOf(currentQuestion.getInputCount());
		} else {
			answerCount = allAnswers.size();
		}

		if (currentPosition < corrections.size()) {
			TextView reponse = (TextView) findViewById(R.id.reponse);
			reponse.setText(getResources().getString(R.string.reponse) + "  "
					+ corrections.get(currentPosition).getQuestionPoint());
		}

		int count = answerCount;
		if (currentQuestion.getType().equalsIgnoreCase("3")) {
			try {
				count = Integer.valueOf(currentQuestion.getInputCount());

				for (int i = 0; i < count; i++) {
					ImageView img = new ImageView(CorrectionActivity.this);
					LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
							imgSize, imgSize);
					imageParams.setMargins(padding, padding, padding, padding);

					try {
						for (int k = 0; k < corrections.size(); k++) {
							if (corrections
									.get(k)
									.getQuestionId()
									.equalsIgnoreCase(
											String.valueOf(currentQuestionId))) {
								ArrayList<String> answersArr = corrections.get(
										k).getAnswersArray();

								JSONObject obj = new JSONObject(
										answersArr.get(i));

								int IS_GOOD = obj.getInt("is_good");
								String name = obj.getString("name");
								editTextsArray.get(i).setText(name);

								if (IS_GOOD == 1) {
									img.setBackgroundResource(R.drawable.correction_true);
								} else {
									img.setBackgroundResource(R.drawable.correction_false);
								}

								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					correctionsLayout.addView(img, imageParams);
				}

			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else

		if (count != 0) {
			for (int j = 0; j < count; j++) {
				// Current answer's id
				String currentAnswerId = String.valueOf(allAnswers.get(j)
						.getId());
				ImageView img = new ImageView(CorrectionActivity.this);
				LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
						imgSize, imgSize);
				imageParams.setMargins(padding, padding, padding, padding);

				if (currentQuestion.getType().equalsIgnoreCase("2")) {
					for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
						mRadioGroup.getChildAt(i).setEnabled(false);
					}

					String userAnswerId = "";
					for (int i = 0; i < answers.size(); i++) {
						if (answers.get(i).getQuestionId() == currentQuestionId)
							userAnswerId = answers.get(i).getAnswers().get(0);
					}

					String correctAnswerId = "";
					for (int i = 0; i < corrections.size(); i++) {
						if (corrections
								.get(i)
								.getQuestionId()
								.equalsIgnoreCase(
										String.valueOf(currentQuestionId)))
							correctAnswerId = corrections.get(i)
									.getAnswersArray().get(0);
					}

					boolean USER_IS_RIGHT = userAnswerId
							.equalsIgnoreCase(correctAnswerId);
					boolean CURRENT_IS_RIGHT = currentAnswerId
							.equalsIgnoreCase(correctAnswerId);
					boolean CURRENT_IS_USER = currentAnswerId
							.equalsIgnoreCase(userAnswerId);

					if (CURRENT_IS_RIGHT) {
						img.setBackgroundResource(R.drawable.correction_true);
					} else if (CURRENT_IS_USER) {
						img.setBackgroundResource(R.drawable.correction_false);
					}

					if (CURRENT_IS_USER) {
						View v = mRadioGroup.getChildAt(j);
						mRadioGroup.check(v.getId());
					}

				} else if (currentQuestion.getType().equalsIgnoreCase("1")) {
					for (int i = 0; i < answersLayout.getChildCount(); i++) {
						LinearLayout layout = (LinearLayout) answersLayout
								.getChildAt(i);
						for (int i1 = 0; i1 < layout.getChildCount(); i1++) {
							layout.getChildAt(i1).setEnabled(false);
						}
					}

					// User's answers
					ArrayList<String> userAnswerIds = new ArrayList<String>();
					for (int i = 0; i < answers.size(); i++) {
						if (answers.get(i).getQuestionId() == currentQuestionId) {
							for (int k = 0; k < answers.get(i).getAnswers()
									.size(); k++) {
								userAnswerIds.add(answers.get(i).getAnswers()
										.get(k));
							}

						}
					}

					// Correct answers
					ArrayList<String> correctAnswerIds = new ArrayList<String>();
					for (int i = 0; i < corrections.size(); i++) {
						if (corrections
								.get(i)
								.getQuestionId()
								.equalsIgnoreCase(
										String.valueOf(currentQuestionId)))
							for (int k = 0; k < corrections.get(i)
									.getAnswersArray().size(); k++) {
								correctAnswerIds.add(corrections.get(i)
										.getAnswersArray().get(k));
							}
					}

					boolean CURRENT_IS_RIGHT = false;
					for (int i = 0; i < correctAnswerIds.size(); i++) {
						if (correctAnswerIds.get(i).equalsIgnoreCase(
								currentAnswerId)) {
							{
								CURRENT_IS_RIGHT = true;
								break;
							}
						}
					}

					boolean CURRENT_IS_USER = false;
					for (int i = 0; i < userAnswerIds.size(); i++) {
						if (userAnswerIds.get(i).equalsIgnoreCase(
								currentAnswerId)) {
							{
								CURRENT_IS_USER = true;
								break;
							}
						}
					}

					if (CURRENT_IS_RIGHT) {
						img.setBackgroundResource(R.drawable.correction_true);
					} else if (CURRENT_IS_USER) {
						img.setBackgroundResource(R.drawable.correction_false);
					}

					if (CURRENT_IS_USER) {
						LinearLayout layout = (LinearLayout) answersLayout
								.getChildAt(j);
						CheckBox box = (CheckBox) layout.getChildAt(0);
						box.setChecked(true);
					}

				}

				correctionsLayout.addView(img, imageParams);
			}
		}
		String corrText = "";
		for (int i = 0; i < corrections.size(); i++) {
			if (corrections.get(i).getQuestionId()
					.equalsIgnoreCase(String.valueOf(currentQuestionId))) {
				corrText = corrections.get(i).getText();
				break;
			}
		}

		TextView correctionText = (TextView) findViewById(R.id.correctionAnswer);
		correctionText.setText(corrText);

		btnImageCorrection = (Button) findViewById(R.id.btnImageCorrection);
		btnAudioCorrection = (Button) findViewById(R.id.btnAudioCorrection);
		btnVideoCorrection = (Button) findViewById(R.id.btnVideoCorrection);
		btnImageCorrection.setOnClickListener(this);
		btnAudioCorrection.setOnClickListener(this);
		btnVideoCorrection.setOnClickListener(this);

		try {
			if (currentQuestionCorrectionFiles != null
					&& !currentQuestionCorrectionFiles.isEmpty()) {
				setCorrectionFileIcons(currentQuestionCorrectionFiles);
			} else {
				findViewById(R.id.btnImageCorrection).setAlpha(0.5f);
				findViewById(R.id.btnAudioCorrection).setAlpha(0.5f);
				findViewById(R.id.btnVideoCorrection).setAlpha(0.5f);
				findViewById(R.id.btnImageCorrection).setEnabled(false);
				findViewById(R.id.btnAudioCorrection).setEnabled(false);
				findViewById(R.id.btnVideoCorrection).setEnabled(false);
				findViewById(R.id.btnImageCorrection).setBackgroundResource(
						R.drawable.ic_camera_gray);
				findViewById(R.id.btnAudioCorrection).setBackgroundResource(
						R.drawable.ic_sound_gray);
				findViewById(R.id.btnVideoCorrection).setBackgroundResource(
						R.drawable.ic_video_gray);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	private void setCorrectionFileIcons(HashMap<String, String> files) {
		if (files.get("image") != null && !files.get("image").isEmpty()) {
			btnImageCorrection.setBackgroundResource(R.drawable.ic_camera);
			btnImageCorrection.setClickable(true);
			btnImageCorrection.setAlpha(1f);
		} else {
			btnImageCorrection.setBackgroundResource(R.drawable.ic_camera_gray);
			btnImageCorrection.setClickable(false);
			btnImageCorrection.setAlpha(0.5f);
		}
		if (files.get("sound") != null && !files.get("sound").isEmpty()) {
			btnAudioCorrection.setBackgroundResource(R.drawable.ic_sound);
			btnAudioCorrection.setClickable(true);
			btnAudioCorrection.setAlpha(1f);
		} else {
			btnAudioCorrection.setBackgroundResource(R.drawable.ic_sound_gray);
			btnAudioCorrection.setClickable(false);
			btnAudioCorrection.setAlpha(0.5f);
		}
		if (files.get("video") != null && !files.get("video").isEmpty()) {
			btnVideoCorrection.setBackgroundResource(R.drawable.ic_video);
			btnVideoCorrection.setClickable(true);
			btnVideoCorrection.setAlpha(1f);
		} else {
			btnVideoCorrection.setBackgroundResource(R.drawable.ic_video_gray);
			btnVideoCorrection.setClickable(false);
			btnVideoCorrection.setAlpha(0.5f);
		}
	}

	private void getCorrections() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("exercise_id", exercise_id);
		params.put("event_id", event_id);
		DatabaseHelper db = new DatabaseHelper(CorrectionActivity.this);
		Event event = db.getEvent(event_id);
		db.close();
		final String dir = Utilities.readString(CorrectionActivity.this,
				"directory:" + event.getName(), "");

		aq.ajax(Constants.EXAM_EXERCISE_CORRECTIONS_URL, params,
				JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						try {
							if (json.has("data") && json.get("data") != null) {
								corrections = JSONParser
										.parseResultCorrections(json, dir);
								answers = JSONParser
										.parseCorrectionAnswers(json);
								try {
									selectQuestion(questions.get(0), 0);
								} catch (IndexOutOfBoundsException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NullPointerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.abandonner:
			finish();
			break;
		case R.id.ennouncer:
			if (exerciseFiles != null)
				openDialog(exerciseFiles, 0);
			break;
		case R.id.btnImage:
			if (currentQuestionFiles != null)
				openDialog(currentQuestionFiles, 1);
			break;
		case R.id.btnAudio:
			if (currentQuestionFiles != null)
				openDialog(currentQuestionFiles, 2);
			break;
		case R.id.btnVideo:
			if (currentQuestionFiles != null)
				openDialog(currentQuestionFiles, 3);
			break;
		case R.id.btnImageCorrection:
			if (currentQuestionCorrectionFiles != null)
				openDialog(currentQuestionCorrectionFiles, 1);
			break;
		case R.id.btnAudioCorrection:
			if (currentQuestionCorrectionFiles != null)
				openDialog(currentQuestionCorrectionFiles, 2);
			break;
		case R.id.btnVideoCorrection:
			if (currentQuestionCorrectionFiles != null)
				openDialog(currentQuestionCorrectionFiles, 3);
			break;
		case R.id.noteBtn:
			openNoteDialog();
			break;

		default:
			break;
		}

	}

	private void openNoteDialog() {
		final Dialog notedialog = new Dialog(CorrectionActivity.this);
		notedialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		notedialog.setContentView(R.layout.note_dialog);

		final DatabaseHelper dbh = new DatabaseHelper(CorrectionActivity.this);
		final LineEditText note = (LineEditText) notedialog
				.findViewById(R.id.editTextNote);
		Button done = (Button) notedialog.findViewById(R.id.closeNoteDialog);
		done.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveNote(dbh, note.getText().toString());
				notedialog.dismiss();
			}
		});

		if (!notedialog.isShowing()) {
			notedialog.show();
			notedialog.getWindow().setLayout(
					getResources().getDimensionPixelSize(
							R.dimen.note_dialog_width),
					getResources().getDimensionPixelSize(
							R.dimen.note_dialog_height));
		}

		notedialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				dbh.closeDB();
			}
		});

		Note n = dbh.getNote(currentQuestionId);
		if (n != null) {
			note.setText(n.getText());
		}
	}

	private void saveNote(DatabaseHelper dbh, String note) {
		try {
			Note n = new Note();
			n.setId(currentQuestionId);
			n.setText(note);

			if (dbh.getNote(currentQuestionId) == null) {
				dbh.createNote(n);
			} else {
				dbh.updateNote(n);
			}

			if (note.isEmpty())
				noteBtn.setBackgroundResource(R.drawable.note_icon_gray);
			else
				noteBtn.setBackgroundResource(R.drawable.note_icon);
		} catch (Exception e) {
			e.printStackTrace();
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

	public void openDialog(HashMap<String, String> files, int from) {
		dialog = new Dialog(CorrectionActivity.this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.new_dialog);

		if (files.isEmpty())
			return;

		Button button1 = (Button) dialog.findViewById(R.id.button1);
		Button button2 = (Button) dialog.findViewById(R.id.button2);
		Button button3 = (Button) dialog.findViewById(R.id.button3);
		Button close = (Button) dialog.findViewById(R.id.buttonClose2);
		close.bringToFront();
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
		db.close();
		super.onDestroy();
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

}
