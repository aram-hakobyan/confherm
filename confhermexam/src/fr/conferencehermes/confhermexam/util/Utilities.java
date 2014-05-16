package fr.conferencehermes.confhermexam.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.fragments.ExamineFragment;
import fr.conferencehermes.confhermexam.fragments.MyProfileFragment;
import fr.conferencehermes.confhermexam.fragments.NotesFragment;
import fr.conferencehermes.confhermexam.fragments.PlanningFragment;
import fr.conferencehermes.confhermexam.fragments.ResultatFragment;
import fr.conferencehermes.confhermexam.fragments.TelechargementFragment;

public class Utilities {
	private static ProgressDialog mDialog;

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
			fr = new NotesFragment();
			break;
		case 4:
			fr = new ResultatFragment();
			break;
		case 5:
			fr = new TelechargementFragment();
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

}
