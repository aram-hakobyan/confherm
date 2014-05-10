package fr.conferencehermes.confhermexam;

import fr.conferencehermes.confhermexam.model.ExamLayout;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.Window;
import android.widget.LinearLayout;

public class TelechargementActivity extends Activity {
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_telechargement);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout examsLayout = (LinearLayout) findViewById(R.id.examsLayout);
		for (int i = 0; i < 5; i++) {
			examsLayout.addView(new ExamLayout(TelechargementActivity.this));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

}
