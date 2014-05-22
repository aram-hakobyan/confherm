package fr.conferencehermes.confhermexam.connectionhelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import fr.conferencehermes.confhermexam.util.Constants;

/**
 * Supporting methods for creating HTTP request object
 * 
 * @author aabraham
 */
public class RequestHelper {

	/**
	 * Main GET Request Creator
	 * 
	 * @param map
	 * @return
	 */
	public String constructGetRequestString(final Map<String, String> map,
			final String serverUrl, final String serviceUrl) {
		final StringBuilder builder = new StringBuilder();

		builder.append(serverUrl);
		builder.append(Constants.RIGHT_SLASH);
		builder.append(serviceUrl);

		if (map != null && !map.isEmpty()) {
			builder.append(Constants.FIRST_PARAM_SEPARATOR);
			final Iterator<String> iter = map.keySet().iterator();
			int counter = 0;
			while (iter.hasNext()) {
				final String currentKey = iter.next();
				final String currentValue = map.get(currentKey);

				builder.append(currentKey);
				builder.append(Constants.EQUAL);
				if (currentValue != null)
					try {
						builder.append(URLEncoder.encode(currentValue, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}

				if (counter != map.size() - 1) {
					builder.append(Constants.PARAMETER_SEPARATOR);
				}

				counter++;
			}
		}
		return builder.toString();
	}

	/**
	 * Main GET Request Creator
	 * 
	 * @param map
	 * @return
	 */
	public String constructPostRequestString(final Map<String, String> map) {
		final StringBuilder builder = new StringBuilder();

		if (map != null && !map.isEmpty()) {
			// builder.append(Constants.PARAMETER_SEPARATOR);
			final Iterator<String> iter = map.keySet().iterator();
			int counter = 0;
			while (iter.hasNext()) {
				final String currentKey = iter.next();
				final String currentValue = map.get(currentKey);

				// builder.append(URLEncoder.encode(currentKey));
				builder.append(currentKey);
				builder.append(Constants.EQUAL);
				// if (currentValue != null)// &&
				// !currentKey.equals(Constants.AUTH_TOKEN)
				// builder.append(URLEncoder.encode(currentValue));
				// else if(currentValue != null)
				builder.append(currentValue);

				if (counter != map.size() - 1) {
					builder.append(Constants.PARAMETER_SEPARATOR);
				}

				counter++;
			}
		}

		return builder.toString();
	}

	/**
	 * 
	 * @param keyValueMap
	 * @return
	 */
	public List<NameValuePair> createPostDataWithKeyValuePair(
			final Map<String, String> keyValueMap) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		Iterator<String> iter = keyValueMap.keySet().iterator();

		while (iter.hasNext()) {

			final String currentKey = iter.next();

			if (currentKey != null) {
				final String currentValue = keyValueMap.get(currentKey);

				nameValuePairs.add(new BasicNameValuePair(currentKey,
						currentValue));
			}
		}

		return nameValuePairs;

	}

}
