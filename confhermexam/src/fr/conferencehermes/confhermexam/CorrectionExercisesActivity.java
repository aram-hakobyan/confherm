package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.adapters.GridViewAdapter;
import fr.conferencehermes.confhermexam.parser.CorrectionsExercise;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;

public class CorrectionExercisesActivity extends FragmentActivity implements
		OnClickListener {

	LayoutInflater inflater;
	GridView gvMain;
	GridViewAdapter adapter;
	ArrayList<CorrectionsExercise> corExercises;
	int exam_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_correction_exersice);
		exam_id = getIntent().getIntExtra("exam_id", 0);

		gvMain = (GridView) findViewById(R.id.gvMain);
		adjustGridView();
		gvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.parseColor("#0d5c7c"));
				int exersice_id = corExercises.get(position).getExercise_id();
				int exam_id = corExercises.get(position).getEvent_id();
				openExamCorrection(exersice_id, exam_id);
			}

		});

		AQuery aq = new AQuery(CorrectionExercisesActivity.this);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("exam_id", exam_id);

		aq.ajax(Constants.EXAM_CORRECTIONS_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						try {
							if (json.has("data") && json.get("data") != null) {
								corExercises = JSONParser
										.parseCorrectionsExercises(json);

								adapter = new GridViewAdapter(
										CorrectionExercisesActivity.this,
										corExercises);
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
		gvMain.setColumnWidth(250);
		gvMain.setVerticalSpacing(20);
		gvMain.setHorizontalSpacing(50);
		gvMain.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
	}

	private void openExamCorrection(int exersice_id, int exam_id) {
		Intent intent = new Intent(CorrectionExercisesActivity.this,
				CorrectionActivity.class);
		intent.putExtra("exercise_id", exersice_id);
		intent.putExtra("event_id", exam_id);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}