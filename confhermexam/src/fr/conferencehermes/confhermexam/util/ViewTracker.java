package fr.conferencehermes.confhermexam.util;

import android.content.Context;

public class ViewTracker {

	private static ViewTracker viewContext = null;

	private Context currentContext = null;

	private ViewTracker() {

	}

	/**
	 * Static instance
	 * 
	 * @return
	 */
	public static ViewTracker getInstance() {

		if (viewContext == null) {
			viewContext = new ViewTracker();
		}

		return viewContext;
	}

	/** Returns application current context */
	public Context getCurrentContext() {
		return currentContext;
	}

	/** Applies application current context */
	public void setCurrentContext(Context currentContext) {
		this.currentContext = currentContext;
	}

}
