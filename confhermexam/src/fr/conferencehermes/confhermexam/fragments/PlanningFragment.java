package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.TimeSlot;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class PlanningFragment extends Fragment implements OnClickListener {
	private ViewGroup layoutContainer;
	private ArrayList<TimeSlot> timeSlotsArray;
	private long currentTime;
	private long currentTimeAddWeek;
	private ScrollView fragment;
	private TextView MON, TUE, WED, THU, FRI, SAT, SUN;
	private AQuery aq;
	private RelativeLayout calendarContainer;
	private static final long WEEK = 7 * 24 * 3600;
	private static final long DAY_MILLS = 24 * 3600 * 1000;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		fragment = (ScrollView) inflater.inflate(
				R.layout.activity_planning_new, container, false);

		layoutContainer = (ViewGroup) fragment.findViewById(R.id.calendar);
		calendarContainer = (RelativeLayout) fragment
				.findViewById(R.id.calendarContainer);
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

		MON = (TextView) fragment.findViewById(R.id.textView1);
		TUE = (TextView) fragment.findViewById(R.id.textView2);
		WED = (TextView) fragment.findViewById(R.id.textView3);
		THU = (TextView) fragment.findViewById(R.id.textView4);
		FRI = (TextView) fragment.findViewById(R.id.textView5);
		SAT = (TextView) fragment.findViewById(R.id.textView6);
		SUN = (TextView) fragment.findViewById(R.id.textView7);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		currentTime = cal.getTimeInMillis() / 1000;
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		currentTimeAddWeek = cal.getTimeInMillis() / 1000;

		fragment.findViewById(R.id.buttonPrevious).setOnClickListener(this);
		fragment.findViewById(R.id.buttonNext).setOnClickListener(this);
		fragment.findViewById(R.id.textPrevious).setOnClickListener(this);
		fragment.findViewById(R.id.textNext).setOnClickListener(this);
		aq = new AQuery(getActivity());
		getCalendarData();

		return fragment;
	}

	private void getCalendarData() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put(Constants.KEY_DEVICE_ID,
				Utilities.getDeviceId(getActivity()));
		params.put(Constants.KEY_FROM, currentTime);
		params.put(Constants.KEY_TO, currentTimeAddWeek);

		setCalendarHeader(currentTime * 1000);

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
									Calendar cal = Calendar.getInstance();
									if (!timeSlotsArray.isEmpty()) {
										for (int i = 0; i < timeSlotsArray
												.size(); i++) {
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
	}

	private void setCalendarHeader(long mills) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mills);
		MON.setText("LUN "
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
		calendar.setTimeInMillis(mills + DAY_MILLS);
		TUE.setText("MAR "
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
		calendar.setTimeInMillis(mills + 2 * DAY_MILLS);
		WED.setText("MER "
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
		calendar.setTimeInMillis(mills + 3 * DAY_MILLS);
		THU.setText("JEU "
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
		calendar.setTimeInMillis(mills + 4 * DAY_MILLS);
		FRI.setText("VEN "
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
		calendar.setTimeInMillis(mills + 5 * DAY_MILLS);
		SAT.setText("SAM "
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
		calendar.setTimeInMillis(mills + 6 * DAY_MILLS);
		SUN.setText("DIM "
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
	}

	private void drawTimeSlot(final TimeSlot timeSlot, int day) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeSlot.getStart_date() * 1000);
		int STARTING_HOUR = calendar.get(Calendar.HOUR_OF_DAY);
		if (STARTING_HOUR < 6 || STARTING_HOUR > 22)
			return;
		calendar.setTimeInMillis(timeSlot.getEnd_date() * 1000);
		int ENDING_HOUR = calendar.get(Calendar.HOUR_OF_DAY);

		int CALENDAR_HEIGHT_PX = layoutContainer.getHeight()
				- Utilities.convertDpToPixel(30, getActivity());
		int LEFT_MARGIN = Utilities.convertDpToPixel(100, getActivity());
		int TOP_MARGIN = (STARTING_HOUR - 6) * CALENDAR_HEIGHT_PX / 16;
		int ENDING_MARGIN = (ENDING_HOUR - 6) * CALENDAR_HEIGHT_PX / 16;
		int TEXTVIEW_HEIGHT = ENDING_MARGIN - TOP_MARGIN;

		TextView timeSlotText = new TextView(getActivity());
		timeSlotText.setBackgroundColor(getResources().getColor(
				R.color.app_main_color));
		timeSlotText.setTextColor(Color.WHITE);
		timeSlotText.setGravity(Gravity.CENTER);

		timeSlotText.setText(DateFormat.format("hh:mm",
				new Date(timeSlot.getStart_date() * 1000)).toString()
				+ " - "
				+ DateFormat.format("hh:mm",
						new Date(timeSlot.getEnd_date() * 1000)).toString()
				+ "\n" + timeSlot.getTest_name());

		RelativeLayout calendarMenu = (RelativeLayout) fragment
				.findViewById(R.id.calendarMenu);
		LinearLayout calendarHeader = (LinearLayout) fragment
				.findViewById(R.id.calendarHeader);

		int LAYOUT_TOP_MARGIN = calendarMenu.getHeight()
				+ calendarHeader.getHeight()
				+ Utilities.convertDpToPixel(50, getActivity());
		TextView txt1 = (TextView) fragment.findViewById(R.id.textView1);
		int TEXTVIEW_WIDTH = txt1.getWidth();

		int LINE_WIDTH = Utilities.convertDpToPixel(1, getActivity());
		switch (day) {
		case Calendar.MONDAY:

			break;
		case Calendar.TUESDAY:
			LEFT_MARGIN += TEXTVIEW_WIDTH + LINE_WIDTH;
			break;
		case Calendar.WEDNESDAY:
			LEFT_MARGIN += (2 * TEXTVIEW_WIDTH + 2 * LINE_WIDTH);
			break;
		case Calendar.THURSDAY:
			LEFT_MARGIN += (3 * TEXTVIEW_WIDTH + 4 * LINE_WIDTH);
			break;
		case Calendar.FRIDAY:
			LEFT_MARGIN += (4 * TEXTVIEW_WIDTH + 6 * LINE_WIDTH);
			break;
		case Calendar.SATURDAY:
			LEFT_MARGIN += (5 * TEXTVIEW_WIDTH + 8 * LINE_WIDTH);
			break;
		case Calendar.SUNDAY:
			LEFT_MARGIN += (6 * TEXTVIEW_WIDTH + 10 * LINE_WIDTH);
			break;
		}

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				TEXTVIEW_WIDTH, TEXTVIEW_HEIGHT);
		params.leftMargin = LEFT_MARGIN;
		params.topMargin = TOP_MARGIN + LAYOUT_TOP_MARGIN;

		timeSlotText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialog(timeSlot);
			}
		});

		calendarContainer.addView(timeSlotText, params);

	}

	public void openDialog(TimeSlot ts) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.planning_dialog);

		TextView title = (TextView) dialog.findViewById(R.id.title);
		TextView date = (TextView) dialog.findViewById(R.id.date);
		TextView room = (TextView) dialog.findViewById(R.id.room);
		TextView adress = (TextView) dialog.findViewById(R.id.adress);
		TextView city = (TextView) dialog.findViewById(R.id.city);
		TextView status = (TextView) dialog.findViewById(R.id.status);
		TextView hour = (TextView) dialog.findViewById(R.id.hour);
		Button download = (Button) dialog.findViewById(R.id.downloadBtn);
		Button close = (Button) dialog.findViewById(R.id.buttonClose);

		title.setText(ts.getTest_name());
		date.setText(new Date(ts.getStart_date() * 1000).toString());
		room.setText(ts.getRoom());
		adress.setText(ts.getPlace());
		city.setText(ts.getAcademy());

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ts.getStart_date() * 1000);
		String DAY_TEXT = "";
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			DAY_TEXT = "LUN ";
			break;
		case Calendar.TUESDAY:
			DAY_TEXT = "MAR ";
			break;
		case Calendar.WEDNESDAY:
			DAY_TEXT = "MER ";
			break;
		case Calendar.THURSDAY:
			DAY_TEXT = "JEU";
			break;
		case Calendar.FRIDAY:
			DAY_TEXT = "VEN";
			break;
		case Calendar.SATURDAY:
			DAY_TEXT = "SAM";
			break;
		case Calendar.SUNDAY:
			DAY_TEXT = "DIM";
			break;
		}
		date.setText(DAY_TEXT
				+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
				+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
				+ String.valueOf(calendar.get(Calendar.YEAR)));
		hour.setText("De "
				+ DateFormat.format("hh:mm",
						new Date(ts.getStart_date() * 1000)).toString()
				+ " a "
				+ DateFormat.format("hh:mm", new Date(ts.getEnd_date() * 1000))
						.toString());

		switch (ts.getStatus()) {
		case 1:
			status.setText("Telechargement: Disponible");
			status.setCompoundDrawables(
					getResources().getDrawable(R.drawable.exam_checked), null,
					null, null);
			download.setEnabled(true);
			break;
		case 2:
			status.setText("Telechargement: Need update");
			download.setEnabled(true);
			status.setCompoundDrawables(
					getResources().getDrawable(R.drawable.exam_refresh), null,
					null, null);
			break;
		case 3:
			status.setText("Telechargement: Not downloaded yet");
			download.setEnabled(true);
			status.setCompoundDrawables(
					getResources().getDrawable(R.drawable.download_icon_white),
					null, null, null);
			break;
		case 4:
			status.setText("Telechargement: Non disponible");
			download.setEnabled(false);
			status.setCompoundDrawables(
					getResources().getDrawable(R.drawable.exam_x), null, null,
					null);
			break;

		}

		download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		if (!dialog.isShowing()) {
			dialog.show();
			dialog.getWindow().setLayout(
					getResources().getDimensionPixelSize(
							R.dimen.planning_dialog_width),
					getResources().getDimensionPixelSize(
							R.dimen.planning_dialog_height));
		}

	}

	private void changeWeek(boolean IS_NEXT) {
		calendarContainer.removeViews(3, calendarContainer.getChildCount() - 3);
		if (IS_NEXT) {
			currentTime += WEEK;
			currentTimeAddWeek += WEEK;
		} else {
			currentTime -= WEEK;
			currentTimeAddWeek -= WEEK;
		}
		getCalendarData();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonPrevious:
			changeWeek(false);
			break;
		case R.id.buttonNext:
			changeWeek(true);
			break;
		case R.id.textPrevious:
			changeWeek(false);
			break;
		case R.id.textNext:
			changeWeek(true);
			break;

		default:
			break;
		}

	}
}
