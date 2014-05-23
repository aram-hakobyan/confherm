package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;

public class NotesAdapter extends BaseAdapter {
	private ArrayList<String> mListItems;
	private LayoutInflater mLayoutInflater;
	private Context c;

	public NotesAdapter(Context context, ArrayList<String> arrayList) {
		mListItems = arrayList;
		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.c = context;
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
			view = mLayoutInflater.inflate(R.layout.notes_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.noteRang);
			holder.rang = (TextView) view.findViewById(R.id.noteRang);
			holder.score = (TextView) view.findViewById(R.id.noteScore);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String stringItem = (String) mListItems.get(position);
		if (stringItem != null) {
			if (holder.name != null) {
				holder.name.setText(stringItem);
				holder.rang.setText("0" + position);
				holder.score.setText(String.valueOf(position * 100));

			}
		}

		if (position % 2 == 0) {
			view.setBackgroundColor(Color.parseColor("#f5f5f5"));
		} else {
			view.setBackgroundColor(Color.parseColor("#e7e7e7"));
		}
		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected TextView rang;
		protected TextView score;

	}
}