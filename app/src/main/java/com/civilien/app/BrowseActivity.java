package com.civilien.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BrowseActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

//        IncidentData = new ArrayList<>();

        if (IncidentData.isEmpty()) {
            new getIncidentData().execute();
        }

    }
    class getIncidentData extends AsyncTask<Void, Void, Void>{

        public ProgressDialog pDialog= new ProgressDialog(BrowseActivity.this);
        public int success;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Getting Incidents..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {

            HttpJSONRequest request = new HttpJSONRequest();
            JSONObject json = request.makeJSONRequest(CONSTANTS.URL_GET_INCIDENTS);

            JSONArray IncidentList = null;
            try {
                // Checking for SUCCESS TAGS
                 success = json.getInt(TAGS.SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    IncidentList = json.getJSONArray(TAGS.INCIDENTS);

                    // looping through All Products
                    for (int i = 0; i < IncidentList.length(); i++) {
//                        check log cat for individual elements
//                        Log.d("INCIDENT "+Integer.toString(i), IncidentList.getJSONObject(i).toString());

                        // adding incident element into array
                        IncidentData.add(new Incident(IncidentList.getJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    pDialog.dismiss();
                    if (success == 0) {
                        Toast.makeText(BrowseActivity.this, "No Incidents!", Toast.LENGTH_LONG).show();
                    }

                    ArrayAdapter Adapter = new browseAdapter(BrowseActivity.this, IncidentData);
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
            });
        }
    }

}

