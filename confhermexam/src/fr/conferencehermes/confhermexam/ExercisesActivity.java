package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.TrainingExercise;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;

public class ExercisesActivity extends FragmentActivity implements
		OnClickListener {

	private LayoutInflater inflater;
	private GridView gvMain;
	private ArrayAdapter<String> adapter;
	private ArrayList<TrainingExercise> exercises;
	private int training_id;
	private TextView timerText;
	CounterClass timer;
	private int duration = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exersice);
		training_id = getIntent().getIntExtra("training_id", 0);
		timerText = (TextView) findViewById(R.id.timerText);

		DataHolder.getInstance().setMillisUntilFinished(0);

		gvMain = (GridView) findViewById(R.id.gvMain);
		adjustGridView();
		gvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.parseColor("#0d5c7c"));
				int e_id = exercises.get(position).getExercise_id();
				openExam(e_id);
			}
		});

		AQuery aq = new AQuery(ExercisesActivity.this);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("training_id", training_id);

		aq.ajax(Constants.TRAINING_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						try {
							if (json.has("data") && json.get("data") != null) {
								exercises = JSONParser
										.parseTrainingExercises(json);
								String[] data = new String[exercises.size()];
								for (int i = 0; i < exercises.size(); i++) {
									data[i] = exercises.get(i).getTitle();
								}

								adapter = new ArrayAdapter<String>(
										ExercisesActivity.this, R.layout.item,
										R.id.tvText, data);
								gvMain = (GridView) findViewById(R.id.gvMain);
								gvMain.setAdapter(adapter);
								updateTimer(exercises.get(0).getDuration());

							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				});

	}

	@Override
	protected void onResume() {
		if (exercises != null)
			if (!exercises.isEmpty())
				updateTimer(DataHolder.getInstance().getMillisUntilFinished());
		super.onResume();
	}

	private void updateTimer(long mills) {
		// Utilities.writeLong(ExercisesActivity.this, "millisUntilFinished",
		// 0);
		timer = new CounterClass(mills, 1000);
		timer.start();
	}

	private void adjustGridView() {
		gvMain.setNumColumns(GridView.AUTO_FIT);
		gvMain.setColumnWidth(180);
		gvMain.setVerticalSpacing(20);
		gvMain.setHorizontalSpacing(50);
		gvMain.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
	}

	private void openExam(int id) {
		Intent intent = new Intent(ExercisesActivity.this,
				TrainingActivity.class);
		intent.putExtra("exercise_id", id);
		intent.putExtra("training_id", training_id);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		timer.cancel();
		super.onPause();
	}

	@Override
	protected void onStop() {

		super.onStop();
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
			finish();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			long millis = millisUntilFinished;
			/*
			 * Utilities.writeLong(ExercisesActivity.this,
			 * "millisUntilFinished", millisUntilFinished);
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
			timerText.setText("Temps epreuve - " + hms);

		}
	}

}