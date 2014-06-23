package fr.conferencehermes.confhermexam.connection;

import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import fr.conferencehermes.confhermexam.connectionhelper.ActionDelegate;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.ViewTracker;

/**
 * 
 * @author aabraham
 * 
 */
public class BaseNetworkManager {

	// Default Constructor
	public BaseNetworkManager() {

	}

	/**
	 * Main HTTP Service requests POST method
	 * 
	 * @param successMessage
	 * @param startingMessage
	 * @param paramsList
	 * @param managerObject
	 * @param classString
	 * @param serviceName
	 */
	@SuppressLint("HandlerLeak")
	public void constructConnectionAndHitPOST(final String successMessage,
			final String startingMessage, final List<NameValuePair> paramsList,
			final Object managerObject, final String classString,
			final String serviceName,final String loginOrAuth) {

		if (NetworkReachability.isReachable()) {

			final Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case HttpConnection.DID_START:
						Log.d("Request", startingMessage);
						break;
					case 200:
						Log.d("Response Recieved", successMessage);
						handleFinishResponse();
						break;

					case 403:
						Log.d("Access denied. Please login",
								"404");

						break;

					case 400:
						Log.d("Exception occured while hitting response!",
								"400");
						handleFailResponse();
						break;
						
					case 500:
						Log.d("Something went wrong. Please try again later",
								"500");				
						break;

					}
				}
			};

			final HttpConnection connection = new HttpConnection(handler);
			final StringBuilder builder = new StringBuilder();

			// ------------------- Construct Service Url ------------------//

			if (loginOrAuth.equalsIgnoreCase(Constants.SERVER_URL_AUTH)){
				builder.append(Constants.SERVER_URL_AUTH);
			}else{
				builder.append(Constants.SERVER_URL);
			}
			final String httpRequestUrl = builder.toString();

			connection.post(httpRequestUrl, paramsList);
		} else {
			ActionDelegate delegate = (ActionDelegate) ViewTracker
					.getInstance().getCurrentContext();
			delegate.didFailRequestProcessing();
		}
	}




	/**
	 * Hides Activity Indicator
	 */
	private void handleFinishResponse() {
		ActionDelegate del = (ActionDelegate) ViewTracker.getInstance()
				.getCurrentContext();

		del.didFinishRequestProcessing();
	}

	private void handleFailResponse() {
		ActionDelegate del = (ActionDelegate) ViewTracker.getInstance()
				.getCurrentContext();

		del.didFailRequestProcessing();
	}

}