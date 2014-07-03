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
import fr.conferencehermes.confhermexam.parser.ExamExercise;

public class ExerciseAdapter extends BaseAdapter {
	private ArrayList<ExamExercise> mListItems;
	private LayoutInflater mLayoutInflater;
	private Context context;

	public ExerciseAdapter(Context context, ArrayList<ExamExercise> listEx) {
		mListItems = listEx;
		this.context = context;
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
	public View getView(final int position, View view, ViewGroup viewGroup) {
		final ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.exersice_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.listExerciseTitle);
			holder.layuot = (LinearLayout) view
					.findViewById(R.id.listExerciseLayout);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (holder.name != null) {
			holder.name.setText(mListItems.get(position).getExersiceName());
		}

		view.setBackgroundColor(Color.parseColor("#eeeeee"));

		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected LinearLayout layuot;
	}
}