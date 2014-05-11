package fr.conferencehermes.confhermexam;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.Window;
import android.widget.LinearLayout;
import fr.conferencehermes.confhermexam.model.ExerciseLayout;

public class ExersiceActivity extends Activity {
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exersice);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout exerscieLayout = (LinearLayout) findViewById(R.id.exerscieLayout);
		for (int i = 0; i < 4; i++) {
			exerscieLayout.addView(new ExerciseLayout(ExersiceActivity.this));
		}

		LinearLayout exerscieLayout1 = (LinearLayout) findViewById(R.id.exerscieLayout1);
		for (int i = 0; i < 4; i++) {
			exerscieLayout1.addView(new ExerciseLayout(ExersiceActivity.this));
		}

		LinearLayout exerscieLayout2 = (LinearLayout) findViewById(R.id.exerscieLayout2);
		for (int i = 0; i < 4; i++) {
			exerscieLayout2.addView(new ExerciseLayout(ExersiceActivity.this));
		}

		LinearLayout exerscieLayout3 = (LinearLayout) findViewById(R.id.exerscieLayout3);
		for (int i = 0; i < 4; i++) {
			exerscieLayout3.addView(new ExerciseLayout(ExersiceActivity.this));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
