package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.ExamExercisesActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.ExamsAdapter;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class ExamineFragment extends Fragment {

	LayoutInflater inflater;
	ListView listview;
	ExamsAdapter adapter;
	ArrayList<Exam> exams;
	ArrayList<Exam> dbExams;
	ArrayList<Exam> validExams;
	ProgressBar progressBarExamin;
	AQuery aq;
	DatabaseHelper db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_examine, container,
				false);

		aq = new AQuery(getActivity());
		db = new DatabaseHelper(getActivity());
		dbExams = db.getAllExams();
		validExams = new ArrayList<Exam>();

		progressBarExamin = (ProgressBar) fragment
				.findViewById(R.id.progressBarExamin);
		listview = (ListView) fragment.findViewById(R.id.listViewExamine);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String password = validExams.get(position).getPassword();
				if (password != null) {
					if (password.isEmpty()) {
						Intent intent = new Intent(getActivity(),
								ExamExercisesActivity.class);
						intent.putExtra("exam_id", validExams.get(position)
								.getId());
						intent.putExtra("event_id", validExams.get(position)
								.getEventId());
						startActivity(intent);
					} else {
						showPasswordAlert(validExams.get(position).getId(),
								validExams.get(position).getEventId());
					}

				} else {
					Utilities.showAlertDialog(
							getActivity(),
							"Attention",
							getResources().getString(
									R.string.exam_not_downloaded_alert));
				}
			}

		});

		if (Utilities.isNetworkAvailable(getActivity())) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
			params.put("device_id", Utilities.getDeviceId(getActivity()));

			aq.ajax(Constants.EXAM_LIST_URL, params, JSONObject.class,
					new AjaxCallback<JSONObject>() {
						@Override
						public void callback(String url, JSONObject json,
								AjaxStatus status) {
							try {
								if (json.has("data")
										&& json.get("data") != null) {
									exams = JSONParser.parseExams(json);

									for (int i = 0; i < dbExams.size(); i++) {
										if (dbExams.get(i).getStartDate() >= System
												.currentTimeMillis() / 1000)
											validExams.add(dbExams.get(i));
									}

									for (int j = 0; j < exams.size(); j++) {
										if (exams.get(j).getStartDate() >= System
												.currentTimeMillis() / 1000)
											validExams.add(exams.get(j));
									}

									if (adapter == null) {
										adapter = new ExamsAdapter(
												getActivity(), validExams, null);
									} else {
										adapter.notifyDataSetChanged();
									}
									listview.setAdapter(adapter);
									progressBarExamin.setVisibility(View.GONE);
									listview.setVisibility(View.VISIBLE);
								}

							} catch (JSONException e) {
								e.printStackTrace();

							}
						}
					});
		} else {
			validExams.clear();
			for (int i = 0; i < dbExams.size(); i++) {
				if (dbExams.get(i).getStartDate() >= System.currentTimeMillis() / 1000)
					validExams.add(dbExams.get(i));
			}

			if (adapter == null) {
				adapter = new ExamsAdapter(getActivity(), validExams, null);
			} else {
				adapter.notifyDataSetChanged();
			}
			listview.setAdapter(adapter);
			progressBarExamin.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}

		db.closeDB();

		return fragment;
	}

	private void showPasswordAlert(final int id, final int eventId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Enter Password");

		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!input.getText().toString().trim().isEmpty()) {
					Intent intent = new Intent(getActivity(),
							ExamExercisesActivity.class);
					intent.putExtra("exam_id", id);
					intent.putExtra("event_id", eventId);
					startActivity(intent);
				}
			}
		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}
}
