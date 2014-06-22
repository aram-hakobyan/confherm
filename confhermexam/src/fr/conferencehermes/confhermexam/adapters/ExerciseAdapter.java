package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.NotesActivity;
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
			holder.name.setText(mListItems.get(position).getExamName());
		}

		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((NotesActivity) context).setParamExersiceId(mListItems.get(
						position).getExersiceId());
				((NotesActivity) context).setParamGlobalTest(0);

				((NotesActivity) context).exerciseResult(context);

				Log.i("Exersice ", mListItems.get(position).getExersiceId()
						+ "");
			}
		});

		return view;

	}

	private static class ViewHolder {
		protected TextView name;

	}
}