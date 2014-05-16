package fr.conferencehermes.confhermexam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fr.conferencehermes.confhermexam.R;

public class NotesFragment extends Fragment {
	LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View fragment = inflater.inflate(R.layout.activity_notes, container,
				false);

		return fragment;
	}

}
