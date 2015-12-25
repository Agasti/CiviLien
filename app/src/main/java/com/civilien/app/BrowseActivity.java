package com.civilien.app;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

public class BrowseActivity extends BaseActivity {

    Boolean SHOW_DIALOG = true;

    SwipeRefreshLayout mSRLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mSRLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSRLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                IncidentArray.clear();
                createAdapterList(R.id.browseListView, R.layout.browse_row);
                getIncidentsData(!SHOW_DIALOG);
            }
        });

        if (IncidentArray.isEmpty()) {
            getIncidentsData(SHOW_DIALOG);
        } else createAdapterList(R.id.browseListView, R.layout.browse_row);
    }

    // necessary to perform custom async tasks on items within scope
    @Override
    public void useIncidentData() {

        createAdapterList(R.id.browseListView, R.layout.browse_row);
        if (mSRLayout != null) {
            mSRLayout.setRefreshing(false);
        }
    }
}

