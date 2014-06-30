package fr.conferencehermes.confhermexam.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.fragments.DownloadsFragment;
import fr.conferencehermes.confhermexam.fragments.ExamineFragment;
import fr.conferencehermes.confhermexam.fragments.MyProfileFragment;
import fr.conferencehermes.confhermexam.fragments.PlanningFragment;
import fr.conferencehermes.confhermexam.fragments.ResultatFragment;
import fr.conferencehermes.confhermexam.fragments.TrainingsFragment;

public class Utilities {
	private static ProgressDialog mDialog;
	public static final String IS_LOGGED_IN = "IS_LOGGED_IN";

	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean(Context context, String key,
			boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();

	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();

	}

	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}

	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}

	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static int convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	public static int convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return (int) dp;
	}

	public static String timeConverter(int time) {
		if (time > 9)
			return String.valueOf(time);
		else
			return "0" + String.valueOf(time);
	}

	public static void selectFrag(FragmentActivity a, int PAGE_ID) {
		Fragment fr = null;

		switch (PAGE_ID) {
		case 0:
			fr = new MyProfileFragment();
			break;
		case 1:
			fr = new PlanningFragment();
			break;
		case 2:
			fr = new ExamineFragment();
			break;
		case 3:
			fr = new TrainingsFragment();
			break;
		case 4:
			fr = new ResultatFragment();
			break;
		case 5:
			fr = new DownloadsFragment();
			break;

		default:
			break;
		}

		if (fr != null) {
			FragmentManager fm = a.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.fragmentContainer, fr);
			fragmentTransaction.commit();
		}

	}

	public static void showOrHideActivityIndicator(Context ctx, final int hide,
			final String message) {
		if (mDialog != null && hide == 1) {
			if (mDialog.isShowing())
				mDialog.dismiss();
		} else {
			mDialog = new ProgressDialog(ctx);
			mDialog.setMessage(message);
			mDialog.setCancelable(false);
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.show();
		}
	}

	public static void showInfoDialog(Context context) {

		Dialog dialog = new Dialog(context);
		// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*
		 * dialog.getWindow().clearFlags(
		 * WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		 */
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);

		WebView wv = new WebView(context);
		/*
		 * wv.loadUrl(DataHolder.getInstance().getInfoURL() + "&" +
		 * Constants.AUTH_TOKEN);
		 */
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);

				return true;
			}
		});
		dialog.setTitle("Info");
		dialog.setContentView(wv);

		if (!dialog.isShowing())
			dialog.show();
		else
			dialog.dismiss();

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		/*
		 * lp.width = (int) context.getResources().getDimension(
		 * R.dimen.info_dialog_w); lp.height = (int)
		 * context.getResources().getDimension( R.dimen.info_dialog_h);
		 */
		window.setAttributes(lp);

	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void openWebsite(Context c) {
		Intent browserIntent1 = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.conference-hermes.fr"));
		c.startActivity(browserIntent1);
	}

	public static String getDeviceId(Context c) {
		final TelephonyManager tm = (TelephonyManager) c
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getDeviceId() != null)
			return tm.getDeviceId();
		else
			return Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
	}

}
