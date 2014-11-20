package fr.conferencehermes.confhermexam.connection;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;

public class LogoutRequest {

	public static void logOut(Activity activity) {

		AQuery aq = new AQuery(activity);

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);

		aq.ajax(Constants.LOG_OUT_URL, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {
					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						System.out.println(json.toString());
						try {
							if (json.has(Constants.KEY_STATUS)
									&& json.get(Constants.KEY_STATUS) != null) {
								if (json.getInt("status") == 200) {
									// pData =
									// JSONParser.parseProfileData(json);
									// Profile uProf = new Profile();
									Log.i("logout", json + "");
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();

						}
					}
				});
	}

}
