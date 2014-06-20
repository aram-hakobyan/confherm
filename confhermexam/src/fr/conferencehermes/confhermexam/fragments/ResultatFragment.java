package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.ResultsAdapter;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Result;
import fr.conferencehermes.confhermexam.util.Constants;

public class ResultatFragment extends Fragment {
	private LayoutInflater inflater;
	private ListView listview;
	private ResultsAdapter adapter;
	// private ArrayList<Res> pData;
	ArrayList<Result> rList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_resultat, container,
				false);

		rList = new ArrayList<Result>();

		listview = (ListView) fragment.findViewById(R.id.listViewResultat);

		listview.setAdapter(adapter);

		AQuery aq = new AQuery(getActivity());
		String url = "http://ecni.conference-hermes.fr/api/resultlist";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.AUTH_TOKEN, JSONParser.AUTH_KEY);

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				System.out.println(json.toString());
				try {
					if (json.has(Constants.KEY_STATUS)
							&& json.get(Constants.KEY_STATUS) != null) {
						if (json.getInt("status") == 200) {
							// pData = JSONParser.parseProfileData(json);
							// Profile uProf = new Profile();
							rList = JSONParser.parseResults(json);

							if (rList.size() != 0) {
								adapter = new ResultsAdapter(getActivity(),
										rList);
							} else {
								Toast.makeText(
										getActivity().getApplicationContext(),
										"No Any Result", Toast.LENGTH_SHORT)
										.show();

							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		});

		return fragment;
	}

}
