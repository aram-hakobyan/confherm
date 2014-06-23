package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.Exam;

public class ExamsAdapter extends BaseAdapter {
	private ArrayList<Exam> mListItems;
	private LayoutInflater mLayoutInflater;

	public ExamsAdapter(Context context, ArrayList<Exam> arrayList) {
		mListItems = arrayList;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
				holder.name.setText(exam.getTitle());
				holder.desc.setText("Available from : " + new Date(exam.getStartDate()));

				int status = exam.getStatus();
				if (status == 1) {
					holder.status.setText("Disponible");
					holder.button
							.setBackgroundResource(R.drawable.exam_checked);
				} else if (status == 2) {
					holder.status.setText("Non disponible");
					holder.button
							.setBackgroundResource(R.drawable.exam_refresh);
				} else if (status == 3) {
					holder.status.setText("Non disponible");
					holder.button
							.setBackgroundResource(R.drawable.exam_download);

				} else if (status == 4) {
					holder.status.setText("Non disponible");
					holder.button
							.setBackgroundResource(R.drawable.exam_x);
				}
			}

		}

		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected TextView desc;
		protected TextView status;
		protected Button button;

	}
}