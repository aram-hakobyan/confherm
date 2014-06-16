package fr.conferencehermes.confhermexam;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.adapters.QuestionsAdapter;
import fr.conferencehermes.confhermexam.parser.Exercise;
import fr.conferencehermes.confhermexam.parser.Question;
import fr.conferencehermes.confhermexam.util.DataHolder;

public class QuestionResponseActivity extends Activity implements
		OnClickListener {
	LayoutInflater inflater;
	ListView listview;
	QuestionsAdapter adapter;
	Exercise exercise;
	ArrayList<Question> questions;
	LinearLayout answersLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_question_response);
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		int id = getIntent().getIntExtra("id", 1);
		answersLayout = (LinearLayout) findViewById(R.id.answersLayout);
		listview = (ListView) findViewById(R.id.questionsListView);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectQuestion(questions.get(position));
			}

		});

		exercise = DataHolder.getInstance().getExercises().get(0);
		questions = exercise.getQuestions();
		if (!questions.isEmpty()) {
			selectQuestion(questions.get(0));
		}
		if (adapter == null) {
			adapter = new QuestionsAdapter(QuestionResponseActivity.this,
					exercise.getQuestions());
		} else {
			adapter.notifyDataSetChanged();
		}
		listview.setAdapter(adapter);
		TextView temps1 = (TextView) findViewById(R.id.temps1);
		TextView temps2 = (TextView) findViewById(R.id.temps2);
		temps1.setText(exercise.getTimeOpen());
		temps2.setText(exercise.getTimeClose());

	}

	private void selectQuestion(Question q) {
		answersLayout.removeAllViews();
		TextView title = (TextView) findViewById(R.id.questionTitle);
		TextView txt = (TextView) findViewById(R.id.question);
		TextView ennouncer = (TextView) findViewById(R.id.temps1);
		title.setText("QUESTION " + q.getId());
		txt.setText(Html.fromHtml(q.getQuestionText()));
		ennouncer.setText(q.getCreatedBy());

		int answersCount = q.getAnswers().size();

		// Single choice answer
		if (q.getMcType().equalsIgnoreCase("0")) {
			RadioGroup mRadioGroup = new RadioGroup(
					QuestionResponseActivity.this);
			mRadioGroup.setOrientation(RadioGroup.VERTICAL);
			for (int i = 0; i < answersCount; i++) {
				RadioButton newRadioButton = new RadioButton(this);
				newRadioButton.setText(q.getAnswers().get(i).getAnswer());
				newRadioButton.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
						RadioGroup.LayoutParams.WRAP_CONTENT,
						RadioGroup.LayoutParams.WRAP_CONTENT);
				mRadioGroup.addView(newRadioButton, 0, layoutParams);
			}
			answersLayout.addView(mRadioGroup);
		} else // Multichoice answer
		if (q.getMcType().equalsIgnoreCase("1")) {
			for (int i = 0; i < answersCount; i++) {
				LinearLayout checkBoxLayout = new LinearLayout(
						QuestionResponseActivity.this);
				checkBoxLayout.setOrientation(LinearLayout.HORIZONTAL);
				CheckBox checkBox = new CheckBox(QuestionResponseActivity.this);
				checkBox.setGravity(Gravity.CENTER_VERTICAL);
				TextView text = new TextView(QuestionResponseActivity.this);
				text.setText(q.getAnswers().get(i).getAnswer());
				text.setGravity(Gravity.CENTER_VERTICAL);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				// layoutParams.setMargins(50, 20, 30, 20);
				checkBoxLayout.addView(checkBox);
				checkBoxLayout.addView(text, layoutParams);
				answersLayout.addView(checkBoxLayout);
			}

		} else // Essay answer
		if (q.getMcType().equalsIgnoreCase("2")) {
			EditText editText = new EditText(QuestionResponseActivity.this);
			editText.setGravity(Gravity.CENTER_VERTICAL);
			editText.setHint("Type your answer here.");
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			answersLayout.addView(editText, layoutParams);
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
		case R.id.validerBtn:

			break;

		default:
			break;
		}

	}

}
