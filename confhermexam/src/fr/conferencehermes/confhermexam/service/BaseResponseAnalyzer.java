package fr.conferencehermes.confhermexam.service;

import android.app.Activity;
import android.net.ParseException;
import android.util.Log;
import fr.conferencehermes.confhermexam.connectionhelper.ActionDelegate;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.ViewTracker;

/**
 * Synchronized instance which evaluates response data and passes to the
 * structural parsing
 * 
 * @author aabraham
 * 
 */
public class BaseResponseAnalyzer {

	/**
	 * Handles responses and chains parsing responsibilities
	 * 
	 * @param serviceName
	 * @param map
	 * @param responseData
	 * @throws ParseException
	 */

	public static synchronized void analyze(final String serviceName,
			final String urlWithParams, final String responseData) {

		if (serviceName.equals(Constants.SERVER_URL)) {
			try {
				//Parser.parseData(responseData);

				final ActionDelegate del = (ActionDelegate) ViewTracker
						.getInstance().getCurrentContext();
				Activity activity = (Activity) ViewTracker.getInstance()
						.getCurrentContext();

				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						del.didFinishRequestProcessing();
					}
				});

			} catch (Exception e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

		}

	}
}
