package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Planning;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class PlanningFragment extends Fragment {
	private LayoutInflater inflater;
	private ArrayList<Planning> planningList;
	private int currentTime;
	private int currentTimeAddWeek;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_planning_new,
				container, false);

		ViewGroup layoutContainer = (ViewGroup) fragment
				.findViewById(R.id.calendar);
		ArrayList<String> times = new ArrayList<String>();
		times.add("06:00");
		times.add("08:00");
		times.add("10:00");
		times.add("12:00");
		times.add("14:00");
		times.add("16:00");
		times.add("18:00");
		times.add("20:00");
		times.add("22:00");

		for (int i = 0; i < 9; i++) {
			View myLayout = inflater.inflate(R.layout.calendar_hour, null);
			TextView tv = (TextView) myLayout.findViewById(R.id.hour);
			tv.setText(times.get(i));
			layoutContainer.addView(myLayout);
		}

		try {
			Calendar c = Calendar.getInstance();
			currentTime = c.get(Calendar.MILLISECOND);
			currentTimeAddWeek = currentTime + (7 * 24 * 60 * 60 * 1000);
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		AQuery aq = new AQuery(getActivity());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put(Constants.KEY_DEVICE_ID,
				Utilities.getDeviceId(getActivity()));
		params.put(Constants.KEY_FROM, currentTime);
		params.put(Constants.KEY_TO, currentTimeAddWeek);

		aq.ajax(Constants.PLANNING_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						if (json != null) {
							System.out.println(json.toString());
							try {
								if (json.has("data")
										&& json.get("data") != null) {
									planningList = JSONParser
											.parsePlannig(json);
									// if (adapter == null) {
									// adapter = new ExamsAdapter(getActivity(),
									// exams);
									// } else {
									// adapter.notifyDataSetChanged();
									// }
									// listview.setAdapter(adapter);
								}

							} catch (JSONException e) {
								e.printStackTrace();

							}
						}
					}
				});

		return fragment;
	}

}
