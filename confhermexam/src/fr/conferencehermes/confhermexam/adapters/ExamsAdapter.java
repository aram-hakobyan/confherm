package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.fragments.DownloadsFragment;
import fr.conferencehermes.confhermexam.parser.Exam;
import fr.conferencehermes.confhermexam.parser.TimeSlot;
import fr.conferencehermes.confhermexam.util.Utilities;

public class ExamsAdapter extends BaseAdapter {
	private ArrayList<Exam> mListItems;
	private LayoutInflater mLayoutInflater;
	private ArrayList<Integer> mDownloadedExamIds;
	private Context c;

	public ExamsAdapter(Context context, ArrayList<Exam> arrayList,
			ArrayList<Integer> downloadedExamIds) {
		this.c = context;
		this.mListItems = arrayList;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mDownloadedExamIds = downloadedExamIds;
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
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.exam_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.examName);
			holder.desc = (TextView) view.findViewById(R.id.examDesc);
			holder.status = (TextView) view.findViewById(R.id.examStatus);
			holder.button = (Button) view.findViewById(R.id.examButton1);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Exam exam = (Exam) mListItems.get(position);
		if (exam != null) {
			if (holder.name != null) {
				holder.name.setText(exam.getEvent_name() + " / "
						+ exam.getTitle());
			}

			Calendar calendar = new GregorianCalendar(
					TimeZone.getTimeZone("Europe/Paris"));
			calendar.setTimeInMillis(exam.getStartDate() * 1000);
			final String startTimeString = Utilities.timeConverter(calendar
					.get(Calendar.HOUR_OF_DAY))
					+ ":"
					+ Utilities.timeConverter(calendar.get(Calendar.MINUTE));
			calendar.setTimeInMillis(exam.getEndDate() * 1000);
			final String endTimeString = Utilities.timeConverter(calendar
					.get(Calendar.HOUR_OF_DAY))
					+ ":"
					+ Utilities.timeConverter(calendar.get(Calendar.MINUTE));
			holder.desc.setText("Actif le "
					+ String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "/"
					+ String.valueOf(calendar.get(Calendar.MONTH) + 1) + "/"
					+ String.valueOf(calendar.get(Calendar.YEAR)) + " "
					+ "at" + " "
					+ startTimeString);

			int status = exam.getStatus();
			if (status == 1) {
				holder.status.setText("OK");
				holder.button.setBackgroundResource(R.drawable.exam_checked);
			} else if (status == 2) {
				holder.status.setText(c.getResources().getString(
						R.string.need_update));
				holder.button.setBackgroundResource(R.drawable.exam_refresh);
			} else if (status == 3) {
				holder.status.setText(c.getResources().getString(
						R.string.examen_avaible));
				holder.button.setBackgroundResource(R.drawable.exam_download);
			} else if (status == 4) {
				holder.status.setText(c.getResources().getString(
						R.string.examen_not_avaible));
				holder.button.setBackgroundResource(R.drawable.exam_x);
			}

			if (canStartExam(exam))
				view.setBackgroundColor(Color.parseColor("#69a2b9"));
			else
				view.setBackgroundColor(Color.parseColor("#eeeeee"));

		}

		return view;

	}

	public boolean canStartExam(Exam e) {
		Calendar calendar = new GregorianCalendar(
				TimeZone.getTimeZone("Europe/Paris"));
		long currentTime = calendar.getTimeInMillis() / 1000;
		return true;//e.getStartDate() < currentTime && e.getEndDate() > currentTime;

	}

	private static class ViewHolder {
		protected TextView name;
		protected TextView desc;
		protected TextView status;
		protected Button button;

	}

}