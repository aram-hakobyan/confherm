package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.Training;

public class TrainingsAdapter extends BaseAdapter {
	private ArrayList<Training> mListItems;
	private LayoutInflater mLayoutInflater;

	public TrainingsAdapter(Context context, ArrayList<Training> arrayList) {
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
			view = mLayoutInflater.inflate(R.layout.training_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.examName);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Training training = (Training) mListItems.get(position);
		if (training != null) {
			if (holder.name != null) {
				holder.name.setText(training.getTitle());
			}
		}

		return view;

	}

	private static class ViewHolder {
		protected TextView name;

	}
}