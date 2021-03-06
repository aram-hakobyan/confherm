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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.ExercisesActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.TrainingsAdapter;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Training;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class TrainingsFragment extends Fragment {
	LayoutInflater inflater;
	ListView listview;
	TrainingsAdapter adapter;
	ArrayList<Training> trainings;
	ProgressBar progressBarTrainings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_training, container,
				false);

		progressBarTrainings = (ProgressBar) fragment
				.findViewById(R.id.progressBarExamin);
		listview = (ListView) fragment.findViewById(R.id.listViewExamine);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						ExercisesActivity.class);
				intent.putExtra("training_id", trainings.get(position).getId());
				intent.putExtra("exam", false);
				startActivity(intent);
			}

		});
		if (Utilities.isNetworkAvailable(getActivity())) {
			AQuery aq = new AQuery(getActivity());

			Map<String, Object> params = new HashMap<String, Object>();
			params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);

			aq.ajax(Constants.TRAINING_LIST_URL, params, JSONObject.class,
					new AjaxCallback<JSONObject>() {
						@Override
						public void callback(String url, JSONObject json,
								AjaxStatus status) {

							try {
								if (json.has("data")
										&& json.get("data") != null) {
									trainings = JSONParser.parseTrainings(json);
									if (adapter == null) {
										adapter = new TrainingsAdapter(
												getActivity(), trainings);
									} else {
										adapter.notifyDataSetChanged();
									}
									listview.setAdapter(adapter);
									progressBarTrainings
											.setVisibility(View.GONE);
									listview.setVisibility(View.VISIBLE);
								}

							} catch (JSONException e) {
								e.printStackTrace();

							}
						}
					});
		} else {
			progressBarTrainings.setVisibility(View.INVISIBLE);
			Toast.makeText(
					getActivity().getApplicationContext(),
					getActivity().getResources().getString(
							R.string.no_internet_connection),
					Toast.LENGTH_SHORT).show();
		}
		return fragment;
	}

}
