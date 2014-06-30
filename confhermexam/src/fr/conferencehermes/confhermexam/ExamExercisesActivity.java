package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
import android.widget.TextView;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exersice);

		db = new DatabaseHelper(ExamExercisesActivity.this);
		timerText = (TextView) findViewById(R.id.timerText);

		gvMain = (GridView) findViewById(R.id.gvMain);
		adjustGridView();
		gvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.parseColor("#0d5c7c"));
				int e_id = exercises.get(position).getId();
				openExercise(e_id);
			}

		});

		examId = getIntent().getIntExtra("exam_id", -1);
		eventId = getIntent().getIntExtra("event_id", -1);
		exercises = db.getAllExercisesByExamId(examId);
		String[] data = new String[exercises.size()];
		for (int i = 0; i < exercises.size(); i++) {
			data[i] = exercises.get(i).getName();
		}

		adapter = new ArrayAdapter<String>(ExamExercisesActivity.this,
				R.layout.item, R.id.tvText, data);
		gvMain = (GridView) findViewById(R.id.gvMain);
		gvMain.setAdapter(adapter);
		updateTimer();

	}

	private void updateTimer() {
		Utilities.writeLong(ExamExercisesActivity.this, "millisUntilFinished",
				0);
		final CounterClass timer = new CounterClass(DataHolder.getInstance()
				.getTrainingDuration(), 1000);
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
			Utilities.writeLong(ExamExercisesActivity.this,
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
			timerText.setText("Temps epreuve - " + hms);
		}
	}

}