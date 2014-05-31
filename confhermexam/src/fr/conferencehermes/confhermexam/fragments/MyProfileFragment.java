package fr.conferencehermes.confhermexam.fragments;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.Constants;

public class MyProfileFragment extends Fragment {

	private Profile pData;
	private TextView pFirsName, pLastName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View pFragment = inflater.inflate(R.layout.activity_my_profile,
				container, false);
		pFirsName = (TextView) pFragment.findViewById(R.id.pFirsName);
		pLastName = (TextView) pFragment.findViewById(R.id.pLastName);

		AQuery aq = new AQuery(getActivity());
		String url = "http://ecni.conference-hermes.fr/api/profile.php";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.AUTH_TOKEN, JSONParser.AUTH_KEY);

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				try {
					if (json.has("data") && json.get("data") != null) {
						pData = JSONParser.parseProfileData(json);
						// Profile uProf = new Profile();
						pFirsName.setText(pData.getFirstName());
						pLastName.setText(pData.getLastName());
					}
				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		});

		//

		return pFragment;
	}
}
