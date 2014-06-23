package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.Question;

public class QuestionsAdapter extends BaseAdapter {
	private ArrayList<Question> mListItems;
	private LayoutInflater mLayoutInflater;

	public QuestionsAdapter(Context context, ArrayList<Question> arrayList) {
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
			view = mLayoutInflater.inflate(R.layout.question_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.listQuestionTitle);
			holder.layout = (LinearLayout) view
					.findViewById(R.id.listQuestionRow);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (holder.name != null) {
			holder.name.setText("QUESTION "
					+ (mListItems.get(position).getId()));
		}

		/*
		 * if (position == 0)
		 * holder.layout.setBackgroundColor(Color.parseColor("#0d5c7c")); else
		 * holder.layout.setBackgroundColor(Color.parseColor("#86adbd"));
		 */

		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected LinearLayout layout;

	}
}