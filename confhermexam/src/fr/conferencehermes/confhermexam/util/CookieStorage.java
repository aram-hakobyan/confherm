package fr.conferencehermes.confhermexam.util;

import java.util.ArrayList;


public class CookieStorage {
	private ArrayList<Object> arrayList;

	private static CookieStorage instance;

	private CookieStorage() {
		arrayList = new ArrayList<Object>();
	}

	public static CookieStorage getInstance() {
		if (instance == null) {
			instance = new CookieStorage();
		}
		return instance;
	}

	public ArrayList<Object> getArrayList() {
		return arrayList;
	}

	@Override
	public String toString() {
		return getArrayList().toString();
	}
}
