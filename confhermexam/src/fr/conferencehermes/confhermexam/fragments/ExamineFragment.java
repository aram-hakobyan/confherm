package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

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

				Exam clickedExam = validExams.get(position);
				for (int j = 0; j < dbExams.size(); j++) {
					if (clickedExam.getId() == dbExams.get(j).getId()) {
						clickedExam.setPassword(dbExams.get(j).getPassword());
						clickedExam.setIsAlreadyPassed(dbExams.get(j)
								.getIsAlreadyPassed());
						break;
					}
				}

				String password = clickedExam.getPassword();
				if (password != null) { // exam is downloaded
					if (clickedExam.getStatus() == 1) {
						if (canStartExam(clickedExam)) {
							if (clickedExam.getIsAlreadyPassed() >= 0) {
								if (password.isEmpty()) {

									Intent intent = new Intent(getActivity(),
											ExamExercisesActivity.class);
									intent.putExtra("exam_id",
											clickedExam.getId());
									intent.putExtra("event_id",
											clickedExam.getEventId());
									startActivity(intent);
									getActivity().finish();
								} else {
									showPasswordAlert(clickedExam.getId(),
											clickedExam.getEventId(), password);
								}
							} else {
								Utilities
										.showAlertDialog(
												getActivity(),
												"Attention",
												getResources()
														.getString(
																R.string.exam_already_passed_alert));
							}
						} else {
							Utilities.showAlertDialog(getActivity(),
									"Attention", "You can't start exam now.");
						}
					} else if (clickedExam.getStatus() == 2) {
						Utilities.showAlertDialog(getActivity(), "Attention",
								getResources().getString(R.string.need_update));
					}

				} else {
					if (clickedExam.getStatus() == 3)
						Utilities.showAlertDialog(
								getActivity(),
								"Attention",
								getResources().getString(
										R.string.exam_not_downloaded_alert));
					else if (clickedExam.getStatus() == 4)
						return;
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

									Calendar calendar = new GregorianCalendar(
											TimeZone.getTimeZone("Europe/Paris"));
									long currentTime = calendar
											.getTimeInMillis() / 1000;

									for (int j = 0; j < exams.size(); j++) {
										if (exams.get(j).getEndDate() >= currentTime)
											validExams.add(exams.get(j));
									}

									setupAdapterData();
								}

							} catch (JSONException e) {
								e.printStackTrace();

							}
						}
					});
		} else {
			Calendar calendar = new GregorianCalendar(
					TimeZone.getTimeZone("Europe/Paris"));
			long currentTime = calendar.getTimeInMillis() / 1000;
			validExams.clear();
			for (int i = 0; i < dbExams.size(); i++) {
				if (dbExams.get(i).getEndDate() >= currentTime)
					validExams.add(dbExams.get(i));
			}

			setupAdapterData();
		}

		return fragment;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public boolean canStartExam(Exam e) {
		Calendar calendar = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Paris"));
		long currentTime = calendar.getTimeInMillis() / 1000;
		return e.getStartDate() <= currentTime && e.getEndDate() >= currentTime;

	}

	public void setupAdapterData() {
		for (int i = 0; i < validExams.size(); i++) {
			Exam exam = validExams.get(i);
			switch (exam.getStatus()) {
			case 3:
				if (examIsDownloaded(exam.getId())) {
					Exam downloadedExam = db.getExam(exam.getId());
					if (downloadedExam.getLastEditTime() < exam
							.getLastEditTime()) {
						validExams.get(i).setStatus(2); // need update
					} else {
						validExams.get(i).setStatus(1); // status OK
					}

				} else {
					// status is 3 (not downloaded yet)
				}

				break;
			case 4:
				validExams.get(i).setStatus(4); // not available
				break;

			default:
				break;
			}

		}

		if (adapter == null) {
			adapter = new ExamsAdapter(getActivity(), validExams, null);
		} else {
			adapter.notifyDataSetChanged();
		}
		listview.setAdapter(adapter);
		progressBarExamin.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);

		db.closeDB();
	}

	public boolean examIsDownloaded(int examId) {
		for (int i = 0; i < dbExams.size(); i++) {
			if (dbExams.get(i).getId() == examId)
				return true;
		}

		return false;
	}

	private void showPasswordAlert(final int id, final int eventId,
			final String password) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Enter Password");

		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!input.getText().toString().trim().isEmpty()
						&& input.getText().toString().trim()
								.equalsIgnoreCase(password)) {
					Intent intent = new Intent(getActivity(),
							ExamExercisesActivity.class);
					intent.putExtra("exam_id", id);
					intent.putExtra("event_id", eventId);
					startActivity(intent);
					getActivity().finish();
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
