package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.TrainingExercise;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;
import fr.conferencehermes.confhermexam.util.Utilities;

public class ExercisesActivity extends FragmentActivity implements
		OnClickListener {

	private LayoutInflater inflater;
	private GridView gvMain;
	private ArrayAdapter<String> adapter;
	private ArrayList<TrainingExercise> exercises;
	private int training_id;
	private TextView timerText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exersice);
		training_id = getIntent().getIntExtra("training_id", 0);
		timerText = (TextView) findViewById(R.id.timerText);

		gvMain = (GridView) findViewById(R.id.gvMain);
		adjustGridView();
		gvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.parseColor("#0d5c7c"));
				int e_id = exercises.get(position).getExercise_id();
				String key = "trainingexercise" + String.valueOf(e_id);
				if (Utilities.readBoolean(ExercisesActivity.this, key, true)
						|| position == 0) {
					openExam(e_id);
				} else {
					Utilities
							.showAlertDialog(
									ExercisesActivity.this,
									"Attention",
									"Cet examen est termin� ou vous l'avez d�j� pass� vous ne pouvez pas le refaire.");
				}
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
								updateTimer();

							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				});

	}

	private void updateTimer() {
		Utilities.writeLong(ExercisesActivity.this, "millisUntilFinished", 0);
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

	private void openExam(int id) {
		Intent intent = new Intent(ExercisesActivity.this,
				QuestionResponseActivity.class);
		intent.putExtra("exercise_id", id);
		intent.putExtra("training_id", training_id);
		startActivity(intent);
	}

	private void showPasswordAlert(final int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Password");

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!input.getText().toString().trim().isEmpty()) {
					openExam(id);
				}
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
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
			Utilities.writeLong(ExercisesActivity.this, "millisUntilFinished",
					millisUntilFinished);
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