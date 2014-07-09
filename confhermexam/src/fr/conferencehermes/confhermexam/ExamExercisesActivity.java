package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.util.DataHolder;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exam_exersice);

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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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

}