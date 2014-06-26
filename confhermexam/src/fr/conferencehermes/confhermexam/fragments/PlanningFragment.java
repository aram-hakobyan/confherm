package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
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
	private ViewGroup layoutContainer;
	private ArrayList<TimeSlot> timeSlotsArray;
	private long currentTime;
	private long currentTimeAddWeek;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_planning_new,
				container, false);

		layoutContainer = (ViewGroup) fragment.findViewById(R.id.calendar);
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
		cal.set(Calendar.HOUR_OF_DAY, 5);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		currentTime = cal.getTimeInMillis() / 1000;
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		currentTimeAddWeek = cal.getTimeInMillis() / 1000;

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
							try {
								if (json.has("data")
										&& json.get("data") != null) {
									timeSlotsArray = JSONParser
											.parsePlannig(json);
									if (!timeSlotsArray.isEmpty()) {
										for (int i = 0; i < timeSlotsArray
												.size(); i++) {

											Calendar cal = Calendar
													.getInstance();
											cal.setTimeInMillis(timeSlotsArray
													.get(i).getStart_date() * 1000);
											drawTimeSlot(
													timeSlotsArray.get(i),
													cal.get(Calendar.DAY_OF_WEEK));
										}
									}
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

	private void drawTimeSlot(final TimeSlot timeSlot, int day) {
		long startTime = timeSlot.getStart_date();
		long endTime = timeSlot.getEnd_date();
		int DAY_DURATION = 17 * 3600;

		int HEIGHT = layoutContainer.getHeight();
		int[] location = new int[2];
		layoutContainer.getLocationOnScreen(location);
		int LEFT_MARGIN = (int) Utilities.convertDpToPixel(100, getActivity());

		int time = (int) (startTime - currentTime);

		switch (day) {
		case Calendar.MONDAY:

			break;
		case Calendar.TUESDAY:
			time -= 24 * 3600;
			break;
		case Calendar.WEDNESDAY:
			time -= 2 * 24 * 3600;
			break;
		case Calendar.THURSDAY:
			time -= 3 * 24 * 3600;
			break;
		case Calendar.FRIDAY:
			time -= 4 * 24 * 3600;
			break;
		case Calendar.SATURDAY:
			time -= 5 * 24 * 3600;
			break;
		case Calendar.SUNDAY:
			time -= 6 * 24 * 3600;
			break;
		}

		int startY = (int) (HEIGHT * time / DAY_DURATION);

		TextView timeSlotText = new TextView(getActivity());
		timeSlotText.setBackgroundColor(getResources().getColor(
				R.color.app_main_color));
		/*
		 * timeSlotText.setHeight(20); timeSlotText.setWidth(100);
		 * timeSlotText.setX(LEFT_MARGIN); timeSlotText.setY(startY +
		 * location[1] + 20);
		 */
		timeSlotText.setText((new Date(timeSlot.getStart_date() * 1000)
				.toString()));

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,
				20);
		params.leftMargin = LEFT_MARGIN;
		params.topMargin = startY + location[1] + 20;

		timeSlotText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialog(timeSlot);
			}
		});

		layoutContainer.addView(timeSlotText, params);
		layoutContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialog(timeSlotsArray.get(0));
			}
		});

	}

	public void openDialog(TimeSlot ts) {
		Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.planning_dialog);

		TextView title = (TextView) dialog.findViewById(R.id.title);
		TextView date = (TextView) dialog.findViewById(R.id.date);
		TextView room = (TextView) dialog.findViewById(R.id.room);
		TextView adress = (TextView) dialog.findViewById(R.id.adress);
		TextView city = (TextView) dialog.findViewById(R.id.city);
		TextView status = (TextView) dialog.findViewById(R.id.status);
		Button download = (Button) dialog.findViewById(R.id.downloadBtn);

		title.setText(ts.getTest_name());
		date.setText(new Date(ts.getStart_date() * 1000).toString());
		room.setText(ts.getRoom());
		adress.setText(ts.getPlace());
		city.setText(ts.getAcademy());

		if (ts.getStatus() == 1) {
			status.setText("Telechargement: Disponible");
			download.setEnabled(true);
		} else {
			status.setText("Telechargement: Non disponible");
			download.setEnabled(false);
		}

		download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		if (!dialog.isShowing()) {
			dialog.show();
			dialog.getWindow().setLayout(650, 450);
		}

	}
}
