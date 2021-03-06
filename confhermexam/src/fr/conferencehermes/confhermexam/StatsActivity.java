package fr.conferencehermes.confhermexam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.model.Stat;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class StatsActivity extends Activity {

	private Context context;
	private AQuery aq;
	private LinearLayout statsLayout;
	private ProgressBar progress;
	private int exerciseId;
	private int eventId;
	private LayoutInflater inflater;
	private TextView title, desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_stats);

		context = StatsActivity.this;
		aq = new AQuery(context);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		statsLayout = (LinearLayout) findViewById(R.id.statsLayout);
		progress = (ProgressBar) findViewById(R.id.progressBarStats);
		title = (TextView) findViewById(R.id.exerciseName);
		desc = (TextView) findViewById(R.id.descText);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			exerciseId = extras.getInt("exercise_id");
			eventId = extras.getInt("event_id");
		}

		downloadQuestionStats();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	private void downloadQuestionStats() {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put(Constants.KEY_EXERCSICE_ID, exerciseId);
		params.put(Constants.KEY_EVENT_ID, eventId);

		if (Utilities.isNetworkAvailable(context)) {
			aq.ajax(Constants.STATISTICS_URL, params, JSONObject.class,
					new AjaxCallback<JSONObject>() {
						@Override
						public void callback(String url, JSONObject json,
								AjaxStatus status) {
							JSONObject data = json.optJSONObject("data");
							String titleText = data.optString("title");
							String descText = data.optString("text");
							title.setText(titleText);
							desc.setText(descText);

							JSONArray questions = data
									.optJSONArray("questions");
							parseQuestionStats(questions);
						}
					});
		}
	}

	private void parseQuestionStats(JSONArray questions) {
		ArrayList<Stat> stats = new ArrayList<Stat>();

		for (int i = 0; i < questions.length(); i++) {
			JSONObject obj = questions.optJSONObject(i);
			Stat s = new Stat();
			s.setType(obj.optInt("type"));
			s.setTypeName(obj.optString("type_name"));
			s.setName(obj.optString("name"));

			JSONObject percentages = obj.optJSONObject("percentages");
			JSONObject good = percentages.optJSONObject("bonnes_reponses");
			JSONObject bad = percentages.optJSONObject("mauvaises_reponses");
			JSONObject passed = percentages.optJSONObject("pass_de_reponses");

			s.setGoodAnswerPerc(good.optString("percentage"));
			s.setBadAnswerPerc(bad.optString("percentage"));
			s.setPassedAnswerPerc(passed.optString("percentage"));

			if (s.getType() == 3) {
				ArrayList<String> goodAnswers = new ArrayList<String>();
				JSONArray gAnswers = obj.optJSONArray("good_answers");
				for (int j = 0; j < gAnswers.length(); j++) {
					goodAnswers.add(gAnswers.optString(j));
				}
				s.setGoodAnswers(goodAnswers);
				s.setAllAnswers(obj.optString("all_answers"));
			}

			ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
			JSONArray answers = obj.optJSONArray("answers");
			for (int j = 0; j < answers.length(); j++) {
				JSONObject ans = answers.optJSONObject(j);
				if (ans == null)
					break;
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", ans.optString("name"));
				map.put("percentage", ans.optString("percentage"));
				array.add(map);
			}

			s.setAnswers(array);
			stats.add(s);
		}

		drawQuestionStats(stats);

	}

	private void drawQuestionStats(ArrayList<Stat> stats) {

		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 15);

		for (int i = 0; i < stats.size(); i++) {
			Stat s = stats.get(i);

			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.stat_item_qcm, null, false);

			TextView title = (TextView) layout.findViewById(R.id.title);
			TextView res1 = (TextView) layout.findViewById(R.id.res1);
			TextView res2 = (TextView) layout.findViewById(R.id.res2);
			TextView res3 = (TextView) layout.findViewById(R.id.res3);
			TextView type = (TextView) layout.findViewById(R.id.type);
			TextView question = (TextView) layout.findViewById(R.id.question);
			TextView answers = (TextView) layout.findViewById(R.id.answers);

			title.setText("Question " + String.valueOf(i + 1) + ".");
			res1.setText(s.getGoodAnswerPerc() + "%");
			res2.setText(s.getBadAnswerPerc() + "%");
			res3.setText(s.getPassedAnswerPerc() + "%");
			type.setText(s.getTypeName());
			question.setText(s.getName());

			StringBuilder sb = new StringBuilder();
			if (s.getType() == 1 || s.getType() == 2) {
				for (int j = 0; j < s.getAnswers().size(); j++) {
					HashMap<String, String> map = s.getAnswers().get(j);
					sb.append(map.get("percentage") + "% - " + map.get("name")
							+ "\n" + "\n");
				}

			} else if (s.getType() == 3) {
				ArrayList<String> goodAnswers = s.getGoodAnswers();
				for (int j = 0; j < goodAnswers.size(); j++) {
					sb.append(goodAnswers.get(j));
					if (j < goodAnswers.size() - 1)
						sb.append(" / ");
				}
				sb.append("\n\n");
				sb.append(s.getAllAnswers());
				sb.append("\n\n");
			}

			answers.setText(sb.toString());
			statsLayout.addView(layout, params);

		}

		progress.setVisibility(View.GONE);

	}

}
