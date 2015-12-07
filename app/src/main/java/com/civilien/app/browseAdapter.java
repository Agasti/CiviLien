package com.civilien.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SehailFillali on 6/12/15.
 */
class browseAdapter extends ArrayAdapter<String> {
    public browseAdapter(Context context, String[] values) {
        super(context, R.layout.browse_row, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View theView = inflater.inflate(R.layout.browse_row, parent, false);

        String selection = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.textview1);
        theTextView.setText(selection);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.imageView1);
        theImageView.setImageResource(R.drawable.icondot);

        return theView;

    }
}
