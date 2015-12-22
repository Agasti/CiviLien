package com.civilien.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    static LatLng myLatLng;
    static ArrayList IncidentArray = new ArrayList();
    static IncidentList IncidentData;

    public void downloadIncidentData(Boolean showDialog) {

        if (IncidentArray.isEmpty()) {

            getIncidentData.TaskListener listener = null;
            if(showDialog) {
                listener = new getIncidentData.TaskListener() {

                    private ProgressDialog pDialog = new ProgressDialog(BaseActivity.this);

                    @Override
                    public void onStarted() {
                        pDialog.setMessage("Getting Incidents..");
                        pDialog.setIndeterminate(false);
                        pDialog.show();
                    }

                    @Override
                    public void onFinished() {
                        pDialog.dismiss();

                        useIncidentData();
                    }
                };
            }
            new getIncidentData(listener).execute();
        } else {
            useIncidentData();
        }
    }

    public void useIncidentData() {}


    public Void createAdapterList (int listView,int Layout){

        ArrayAdapter Adapter = new browseAdapter(BaseActivity.this, Layout, IncidentArray);
        ListView browseListView = (ListView) findViewById(listView);
        browseListView.setAdapter(Adapter);
        browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent checkIncident = new Intent(BaseActivity.this, viewIncidentsActivity.class);
                checkIncident.putExtra("position", Integer.toString(position));

                startActivity(checkIncident);

//                String selection = "You've selected " + String.valueOf(parent.getItemAtPosition(position));
//                Snackbar.make(view, selection, Snackbar.LENGTH_LONG).show();
            }
        });
        return null;
    }

    public static class getIncidentData extends AsyncTask<Void, Void, Void> {

        public int success;
        public interface TaskListener {
            void onStarted();
            void onFinished();
        }

        private final TaskListener taskListener;

        public getIncidentData(TaskListener Listener){
            this.taskListener = Listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (this.taskListener != null){
                this.taskListener.onStarted();
            }
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
                        IncidentArray.add(new Incident(IncidentList.getJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            IncidentData = new IncidentList(IncidentArray);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (this.taskListener != null){
                this.taskListener.onFinished();
            }

/*            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });*/
        }
    }
}
