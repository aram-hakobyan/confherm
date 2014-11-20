package fr.conferencehermes.confhermexam.connectionhelper;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author aabraham
 * 
 */
public class RequestCreator {

	/**
	 * Construct Parameters Map
	 * 
	 * @param objects
	 * @return
	 */
	public Map<String, String> createAppropriateMapRequest(Object... objects) {

		final Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < objects.length; i += 2) {

			final String currentKey = (String) objects[i];
			final String currentValue = (String) objects[i + 1];

			if (!map.containsKey(currentKey)) {
				map.put(currentKey, currentValue);
			}
		}

		return map;
	}

	

}
