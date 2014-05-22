package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import fr.conferencehermes.confhermexam.QuestionResponseActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.ExamsAdapter;

public class ExamineFragment extends Fragment {
	LayoutInflater inflater;
	ListView listview;
	ExamsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_examine, container,
				false);

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < 21; i++) {
			list.add("Examen Nom_ " + i);
		}
		listview = (ListView) fragment.findViewById(R.id.listViewExamine);
		adapter = new ExamsAdapter(getActivity(), list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						QuestionResponseActivity.class);
				startActivity(intent);
			}

		});

		return fragment;
	}
}
