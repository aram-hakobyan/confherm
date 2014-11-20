package fr.conferencehermes.confhermexam.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import fr.conferencehermes.confhermexam.R;

public class ExerciseLayout extends LinearLayout {

	public ExerciseLayout(Context context) {
		super(context);
		initLayout();
	}

	public ExerciseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout();
	}

	public ExerciseLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout();
	}

	public void initLayout() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.exersice_rowview, this);
	}

}
