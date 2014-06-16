package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.ExercisesActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.TrainingsAdapter;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Training;
import fr.conferencehermes.confhermexam.util.Constants;

public class ExamineFragment extends Fragment {
	LayoutInflater inflater;
	ListView listview;
	TrainingsAdapter adapter;
	ArrayList<Training> trainings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_examine, container,
				false);

		listview = (ListView) fragment.findViewById(R.id.listViewExamine);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						ExercisesActivity.class);
				intent.putExtra("training_id", trainings.get(position).getId());
				intent.putExtra("exam", true);
				startActivity(intent);
			}

		});

		AQuery aq = new AQuery(getActivity());
		String url = "http://ecni.conference-hermes.fr/api/traininglist";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.AUTH_TOKEN, JSONParser.AUTH_KEY);

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				try {
					if (json.has("data") && json.get("data") != null) {
						trainings = JSONParser.parseTrainings(json);
						if (adapter == null) {
							adapter = new TrainingsAdapter(getActivity(),
									trainings);
						} else {
							adapter.notifyDataSetChanged();
						}
						listview.setAdapter(adapter);
					}

				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		});

		return fragment;
	}
}
