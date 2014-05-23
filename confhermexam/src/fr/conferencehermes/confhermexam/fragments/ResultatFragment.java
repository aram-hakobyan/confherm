package fr.conferencehermes.confhermexam.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.adapters.ResultsAdapter;

public class ResultatFragment extends Fragment {
	LayoutInflater inflater;
	ListView listview;
	ResultsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_resultat, container,
				false);

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < 21; i++) {
			list.add("Examen Nom_ " + i);
		}
		listview = (ListView) fragment.findViewById(R.id.listViewResultat);
		adapter = new ResultsAdapter(getActivity(), list);
		listview.setAdapter(adapter);

		return fragment;
	}

}
