package fr.conferencehermes.confhermexam.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import fr.conferencehermes.confhermexam.util.ViewTracker;

public class NetworkReachability {

	/**
	 * Gets network status
	 * 
	 * @return
	 */

	/**
	 * Gets network type
	 * 
	 * @return
	 */
	public static int getNetworkType() {

		Context current = ViewTracker.getInstance().getCurrentContext();
		ConnectivityManager connectivity = (ConnectivityManager) current
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null || !connectivity.getBackgroundDataSetting()) {
			return -1;
		}

		int netType = info.getType();
		return netType;
	}

}
