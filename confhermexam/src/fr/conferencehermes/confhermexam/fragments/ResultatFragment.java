package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.ResultsAdapter;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;

public class ResultatFragment extends Fragment {
	private LayoutInflater inflater;
	private ListView listview;
	private ResultsAdapter adapter;
//	private ArrayList<Res> pData;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_resultat, container,
				false);

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < 21; i++) {
			list.add("Examen Nom_ " + i);
		}
		listview = (ListView) fragment.findViewById(R.id.listViewResultat);
		adapter = new ResultsAdapter(getActivity(), list);
		listview.setAdapter(adapter);

		AQuery aq = new AQuery(getActivity());
		String url = "http://ecni.conference-hermes.fr/api/profile.php";
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
							//pData = JSONParser.parseProfileData(json);
							//Profile uProf = new Profile();
				
						
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
