package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.adapters.QuestionsAdapter;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Question;
import fr.conferencehermes.confhermexam.util.Constants;

public class QuestionResponseActivity extends Activity implements
		OnClickListener {
	LayoutInflater inflater;
	ListView listview;
	QuestionsAdapter adapter;
	Exercise exercise;
	ArrayList<Question> questions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question_response);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int id = getIntent().getIntExtra("id", 1);

		listview = (ListView) findViewById(R.id.questionsListView);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectQuestion(questions.get(position));
			}

		});

		AQuery aq = new AQuery(QuestionResponseActivity.this);
		String url = "http://ecni.conference-hermes.fr/api/exercise.php";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("id", id);

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				try {
					if (json.has("data") && json.get("data") != null) {

						exercise = JSONParser.parseExercises(json);
						questions = exercise.getQuestions();
						if (!questions.isEmpty()) {
							selectQuestion(questions.get(0));
						}
						if (adapter == null) {
							adapter = new QuestionsAdapter(
									QuestionResponseActivity.this, exercise
											.getQuestions());
						} else {
							adapter.notifyDataSetChanged();
						}
						listview.setAdapter(adapter);
						TextView temps = (TextView) findViewById(R.id.temps2);
						temps.setText("Temps exercice - "
								+ exercise.getTimeOpen());

					}
				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		});

	}

	private void selectQuestion(Question q) {
		TextView title = (TextView) findViewById(R.id.questionTitle);
		TextView txt = (TextView) findViewById(R.id.question);
		TextView ennouncer = (TextView) findViewById(R.id.temps1);
		title.setText("QUESTION " + q.getId());
		txt.setText(q.getQuestionText());
		ennouncer.setText(q.getCreatedBy());

		TextView answer1 = (TextView) findViewById(R.id.answer1);
		TextView answer2 = (TextView) findViewById(R.id.answer2);
		TextView answer3 = (TextView) findViewById(R.id.answer3);
		TextView answer4 = (TextView) findViewById(R.id.answer4);
		TextView answer5 = (TextView) findViewById(R.id.answer5);

		answer1.setText("1. " + q.getAnswers().get(0).getAnswer());
		answer2.setText("2. " + q.getAnswers().get(1).getAnswer());
		answer3.setText("3. " + q.getAnswers().get(2).getAnswer());
		answer4.setText("4. " + q.getAnswers().get(3).getAnswer());
		answer5.setText("5. " + q.getAnswers().get(4).getAnswer());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.validerBtn:

			break;

		default:
			break;
		}

	}

}
