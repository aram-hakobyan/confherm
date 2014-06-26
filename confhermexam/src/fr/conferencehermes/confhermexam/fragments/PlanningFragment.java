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
import fr.conferencehermes.confhermexam.parser.TimeSlot;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class PlanningFragment extends Fragment {
	private LayoutInflater inflater;
	private ArrayList<TimeSlot> timeSlotsArray;
	private long currentTime;
	private long currentTimeAddWeek;

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

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		currentTime = cal.getTimeInMillis();
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		currentTimeAddWeek = cal.getTimeInMillis();

		AQuery aq = new AQuery(getActivity());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put(Constants.KEY_DEVICE_ID,
				Utilities.getDeviceId(getActivity()));
		params.put(Constants.KEY_FROM, currentTime / 1000);
		params.put(Constants.KEY_TO, currentTimeAddWeek / 1000);

		aq.ajax(Constants.PLANNING_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json != null) {
							try {
								if (json.has("data")
										&& json.get("data") != null) {
									timeSlotsArray = JSONParser
											.parsePlannig(json);
									if (!timeSlotsArray.isEmpty())
										drawTimeSlot(timeSlotsArray.get(0));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (NullPointerException e1) {
								e1.printStackTrace();
							}
						}
					}
				});

		return fragment;
	}

	private void drawTimeSlot(TimeSlot timeSlot) {

	}

}
