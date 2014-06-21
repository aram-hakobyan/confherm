package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.TrainingExercise;
import fr.conferencehermes.confhermexam.util.Constants;

public class ExercisesActivity extends FragmentActivity implements
		OnClickListener {
	LayoutInflater inflater;
	GridView gvMain;
	ArrayAdapter<String> adapter;
	ArrayList<TrainingExercise> exercises;
	int training_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exersice);
		training_id = getIntent().getIntExtra("training_id", 0);

		gvMain = (GridView) findViewById(R.id.gvMain);
		adjustGridView();
		gvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.parseColor("#0d5c7c"));
				int e_id = exercises.get(position).getExercise_id();
				if (getIntent().getBooleanExtra("exam", false))
					showPasswordAlert(e_id);
				else
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

							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				});

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
}