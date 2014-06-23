package fr.conferencehermes.confhermexam;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.adapters.QuestionsAdapter;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Question;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class QuestionResponseActivity extends Activity implements
		OnClickListener {
	private LayoutInflater inflater;
	private ListView listview;
	private QuestionsAdapter adapter;
	private Exercise exercise;
	private ArrayList<Question> questions;
	private LinearLayout answersLayout;
	private Button btnImage, btnAudio, btnVideo, abandonner, ennouncer,
			valider;
	private int exercise_id;
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
	private EditText editText;

	private boolean onPaused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question_response);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		aq = new AQuery(QuestionResponseActivity.this);
		answersArray = new JSONArray();
		multipleAnswers = new ArrayList<Integer>();

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

		CounterClass timer = new CounterClass(Utilities.readLong(
				QuestionResponseActivity.this, "millisUntilFinished", 7200000),
				1000);
		timer.start();

		startTime = SystemClock.uptimeMillis();
		customHandler.postDelayed(updateTimerThread, 0);

		exercise_id = getIntent().getIntExtra("exercise_id", 1);
		answersLayout = (LinearLayout) findViewById(R.id.answersLayout);
		listview = (ListView) findViewById(R.id.questionsListView);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectQuestion(questions.get(position), position);
			}

		});

		AQuery aq = new AQuery(QuestionResponseActivity.this);

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

								questions = exercise.getQuestions();
								if (!questions.isEmpty()) {
									selectQuestion(questions.get(0), 0);
								}
								if (adapter == null) {
									adapter = new QuestionsAdapter(
											QuestionResponseActivity.this,
											exercise.getQuestions());
								} else {
									adapter.notifyDataSetChanged();
								}
								listview.setAdapter(adapter);
								examName.setText(exercise.getName());
								teacher.setText("Teacher "
										+ exercise.getTeacher());

							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				});

	}

	private void selectQuestion(Question q, int position) {
		currentQuestionId = position;
		currentQuestion = q;
		answersLayout.removeAllViews();
		TextView title = (TextView) findViewById(R.id.questionTitle);
		TextView txt = (TextView) findViewById(R.id.question);
		title.setText("QUESTION " + String.valueOf(position + 1));
		txt.setText(Html.fromHtml(q.getQuestionText()));

		int answersCount = q.getAnswers().size();

		// Single choice answer
		if (q.getType().equalsIgnoreCase("0")) {
			mRadioGroup = new RadioGroup(QuestionResponseActivity.this);
			mRadioGroup.setOrientation(RadioGroup.VERTICAL);
			for (int i = 0; i < answersCount; i++) {
				RadioButton newRadioButton = new RadioButton(this);
				newRadioButton.setText(q.getAnswers().get(i).getAnswer());
				newRadioButton.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.WRAP_CONTENT,
						RadioGroup.LayoutParams.WRAP_CONTENT);
				mRadioGroup.addView(newRadioButton, 0, layoutParams);
			}
			answersLayout.addView(mRadioGroup);
		} else // Multichoice answer
		if (q.getType().equalsIgnoreCase("1")) {
			for (int i = 0; i < answersCount; i++) {
				LinearLayout checkBoxLayout = new LinearLayout(
						QuestionResponseActivity.this);
				checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
				final CheckBox checkBox = new CheckBox(
						QuestionResponseActivity.this);
				checkBox.setGravity(Gravity.CENTER_VERTICAL);
				TextView text = new TextView(QuestionResponseActivity.this);
				text.setText(q.getAnswers().get(i).getAnswer());
				text.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				// layoutParams.setMargins(50, 20, 30, 20);
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

		} else // Essay answer
		if (q.getType().equalsIgnoreCase("2")) {
			editText = new EditText(QuestionResponseActivity.this);
			editText.setGravity(Gravity.CENTER_VERTICAL);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			answersLayout.addView(editText, layoutParams);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	public void sendAnswers() throws JSONException {
		Utilities.showOrHideActivityIndicator(QuestionResponseActivity.this, 0,
				"Please wait...");
		String url = "http://ecni.conference-hermes.fr/api/traninganswer";
		Map<String, String> params = new HashMap<String, String>();
		JSONObject data = new JSONObject();

		data.put("training_id", 0);
		data.put("exercise_id", exercise.getId());
		data.put("type", exercise.getType());

		JSONArray answers = answersArray;
		data.put("question_answers", answers.toString());

		params.put("auth_key", JSONParser.AUTH_KEY);
		params.put("data", data.toString());
		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				Utilities.showOrHideActivityIndicator(
						QuestionResponseActivity.this, 1, "Please wait...");
			}
		});
	}

	private void saveQuestionAnswers() throws JSONException {
		JSONObject questionAnswer = new JSONObject();
		questionAnswer.put("question_id", currentQuestion.getId());
		questionAnswer.put("question_type", currentQuestion.getType());

		JSONArray answers = new JSONArray();

		try {
			if (currentQuestion.getType().equalsIgnoreCase("0")) {

				int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
				View radioButton = mRadioGroup.findViewById(radioButtonID);
				int idx = mRadioGroup.indexOfChild(radioButton);
				int answerId = currentQuestion.getAnswers().get(idx).getId();
				answers.put(answerId);

			} else if (currentQuestion.getType().equalsIgnoreCase("1")) {
				for (int i = 0; i < multipleAnswers.size(); i++) {
					answers.put(multipleAnswers.get(i));
				}
			} else if (currentQuestion.getType().equalsIgnoreCase("2")) {
				answers.put(editText.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		questionAnswer.put("answers", answers);
		answersArray.put(questionAnswer);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.validerBtn:
			if (currentQuestionId < questions.size() - 1) {
				try {
					saveQuestionAnswers();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				currentQuestionId++;
				selectQuestion(questions.get(currentQuestionId),
						currentQuestionId);
			} else {
				try {
					sendAnswers();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.abandonner:
			try {
				sendAnswers();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
		case R.id.ennouncer:
			if (exercise != null)
				openDialog(exercise.getFiles());
			break;
		case R.id.btnImage:
			if (currentQuestion != null)
				openDialog(currentQuestion.getFiles());
			break;
		case R.id.btnAudio:
			if (currentQuestion != null)
				openDialog(currentQuestion.getFiles());
			break;
		case R.id.btnVideo:
			if (currentQuestion != null)
				openDialog(currentQuestion.getFiles());
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
			temps1.setText("Completed.");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long millis = millisUntilFinished;
			Utilities.writeLong(QuestionResponseActivity.this,
					"millisUntilFinished", millisUntilFinished);
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

	public void openDialog(HashMap<String, String> files) {
		Dialog dialog = new Dialog(QuestionResponseActivity.this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.new_dialog);

		final ImageView img = (ImageView) dialog.findViewById(R.id.imageView1);
		final VideoView video = (VideoView) dialog
				.findViewById(R.id.videoView1);
		final MediaController mc = (MediaController) dialog
				.findViewById(R.id.mediaController1);
		final MediaPlayer mediaPlayer = new MediaPlayer();

		final String IMAGE_URL = files.get("image");
		final String AUDIO_URL = files.get("audio");
		final String VIDEO_URL = files.get("video");

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

		dialog.findViewById(R.id.button1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mediaPlayer != null) {
							try {
								mediaPlayer.stop();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (video.isPlaying()) {
							video.stopPlayback();
						}
						img.setVisibility(View.VISIBLE);
						video.setVisibility(View.INVISIBLE);
						mc.setVisibility(View.INVISIBLE);
					}
				});
		dialog.findViewById(R.id.button2).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						img.setVisibility(View.INVISIBLE);
						video.setVisibility(View.INVISIBLE);
						mc.setVisibility(View.VISIBLE);
						if (video.isPlaying()) {
							video.stopPlayback();
						}
						try {
							if (AUDIO_URL != null)
								if (!AUDIO_URL.isEmpty()) {
									mediaPlayer.setDataSource(AUDIO_URL);
									mediaPlayer.prepare();
									mediaPlayer
											.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
												@Override
												public void onPrepared(
														MediaPlayer arg0) {
													mediaPlayer.start();
												}
											});
								}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});
		dialog.findViewById(R.id.button3).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mediaPlayer != null) {
							try {
								mediaPlayer.stop();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						img.setVisibility(View.INVISIBLE);
						video.setVisibility(View.VISIBLE);
						mc.setVisibility(View.INVISIBLE);

						try {
							if (VIDEO_URL != null)
								if (VIDEO_URL.isEmpty()) {
									video.setVideoURI(Uri.parse(VIDEO_URL));
									video.start();
								}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

		dialog.show();
		dialog.getWindow().setLayout(800, 600);

	}

	private void showAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				QuestionResponseActivity.this);

		// set title
		alertDialogBuilder.setTitle("You have been dropped out");

		// set dialog message
		alertDialogBuilder
				.setMessage(
						"You dropped out drop examination , because you leave examen")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						Intent intentHome = new Intent(
								QuestionResponseActivity.this,
								HomeActivity.class);
						startActivity(intentHome);

						QuestionResponseActivity.this.finish();
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

		if (onPaused == true) {
			showAlertDialog();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		onPaused = true;

	}

}
