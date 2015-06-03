package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.CorrectionActivity;
import fr.conferencehermes.confhermexam.CorrectionExercisesActivity;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.StatsActivity;
import fr.conferencehermes.confhermexam.parser.CorrectionsExercise;

public class GridViewAdapter extends ArrayAdapter<Object> {
	Context context;
	ArrayList<CorrectionsExercise> data;
	LayoutInflater mLayoutInflater;

	public GridViewAdapter(Context context, ArrayList<CorrectionsExercise> data) {
		super(context, 0);
		this.context = context;
		this.data = data;
		this.mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder;

		if (view == null) {
			holder = new ViewHolder();
			view = mLayoutInflater.inflate(R.layout.grid_item, null);
			holder.name = (TextView) view.findViewById(R.id.exName);
			holder.button = (ImageButton) view.findViewById(R.id.openStats);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.name.setText(data.get(position).getName());
		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openExamCorrection(position);
			}
		});

		holder.button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openStats(position);
			}
		});

		return view;

	}

	private static class ViewHolder {
		protected TextView name;
		protected ImageButton button;

	}

	private void openExamCorrection(int position) {

		int exersice_id = data.get(position).getExercise_id();
		int exam_id = data.get(position).getEvent_id();

		Intent intent = new Intent(context, CorrectionActivity.class);
		intent.putExtra("exercise_id", exersice_id);
		intent.putExtra("event_id", exam_id);
		context.startActivity(intent);
	}

	private void openStats(int position) {

		int exersice_id = data.get(position).getExercise_id();
		int event_id = data.get(position).getEvent_id();

		Intent intent = new Intent(context, StatsActivity.class);
		intent.putExtra("exercise_id", exersice_id);
		intent.putExtra("event_id", event_id);
		context.startActivity(intent);
	}
}
