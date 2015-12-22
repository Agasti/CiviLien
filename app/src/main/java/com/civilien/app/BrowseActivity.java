package com.civilien.app;

import android.os.Bundle;

public class BrowseActivity extends BaseActivity {

    Boolean SHOW_DIALOG = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        downloadIncidentData(SHOW_DIALOG);
    }


    @Override
    public void useIncidentData() {

        createAdapterList(R.id.browseListView,R.layout.browse_row);

    }
}

