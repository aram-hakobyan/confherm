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
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.DownloadsAdapter;
import fr.conferencehermes.confhermexam.parser.DownloadInstance;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class DownloadsFragment extends Fragment {
	LayoutInflater inflater;
	ListView listview;
	DownloadsAdapter adapter;
	ArrayList<DownloadInstance> downloads;
	AQuery aq;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_telechargement,
				container, false);
		aq = new AQuery(getActivity());
		listview = (ListView) fragment.findViewById(R.id.listViewDownloads);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("device_id", Utilities.getDeviceId(getActivity()));

		aq.ajax(Constants.DOWNLOADS_LIST_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						try {
							if (json.has("data") && json.get("data") != null) {
								downloads = JSONParser.parseDownloads(json);
								if (adapter == null) {
									adapter = new DownloadsAdapter(
											getActivity(), downloads);
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
