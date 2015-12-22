package com.civilien.app;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
    static ArrayList<Incident> IncidentData = new ArrayList<>();



    public Void createAdapterList (){

        ArrayAdapter Adapter = new browseAdapter(BaseActivity.this, IncidentData);
        ListView browseListView = (ListView) findViewById(R.id.browseListView);
        browseListView.setAdapter(Adapter);
        browseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selection = "You've selected " + String.valueOf(parent.getItemAtPosition(position));
                Snackbar.make(view, selection, Snackbar.LENGTH_LONG).show();
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
                        IncidentData.add(new Incident(IncidentList.getJSONObject(i)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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

                    pDialog.dismiss();
                    if (success == 0) {
                        Toast.makeText(BaseActivity.this, "No Incidents!", Toast.LENGTH_LONG).show();
                    }

                    ArrayAdapter Adapter = new browseAdapter(BaseActivity.this, IncidentData);
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
            });*/
        }
    }
}
