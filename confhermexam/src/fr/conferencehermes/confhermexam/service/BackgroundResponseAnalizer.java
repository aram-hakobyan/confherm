package fr.conferencehermes.confhermexam.service;

import java.util.List;

import org.apache.http.NameValuePair;

/**
 * Executes new thread for parsing response data
 * 
 * @author aabraham
 * 
 */
public class BackgroundResponseAnalizer extends Thread {

	private String serviceName;
	private String responseData;
	private List<NameValuePair> paramsList;
	private String urlWithParams;

	/**
	 * 
	 * @param serviceName
	 * @param responseData
	 * @param params
	 */
	public BackgroundResponseAnalizer(final String serviceName,
			final String responseData, final String urlWithParams,
			final List<NameValuePair> paramsList) {

		this.serviceName = serviceName;
		this.responseData = responseData;
		this.paramsList = paramsList;
		this.urlWithParams = urlWithParams;
	}

	@Override
	public void run() {
		super.run();

		/*
		 * BaseResponseAnalyzer.analyze(serviceName, urlWithParams,
		 * responseData);
		 */

	}

}
