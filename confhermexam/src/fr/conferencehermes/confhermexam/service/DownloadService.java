package fr.conferencehermes.confhermexam.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import fr.conferencehermes.confhermexam.util.UnzipUtility;

public class DownloadService extends IntentService {
	public static final int UPDATE_PROGRESS = 8344;
	private String path;
	private Intent mIntent;
	Handler mMainThreadHandler = null;

	public DownloadService() {
		super("DownloadService");
		mMainThreadHandler = new Handler();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mIntent = intent;
		String urlToDownload = intent.getStringExtra("url");
		String title = intent.getStringExtra("title");
		DownloaderTask downloadTask = new DownloaderTask();
		downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				urlToDownload, title);
	}

	private class DownloaderTask extends AsyncTask<String, Integer, String> {
		private PowerManager.WakeLock mWakeLock;

		private String urlToDownload;
		private String title;

		@Override
		protected void onPreExecute() {
			PowerManager pm = (PowerManager) DownloadService.this
					.getSystemService(DownloadService.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			ResultReceiver receiver = (ResultReceiver) mIntent
					.getParcelableExtra("receiver");
			try {
				urlToDownload = params[0];
				title = params[1];
				URL url = new URL(urlToDownload);
				URLConnection connection = url.openConnection();
				connection.connect();  

				int fileLength = connection.getContentLength();
				path = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				
				
				path += "/temp/android/data/";

				File mFolder = new File(path);
				if (!mFolder.exists()) {
					mFolder.mkdirs();
				}
				path += title;
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(path);

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					Bundle resultData = new Bundle();
					resultData.putInt("progress",
							(int) (total * 100 / fileLength));
					receiver.send(UPDATE_PROGRESS, resultData);
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Bundle resultData = new Bundle();
				resultData.putInt("progress", 100);
				receiver.send(UPDATE_PROGRESS, resultData);
			} catch (Exception e) {
				e.printStackTrace();
			}

			mWakeLock.release();
			String zipFilePath = path;
			String destDirectory = path + " zipped";
			UnzipUtility unzipper = new UnzipUtility();
			try {
				unzipper.unzip(zipFilePath, destDirectory, DownloadService.this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return "OK";
		}
	}
}