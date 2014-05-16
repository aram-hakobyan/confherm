package fr.conferencehermes.confhermexam.util;


public class DataHolder {
	private static DataHolder instance;

	private DataHolder() {

	}

	public static DataHolder getInstance() {
		if (instance == null) {
			instance = new DataHolder();
		}
		return instance;

	}

}
