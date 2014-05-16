package fr.conferencehermes.confhermexam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import fr.conferencehermes.confhermexam.fragments.ExamineFragment;
import fr.conferencehermes.confhermexam.fragments.MyProfileFragment;
import fr.conferencehermes.confhermexam.fragments.NotesFragment;
import fr.conferencehermes.confhermexam.fragments.PlanningFragment;
import fr.conferencehermes.confhermexam.fragments.ResultatFragment;
import fr.conferencehermes.confhermexam.fragments.TelechargementFragment;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class MyFragmentActivity extends FragmentActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_fragment);

		findViewById(R.id.headerBtnProfil).setOnClickListener(this);
		findViewById(R.id.headerBtnPlanning).setOnClickListener(this);
		findViewById(R.id.headerBtnExamen).setOnClickListener(this);
		findViewById(R.id.headerBtnEntrainment).setOnClickListener(this);
		findViewById(R.id.headerBtnResultats).setOnClickListener(this);
		findViewById(R.id.headerBtnTelecharg).setOnClickListener(this);

		int PAGE_ID = getIntent().getIntExtra("PAGE_ID", 0);
		Fragment fr = null;
		switch (PAGE_ID) {
		case 0:
			fr = new MyProfileFragment();
			break;
		case 1:
			fr = new PlanningFragment();
			break;
		case 2:
			fr = new ExamineFragment();
			break;
		case 3:
			fr = new NotesFragment();
			break;
		case 4:
			fr = new ResultatFragment();
			break;
		case 5:
			fr = new TelechargementFragment();
			break;

		default:
			break;
		}

		if (fr != null) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragmentContainer, fr);
			fragmentTransaction.commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.headerBtnProfil:
			Utilities.selectFrag(this, Constants.PROFILE_FRAGMENT);
			break;
		case R.id.headerBtnPlanning:
			Utilities.selectFrag(this, Constants.PLANNING_FRAGMENT);
			break;
		case R.id.headerBtnExamen:
			Utilities.selectFrag(this, Constants.EXAMINE_FRAGMENT);
			break;
		case R.id.headerBtnEntrainment:
			Utilities.selectFrag(this, Constants.ENTRAINMENT_FRAGMENT);
			break;
		case R.id.headerBtnResultats:
			Utilities.selectFrag(this, Constants.RESULTATS_FRAGMENT);
			break;
		case R.id.headerBtnTelecharg:
			Utilities.selectFrag(this, Constants.TELECHARG_FRAGMENT);
			break;

		default:
			break;
		}

	}

}
