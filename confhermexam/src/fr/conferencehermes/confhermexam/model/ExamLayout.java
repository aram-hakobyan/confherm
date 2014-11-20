package fr.conferencehermes.confhermexam.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import fr.conferencehermes.confhermexam.R;

public class ExamLayout extends LinearLayout {

	public ExamLayout(Context context) {
		super(context);
		initLayout();
	}

	public ExamLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout();
	}

	public ExamLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initLayout();
	}

	public void initLayout() {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.exam_rowview, this);
	}

}
