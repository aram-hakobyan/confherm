package fr.conferencehermes.confhermexam.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import fr.conferencehermes.confhermexam.ExaminationActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ExamJsonTransmitter extends
		AsyncTask<JSONObject, JSONObject, JSONObject> {
	private static final String url = "http://ecni.conference-hermes.fr/api/examanswer";
	private Context context;

	public ExamJsonTransmitter(Context c) {
		this.context = c;
	}

	private String convertStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return total.toString();
	}

	@Override
	protected JSONObject doInBackground(JSONObject... data) {
		JSONObject object = new JSONObject();
		JSONObject json = data[0];

		try {
			object.put("auth_key",
					Utilities.readString(context, "auth_key", ""));
			object.put("device_time", System.currentTimeMillis() / 1000);
			object.put("device_id", Utilities.getDeviceId(context));
			object.put("data", data);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		Log.d("JSON WITH ANSWERS", object.toString());
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppostreq = new HttpPost(url);
		StringEntity se;
		try {
			se = new StringEntity(object.toString());

			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			httppostreq.setEntity(se);
			HttpResponse httpresponse = httpclient.execute(httppostreq);

			HttpEntity resultentity = httpresponse.getEntity();
			InputStream inputstream = resultentity.getContent();
			Header contentencoding = httpresponse
					.getFirstHeader("Content-Encoding");
			if (contentencoding != null
					&& contentencoding.getValue().equalsIgnoreCase("gzip")) {
				inputstream = new GZIPInputStream(inputstream);
			}
			String resultstring = convertStreamToString(inputstream);
			inputstream.close();
			Log.d("RESPONSE******************", resultstring);

			JSONObject ob = null;
			try {
				ob = new JSONObject(resultstring);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (ob != null) {
				Utilities.writeString(context, "jsondata", "");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
