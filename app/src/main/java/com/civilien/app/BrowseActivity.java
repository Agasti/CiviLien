package com.civilien.app;

import android.app.ProgressDialog;
import android.os.Bundle;

public class BrowseActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        if (IncidentData.isEmpty()) {

            getIncidentData.TaskListener listener = new getIncidentData.TaskListener() {

                private ProgressDialog pDialog = new ProgressDialog(BrowseActivity.this);

                @Override
                public void onStarted() {
                    pDialog.setMessage("Getting Incidents..");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }

                @Override
                public void onFinished() {
                    pDialog.dismiss();

                    createAdapterList();
                }
            };
            new getIncidentData(listener).execute();
        } else {
            createAdapterList();
        }
    }


}

