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
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.DownloadsAdapter;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.parser.DownloadInstance;
import fr.conferencehermes.confhermexam.parser.Event;
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
	private DatabaseHelper db;
	private ArrayList<Event> dbEvents;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_telechargement,
				container, false);
		aq = new AQuery(getActivity());
		db = new DatabaseHelper(getActivity());
		try {
			dbEvents = db.getAllEvents();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		progressBarTelecharge = (ProgressBar) fragment
				.findViewById(R.id.progressBarTelecharge);

		listview = (ListView) fragment.findViewById(R.id.listViewDownloads);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("device_id", Utilities.getDeviceId(getActivity()));
		params.put("device_time", System.currentTimeMillis() / 1000);
		if (Utilities.isNetworkAvailable(getActivity())) {
			aq.ajax(Constants.DOWNLOADS_LIST_URL, params, JSONObject.class,
					new AjaxCallback<JSONObject>() {
						@Override
						public void callback(String url, JSONObject json,
								AjaxStatus status) {

							try {
								if (json.has("data")
										&& json.get("data") != null) {
									downloads = JSONParser.parseDownloads(json);
									setupAdapterData();
								}

							} catch (JSONException e) {
								e.printStackTrace();

							}
						}
					});
		} else {
			progressBarTelecharge.setVisibility(View.INVISIBLE);
			Toast.makeText(
					getActivity().getApplicationContext(),
					getActivity().getResources().getString(
							R.string.no_internet_connection),
					Toast.LENGTH_SHORT).show();

		}

		return fragment;
	}

	public void setupAdapterData() {
		DataHolder.getInstance().setDownloadPercents(new int[downloads.size()]);

		for (int i = 0; i < downloads.size(); i++) {
			DownloadInstance download = downloads.get(i);
			switch (download.getStatus()) {
			case 3:
				if (eventIsDownloaded(download.getEventId())) {
					Event event = db.getEvent(download.getEventId());
					if (event.getLastEditTime() < download.getLastEditTime()) {
						downloads.get(i).setStatus(2); // need update
					} else {
						downloads.get(i).setStatus(1); // status OK
					}

				} else {
					// status is 3 (not downloaded yet)
				}

				break;
			case 4:
				downloads.get(i).setStatus(4); // not available
				break;

			default:
				break;
			}

		}

		int event_id = -1;
		Bundle arguments = getArguments();
		if (arguments != null)
			event_id = arguments.getInt("event_id", -1);

		if (adapter == null) {
			adapter = new DownloadsAdapter(getActivity(), downloads, event_id);
		} else {
			adapter.notifyDataSetChanged();
		}
		listview.setAdapter(adapter);
		progressBarTelecharge.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);

		if (event_id != -1)
			selectCurrentEventDownload(event_id);

		db.closeDB();
	}

	public boolean eventIsDownloaded(int eventId) {
		for (int i = 0; i < dbEvents.size(); i++) {
			if (dbEvents.get(i).getId() == eventId)
				return true;
		}

		return false;
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
