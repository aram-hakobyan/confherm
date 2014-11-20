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

import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.parser.ExerciseAnswer;
import android.content.Context;
import android.os.AsyncTask;

public class ExamJsonTransmitter extends
		AsyncTask<JSONObject, JSONObject, JSONObject> {
	private static final String url = "http://ecni.conference-hermes.fr/api/examanswer";
	private Context context;
	private ExerciseAnswer exerciseAnswer;

	public ExamJsonTransmitter(Context c, ExerciseAnswer ea) {
		this.context = c;
		this.exerciseAnswer = ea;
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

		JSONObject json = data[0];
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppostreq = new HttpPost(url);
		StringEntity se;
		try {
			se = new StringEntity(json.toString());

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
			JSONObject ob = null;
			try {
				ob = new JSONObject(resultstring);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (ob != null) {
				if (ob.has("status")) {
					int status = ob.optInt("status");
					if (status == 200) {
						Utilities.writeString(context, "jsondata", "");
						updateAnswer(exerciseAnswer);
					}
				}
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

	private void updateAnswer(ExerciseAnswer ea) {
		DatabaseHelper db = new DatabaseHelper(context);
		ea.setIsSent(1);
		db.updateExerciseAnswer(ea);
		db.closeDB();
	}
}
