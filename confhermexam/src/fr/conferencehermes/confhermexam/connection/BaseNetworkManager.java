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
			final String serviceName) {

		if (NetworkReachability.isReachable()) {

			final Handler handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case HttpConnection.DID_START:
						Log.d("Request", startingMessage);
						break;
					case HttpConnection.DID_SUCCEED:
						Log.d("Response Recieved", successMessage);
						break;

					case HttpConnection.PUBLISH_SUCCESS:
						final Integer progress = (Integer) msg.obj;

						break;

					case HttpConnection.DID_ERROR:
						Exception ex = (Exception) msg.obj;
						Log.d("Exception occured while hitting response!",
								ex.getMessage());
						break;

					}
				}
			};

			final HttpConnection connection = new HttpConnection(handler);
			final StringBuilder builder = new StringBuilder();

			// ------------------- Construct Service Url ------------------//

			builder.append(Constants.SERVER_URL);
			builder.append(Constants.RIGHT_SLASH);
			builder.append(serviceName);
			final String httpRequestUrl = builder.toString();

			connection.post(httpRequestUrl, paramsList);
		} else {
			ActionDelegate delegate = (ActionDelegate) ViewTracker
					.getInstance().getCurrentContext();
			delegate.didFailRequestProcessing();
		}
	}

	/**
	 * Main HTTP Service requests GET method
	 * 
	 * @param successMessage
	 * @param startingMessage
	 * @param paramsList
	 * @param managerObject
	 * @param classString
	 * @param serviceName
	 */
	public void constructConnectionAndHitGET(final String successMessage,
			final String startingMessage, final String urlAndParamsList,
			final Object managerObject, final String classString,
			final String serviceName) {

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {

				case HttpConnection.DID_START:
					Log.d("Request", startingMessage);
					break;
				case HttpConnection.DID_SUCCEED:
					Log.d("Response Recieved", msg.obj.toString());
					

					/*
					 * chainOfResponsibilities(msg.obj.toString(), classString,
					 * managerObject, serviceName, null, urlAndParamsList);
					 */

					break;
				case HttpConnection.DID_ERROR:
					Exception ex = (Exception) msg.obj;
					handleProblematicResponse();
					Log.d("Exception occured while hitting response!",
							ex.getMessage());
					/*
					 * Toast.makeText(
					 * ViewTracker.getInstance().getCurrentContext(),
					 * "Exception occured while hitting response!",
					 * Toast.LENGTH_LONG).show();
					 */
					break;

				}
			}
		};

		final HttpConnection connection = new HttpConnection(handler);

		connection.get(urlAndParamsList, null);
		// connection.get("http://belbooner.site40.net/drakonich", null);

	}

	/**
	 * @param responseHtml
	 */
	/*
	 * private void chainOfResponsibilities(String responseHtml, final String
	 * classString, final Object managerObject, final String requestType, final
	 * List<NameValuePair> paramsList, final String urlAndParamsList) {
	 * 
	 * if (responseHtml != null) { BackgroundResponseAnalizer
	 * backgroundRespAnalyzer = new BackgroundResponseAnalizer( requestType,
	 * responseHtml, urlAndParamsList, paramsList);
	 * 
	 * backgroundRespAnalyzer.start(); }
	 * 
	 * }
	 */

	/**
	 * Hides Activity Indicator
	 */
	private void handleProblematicResponse() {
//		ActionDelegate del = (ActionDelegate) ViewTracker.getInstance()
//				.getCurrentContext();

		/* del.didFailRequestProcessing(); */
	}

}
