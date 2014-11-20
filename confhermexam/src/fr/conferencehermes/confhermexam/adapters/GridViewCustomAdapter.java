package fr.conferencehermes.confhermexam.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.conferencehermes.confhermexam.R;
import fr.conferencehermes.confhermexam.parser.Exercise;

public class GridViewCustomAdapter extends ArrayAdapter<Object> {
  Context context;
  ArrayList<Exercise> data;


  public GridViewCustomAdapter(Context context, ArrayList<Exercise> data) {
    super(context, 0);
    this.context = context;
    this.data = data;
  }

  public int getCount() {
    return data.size();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = convertView;

    if (row == null) {
      LayoutInflater inflater = ((Activity) context).getLayoutInflater();
      row = inflater.inflate(R.layout.item, parent, false);

      TextView textViewTitle = (TextView) row.findViewById(R.id.tvText);
      textViewTitle.setText(data.get(position).getName());

    }

    if (row != null && data.get(position).isClicked())
      row.setBackgroundColor(Color.parseColor("#0d5c7c"));
    else
      row.setBackgroundColor(Color.parseColor("#86adbd"));

    return row;

  }
}
