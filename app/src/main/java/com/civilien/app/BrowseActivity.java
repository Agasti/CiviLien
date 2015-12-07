package com.civilien.app;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BrowseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        IncidentExamples exmp = new IncidentExamples();
        ListAdapter Adapter = new browseAdapter(this, exmp.list);

        ListView browseListView = (ListView) findViewById(R.id.browseListView);
        browseListView.setAdapter(Adapter);
        browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selection = "You've selected " + String.valueOf(parent.getItemAtPosition(position));
                Snackbar.make(view, selection, Snackbar.LENGTH_LONG).show();
            }
        });



    }
}
