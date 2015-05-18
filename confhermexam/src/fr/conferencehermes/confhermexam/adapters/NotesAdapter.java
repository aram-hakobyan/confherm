package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.NotesResult;

public class NotesAdapter extends BaseAdapter {
	private ArrayList<NotesResult> mListItems;
	private LayoutInflater mLayoutInflater;
	private Context c;

	public NotesAdapter(Context context, ArrayList<NotesResult> list) {
		mListItems = list;
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
		return mListItems.get(i) ;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.notes_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.noteName);
			holder.rang = (TextView) view.findViewById(R.id.noteRang);
			holder.score = (TextView) view.findViewById(R.id.noteScore);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String studentName = (String) mListItems.get(position).getStudentName();
		String studentRank = mListItems.get(position).getRank();
		int studentScore = (int) mListItems.get(position).getScore();

		if (studentName != null) {
			if (holder.name != null) {
				holder.name.setText(studentName);
				holder.rang.setText(studentRank);
				holder.score.setText(String.valueOf(studentScore));
				holder.name.setTextColor(Color.parseColor("#000000"));
				holder.rang.setTextColor(Color.parseColor("#000000"));
				holder.score.setTextColor(Color.parseColor("#000000"));

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