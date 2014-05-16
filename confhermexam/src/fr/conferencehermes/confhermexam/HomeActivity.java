package fr.conferencehermes.confhermexam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import fr.conferencehermes.confhermexam.util.Constants;

public class HomeActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		findViewById(R.id.homeButton1).setOnClickListener(this);
		findViewById(R.id.homeButton2).setOnClickListener(this);
		findViewById(R.id.homeButton3).setOnClickListener(this);
		findViewById(R.id.homeButton4).setOnClickListener(this);
		findViewById(R.id.homeButton5).setOnClickListener(this);
		findViewById(R.id.homeButton6).setOnClickListener(this);

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
