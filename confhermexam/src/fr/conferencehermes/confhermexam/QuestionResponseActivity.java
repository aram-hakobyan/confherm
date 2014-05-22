package fr.conferencehermes.confhermexam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.conferencehermes.confhermexam.adapters.QuestionsAdapter;

public class QuestionResponseActivity extends Activity {
	LayoutInflater inflater;
	ListView listview;
	QuestionsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question_response);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < 12; i++) {
			list.add("Examen Nom_ " + i);
		}
		listview = (ListView) findViewById(R.id.questionsListView);
		adapter = new QuestionsAdapter(QuestionResponseActivity.this, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
