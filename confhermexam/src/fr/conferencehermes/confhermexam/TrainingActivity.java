package fr.conferencehermes.confhermexam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.InputType;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.adapters.QuestionsAdapter;
import fr.conferencehermes.confhermexam.correction.QuestionAnswer;
import fr.conferencehermes.confhermexam.lifecycle.ScreenReceiver;
import fr.conferencehermes.confhermexam.parser.Answer;
import fr.conferencehermes.confhermexam.parser.Correction;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Question;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;
import fr.conferencehermes.confhermexam.util.Utilities;

public class TrainingActivity extends Activity implements OnClickListener {
	private LayoutInflater inflater;
	private ListView listview;
	private QuestionsAdapter adapter;
	private Exercise exercise;
	private ArrayList<Question> questions;
	private LinearLayout answersLayout, correctionsLayout;
	private Button btnImage, btnAudio, btnVideo, abandonner, ennouncer,
			valider;
	private int exercise_id, training_id;
	private TextView temps1;
	private TextView temps2;
	private TextView teacher;
	private TextView examName;
	private Handler customHandler = new Handler();
	long updatedTime = 0L, timeSwapBuff = 0L, timeInMilliseconds = 0L,
			startTime;
	Question currentQuestion;
	int currentQuestionId = 0;

	AQuery aq;
	JSONArray answersArray;
	private RadioGroup mRadioGroup;
	private ArrayList<Integer> multipleAnswers;

	int ANSWERED_QUESTIONS_COUNT = 0;
	ArrayList<EditText> editTextsArray;

	private boolean CORRECTED_ANSWERS = false;
	private boolean DATA_SENT = false;

	private SparseBooleanArray validAnswers;
	private ArrayList<QuestionAnswer> questionAnswers;
	private LinearLayout checkBoxLayout;
	MediaPlayer mediaPlayer;
	private Button btnImageCorrection;
	private Button btnAudioCorrection;
	private Button btnVideoCorrection;
	private int resumPlayingSound = 0;
	private int resumPlayingVideo = 0;
	private CounterClass timer;
	private int currentPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question_response);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aq = new AQuery(TrainingActivity.this);
		answersArray = new JSONArray();
		multipleAnswers = new ArrayList<Integer>();
		validAnswers = new SparseBooleanArray();
		questionAnswers = new ArrayList<QuestionAnswer>();

		// INITIALIZE RECEIVER
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);

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

		long time = DataHolder.getInstance().getMillisUntilFinished();
		timer = new CounterClass(time, 1000);
		timer.start();

		startTime = SystemClock.uptimeMillis();
		customHandler.postDelayed(updateTimerThread, 0);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		exercise_id = getIntent().getIntExtra("exercise_id", 1);
		training_id = getIntent().getIntExtra("training_id", 1);
		answersLayout = (LinearLayout) findViewById(R.id.answersLayout);
		correctionsLayout = (LinearLayout) findViewById(R.id.correctionsLayout);
		listview = (ListView) findViewById(R.id.questionsListView);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectQuestion(questions.get(position), position);
			}

		});

		AQuery aq = new AQuery(TrainingActivity.this);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("exercise_id", exercise_id);

		aq.ajax(Constants.TRAINING_EXERCISE_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						try {
							if (json.has("data") && json.get("data") != null) {
								exercise = JSONParser.parseExercise(json);
								if (exercise.getExerciseType() == 2) {
									ennouncer.setVisibility(View.GONE);
								}

								adapter = new QuestionsAdapter(
										TrainingActivity.this, exercise
												.getQuestions());

								listview.setAdapter(adapter);
								examName.setText(exercise.getName());
								teacher.setText("Teacher "
										+ exercise.getTeacher());

								ViewTreeObserver vto = listview
										.getViewTreeObserver();
								vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
									@SuppressWarnings("deprecation")
									@Override
									public void onGlobalLayout() {
										for (int i = 0; i < listview
												.getChildCount(); i++) {
											if (i == 0)
												listview.getChildAt(i)
														.setBackgroundColor(
																getResources()
																		.getColor(
																				R.color.app_main_color_dark));
											else
												listview.getChildAt(i)
														.setBackgroundColor(
																getResources()
																		.getColor(
																				R.color.app_main_color));
										}

										listview.getViewTreeObserver()
												.removeGlobalOnLayoutListener(
														this);
									}
								});

								questions = exercise.getQuestions();
								if (!questions.isEmpty()) {

									for (int i = 0; i < questions.size(); i++) {
										questionAnswers.add(null);
									}
									selectQuestion(questions.get(0), 0);

								}

							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				});

	}

	private void selectQuestion(Question q, int position) {
		currentPosition = position;
		int wantedPosition = position;
		int firstPosition = listview.getFirstVisiblePosition()
				- listview.getHeaderViewsCount();
		int wantedChild = wantedPosition - firstPosition;
		if (wantedChild >= 0 && wantedChild < listview.getChildCount()) {
			for (int i = 0; i < listview.getChildCount(); i++) {
				if (i == wantedChild)
					listview.getChildAt(i).setBackgroundColor(
							getResources()
									.getColor(R.color.app_main_color_dark));
				// else
				// listview.getChildAt(i).setBackgroundColor(
				// getResources().getColor(R.color.app_main_color));
			}
		}

		currentQuestionId = position;
		currentQuestion = q;
		answersLayout.removeAllViews();
		correctionsLayout.removeAllViews();
		TextView title = (TextView) findViewById(R.id.questionTitle);
		TextView txt = (TextView) findViewById(R.id.question);
		title.setText("QUESTION " + String.valueOf(position + 1));
		txt.setText(Html.fromHtml(q.getQuestionText()));

		setFileIcons(currentQuestion.getFiles());

		int answersCount = q.getAnswers().size();

		// Single choice answer
		if (q.getType().equalsIgnoreCase("2")) {
			mRadioGroup = new RadioGroup(TrainingActivity.this);
			mRadioGroup.setOrientation(RadioGroup.VERTICAL);
			for (int i = answersCount - 1; i >= 0; i--) {
				RadioButton newRadioButton = new RadioButton(this);
				newRadioButton.setText(q.getAnswers().get(i).getAnswer());
				newRadioButton.setTextSize(16);
				newRadioButton.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.WRAP_CONTENT,
						RadioGroup.LayoutParams.WRAP_CONTENT);
				mRadioGroup.addView(newRadioButton, 0, layoutParams);

			}
			answersLayout.addView(mRadioGroup);

			if (questionAnswers.get(currentQuestionId) != null
					&& questionAnswers.get(currentQuestionId)
							.getSingleAnswerPosition() != -1) {
				int pos = questionAnswers.get(currentQuestionId)
						.getSingleAnswerPosition();
				View v = mRadioGroup.getChildAt(pos);
				mRadioGroup.check(v.getId());
			}

		} else // Multichoice answer
		if (q.getType().equalsIgnoreCase("1")) {
			ArrayList<String> answers = null;

			for (int i = 0; i < answersCount; i++) {
				checkBoxLayout = new LinearLayout(TrainingActivity.this);
				checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
				final CheckBox checkBox = new CheckBox(TrainingActivity.this);
				checkBox.setGravity(Gravity.CENTER_VERTICAL);
				TextView text = new TextView(TrainingActivity.this);
				text.setText(q.getAnswers().get(i).getAnswer());
				text.setTextSize(16);
				text.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layoutParams.setMargins(0, -5, 0, 0);
				checkBoxLayout.addView(checkBox);
				checkBoxLayout.addView(text, layoutParams);
				answersLayout.addView(checkBoxLayout);

				checkBox.setTag(q.getAnswers().get(i).getId());
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

			if (questionAnswers.get(currentQuestionId) != null) {
				ArrayList<Integer> answerIds = new ArrayList<Integer>();
				answerIds.addAll(questionAnswers.get(currentQuestionId)
						.getMultiAnswerPositions());
				for (int j = 0; j < answersLayout.getChildCount(); j++) {
					try {
						LinearLayout layout = (LinearLayout) answersLayout
								.getChildAt(j);
						CheckBox box = (CheckBox) layout.getChildAt(0);
						int currentId = questions.get(currentQuestionId)
								.getAnswers().get(j).getId();
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
				EditText editText = new EditText(TrainingActivity.this);
				editText.setGravity(Gravity.CENTER_VERTICAL);
				editText.setInputType(InputType.TYPE_CLASS_TEXT);
				editText.setTextSize(16);

				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				if (i != 0)
					layoutParams.setMargins(0, 10, 0, 0);
				answersLayout.addView(editText, layoutParams);
				editTextsArray.add(editText);

			}
			if (questionAnswers.get(currentQuestionId) != null) {
				ArrayList<String> answers = questionAnswers.get(
						currentQuestionId).getTextAnswers();
				for (int c = 0; c < editTextsArray.size(); c++) {
					editTextsArray.get(c).setText(answers.get(c));
				}
			}
		}

		if (CORRECTED_ANSWERS)
			drawCorrections(DataHolder.getInstance().getCorrections());

	}

	private void setFileIcons(HashMap<String, String> files) {
		if (files.get("image").isEmpty()) {
			btnImage.setBackgroundResource(R.drawable.ic_camera_gray);
			btnImage.setClickable(false);
			btnImage.setAlpha(0.5f);
		} else {
			btnImage.setBackgroundResource(R.drawable.ic_camera);
			btnImage.setClickable(true);
			btnImage.setAlpha(1f);
		}
		if (files.get("sound").isEmpty()) {
			btnAudio.setBackgroundResource(R.drawable.ic_sound_gray);
			btnAudio.setClickable(false);
			btnAudio.setAlpha(0.5f);
		} else {
			btnAudio.setBackgroundResource(R.drawable.ic_sound);
			btnAudio.setClickable(true);
			btnAudio.setAlpha(1f);
		}
		if (files.get("video").isEmpty()) {
			btnVideo.setBackgroundResource(R.drawable.ic_video_gray);
			btnVideo.setClickable(false);
			btnVideo.setAlpha(0.5f);
		} else {
			btnVideo.setBackgroundResource(R.drawable.ic_video);
			btnVideo.setClickable(true);
			btnVideo.setAlpha(1f);
		}
	}

	private void setCorrectionFileIcons(HashMap<String, String> files) {
		if (files.get("image").isEmpty()) {
			btnImageCorrection.setBackgroundResource(R.drawable.ic_camera_gray);
			btnImageCorrection.setClickable(false);
			btnImageCorrection.setAlpha(0.5f);
		} else {
			btnImageCorrection.setBackgroundResource(R.drawable.ic_camera);
			btnImageCorrection.setClickable(true);
			btnImageCorrection.setAlpha(1f);
		}
		if (files.get("sound").isEmpty()) {
			btnAudioCorrection.setBackgroundResource(R.drawable.ic_sound_gray);
			btnAudioCorrection.setClickable(false);
			btnAudioCorrection.setAlpha(0.5f);
		} else {
			btnAudioCorrection.setBackgroundResource(R.drawable.ic_sound);
			btnAudioCorrection.setClickable(true);
			btnAudioCorrection.setAlpha(1f);
		}
		if (files.get("video").isEmpty()) {
			btnVideoCorrection.setBackgroundResource(R.drawable.ic_video_gray);
			btnVideoCorrection.setClickable(false);
			btnVideoCorrection.setAlpha(0.5f);
		} else {
			btnVideoCorrection.setBackgroundResource(R.drawable.ic_video);
			btnVideoCorrection.setClickable(true);
			btnVideoCorrection.setAlpha(1f);
		}
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

	public void sendAnswers() throws JSONException {
		Utilities.showOrHideActivityIndicator(TrainingActivity.this, 0,
				"Please wait...");

		Map<String, String> params = new HashMap<String, String>();
		JSONObject object = new JSONObject();
		JSONObject data = new JSONObject();

		data.put("training_id", training_id);
		data.put("exercise_id", exercise.getId());
		data.put("type", exercise.getType());

		JSONArray answers = answersArray;
		data.put("question_answers", answers);
		object.put("auth_key", JSONParser.AUTH_KEY);
		object.put("data", data);
		params.put("data", object.toString());

		JSONTransmitter transmitter = new JSONTransmitter();
		transmitter.execute(object);

	}

	public class JSONTransmitter extends
			AsyncTask<JSONObject, JSONObject, JSONObject> {

		String url = "http://ecni.conference-hermes.fr/api/traninganswer";

		@Override
		protected JSONObject doInBackground(JSONObject... data) {
			JSONObject json = data[0];
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppostreq = new HttpPost(url);
			StringEntity se;
			try {
				se = new StringEntity(json.toString());

				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json;charset=UTF-8"));
				httppostreq.setEntity(se);
				HttpResponse httpresponse = httpclient.execute(httppostreq);

				HttpEntity resultentity = httpresponse.getEntity();
				InputStream inputstream = resultentity.getContent();
				Header contentencoding = httpresponse
						.getFirstHeader("Content-Encoding");
				if (contentencoding != null
						&& contentencoding.getValue().equalsIgnoreCase("gzip")) {
					inputstream = new GZIPInputStream(inputstream);
				}
				String resultstring = convertStreamToString(inputstream);
				inputstream.close();
				Log.d("RESPONSE******************", resultstring);

				JSONObject ob = null;
				try {
					ob = new JSONObject(resultstring);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (ob != null) {
					final String score = JSONParser.parseCorrections(ob);
					runOnUiThread(new Runnable() {
						public void run() {
							showScoreDialog(score);
							makeCorrections(score, DataHolder.getInstance()
									.getCorrections());
							DATA_SENT = true;
						}
					});

				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Utilities.showOrHideActivityIndicator(TrainingActivity.this, 1,
						"Please wait...");
			}
			return null;
		}
	}

	private String convertStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (Exception e) {
			Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
		}
		return total.toString();
	}

	private void makeCorrections(String score, ArrayList<Correction> corrections) {
		ANSWERED_QUESTIONS_COUNT = 0;
		abandonner.setText("ABANDONNER");
		CORRECTED_ANSWERS = true;

		selectQuestion(questions.get(0), 0);
		drawCorrections(corrections);

	}

	private void drawCorrections(ArrayList<Correction> corrections) {
		correctionsLayout.removeAllViews();
		ArrayList<String> correctionAnswerIDs = new ArrayList<String>();
		for (Correction c : corrections) {
			if (currentQuestion.getId() == Integer.valueOf(c.getQuestionId())) {
				correctionAnswerIDs = c.getAnswersArray();
			}
		}

		ArrayList<String> allAnswerIDs = new ArrayList<String>();
		ArrayList<Answer> allAnswers = currentQuestion.getAnswers();
		for (int i = 0; i < allAnswers.size(); i++) {
			allAnswerIDs.add(String.valueOf(allAnswers.get(i).getId()));
		}

		int answerCount = 0;
		if (currentQuestion.getType().equalsIgnoreCase("3")) {
			answerCount = Integer.valueOf(currentQuestion.getInputCount());
		} else {
			answerCount = allAnswers.size();
		}

		for (int j = 0; j < answerCount; j++) {
			ImageView img = new ImageView(TrainingActivity.this);

			LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
					30, 30);

			if (currentQuestion.getType().equalsIgnoreCase("2")) {
				for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
					mRadioGroup.getChildAt(i).setEnabled(false);
				}

				imageParams.setMargins(0, 7, 0, 0);
				int userAnswerIDs = questionAnswers.get(currentQuestionId)
						.getSingleAnswerPosition();

				if (userAnswerIDs == j) {
					if (allAnswerIDs.get(userAnswerIDs).equalsIgnoreCase(
							correctionAnswerIDs.get(0)))
						img.setBackgroundResource(R.drawable.correction_true);
					else
						img.setBackgroundResource(R.drawable.correction_false);
				} else {
					if (allAnswerIDs.get(j).equalsIgnoreCase(
							correctionAnswerIDs.get(0)))
						img.setBackgroundResource(R.drawable.correction_true);
				}

			} else if (currentQuestion.getType().equalsIgnoreCase("1")) {
				for (int i = 0; i < answersLayout.getChildCount(); i++) {
					LinearLayout layout = (LinearLayout) answersLayout
							.getChildAt(i);
					for (int i1 = 0; i1 < layout.getChildCount(); i1++) {
						layout.getChildAt(i1).setEnabled(false);
					}
				}

				imageParams.setMargins(0, 5, 0, 0);

				ArrayList<Integer> userAnswerIDs = questionAnswers.get(
						currentQuestionId).getMultiAnswerPositions();
				String currentAsnwerId = allAnswerIDs.get(j);
				for (int i = 0; i < userAnswerIDs.size(); i++) {
					if (currentAsnwerId.equalsIgnoreCase(String
							.valueOf(userAnswerIDs.get(i)))) { // User has
																// checked
																// this
																// answer
						for (int k = 0; k < correctionAnswerIDs.size(); k++) {
							if (correctionAnswerIDs.get(k).equalsIgnoreCase(
									currentAsnwerId)) // The checked answer
														// exists in correct
														// answers
								img.setBackgroundResource(R.drawable.correction_true);
							else
								img.setBackgroundResource(R.drawable.correction_false);
						}
					}
				}
				for (int k1 = 0; k1 < correctionAnswerIDs.size(); k1++) {
					if (currentAsnwerId.equalsIgnoreCase(correctionAnswerIDs
							.get(k1)))
						img.setBackgroundResource(R.drawable.correction_true);
				}

			} else if (currentQuestion.getType().equalsIgnoreCase("3")) {
				imageParams.setMargins(0, 20, 0, 0);

				try {
					JSONObject corObj = new JSONObject(corrections
							.get(currentQuestionId).getAnswersArray().get(j));
					String answerText = corObj.getString("name");
					editTextsArray.get(j).setText(answerText);
					editTextsArray.get(j).setEnabled(false);

					int IS_GOOD = corObj.getInt("is_good");
					if (IS_GOOD == 1) {
						img.setBackgroundResource(R.drawable.correction_true);
					} else {
						img.setBackgroundResource(R.drawable.correction_false);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

			}

			correctionsLayout.addView(img, imageParams);
		}

		TextView reponse = (TextView) findViewById(R.id.reponse);
		reponse.setText(getResources().getString(R.string.reponse) + "  "
				+ corrections.get(currentPosition).getQuestionPoint());
		TextView correctionText = (TextView) findViewById(R.id.correctionAnswer);
		correctionText.setText(corrections.get(currentQuestionId).getText());
		LinearLayout correctionButtons = (LinearLayout) findViewById(R.id.correctionButtons);
		correctionButtons.setVisibility(View.VISIBLE);

		btnImageCorrection = (Button) findViewById(R.id.btnImageCorrection);
		btnAudioCorrection = (Button) findViewById(R.id.btnAudioCorrection);
		btnVideoCorrection = (Button) findViewById(R.id.btnVideoCorrection);
		btnImageCorrection.setOnClickListener(this);
		btnAudioCorrection.setOnClickListener(this);
		btnVideoCorrection.setOnClickListener(this);

		setCorrectionFileIcons(currentQuestion.getCorrectionFiles());

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
				int answerId = currentQuestion.getAnswers().get(idx).getId();
				answers.put(answerId);

			} else if (currentQuestion.getType().equalsIgnoreCase("1")) {
				for (int i = 0; i < multipleAnswers.size(); i++) {
					answers.put(multipleAnswers.get(i));
				}
				multipleAnswers.clear();
			} else if (currentQuestion.getType().equalsIgnoreCase("3")) {
				for (EditText editText : editTextsArray) {
					if (editText.getText().length() != 0) {
						answers.put(editText.getText());
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
				// if (isValidAnswer())
				{
					saveValidation();
					saveQuestionAnswers();
					ANSWERED_QUESTIONS_COUNT++;
					if (areAllAnswersValidated()) {
						abandonner.setText("ENVOYER");
						valider.setVisibility(View.GONE);
					}

					if (currentQuestionId < questions.size() - 1) {
						currentQuestionId++;
						selectQuestion(questions.get(currentQuestionId),
								currentQuestionId);
					}
				} /*
				 * else Toast.makeText(TrainingActivity.this,
				 * "Please select at least one answer.",
				 * Toast.LENGTH_SHORT).show();
				 */
			} catch (JSONException e) {
				e.printStackTrace();
			}
			validAnswers.put(currentQuestionId, true);
			break;
		case R.id.abandonner:
			try {
				if (DATA_SENT) {
					finish();
				} else {
					if (areAllAnswersValidated()) {
						sendAnswers();
					} else {
						finish();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case R.id.ennouncer:
			if (exercise != null)
				openDialog(exercise.getFiles(), 0);
			break;
		case R.id.btnImage:
			if (currentQuestion != null)
				openDialog(currentQuestion.getFiles(), 1);
			break;
		case R.id.btnAudio:
			if (currentQuestion != null)
				openDialog(currentQuestion.getFiles(), 2);
			break;
		case R.id.btnVideo:
			if (currentQuestion != null)
				openDialog(currentQuestion.getFiles(), 3);
			break;
		case R.id.btnImageCorrection:
			if (currentQuestion != null)
				openDialog(currentQuestion.getCorrectionFiles(), 1);
			break;
		case R.id.btnAudioCorrection:
			if (currentQuestion != null)
				openDialog(currentQuestion.getCorrectionFiles(), 2);
			break;
		case R.id.btnVideoCorrection:
			if (currentQuestion != null)
				openDialog(currentQuestion.getCorrectionFiles(), 3);
			break;

		default:
			break;
		}

	}

	public class CounterClass extends CountDownTimer {
		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			temps1.setText("");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long millis = millisUntilFinished;
			/*
			 * Utilities.writeLong(TrainingActivity.this, "millisUntilFinished",
			 * millisUntilFinished);
			 */
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
		}
	}

	private Runnable updateTimerThread = new Runnable() {
		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;

			String hms = String.format(
					"%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(updatedTime),
					TimeUnit.MILLISECONDS.toMinutes(updatedTime)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(updatedTime)),
					TimeUnit.MILLISECONDS.toSeconds(updatedTime)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(updatedTime)));

			// temps2.setText("" + mins + ":" + String.format("%02d", secs));
			temps2.setText("Temps exercice - " + hms);
			customHandler.postDelayed(this, 0);

		}

	};
	private Dialog dialog = null;

	public void openDialog(HashMap<String, String> files, int from) {
		dialog = new Dialog(TrainingActivity.this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.new_dialog);

		Button button1 = (Button) dialog.findViewById(R.id.button1);
		Button button2 = (Button) dialog.findViewById(R.id.button2);
		Button button3 = (Button) dialog.findViewById(R.id.button3);
		Button close = (Button) dialog.findViewById(R.id.buttonClose2);
		final TextView text = (TextView) dialog
				.findViewById(R.id.ennouncerText);

		if (files.get("image").isEmpty()) {
			button1.setBackgroundResource(R.drawable.ic_camera_gray);
			button1.setEnabled(false);
			button1.setAlpha(0.5f);
		} else {
			button1.setBackgroundResource(R.drawable.ic_camera);
			button1.setEnabled(true);
			button1.setAlpha(1.0f);
		}
		if (files.get("sound").isEmpty()) {
			button2.setBackgroundResource(R.drawable.ic_sound_gray);
			button2.setEnabled(false);
			button2.setAlpha(0.5f);
		} else {
			button2.setBackgroundResource(R.drawable.ic_sound);
			button2.setEnabled(true);
			button2.setAlpha(1f);
		}
		if (files.get("video").isEmpty()) {
			button3.setBackgroundResource(R.drawable.ic_video_gray);
			button3.setEnabled(false);
			button3.setAlpha(0.5f);
		} else {
			button3.setBackgroundResource(R.drawable.ic_video);
			button3.setEnabled(true);
			button3.setAlpha(1f);
		}

		final String IMAGE_URL = files.get("image");
		final String AUDIO_URL = files.get("sound");
		final String VIDEO_URL = files.get("video");
		VIDEO_URL.replaceAll(" ", "%20");
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

		if (AUDIO_URL != null)
			if (!AUDIO_URL.isEmpty()) {
				try {
					mediaPlayer.setDataSource(AUDIO_URL);
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
		if (VIDEO_URL != null)
			if (!VIDEO_URL.isEmpty()) {
				Uri videoURI = Uri.parse(VIDEO_URL);
				//
				// View v = (View) dialog.findViewById(R.id.videoMedioControls);
				// mc = new MediaController(this);
				// mc.setAnchorView(v);

				video.setVideoURI(videoURI);
				video.setZOrderOnTop(true);

			}

		if (IMAGE_URL != null)
			if (!IMAGE_URL.isEmpty()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							URL url = new URL(IMAGE_URL);
							URLConnection conn = url.openConnection();
							HttpURLConnection httpConn = (HttpURLConnection) conn;
							httpConn.setRequestMethod("GET");
							httpConn.connect();
							if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
								InputStream inputStream = httpConn
										.getInputStream();
								final Bitmap bitmap = BitmapFactory
										.decodeStream(inputStream);
								inputStream.close();
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										img.setImageBitmap(bitmap);
										img.refreshDrawableState();
										img.invalidate();
									}
								});

							}
						} catch (MalformedURLException e1) {
							e1.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

		switch (from) {
		case 0:
			text.setText(exercise.getText());
			text.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
			video.setVisibility(View.GONE);
			videoControlLayout.setVisibility(View.GONE);

			soundControlLayout.setVisibility(View.GONE);

			break;
		case 1:
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
			break;
		case 2:
			img.setVisibility(View.GONE);
			video.setVisibility(View.GONE);
			videoControlLayout.setVisibility(View.GONE);

			text.setVisibility(View.GONE);
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

			break;
		case 3:
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
					}
				});

		dialog.findViewById(R.id.button2).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						img.setVisibility(View.GONE);
						video.setVisibility(View.GONE);
						videoControlLayout.setVisibility(View.GONE);
						text.setVisibility(View.GONE);

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

					}
				});

		dialog.findViewById(R.id.button3).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
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

						try {
							video.start();
						} catch (Exception e) {
							e.printStackTrace();
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

		if (!dialog.isShowing()) {
			dialog.show();
			dialog.getWindow().setLayout(
					getResources().getDimensionPixelSize(
							R.dimen.file_dialog_width),
					getResources().getDimensionPixelSize(
							R.dimen.file_dialog_height));
		}

	}

	@Override
	protected void onDestroy() {
		mediaPlayer.stop();
		mediaPlayer.release();

		super.onDestroy();
	}

	@Override
	protected void onStop() {
		timer.cancel();
		super.onStop();
	}

	private void showScoreDialog(String score) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				TrainingActivity.this);
		if (score.equalsIgnoreCase("")) {
			score = "No score available.";
		}
		alertDialogBuilder.setTitle("Final Score");
		alertDialogBuilder.setMessage(score).setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		// show it
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
