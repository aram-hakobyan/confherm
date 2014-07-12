package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.db.DatabaseHelper;
import fr.conferencehermes.confhermexam.parser.DownloadInstance;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.JSONParser;
import fr.conferencehermes.confhermexam.service.DownloadService;
import fr.conferencehermes.confhermexam.util.Constants;
import fr.conferencehermes.confhermexam.util.Utilities;

public class DownloadsAdapter extends BaseAdapter {
	private ArrayList<DownloadInstance> mListItems;
	private LayoutInflater mLayoutInflater;
	private Context c;
	private AQuery aq;
	int eventId = -1;

	public DownloadsAdapter(Context context,
			ArrayList<DownloadInstance> arrayList, int event_id) {
		mListItems = arrayList;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.c = context;
		aq = new AQuery(c);
		this.eventId = event_id;
	}

	@Override
	public int getCount() {
		return mListItems.size();
	}

	@Override
	public Object getItem(int i) {
		return mListItems.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {

		final ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.downloads_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.examName);
			holder.desc = (TextView) view.findViewById(R.id.examDesc);
			holder.btnAction = (Button) view.findViewById(R.id.buttonAction);
			holder.btnRemove = (Button) view.findViewById(R.id.buttonRemove);
			holder.progressBar = (ProgressBar) view
					.findViewById(R.id.progressBar);
			holder.downloadProgressNumber = (TextView) view
					.findViewById(R.id.downloadProgressNumber);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String stringItem = (String) mListItems.get(position).getName();
		if (stringItem != null)
			if (holder.name != null) {
				holder.name.setText(stringItem);

			}

		int progress = mListItems.get(position).getProgress();
		int status = mListItems.get(position).getStatus();

		if (status == 1) {
			holder.desc.setText("OK");
			holder.btnAction.setBackgroundResource(R.drawable.exam_checked);
			holder.btnRemove.setBackgroundResource(R.drawable.exam_delete);
		} else if (status == 2) {
			holder.desc.setText(c.getResources()
					.getString(R.string.need_update));
			holder.btnAction.setBackgroundResource(R.drawable.exam_refresh);
			holder.btnRemove.setBackgroundResource(R.drawable.exam_delete);
		} else if (status == 3) {
			holder.desc.setText("Disponible");
			holder.btnAction.setBackgroundResource(R.drawable.exam_download);
			holder.btnRemove
					.setBackgroundResource(R.drawable.exam_delete_disabled);
		}

		if (mListItems.get(position).isDownloading()) {
			holder.downloadProgressNumber.setVisibility(View.VISIBLE);
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.btnAction.setVisibility(View.INVISIBLE);
		} else {
			holder.downloadProgressNumber.setVisibility(View.INVISIBLE);
			holder.progressBar.setVisibility(View.INVISIBLE);
			holder.btnAction.setVisibility(View.VISIBLE);
		}

		if (progress > 0 && progress < 100) {
			holder.downloadProgressNumber.setText(String.valueOf(progress)
					+ "%");
			holder.desc.setText(c.getResources().getString(R.string.download));
		} else if (progress == 100) {
			holder.desc.setText("OK");
			holder.btnAction.setBackgroundResource(R.drawable.exam_checked);
			holder.btnRemove.setBackgroundResource(R.drawable.exam_delete);
			holder.progressBar.setVisibility(View.INVISIBLE);
			holder.downloadProgressNumber.setVisibility(View.INVISIBLE);
			holder.btnAction.setVisibility(View.VISIBLE);
			mListItems.get(position).setStatus(1);
		}

		holder.btnAction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int status = mListItems.get(position).getStatus();
				if (status == 2 || status == 3) {
					downloadFile(mListItems.get(position).getDownloadUrl(),
							mListItems.get(position).getName(), position);
					holder.progressBar.setVisibility(View.VISIBLE);
					holder.btnAction.setVisibility(View.INVISIBLE);
					holder.downloadProgressNumber.setVisibility(View.VISIBLE);
				}

			}
		});

		holder.btnRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int status = mListItems.get(position).getStatus();
				if (status == 1 || status == 2) {
					AlertDialog.Builder b = new AlertDialog.Builder(c)
							.setTitle("Attention")
							.setMessage(
									c.getResources().getString(
											R.string.delete_event_alert))

							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											removeFile(mListItems.get(position)
													.getRemoveUrl(), position);
											DatabaseHelper db = new DatabaseHelper(
													c);
											int eventId = mListItems.get(
													position).getEventId();
											db.deleteEvent(eventId);
											ArrayList<Exam> exams = db
													.getAllExams();
											for (int i = 0; i < exams.size(); i++) {
												if (exams.get(i).getEventId() == eventId)
													db.deleteExam(exams.get(i)
															.getId());
											}
											db.close();

											holder.desc
													.setText(c
															.getResources()
															.getString(
																	R.string.telecharger));
											holder.btnAction
													.setBackgroundResource(R.drawable.exam_download);
											holder.btnRemove
													.setBackgroundResource(R.drawable.exam_delete_disabled);
											mListItems.get(position).setStatus(
													3);
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											dialog.dismiss();
										}
									});

					AlertDialog alertDialog = b.create();
					alertDialog.setCancelable(true);
					alertDialog.show();
				}

			}
		});

		if (position % 2 == 0) {
			view.setBackgroundColor(Color.parseColor("#f5f5f5"));
		} else {
			view.setBackgroundColor(Color.parseColor("#e7e7e7"));
		}

		if (eventId != -1) {
			if (mListItems.get(position).getEventId() == eventId) {
				eventId = -1;
				downloadFile(mListItems.get(position).getDownloadUrl(),
						mListItems.get(position).getName(), position);
				holder.progressBar.setVisibility(View.VISIBLE);
				holder.btnAction.setVisibility(View.INVISIBLE);
				holder.downloadProgressNumber.setVisibility(View.VISIBLE);
			}
		}

		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected TextView desc;
		protected Button btnAction;
		protected Button btnRemove;
		protected ProgressBar progressBar;
		public TextView downloadProgressNumber;
	}

	private void downloadFile(String url, final String title, final int position) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("device_id", Utilities.getDeviceId(c));

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				try {
					if (json.has("data") && json.get("data") != null) {
						Intent intent = new Intent(c, DownloadService.class);
						intent.putExtra("url", json.getString("data"));
						intent.putExtra("title", title);
						intent.putExtra("position", position);
						intent.putExtra("receiver", new DownloadReceiver(
								new Handler()));
						c.startService(intent);
					}

				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		});

	}

	private class DownloadReceiver extends ResultReceiver {
		public DownloadReceiver(Handler handler) {
			super(handler);
		}

		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (resultCode == DownloadService.UPDATE_PROGRESS) {
				int progress = resultData.getInt("progress");
				int pos = resultData.getInt("position");

				mListItems.get(pos).setDownloading(true);
				mListItems.get(pos).setProgress(progress);

				Log.d(String.valueOf(pos) + " DOWNLOADED: ",
						String.valueOf(progress));
				if (progress == 100) {
					DownloadsAdapter.this.mListItems.get(pos).setStatus(1);
				}

				notifyDataSetChanged();
			}
		}
	}

	private void removeFile(String url, final int position) {
		AQuery aq = new AQuery(c);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(Constants.KEY_AUTH_TOKEN, JSONParser.AUTH_KEY);
		params.put("device_id", Utilities.getDeviceId(c));

		aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				try {
					if (json != null)
						if (json.has("status") && json.getInt("status") == 200) {
							mListItems.get(position).setStatus(3);
							mListItems.get(position).setProgress(0);
							mListItems.get(position).setDownloading(false);
							notifyDataSetChanged();
						}

				} catch (JSONException e) {
					e.printStackTrace();

				}
			}
		});
	}

}