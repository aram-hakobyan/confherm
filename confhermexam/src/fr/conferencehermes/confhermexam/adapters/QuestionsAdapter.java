package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.Question;
import fr.conferencehermes.confhermexam.util.DataHolder;

public class QuestionsAdapter extends BaseAdapter {
	private ArrayList<Question> mListItems;
	private LayoutInflater mLayoutInflater;
	private Context mContext;

	public QuestionsAdapter(Context context, ArrayList<Question> arrayList) {
		this.mContext = context;
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
			holder.name.setText("QUESTION " + (String.valueOf(position + 1)));
		}

		if (DataHolder.getInstance().getSelectedQuestions().get(position)
				|| position == 0) {
			view.setBackgroundColor(mContext.getResources().getColor(
					R.color.app_main_color_dark));
		} else {
			view.setBackgroundColor(mContext.getResources().getColor(
					R.color.app_main_color));
		}

		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected LinearLayout layout;

	}
}