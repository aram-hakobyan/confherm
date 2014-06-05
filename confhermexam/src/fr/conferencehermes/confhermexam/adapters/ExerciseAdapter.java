package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;

public class ExerciseAdapter extends BaseAdapter {
	private ArrayList<String> mListItems;
	private LayoutInflater mLayoutInflater;

	public ExerciseAdapter(Context context, ArrayList<String> arrayList) {
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
			view = mLayoutInflater.inflate(R.layout.exersice_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.listExerciseTitle);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (holder.name != null) {
			holder.name.setText(mListItems.get(position));
		}

		return view;

	}

	private static class ViewHolder {
		protected TextView name;

	}
}