package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.NotesActivity;
import fr.conferencehermes.confhermexam.R;

public class ResultsAdapter extends BaseAdapter {
	private ArrayList<String> mListItems;
	private LayoutInflater mLayoutInflater;
	private Context c;

	public ResultsAdapter(Context context, ArrayList<String> arrayList) {
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
			view = mLayoutInflater.inflate(R.layout.resultat_rowview, null);
			holder.name = (TextView) view.findViewById(R.id.examNameResultat);
			holder.desc = (TextView) view.findViewById(R.id.examCorrection);
			holder.status = (TextView) view.findViewById(R.id.examStats);
			holder.status.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(c, NotesActivity.class);
					c.startActivity(intent);
				}
			});
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String stringItem = (String) mListItems.get(position);
		if (stringItem != null) {
			if (holder.name != null) {
				holder.name.setText(stringItem);
				holder.desc.setText("Voir la correction" + position);
				holder.status.setText("Note & Stats");
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