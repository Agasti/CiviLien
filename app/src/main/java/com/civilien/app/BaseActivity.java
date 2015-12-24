package com.civilien.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    static JSONObject User_data;
    static LatLng myLatLng;
    static ArrayList IncidentArray = new ArrayList();
    static JSONArray IncidentData;

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

            HTTPHelper request = new HTTPHelper();
            JSONObject json = request.JSON_GET_Request(CONSTANTS.URL_GET_INCIDENTS);

            JSONArray Incident_List = null;
            try {
                // Checking for SUCCESS TAGS
                success = json.getInt(TAGS.SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    Incident_List = json.getJSONArray(TAGS.INCIDENTS);

                    // looping through All Products
                    for (int i = 0; i < Incident_List.length(); i++) {
//                        check log cat for individual elements
//                        Log.d("INCIDENT "+Integer.toString(i), Incident_List.getJSONObject(i).toString());

                        // adding incident element into array
                        IncidentArray.add(new Incident(Incident_List.getJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            IncidentData = new JSONArray(IncidentArray);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            List<JSONObject> jsonList = new ArrayList<JSONObject>();

            int length = IncidentData.length();
            for (int i = 0; i < length; i++) {
                try {
                    jsonList.add(IncidentData.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Collections.sort(jsonList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject lhs, JSONObject rhs) {
                    Integer A = 0, B = 0;

                    try {
                        A = lhs.getInt(TAGS.VOTES);
                        B = rhs.getInt(TAGS.VOTES);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return B.compareTo(A);
                }
            });

            IncidentData = new JSONArray();
            IncidentArray = new ArrayList();
            for (int i = 0; i < length; i++) {
                IncidentData.put(jsonList.get(i));
                try {
                    IncidentArray.add(new Incident(new JSONObject(jsonList.get(i).toString())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Sorted Data", IncidentData.toString());

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
