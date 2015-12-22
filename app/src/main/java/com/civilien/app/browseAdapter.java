package com.civilien.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by SehailFillali on 6/12/15.
 */
class browseAdapter extends ArrayAdapter<Incident> {
    int customLayout;
    public browseAdapter(Context context,int myLayout, ArrayList<Incident> values) {
        super(context, 0, values);
        this.customLayout = myLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        class ViewHolder {
            TextView PostDate_View, User_View, Title_View, GPSLat_View, GPSLon_View, IncId_view;
            ImageView image_view;
        }
        Incident element = getItem(position);
        ViewHolder Holder;
        if (convertView == null) {

            Holder = new ViewHolder();
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(customLayout, parent, false);
            convertView.setTag(Holder);
        }
        else {
            Holder = (ViewHolder) convertView.getTag();
        }


        Holder.IncId_view = (TextView) convertView.findViewById(R.id.text_ID);
        Holder.PostDate_View = (TextView) convertView.findViewById(R.id.text_PostDate);
        Holder.User_View = (TextView) convertView.findViewById(R.id.text_User);
        Holder.Title_View = (TextView) convertView.findViewById(R.id.text_Title);
        Holder.GPSLat_View = (TextView) convertView.findViewById(R.id.text_GPSLat);
        Holder.GPSLon_View = (TextView) convertView.findViewById(R.id.text_GPSLon);
        Holder.image_view= (ImageView) convertView.findViewById(R.id.imageView1);

        try {
            Holder.IncId_view.setText(element.getIncID().toString());
            Holder.PostDate_View.setText(element.getPostDate().toString());
            Holder.User_View.setText(element.getUser().toString());
            Holder.Title_View.setText(element.getTitle().toString());
            Holder.GPSLat_View.setText(element.getGPSLat().toString());
            Holder.GPSLon_View.setText(element.getGPSLon().toString());
            Holder.image_view.setImageResource(R.drawable.icondot);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }
}
