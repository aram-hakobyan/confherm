package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Exam exam = (Exam) mListItems.get(position);
		if (exam != null) {
			if (holder.name != null) {
				holder.name.setText(exam.getTitle());
				holder.desc.setText(exam.getStartDate());
				holder.status.setText("Status: "
						+ String.valueOf(exam.getCategoryType()));
			}
		}

		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected TextView desc;
		protected TextView status;

	}
}