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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.ResultsAdapter;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Result;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class ResultatFragment extends Fragment {
	private LayoutInflater inflater;
	private ListView listview;
	private ResultsAdapter adapter;
	// private ArrayList<Res> pData;
	ArrayList<Result> rList;
	ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_resultat, container,
				false);

		rList = new ArrayList<Result>();

		progressBar = (ProgressBar) fragment.findViewById(R.id.resultProgress);

		listview = (ListView) fragment.findViewById(R.id.listViewResultat);

		AQuery aq = new AQuery(getActivity());

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);

		if (Utilities.isNetworkAvailable(getActivity())) {
			aq.ajax(Constants.RESULT_LIST_URL, params, JSONObject.class,
					new AjaxCallback<JSONObject>() {
						@Override
						public void callback(String url, JSONObject json,
								AjaxStatus status) {

							try {
								if (json.has(Constants.KEY_STATUS)
										&& json.get(Constants.KEY_STATUS) != null) {
									if (json.getInt("status") == 200) {
										// pData =
										// JSONParser.parseProfileData(json);
										// Profile uProf = new Profile();
										rList = JSONParser.parseResults(json);

										if (rList.size() != 0) {
											adapter = new ResultsAdapter(
													getActivity(), rList);
											listview.setAdapter(adapter);
										} else {
											Toast.makeText(
													getActivity(),
													"Currently not available for any of event",
													Toast.LENGTH_LONG).show();
										}

										progressBar.setVisibility(View.GONE);
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (NullPointerException e) {
								e.printStackTrace();
								progressBar.setVisibility(View.GONE);
								Toast.makeText(
										getActivity(),
										"Currently not available for any of event",
										Toast.LENGTH_LONG).show();
							}
						}
					});
		} else {
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(
					getActivity().getApplicationContext(),
					getActivity().getResources().getString(
							R.string.no_internet_connection),
					Toast.LENGTH_SHORT).show();

		}
		return fragment;
	}

}
