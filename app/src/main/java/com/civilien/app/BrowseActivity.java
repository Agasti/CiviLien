package com.civilien.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    public JSONArray IncidentData;

    private static final String TAG_IP = "160.177.100.100";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_INCIDENTS = "incidents";
    private static final String TAG_INCID = "IncID";
    private static final String TAG_POSTDATE = "PostDate";
    private static final String TAG_CATEGORY = "Category";
    private static final String TAG_TYPE = "Type";
    private static final String TAG_USER = "User";
    private static final String TAG_TITLE = "Title";
    private static final String TAG_GPSLAT = "GpsLat";
    private static final String TAG_GPSLON = "GpsLon";
    private static final String TAG_RELEVANCE = "Relevance";

    JSONParser jsonParser = new JSONParser();
    private static String AllIncidentsUrl = "http://160.177.100.100/get_all_incidents.php";
    //private static String AllIncidentsUrl = "http://"+TAG_IP+"/get_all_incidents.php";
    JSONArray IncidentList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        IncidentData = new JSONArray();

        new getIncidentData().execute();

    }
    class getIncidentData extends AsyncTask<Void, Void, Void>{

        public ProgressDialog pDialog= new ProgressDialog(BrowseActivity.this);
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

            List<NameValuePair> parameters = new ArrayList<>();

            JSONObject json = jsonParser.makeHttpRequest(AllIncidentsUrl, "GET", parameters);

            Toast.makeText(BrowseActivity.this, json.toString(), Toast.LENGTH_LONG).show();
            // Check log cat for JSON response
            Log.d("All incidents: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    IncidentList = json.getJSONArray(TAG_INCIDENTS);

                    // looping through All Products
                    for (int i = 0; i < IncidentList.length(); i++) {
                        JSONObject element = IncidentList.getJSONObject(i);

                        // Storing each json item in variable
                        String InID = element.getString(TAG_INCID);
                        String PostDate = element.getString(TAG_POSTDATE);
                        String Category = element.getString(TAG_CATEGORY);
                        String Type = element.getString(TAG_TYPE);
                        String User = element.getString(TAG_USER);
                        String Title = element.getString(TAG_TITLE);
                        double GpsLat = element.getDouble(TAG_GPSLAT);
                        double GpsLon = element.getDouble(TAG_GPSLON);
                        double Relevance = element.getDouble(TAG_RELEVANCE);

                        Incident incident = new Incident(InID, PostDate,Category, Type, User, Title, GpsLat,GpsLon, Relevance);

                        // adding HashList to ArrayList
                        IncidentData.put(incident);
                    }
                } else {
                    // no incidents found
                    Toast.makeText(BrowseActivity.this, "No Incidents!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

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

