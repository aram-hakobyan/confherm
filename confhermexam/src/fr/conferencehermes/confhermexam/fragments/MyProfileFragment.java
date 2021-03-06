package fr.conferencehermes.confhermexam.fragments;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.LoginActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.PreferenceStorage;
import fr.conferencehermes.confhermexam.util.Utilities;

public class MyProfileFragment extends Fragment {

	private static Profile pData;
	private TextView pFirsName, pLastName, pUserName, pEmailAdress, pGroups,
			pInformation;
	private Button logout;
	private SharedPreferences.Editor logoutEditor;
	private SharedPreferences logoutPrefs;
	private SharedPreferences.Editor profileEditor;
	private SharedPreferences profilePreferences;
	private SharedPreferences groupsShared;
	private SharedPreferences.Editor groupsEditor;
	private CheckBox hideProfile;

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
		pInformation = (TextView) pFragment.findViewById(R.id.pInformation);
		hideProfile = (CheckBox) pFragment.findViewById(R.id.hideProfile);
		hideProfile.setChecked(PreferenceStorage.getInstance(getActivity())
				.isNameHidden());
		hideProfile.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				hideProfileRequest(isChecked);
			}
		});

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
				startActivity(i);

				getActivity().finish();
			}
		});
		if (Utilities.isNetworkAvailable(getActivity())) {

			profileEditor = profilePreferences.edit();

			if (pData != null) {
				profileEditor.clear();
				profileEditor.commit();
				profileEditor.putString("profileFirstName",
						pData.getFirstName());
				profileEditor.putString("profileLastName", pData.getLastName());
				profileEditor.putString("profileUsername", pData.getUserName());
				profileEditor.putString("profileEmail", pData.getEmailAdress());
				profileEditor.putString("pInformation", pData.getInformation());
			}

			if (pData == null || pData.getGroups().size() == 0) {
				pGroups.setText(groups + "no groups available");
			} else {
				HashMap<String, String> groupsHashmap = pData.getGroups();
				groupsEditor = groupsShared.edit();
				if (groupsEditor != null) {
					groupsEditor.clear();
					groupsEditor.commit();
				}

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
		String information = profilePreferences.getString("pInformation", "");
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
		pInformation.setText(information);
		pFirsName.setText(name + "  " + firstName);
		pLastName.setText(lastname + "  " + lastName);
		pUserName.setText(login + "  " + userName);
		pEmailAdress.setText(emails + "  " + email);

		return pFragment;
	}

	private void hideProfileRequest(final boolean isChecked) {

		AQuery aq = new AQuery(getActivity());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put(Constants.KEY_DEVICE_ID,
				Utilities.getDeviceId(getActivity()));
		params.put(Constants.KEY_HIDE_PROFILE, isChecked);

		if (Utilities.isNetworkAvailable(getActivity())) {
			aq.ajax(Constants.HIDE_PROFILE_URL, params, JSONObject.class,
					new AjaxCallback<JSONObject>() {
						@Override
						public void callback(String url, JSONObject json,
								AjaxStatus status) {

							try {
								if (json.optInt("status") == 200) {
									PreferenceStorage
											.getInstance(getActivity())
											.setNameHidden(isChecked);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	public static void setProfileData(Profile profileData) {
		pData = profileData;
	}
}
