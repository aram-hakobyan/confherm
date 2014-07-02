package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.DownloadsAdapter;
import fr.conferencehermes.confhermexam.parser.DownloadInstance;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.DataHolder;
import fr.conferencehermes.confhermexam.util.Utilities;

public class DownloadsFragment extends Fragment {
	LayoutInflater inflater;
	ListView listview;
	DownloadsAdapter adapter;
	ArrayList<DownloadInstance> downloads;
	AQuery aq;

	ProgressBar progressBarTelecharge;
	protected int id = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_telechargement,
				container, false);
		aq = new AQuery(getActivity());

		progressBarTelecharge = (ProgressBar) fragment
				.findViewById(R.id.progressBarTelecharge);

		listview = (ListView) fragment.findViewById(R.id.listViewDownloads);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("device_id", Utilities.getDeviceId(getActivity()));
		params.put("device_time", System.currentTimeMillis() / 1000);

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
								progressBarTelecharge.setVisibility(View.GONE);
								listview.setVisibility(View.VISIBLE);

								int timeslotId = -1;
								Bundle arguments = getArguments();
								if (arguments != null)
									timeslotId = arguments.getInt(
											"timeslot_id", -1);
								if (timeslotId != -1)
									selectCurrentEventDownload(timeslotId);
							}

						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				});

		return fragment;
	}

	private void selectCurrentEventDownload(final int eventId) {

		if (listview != null) {
			ArrayList<DownloadInstance> downloads = DataHolder.getInstance()
					.getDownloads();
			for (int i = 0; i < downloads.size(); i++) {
				DownloadInstance d = (DownloadInstance) downloads.get(i);
				if (eventId == d.getEventId()) {
					id = i;
					break;
				}

			}

			listview.post(new Runnable() {
				@Override
				public void run() {
					listview.setSelection(id);
				}
			});

			listview.postDelayed(new Runnable() {
				int wantedPosition, wantedChild;

				@Override
				public void run() {
					wantedPosition = id;
					int firstPosition = listview.getFirstVisiblePosition()
							- listview.getHeaderViewsCount();
					wantedChild = wantedPosition - firstPosition;
					View wantedView = listview.getChildAt(wantedChild);
					if (wantedView != null) {
						wantedView.requestFocus();
						wantedView.setBackgroundColor(getResources().getColor(
								R.color.app_main_color));
					}

					if (wantedChild < 0
							|| wantedChild >= listview.getChildCount()) {
						Log.w("TAG",
								"Unable to get view for desired position, because it's not being displayed on screen.");
						return;
					}
				}
			}, 400);

		}
	}

}
