package fr.conferencehermes.confhermexam.util;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferenceStorage {

	private static final String KEY_HIDE_NAME = "key_hide_name";

	private static PreferenceStorage sInstance;

	private SharedPreferences mPreferences;

	private PreferenceStorage(Context c) {
		mPreferences = c.getSharedPreferences("preferences",
				Context.MODE_PRIVATE);
	}

	public static PreferenceStorage getInstance(Context c) {
		PreferenceStorage localInstance = sInstance;
		if (localInstance == null) {
			synchronized (PreferenceStorage.class) {
				localInstance = sInstance;
				if (localInstance == null) {
					sInstance = localInstance = new PreferenceStorage(c);
				}
			}
		}
		return localInstance;
	}

	public boolean isNameHidden() {
		return mPreferences.getBoolean(KEY_HIDE_NAME, false);
	}

	public void setNameHidden(final boolean isLogged) {
		mPreferences.edit().putBoolean(KEY_HIDE_NAME, isLogged).commit();
	}

}
