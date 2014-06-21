package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.adapters.ExerciseAdapter;
import fr.conferencehermes.confhermexam.adapters.NotesAdapter;
import fr.conferencehermes.confhermexam.parser.ExamExercise;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.NotesResult;
import fr.conferencehermes.confhermexam.util.Constants;

public class NotesActivity extends Activity {
	private LayoutInflater inflater;
	private ListView listviewNt;
	private NotesAdapter adapterNt;
	private ListView listviewEx;
	private ExerciseAdapter adapterEx;
	private int examID;
	private ArrayList<NotesResult> listNt;
	private TextView teacherName, examName;
	private ArrayList<ExamExercise> listEx;
	private ExamExercise ex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notes);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		teacherName = (TextView) findViewById(R.id.teacherName);
		examName = (TextView) findViewById(R.id.examName);

		try {
			Intent intent = getIntent();
			examID = intent.getIntExtra("exam_id", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		listviewNt = (ListView) findViewById(R.id.notesListView);
		listviewEx = (ListView) findViewById(R.id.exercizesListViewNotes);

		listviewEx.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}

		});

		AQuery aqNotes = new AQuery(NotesActivity.this);

		HashMap<String, Object> paramsNotes = new HashMap<String, Object>();

		paramsNotes.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		// params.put(Constants.KEY_EXERCSICE_ID, JSONParser.AUTH_KEY);
		paramsNotes.put(Constants.KEY_EXAM_ID, examID);
		paramsNotes.put(Constants.KEY_GLOBAL_TEST, true);
		paramsNotes.put(Constants.KEY_GROUPS, true);

		aqNotes.ajax(Constants.EXERCISE_RESULT_URL, paramsNotes,
				JSONObject.class, new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json.toString() == null)

							System.out.println(json.toString());
						try {
							if (json.has(Constants.KEY_STATUS)
									&& json.get(Constants.KEY_STATUS) != null) {
								if (json.getInt("status") == 200) {
									// pData =
									listNt = JSONParser
											.parseExerciseResult(json);

									if (listNt.size() != 0) {
										adapterNt = new NotesAdapter(
												NotesActivity.this, listNt);
										listviewNt.setAdapter(adapterNt);

									} else {

										Toast.makeText(
												NotesActivity.this
														.getApplicationContext(),
												"No Any Result",
												Toast.LENGTH_SHORT).show();
									}

								} else {

									Toast.makeText(
											NotesActivity.this
													.getApplicationContext(),
											json.getInt("status"),
											Toast.LENGTH_SHORT).show();

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					};
				});

		AQuery aqExersice = new AQuery(NotesActivity.this);

		HashMap<String, Object> paramsExersice = new HashMap<String, Object>();

		paramsExersice.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);

		paramsExersice.put(Constants.KEY_EXAM_ID, examID);

		aqExersice.ajax(Constants.CURRENT_EXAM_URL, paramsExersice,
				JSONObject.class, new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json.toString() == null)

							System.out.println(json.toString());
						try {
							if (json.has(Constants.KEY_STATUS)
									&& json.get(Constants.KEY_STATUS) != null) {
								if (json.getInt("status") == 200) {
									// pData =
									listEx = JSONParser.parseExamExercise(json);

									if (listEx.size() != 0) {
										adapterEx = new ExerciseAdapter(
												NotesActivity.this, listEx);
										listviewEx.setAdapter(adapterEx);

										teacherName.setText(ex.getCreatedBy());
										//examName.setText(text);

									} else {

										Toast.makeText(
												NotesActivity.this
														.getApplicationContext(),
												"No Any Result",
												Toast.LENGTH_SHORT).show();
									}

								} else {

									Toast.makeText(
											NotesActivity.this
													.getApplicationContext(),
											json.getInt("status"),
											Toast.LENGTH_SHORT).show();

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					};
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
