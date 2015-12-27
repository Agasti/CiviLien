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
class BrowseAdapter extends ArrayAdapter<Incident> {
    int customLayout;
    public BrowseAdapter(Context context, int myLayout, ArrayList values) {
        super(context, 0, values);
        this.customLayout = myLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        class ViewHolder {
            TextView PostDate_label, Category_label, Type_label, Username_label, Title_label, GPSLat_label, GPSLon_label, Votes_label;
            ImageView Category_icon, Type_icon;
        }
        Incident element = getItem(position);
        ViewHolder Holder;
        if (convertView == null) {

            Holder = new ViewHolder();
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Custom layout allows using the same adapter with different layouts
            convertView = inflater.inflate(customLayout, parent, false);
            convertView.setTag(Holder);
        }
        else {
            Holder = (ViewHolder) convertView.getTag();
        }


        try
        {
        Holder.Votes_label = (TextView) convertView.findViewById(R.id.label_PostDate);
        Holder.PostDate_label = (TextView) convertView.findViewById(R.id.label_PostDate);
        Holder.Username_label = (TextView) convertView.findViewById(R.id.label_Username);
        Holder.Title_label = (TextView) convertView.findViewById(R.id.label_Title);
        Holder.GPSLat_label = (TextView) convertView.findViewById(R.id.Label_GPSLat);
        Holder.GPSLon_label = (TextView) convertView.findViewById(R.id.label_GPSLon);
        Holder.Category_label = (TextView) convertView.findViewById(R.id.Label_Category);
        Holder.Type_label = (TextView) convertView.findViewById(R.id.label_Type);
        Holder.Category_icon= (ImageView) convertView.findViewById(R.id.imageView1);
//        Holder.Type_icon= (ImageView) convertView.findViewById(R.id.imageView1);

        try {
            Holder.Votes_label.setText(element.getVotes().toString());
            Holder.PostDate_label.setText(element.getPostDate().toString());
            Holder.Username_label.setText(element.getUsername().toString());
            Holder.Title_label.setText(element.getTitle().toString());
            Holder.GPSLat_label.setText(element.getGPSLat().toString());
            Holder.GPSLon_label.setText(element.getGPSLon().toString());
            String Category = element.getCategory().toString();
            String Type = element.getType().toString();
            Holder.Category_label.setText(Category);
            Holder.Type_label.setText(Type);
            Holder.GPSLon_label.setText(element.getGPSLon().toString());
            Holder.Category_icon.setImageResource(R.drawable.icondot);
//            Holder.Category_icon.setImageResource(parent.getResources().
//                    getIdentifier(element.getCategory().toString().replace(" ","_"),
//                            "drawable",getContext().getPackageName()));
//            Holder.Type_icon.setImageResource(parent.getResources().
//                    getIdentifier(element.getType().toString().replace(" ","_"),
//                            "drawable",getContext().getPackageName()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }
}
