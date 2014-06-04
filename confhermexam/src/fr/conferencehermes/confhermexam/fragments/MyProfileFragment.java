package fr.conferencehermes.confhermexam.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.Profile;

public class MyProfileFragment extends Fragment {

	private static Profile pData;
	private TextView pFirsName, pLastName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View pFragment = inflater.inflate(R.layout.activity_my_profile,
				container, false);
		pFirsName = (TextView) pFragment.findViewById(R.id.pFirsName);
		pLastName = (TextView) pFragment.findViewById(R.id.pLastName);

	//	pData = new Profile();

		Log.d("MYPROFILE NAME ", pData.getFirstName() + "");
		Log.d("MYPROFILE SURENAME ", pData.getLastName() + "");
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				pFirsName.setText(pData.getFirstName());
				pLastName.setText(pData.getLastName());
			}
		});

		return pFragment;
	}

	public static void setProfileData(Profile profileData) {
		pData = profileData;
	}
}
