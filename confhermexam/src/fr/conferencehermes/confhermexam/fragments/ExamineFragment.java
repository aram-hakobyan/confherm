package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.ExamsAdapter;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class ExamineFragment extends Fragment {

	LayoutInflater inflater;
	ListView listview;
	ExamsAdapter adapter;
	ArrayList<Exam> exams;
	ProgressBar progressBarExamin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_examine, container,
				false);

		progressBarExamin = (ProgressBar) fragment
				.findViewById(R.id.progressBarExamin);

		listview = (ListView) fragment.findViewById(R.id.listViewExamine);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}

		});

		AQuery aq = new AQuery(getActivity());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("device_id", Utilities.getDeviceId(getActivity()));

		aq.ajax(Constants.EXAM_LIST_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						try {
							if (json.has("data") && json.get("data") != null) {
								exams = JSONParser.parseExams(json);
								if (adapter == null) {
									adapter = new ExamsAdapter(getActivity(),
											exams);
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

		return fragment;
	}
}
