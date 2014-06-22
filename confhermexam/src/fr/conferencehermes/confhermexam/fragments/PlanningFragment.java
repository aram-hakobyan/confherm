package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;

public class PlanningFragment extends Fragment {
	LayoutInflater inflater;

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

		return fragment;
	}

}
