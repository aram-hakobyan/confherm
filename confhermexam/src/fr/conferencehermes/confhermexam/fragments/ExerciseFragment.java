package fr.conferencehermes.confhermexam.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import fr.conferencehermes.confhermexam.R;

public class ExerciseFragment extends Fragment {
	LayoutInflater inflater;
	GridView gvMain;
	ArrayAdapter<String> adapter;
	String[] data = { "EXERCISE 1", "EXERCISE 2", "EXERCISE 3", "EXERCISE 4",
			"EXERCISE 5", "EXERCISE 6", "EXERCISE 7", "EXERCISE 8",
			"EXERCISE 9", "EXERCISE 10", "EXERCISE 11" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_exersice, container,
				false);

		adapter = new ArrayAdapter<String>(getActivity(), R.layout.item,
				R.id.tvText, data);
		gvMain = (GridView) fragment.findViewById(R.id.gvMain);
		gvMain.setAdapter(adapter);
		adjustGridView();

		gvMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view.setBackgroundColor(Color.parseColor("#0d5c7c"));
			}
		});

		return fragment;
	}

	private void adjustGridView() {
		gvMain.setNumColumns(GridView.AUTO_FIT);
		gvMain.setColumnWidth(180);
		gvMain.setVerticalSpacing(20);
		gvMain.setHorizontalSpacing(50);
		gvMain.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
	}

}
