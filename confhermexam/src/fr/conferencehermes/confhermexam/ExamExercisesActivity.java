package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;

import fr.conferencehermes.confhermexam.lifecycle.ScreenReceiver;

import fr.conferencehermes.confhermexam.parser.Event;

import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.ExerciseAnswer;
import fr.conferencehermes.confhermexam.util.DataHolder;
import fr.conferencehermes.confhermexam.util.ExamJsonTransmitter;
import fr.conferencehermes.confhermexam.util.Utilities;

public class ExamExercisesActivity extends FragmentActivity implements
		OnClickListener {

	LayoutInflater inflater;
	GridView gvMain;
	ArrayAdapter<String> adapter;
	ArrayList<Exercise> exercises;
	TextView timerText;
	DatabaseHelper db;
	private int examId;
	private int eventId;
	private ImageView timePause, timePlay;
	private CounterClass timer;
	private long timeToResume;
	private long duration = 0;
	private boolean TIMER_PAUSED = false;

	private boolean onPaused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exam_exersice);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		db = new DatabaseHelper(ExamExercisesActivity.this);
		timerText = (TextView) findViewById(R.id.timerText);
		timePause = (ImageView) findViewById(R.id.time_pause);
		timePlay = (ImageView) findViewById(R.id.time_play);

		gvMain = (GridView) findViewById(R.id.gvMain);
		adjustGridView();
		gvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.parseColor("#0d5c7c"));
				int e_id = exercises.get(position).getId();
				String key = "exercise_passed" + String.valueOf(e_id);
				if (!Utilities.readBoolean(ExamExercisesActivity.this, key,
						false)) {
					if (!TIMER_PAUSED)
						openExercise(e_id);
				} else {
					Utilities.showAlertDialog(
							ExamExercisesActivity.this,
							"Attention",
							getResources().getString(
									R.string.exam_already_passed_alert));
				}
			}
		});

		examId = getIntent().getIntExtra("exam_id", -1);
		eventId = getIntent().getIntExtra("event_id", -1);
		exercises = db.getAllExercisesByExamId(examId);
		String[] data = new String[exercises.size()];
		for (int i = 0; i < exercises.size(); i++) {
			data[i] = exercises.get(i).getName();
			String key = "exercise_passed"
					+ String.valueOf(exercises.get(i).getId());
			Utilities.writeBoolean(ExamExercisesActivity.this, key, false);
		}

		DataHolder.getInstance().setMillisUntilFinished(0);
		Exam exam = db.getExam(examId);
		long startTime = exam.getStartDate() * 1000;
		long endTime = exam.getEndDate() * 1000;
		duration = getDuration(startTime, endTime);
		updateTimer();

		if (db.getExam(examId).getCategoryType().equalsIgnoreCase("2")) {
			timePause.setVisibility(View.GONE);
			timePlay.setVisibility(View.GONE);
		}

		exam.setIsAlreadyPassed(1);
		db.updateExam(exam);
		db.close();

		adapter = new ArrayAdapter<String>(ExamExercisesActivity.this,
				R.layout.item, R.id.tvText, data);
		gvMain = (GridView) findViewById(R.id.gvMain);
		gvMain.setAdapter(adapter);

		timePause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timePause.setVisibility(View.GONE);
				timePlay.setVisibility(View.VISIBLE);
				timer.cancel();
				TIMER_PAUSED = true;
			}
		});

		timePlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timePause.setVisibility(View.VISIBLE);
				timePlay.setVisibility(View.GONE);
				timer = new CounterClass(timeToResume, 1000);
				timer.start();
				TIMER_PAUSED = false;
			}
		});

	}

	public long getDuration(long start, long end) {
		Calendar calendar = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Paris"));
		long currentTime = calendar.getTimeInMillis() / 1000;

		long maxDuration = end - start;
		long mDuration = end - currentTime;

		return mDuration <= maxDuration ? mDuration : maxDuration;

	}

	private void updateTimer() {
		timer = new CounterClass(duration, 1000);
		timer.start();
	}

	private void adjustGridView() {
		gvMain.setNumColumns(GridView.AUTO_FIT);
		gvMain.setColumnWidth(180);
		gvMain.setVerticalSpacing(20);
		gvMain.setHorizontalSpacing(50);
		gvMain.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
	}

	private void openExercise(int id) {
		Intent intent = new Intent(ExamExercisesActivity.this,
				ExaminationActivity.class);
		intent.putExtra("exercise_id", id);
		intent.putExtra("exam_id", examId);
		intent.putExtra("event_id", eventId);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		timer.cancel();
		super.onStop();
	}

	@Override
	protected void onStart() {
		if (timer != null)
			timer.start();
		super.onStart();
	}

	public class CounterClass extends CountDownTimer {
		public CounterClass(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			timerText.setText("");
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
			timerText.setText("Temps epreuve - " + hms);
			timeToResume = millis;
		}
	}

	@Override
	protected void onDestroy() {
		Utilities.writeBoolean(ExamExercisesActivity.this,
				String.valueOf("exam" + examId), false);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		showAlertDialog("Attention",
				getResources().getString(R.string.exercise_finish_text));
	}

	public void showAlertDialog(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ExamExercisesActivity.this);

		alertDialogBuilder.setTitle(title);
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						DatabaseHelper dbHelper = new DatabaseHelper(
								ExamExercisesActivity.this);
						ArrayList<Exercise> dbExercises = dbHelper
								.getAllExercisesByExamId(examId);
						for (int i = 0; i < dbExercises.size(); i++) {
							if (dbExercises.get(i).getExerciseIsAlreadyPassed() == 0) {
								try {
									sendEmptyAnswers(dbExercises.get(i));
									Log.d("EMPTY EXERCISE SAVED: ",
											String.valueOf(dbExercises.get(i)
													.getId()));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
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
	protected void onResume() {
		super.onResume();
		// ONLY WHEN SCREEN TURNS ON
		if (!ScreenReceiver.wasScreenOn) {
			// THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE

		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT
			// CHANGED
			System.out.println("SCREEN TURNED ON");
		}

		if (onPaused == true) {
			showAlertDialog();
		}

	}

	@Override
	protected void onPause() {

		onPaused = true;

		// WHEN THE SCREEN IS ABOUT TO TURN OFF
		if (ScreenReceiver.wasScreenOn) {
			// THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A
			// SCREEN STATE CHANGE
			System.out.println("SCREEN TURNED OFF");
		} else {
			// THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT
			// CHANGED
		}
		super.onPause();
	}

	private void showAlertDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ExamExercisesActivity.this);

		// set dialog message
		alertDialogBuilder
				.setMessage(getResources().getString(R.string.drop_out_text))
				.setCancelable(false)
				.setPositiveButton(
						getResources().getString(R.string.drop_out_text_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								Intent intentHome = new Intent(
										ExamExercisesActivity.this,
										HomeActivity.class);
								startActivity(intentHome);

								ExamExercisesActivity.this.finish();
								onPaused = false;
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

	public void sendEmptyAnswers(Exercise exercise) throws JSONException {
		JSONObject object = new JSONObject();
		JSONObject data = new JSONObject();

		DatabaseHelper db = new DatabaseHelper(ExamExercisesActivity.this);
		Event event = db.getEvent(eventId);

		data.put("event_id", eventId);
		data.put("exam_id", event.getTestId());
		data.put("exercise_id", exercise.getId());
		data.put("is_send", 0);
		data.put("type", exercise.getType());

		JSONArray answers = new JSONArray();
		data.put("question_answers", answers);
		object.put("auth_key", Utilities.readString(ExamExercisesActivity.this,
				"auth_key", ""));
		object.put("device_id",
				Utilities.getDeviceId(ExamExercisesActivity.this));
		object.put("device_time", System.currentTimeMillis() / 1000);
		object.put("data", data);

		if (Utilities.isNetworkAvailable(ExamExercisesActivity.this)) {
			ExamJsonTransmitter transmitter = new ExamJsonTransmitter(
					ExamExercisesActivity.this);
			transmitter.execute(object);
			finish();
		} else {
			ExerciseAnswer exerciseAnswer = new ExerciseAnswer();
			exerciseAnswer.setExerciseId(exercise.getId());
			exerciseAnswer.setExamId(exercise.getExamId());
			exerciseAnswer.setEventId(eventId);
			exerciseAnswer.setJsonString(data.toString());
			db.createExerciseAnswer(exerciseAnswer);
		}

		db.closeDB();

	}

}