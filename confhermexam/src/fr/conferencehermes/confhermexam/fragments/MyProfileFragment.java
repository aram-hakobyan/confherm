package fr.conferencehermes.confhermexam.fragments;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import android.widget.Toast;
import fr.conferencehermes.confhermexam.LoginActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.connection.NetworkReachability;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;

public class MyProfileFragment extends Fragment {

	private static Profile pData;
	private TextView pFirsName, pLastName, pUserName, pEmailAdress, pGroups;
	private Button logout;
	private SharedPreferences.Editor logoutEditor;
	private SharedPreferences logoutPrefs;
	private SharedPreferences.Editor profileEditor;
	private SharedPreferences profilePreferences;
	private SharedPreferences groupsShared;
	private SharedPreferences.Editor groupsEditor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View pFragment = inflater.inflate(R.layout.activity_my_profile,
				container, false);

		String groups = getActivity().getResources().getString(
				R.string.my_profile_groups);
		String name = getActivity().getResources().getString(
				R.string.my_profile_name);
		String lastname = getActivity().getResources().getString(
				R.string.my_profile_lastname);
		String login = getActivity().getResources().getString(
				R.string.my_profile_login);
		String emails = getActivity().getResources().getString(
				R.string.my_profile_email);

		pFirsName = (TextView) pFragment.findViewById(R.id.pFirsName);
		pLastName = (TextView) pFragment.findViewById(R.id.pLastName);
		pUserName = (TextView) pFragment.findViewById(R.id.pUsername);
		pEmailAdress = (TextView) pFragment.findViewById(R.id.pEmail);
		pGroups = (TextView) pFragment.findViewById(R.id.pGroups);
		profilePreferences = getActivity().getSharedPreferences(
				"fr.conferencehermes.confhermexam.fragments.MYPROFILE",
				Context.MODE_PRIVATE);

		groupsShared = getActivity().getSharedPreferences(
				"fr.conferencehermes.confhermexam.fragments.MYPROFILE_GROUPS",
				Context.MODE_PRIVATE);

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
		if (NetworkReachability.isReachable()) {

			profileEditor = profilePreferences.edit();

			if (pData != null) {
				profileEditor.putString("profileFirstName",
						pData.getFirstName());
				profileEditor.putString("profileLastName", pData.getLastName());
				profileEditor.putString("profileUsername", pData.getUserName());
				profileEditor.putString("profileEmail", pData.getEmailAdress());
			}

			if (pData != null && pData.getGroups().size() == 0) {
				pGroups.setText(groups + "no groups available");
			} else {
				HashMap<String, String> groupsHashmap = pData.getGroups();

				groupsEditor = groupsShared.edit();
				for (String s : groupsHashmap.keySet()) {
					groupsEditor.putString(s, groupsHashmap.get(s));
				}
				groupsEditor.commit();
			}
			profileEditor.commit();

		} else {

			/*
			 * Toast.makeText(getActivity().getApplicationContext(),
			 * "Check your internet connection", Toast.LENGTH_SHORT) .show();
			 */

		}
		String firstName = profilePreferences.getString("profileFirstName", "");
		String lastName = profilePreferences.getString("profileLastName", "");
		String userName = profilePreferences.getString("profileUsername", "");
		String email = profilePreferences.getString("profileEmail", "");
		Handler h = new Handler(getActivity().getMainLooper());
		@SuppressWarnings("unchecked")
		HashMap<String, String> groupsHashmap = (HashMap<String, String>) groupsShared
				.getAll();
		pGroups.setText(groups + " : ");
		for (String s : groupsHashmap.keySet()) {
			final String group = groupsHashmap.get(s);

			h.post(new Runnable() {

				@Override
				public void run() {
					pGroups.append("\n" + group.toString());
				}
			});

		}

		pFirsName.setText(name + " : " + firstName);
		pLastName.setText(lastname + " : " + lastName);
		pUserName.setText(login + " : " + userName);
		pEmailAdress.setText(emails + " : " + email);

		return pFragment;
	}

	public static void setProfileData(Profile profileData) {
		pData = profileData;
	}
}