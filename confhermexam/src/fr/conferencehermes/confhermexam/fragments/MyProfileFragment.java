package fr.conferencehermes.confhermexam.fragments;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.LoginActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;

public class MyProfileFragment extends Fragment {

	private static Profile pData;
	private TextView pFirsName, pLastName, pUserName, pEmailAdress, pGroups;
	private Button logout;
	private SharedPreferences.Editor logoutEditor;
	private SharedPreferences logoutPrefs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View pFragment = inflater.inflate(R.layout.activity_my_profile,
				container, false);
		pFirsName = (TextView) pFragment.findViewById(R.id.pFirsName);
		pLastName = (TextView) pFragment.findViewById(R.id.pLastName);
		pUserName = (TextView) pFragment.findViewById(R.id.pUsername);
		pEmailAdress = (TextView) pFragment.findViewById(R.id.pEmail);
		pGroups = (TextView) pFragment.findViewById(R.id.pGroups);
		logout = (Button) pFragment.findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TASK);

				logoutEditor = getActivity().getSharedPreferences(
						"logoutPrefs", Context.MODE_PRIVATE).edit();
				logoutEditor.putBoolean(Constants.LOGOUT_SHAREDPREFS_KEY, true);
				logoutEditor.commit();

				logoutPrefs = getActivity().getSharedPreferences("logoutPrefs",
						Context.MODE_PRIVATE);
				boolean b = logoutPrefs.getBoolean(
						Constants.LOGOUT_SHAREDPREFS_KEY, false);
				Log.i("Utils", b + "");
				startActivity(i);

				getActivity().finish();
			}
		});

		pFirsName.setText(pData.getFirstName());
		pLastName.setText(pData.getLastName());
		pUserName.setText("Username : " + pData.getUserName());
		pEmailAdress.setText("Email : " + pData.getEmailAdress());

		if (pData.getGroups().size() == 0) {
			pGroups.setText("Groups : " + "no groups available");
		} else {
			Handler h = new Handler(getActivity().getMainLooper());

			pGroups.setText("Groups : ");
			for (Map.Entry<String, String> entry : pData.getGroups().entrySet()) {
				String gKey = entry.getKey();
				final String gValue = entry.getValue();

				h.post(new Runnable() {
					@Override
					public void run() {
						pGroups.append("\n" + gValue.toString());
					}
				});

			}
		}

		return pFragment;
	}

	public static void setProfileData(Profile profileData) {
		pData = profileData;
	}
}