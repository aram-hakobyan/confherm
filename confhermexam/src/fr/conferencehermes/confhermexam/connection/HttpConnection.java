package fr.conferencehermes.confhermexam.connection;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import fr.conferencehermes.confhermexam.LoginActivity;
import fr.conferencehermes.confhermexam.fragments.MyProfileFragment;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.parser.Profile;
import fr.conferencehermes.confhermexam.util.CookieStorage;

/**
 * 
 * @author aabraham
 * 
 */
public class HttpConnection implements Runnable {

	public static final int DID_START = 0;
	public static final int DID_ERROR = 1;
	public static final int DID_SUCCEED = 2;
	public static final int PUBLISH_SUCCESS = 3;

	private static final int GET = 0;
	private static final int POST = 1;
	int status;
	// private String cookieString = "";
	private String url;
	private int method;
	private Handler handler;
	private String data;
	private List<NameValuePair> nameValuePairs;

	private HttpClient httpClient;

	public HttpConnection() {
		this(new Handler());
	}

	public HttpConnection(Handler _handler) {
		handler = _handler;
	}

	public void create(int method, String url, String data,
			final List<NameValuePair> nameValuePairs) {
		this.method = method;
		this.url = url;
		this.data = data;
		this.nameValuePairs = nameValuePairs;
		ConnectionManager.getInstance().push(this);
	}

	public void get(String url, String data) {
		create(GET, url, data, null);
	}

	public void post(String url, final List<NameValuePair> nameValuePairs) {
		create(POST, url, data, nameValuePairs);
	}

	@Override
	public void run() {
		handler.sendMessage(Message.obtain(handler, HttpConnection.DID_START));

		httpClient = new DefaultHttpClient();
	


		try {
			HttpResponse response = null;
			switch (method) {

			case POST:

				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(this.nameValuePairs));

				response = httpClient.execute(httpPost);

				String responses = EntityUtils.toString(response.getEntity());

				status = response.getStatusLine().getStatusCode();
				Log.d("Response HTTP", status + "");
				Log.d("RESPONS", responses + "");
				JSONParser.parseLoginData(responses);

				JSONObject jsonObj = new JSONObject(responses);
				Profile profileData = JSONParser.parseProfileData(jsonObj);
				LoginActivity.setLoginData(profileData);
				MyProfileFragment.setProfileData(profileData);
				handler.sendMessage(Message.obtain(handler, status));
				// JSONParserCategories.parseJSONData(result)

				break;

			}

		} catch (Exception e) {
			e.printStackTrace();

			handler.sendMessage(Message.obtain(handler, status));
		}
		ConnectionManager.getInstance().didComplete(this);
	}

}
