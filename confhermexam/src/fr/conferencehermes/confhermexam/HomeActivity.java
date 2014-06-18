package fr.conferencehermes.confhermexam;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import fr.conferencehermes.confhermexam.connection.HttpAsyncPost;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;

public class HomeActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		((LinearLayout) findViewById(R.id.homeButton1))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.homeButton2))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.homeButton3))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.homeButton4))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.homeButton5))
				.setOnClickListener(this);
		((LinearLayout) findViewById(R.id.homeButton6))
				.setOnClickListener(this);

		// new HttpAsyncPost().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(HomeActivity.this, MyFragmentActivity.class);

		switch (v.getId()) {
		case R.id.homeButton1:
			intent.putExtra("PAGE_ID", Constants.PROFILE_FRAGMENT);
			break;
		case R.id.homeButton2:
			intent.putExtra("PAGE_ID", Constants.PLANNING_FRAGMENT);
			break;
		case R.id.homeButton3:
			intent.putExtra("PAGE_ID", Constants.EXAMINE_FRAGMENT);
			break;
		case R.id.homeButton4:
			intent.putExtra("PAGE_ID", Constants.ENTRAINMENT_FRAGMENT);
			break;
		case R.id.homeButton5:
			intent.putExtra("PAGE_ID", Constants.RESULTATS_FRAGMENT);
			break;
		case R.id.homeButton6:
			intent.putExtra("PAGE_ID", Constants.TELECHARG_FRAGMENT);
			break;

		}

		if (intent != null)
			startActivity(intent);

	}

}
