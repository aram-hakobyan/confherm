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
import fr.conferencehermes.confhermexam.adapters.ExerciseAdapter;
import fr.conferencehermes.confhermexam.adapters.NotesAdapter;

public class NotesActivity extends Activity {
	LayoutInflater inflater;
	ListView listview;
	NotesAdapter adapter;
	ListView listviewEx;
	ExerciseAdapter adapterEx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_notes);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < 35; i++) {
			list.add("LASTNAME Firstname" + i);
		}
		listview = (ListView) findViewById(R.id.notesListView);
		adapter = new NotesAdapter(NotesActivity.this, list);
		listview.setAdapter(adapter);

		ArrayList<String> list2 = new ArrayList<String>();
		for (int i = 1; i < 21; i++) {
			list2.add("EXERCICE " + i);
		}
		listviewEx = (ListView) findViewById(R.id.exercizesListViewNotes);
		adapterEx = new ExerciseAdapter(NotesActivity.this, list2);
		listviewEx.setAdapter(adapterEx);

		listviewEx.setOnItemClickListener(new OnItemClickListener() {
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
