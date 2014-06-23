package fr.conferencehermes.confhermexam.connection;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
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



	private String cookieString = "";
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
		CookieStore cookieStore = new BasicCookieStore();

		if (CookieStorage.getInstance().getArrayList() != null
				&& !CookieStorage.getInstance().getArrayList().isEmpty()) {
			this.cookieString = (String) CookieStorage.getInstance()
					.getArrayList().get(0);
		}

		try {
			HttpResponse response = null;
			switch (method) {
			case GET:
				HttpGet httpGet = new HttpGet(url);
				if (cookieString != null) {
					httpGet.setHeader("Cookie", cookieString);
				}
				response = httpClient.execute(httpGet);
				break;
			case POST:
				httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
						CookiePolicy.RFC_2109);

				// Create local HTTP context
				HttpContext localContext = new BasicHttpContext();
				// Bind custom cookie store to the local context
				localContext.setAttribute(ClientContext.COOKIE_STORE,
						cookieStore);

				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(this.nameValuePairs));

				if (cookieString != null) {
					httpPost.setHeader("Cookie", cookieString);
				}
				httpPost.setHeader(
						"User-Agent",
						"Mozilla/5.0 (X11; U; Linux "
								+ "i686; en-US; rv:1.8.1.6) Gecko/20061201 Firefox/2.0.0.6 (Ubuntu-feisty)");
				httpPost.setHeader(
						"Accept",
						"text/html,application/xml,"
								+ "application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");

				response = httpClient.execute(httpPost, localContext);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();

				String responses = httpClient
						.execute(httpPost, responseHandler);

				int status = response.getStatusLine().getStatusCode();
				Log.d("Response HTTP", status + "");
				Log.d("RESPONS", responses + "");
				JSONParser.parseLoginData(responses);

				JSONObject jsonObj = new JSONObject(responses);
				Profile profileData= JSONParser.parseProfileData(jsonObj);
				LoginActivity.setLoginData(profileData);
				MyProfileFragment.setProfileData(profileData);
				handler.sendMessage(Message.obtain(handler, status));
				// JSONParserCategories.parseJSONData(result)

				break;

			}
	
		} catch (Exception e) {
			handler.sendMessage(Message.obtain(handler,
					HttpConnection.DID_ERROR, e));
		}
		ConnectionManager.getInstance().didComplete(this);
	}

}
