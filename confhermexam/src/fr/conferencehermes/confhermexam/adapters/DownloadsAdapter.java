package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.DownloadInstance;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class DownloadsAdapter extends BaseAdapter {
	private ArrayList<DownloadInstance> mListItems;
	private LayoutInflater mLayoutInflater;
	private Context c;

	public DownloadsAdapter(Context context,
			ArrayList<DownloadInstance> arrayList) {
		mListItems = arrayList;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.c = context;
	}

	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.downloads_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.examName);
			holder.desc = (TextView) view.findViewById(R.id.examDesc);
			holder.btnAction = (Button) view.findViewById(R.id.buttonAction);
			holder.btnRemove = (Button) view.findViewById(R.id.buttonRemove);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String stringItem = (String) mListItems.get(position).getName();
		if (stringItem != null) {
			if (holder.name != null) {
				holder.name.setText(stringItem);
			}

			int status = mListItems.get(position).getStatus();
			if (status == 1) {
				holder.desc.setText("Disponible");
				holder.btnAction.setBackgroundResource(R.drawable.exam_checked);
				holder.btnRemove.setBackgroundResource(R.drawable.exam_delete);
			} else if (status == 2) {
				holder.desc.setText("Mettre a jour");
				holder.btnAction.setBackgroundResource(R.drawable.exam_refresh);
				holder.btnRemove
						.setBackgroundResource(R.drawable.exam_delete_disabled);
			} else if (status == 3) {
				holder.desc.setText("Telecharger");
				holder.btnAction
						.setBackgroundResource(R.drawable.exam_download);
				holder.btnRemove
						.setBackgroundResource(R.drawable.exam_delete_disabled);
			}

			holder.btnAction.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int status = mListItems.get(position).getStatus();
					if (status == 2 || status == 3) {
						downloadFile(mListItems.get(position).getDownloadUrl(),
								mListItems.get(position).getName());
					}

				}
			});

			holder.btnRemove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int status = mListItems.get(position).getStatus();
					if (status == 1) {
						showDialog(position);
					}

				}
			});
		}

		if (position % 2 == 0) {
			view.setBackgroundColor(Color.parseColor("#f5f5f5"));
		} else {
			view.setBackgroundColor(Color.parseColor("#e7e7e7"));
		}
		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected TextView desc;
		protected Button btnAction;
		protected Button btnRemove;

	}

	private void downloadFile(String url, String title) {
		if (isDownloadManagerAvailable(c)) {
			DownloadManager.Request request = new DownloadManager.Request(
					Uri.parse(url));
			request.setDescription("Downloading exam...");
			request.setTitle(title);
			// in order for this if to run, you must use the android 3.2 to
			// compile your app
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				request.allowScanningByMediaScanner();
				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			}

			String fileName = "file" + System.currentTimeMillis();
			request.setDestinationInExternalPublicDir(
					Environment.DIRECTORY_DOWNLOADS, fileName);
			// get download service and enqueue file
			DownloadManager manager = (DownloadManager) c
					.getSystemService(Context.DOWNLOAD_SERVICE);
			manager.enqueue(request);
		}
	}

	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui",
					"com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	private void showDialog(final int pos) {
		AlertDialog.Builder b = new AlertDialog.Builder(c)
				.setTitle("Delete files?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								removeFile(mListItems.get(pos).getDownloadUrl());
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});

		AlertDialog alertDialog = b.create();
		alertDialog.setCancelable(true);
		alertDialog.show();
	}

	private void removeFile(String url) {
		AQuery aq = new AQuery(c);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("device_id", Utilities.getDeviceId(c));

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				try {
					if (json.has("data") && json.get("data") != null) {

					}

				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		});
	}
}