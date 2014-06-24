package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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
	private static ListView listviewNt;
	// private NotesAdapter adapterNt;
	private ListView listviewEx;
	private ExerciseAdapter adapterEx;

	private static TextView teacherName, examName, medianScore, moyenneScore;
	private ArrayList<ExamExercise> listEx;
	private ExamExercise ex;
	private static ProgressBar progressBarNotes;
	private ImageView targetMe;

	static NotesAdapter adapterNt;
	private TextView globalTest;
	int paramExersiceId = -1;
	int paramExamId = -1;
	int paramGlobalTest = 1;
	int wantedPosition;
	int wantedChild;
	int paramGroups = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notes);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		teacherName = (TextView) findViewById(R.id.teacherName);
		examName = (TextView) findViewById(R.id.examName);
		globalTest = (TextView) findViewById(R.id.globalTest);
		medianScore = (TextView) findViewById(R.id.medianScore);
		moyenneScore = (TextView) findViewById(R.id.moyenneScore);
		targetMe = (ImageView) findViewById(R.id.targetMe);
		progressBarNotes = (ProgressBar) findViewById(R.id.progressBarNotes);
		try {
			Intent intent = getIntent();
			paramExamId = intent.getIntExtra("exam_id", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		exerciseResult(NotesActivity.this);

		listviewNt = (ListView) findViewById(R.id.notesListView);
		listviewEx = (ListView) findViewById(R.id.exercizesListViewNotes);

		// listviewEx.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		//
		//
		// progressBarNotes.setVisibility(View.VISIBLE);
		// listviewNt.setVisibility(View.GONE);
		//
		// }
		//
		// });

		globalTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				paramExersiceId = -1;
				paramGlobalTest = 1;
				exerciseResult(NotesActivity.this);
			}
		});

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupPG);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == R.id.radio_globe) {

					paramGroups = 0;
					exerciseResult(NotesActivity.this);
					Toast.makeText(getApplicationContext(), "radio_globe",
							Toast.LENGTH_SHORT).show();

				} else if (checkedId == R.id.radio_people) {
					paramGroups = 1;
					exerciseResult(NotesActivity.this);
					Toast.makeText(getApplicationContext(), "radio_people",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

		targetMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(NotesActivity.this, JSONParser.USER_ID,
						Toast.LENGTH_SHORT).show();

				// For a direct scroll:
				if (listviewNt != null) {

					listviewNt.post(new Runnable() {
						@Override
						public void run() {

							listviewNt.smoothScrollToPosition(21);

							wantedPosition = 21; // Whatever position you're
							// looking for
							int firstPosition = listviewNt
									.getFirstVisiblePosition()
									- listviewNt.getHeaderViewsCount();
							wantedChild = wantedPosition - firstPosition;

							if (wantedChild < 0
									|| wantedChild >= listviewNt
											.getChildCount()) {
								Log.w("TAG",
										"Unable to get view for desired position, because it's not being displayed on screen.");
								return;
							}
						}
					});

					Handler h = new Handler();
					h.postDelayed(new Runnable() {

						@Override
						public void run() {
							 View wantedView = listviewNt
									.getChildAt(21);
			
							// TODO Auto-generated method stub
							if (wantedView != null) {
								//wantedView.requestFocus();
						       
								wantedView.setBackgroundColor(getResources()
										.getColor(R.color.app_main_color));
							}
						}
					}, 5000);

					// myListView.setClickable(true);

				}
				// For a smooth scroll:
				// getListView().smoothScrollToPosition(21);

			}
		});

		AQuery aqExersice = new AQuery(NotesActivity.this);

		HashMap<String, Object> paramsExersice = new HashMap<String, Object>();

		paramsExersice.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);

		paramsExersice.put(Constants.KEY_EXAM_ID, paramExamId);

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

										teacherName
												.setText(ExamExercise.created_by);
										examName.setText(ExamExercise.exam_name);

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

	public void exerciseResult(final Context context) {
		AQuery aqNotes = new AQuery(context);

		HashMap<String, Object> paramsNotes = new HashMap<String, Object>();

		paramsNotes.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		paramsNotes.put(Constants.KEY_EXERCSICE_ID, paramExersiceId);
		paramsNotes.put(Constants.KEY_EXAM_ID, paramExamId);
		paramsNotes.put(Constants.KEY_GLOBAL_TEST, paramGlobalTest);
		paramsNotes.put(Constants.KEY_GROUPS, paramGroups);

		Log.i("Request*****", paramsNotes + "");

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
									ArrayList<NotesResult> listNt;
									listNt = JSONParser
											.parseExerciseResult(json);

									if (listNt.size() != 0) {
										adapterNt = new NotesAdapter(context,
												listNt);
										listviewNt.setAdapter(adapterNt);

										medianScore.setText("Median :"
												+ String.valueOf(NotesResult.median_score));
										moyenneScore.setText("Moyenne :"
												+ String.valueOf(NotesResult.moyenne_score));
										progressBarNotes
												.setVisibility(View.GONE);
										listviewNt.setVisibility(View.VISIBLE);
									} else {

										Toast.makeText(
												context.getApplicationContext(),
												"No Any Result",
												Toast.LENGTH_SHORT).show();
									}

								} else {

									Toast.makeText(
											context.getApplicationContext(),
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

	public int getParamExersiceId() {
		return paramExersiceId;
	}

	public void setParamExersiceId(int paramExersiceId) {
		this.paramExersiceId = paramExersiceId;
	}

	public int getParamGlobalTest() {
		return paramGlobalTest;
	}

	public void setParamGlobalTest(int paramGlobalTest) {
		this.paramGlobalTest = paramGlobalTest;
	}

}
