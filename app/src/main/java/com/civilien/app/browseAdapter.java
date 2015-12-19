package com.civilien.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


/**
 * Created by SehailFillali on 6/12/15.
 */
class browseAdapter extends ArrayAdapter<Incident> {
    public browseAdapter(Context context, JSONArray values) {
        super(context, 0, (List<Incident>) values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        class ViewHolder {
            TextView Title_view;
            TextView InId_view;
            ImageView image_view;
        }
        Incident element = getItem(position);
        ViewHolder Holder;
        if (convertView == null) {

            Holder = new ViewHolder();
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.browse_row, parent, false);
            convertView.setTag(Holder);
        }
        else {
            Holder = (ViewHolder) convertView.getTag();
        }


        Holder.InId_view = (TextView) convertView.findViewById(R. id.text_ID);
        Holder.Title_view = (TextView) convertView.findViewById(R. id.text_Title);
        Holder.image_view= (ImageView) convertView.findViewById(R.id.imageView1);
        //TextView InID_view = (TextView) convertView.findViewById(R.id.text_ID);
        try {
            Holder.InId_view.setText(element.getIncID().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TextView Title_view = (TextView) convertView.findViewById(R.id.text_ID);
        try {
            Holder.Title_view.setText(element.getTitle().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //ImageView theImageView = (ImageView) convertView.findViewById(R.id.imageView1);
        Holder.image_view.setImageResource(R.drawable.icondot);

        return convertView;

    }
}
