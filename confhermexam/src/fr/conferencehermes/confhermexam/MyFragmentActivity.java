package fr.conferencehermes.confhermexam;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.fragments.DownloadsFragment;
import fr.conferencehermes.confhermexam.fragments.ExamineFragment;
import fr.conferencehermes.confhermexam.fragments.MyProfileFragment;
import fr.conferencehermes.confhermexam.fragments.PlanningFragment;
import fr.conferencehermes.confhermexam.fragments.ResultatFragment;
import fr.conferencehermes.confhermexam.fragments.TrainingsFragment;
import fr.conferencehermes.confhermexam.parser.ExerciseAnswer;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.ExamJsonTransmitter;
import fr.conferencehermes.confhermexam.util.Utilities;

public class MyFragmentActivity extends FragmentActivity implements OnClickListener {

  private DatabaseHelper db;

  private ImageView profile, planning, examen, training, resultat, download;
  int dSelector = R.drawable.top_buttons_selector;
  int cWhite = Color.parseColor("#ffffff");

  @SuppressWarnings("deprecation")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_my_fragment);

    profile = (ImageView) findViewById(R.id.headerBtnProfil);
    planning = (ImageView) findViewById(R.id.headerBtnPlanning);
    examen = (ImageView) findViewById(R.id.headerBtnExamen);
    training = (ImageView) findViewById(R.id.headerBtnEntrainment);
    resultat = (ImageView) findViewById(R.id.headerBtnResultats);
    download = (ImageView) findViewById(R.id.headerBtnTelecharg);

    profile.setOnClickListener(this);
    planning.setOnClickListener(this);
    examen.setOnClickListener(this);
    training.setOnClickListener(this);
    resultat.setOnClickListener(this);
    download.setOnClickListener(this);

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
        fr = new TrainingsFragment();

        break;
      case 4:
        fr = new ResultatFragment();

        break;
      case 5:
        fr = new DownloadsFragment();

        break;

      default:
        break;
    }

    profile.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        profile.setBackgroundDrawable(getResources().getDrawable(dSelector));
        planning.setBackgroundColor(cWhite);
        examen.setBackgroundColor(cWhite);
        download.setBackgroundColor(cWhite);
        resultat.setBackgroundColor(cWhite);
        training.setBackgroundColor(cWhite);

        return false;
      }
    });


    planning.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        planning.setBackgroundDrawable(getResources().getDrawable(dSelector));
        profile.setBackgroundColor(cWhite);
        examen.setBackgroundColor(cWhite);
        download.setBackgroundColor(cWhite);
        resultat.setBackgroundColor(cWhite);
        training.setBackgroundColor(cWhite);

        return false;
      }
    });

    examen.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        examen.setBackgroundDrawable(getResources().getDrawable(dSelector));
        profile.setBackgroundColor(cWhite);
        planning.setBackgroundColor(cWhite);
        download.setBackgroundColor(cWhite);
        resultat.setBackgroundColor(cWhite);
        training.setBackgroundColor(cWhite);

        return false;
      }
    });

    download.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        download.setBackgroundDrawable(getResources().getDrawable(dSelector));
        profile.setBackgroundColor(cWhite);
        planning.setBackgroundColor(cWhite);
        examen.setBackgroundColor(cWhite);
        resultat.setBackgroundColor(cWhite);
        training.setBackgroundColor(cWhite);

        return false;
      }
    });

    resultat.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        resultat.setBackgroundDrawable(getResources().getDrawable(dSelector));
        profile.setBackgroundColor(cWhite);
        planning.setBackgroundColor(cWhite);
        examen.setBackgroundColor(cWhite);
        download.setBackgroundColor(cWhite);
        training.setBackgroundColor(cWhite);

        return false;
      }
    });

    training.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {

        training.setBackgroundDrawable(getResources().getDrawable(dSelector));
        profile.setBackgroundColor(cWhite);
        planning.setBackgroundColor(cWhite);
        examen.setBackgroundColor(cWhite);
        download.setBackgroundColor(cWhite);
        resultat.setBackgroundColor(cWhite);

        return false;
      }
    });



    if (fr != null) {
      FragmentManager fm = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fm.beginTransaction();
      fragmentTransaction.replace(R.id.fragmentContainer, fr);
      fragmentTransaction.commit();
    }

    db = new DatabaseHelper(MyFragmentActivity.this);

  }

  @Override
  protected void onResume() {
    if (Utilities.isNetworkAvailable(MyFragmentActivity.this)) {
      ArrayList<ExerciseAnswer> exerciseAnswers = db.getAllExerciseAnswers();
      if (!exerciseAnswers.isEmpty()) {
        try {

          for (int i = 0; i < exerciseAnswers.size(); i++) {
            String jsonstring = exerciseAnswers.get(i).getJsonString();
            JSONObject object = new JSONObject(jsonstring);
            ExamJsonTransmitter transmitter = new ExamJsonTransmitter(MyFragmentActivity.this);
            transmitter.execute(object);

            db.deleteExerciseAnswer(exerciseAnswers.get(i).getExerciseId());

          }
        } catch (JSONException e) {
          e.printStackTrace();
        } finally {
          db.close();
        }

      }
    }

    super.onResume();
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

  @Override
  public void onBackPressed() {
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
