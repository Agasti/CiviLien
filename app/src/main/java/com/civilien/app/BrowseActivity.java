package com.civilien.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {

    public ArrayList<Incident> IncidentData;

    private static String AllIncidentsUrl = "http://"+TAGS.IP_ADDRESS +"/get_all_incidents.php";
    JSONArray IncidentList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        IncidentData = new ArrayList<>();

        new getIncidentData().execute();

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

            JSONObject json = new JSONObject();
            HttpURLConnection conn = null;
            try {
                URL url = new URL(AllIncidentsUrl);
                conn = (HttpURLConnection) url.openConnection();
                InputStream InpS = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        InpS, "iso-8859-1"), 8);


                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line + "\n";
                    sb.append(line);
                }
                InpS.close();
                json = new JSONObject(sb.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            // Check log cat for JSON response
            Log.d("JSON DATA", json.toString());

            try {
                // Checking for SUCCESS TAGS
                 success = json.getInt(TAGS.SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    IncidentList = json.getJSONArray(TAGS.INCIDENTS);

                    // looping through All Products
                    for (int i = 0; i < IncidentList.length(); i++) {
                        JSONObject element = IncidentList.getJSONObject(i);
//                        check log cat for individual elements
//                        Log.d("INCIDENT", element.toString());

                        // Storing each json item in variable
                        String IncID = element.getString(TAGS.INC_ID);
                        String PostDate = element.getString(TAGS.POSTDATE);
                        String Category = element.getString(TAGS.CATEGORY);
                        String Type = element.getString(TAGS.TYPE);
                        String User = element.getString(TAGS.USER);
                        String Title = element.getString(TAGS.TITLE);
                        String GpsLat = element.getString(TAGS.GPS_LAT);
                        String GpsLon = element.getString(TAGS.GPS_LON);
                        String Relevance = element.getString(TAGS.RELEVANCE);

                        Incident incident = new Incident(IncID, PostDate,Category, Type, User, Title, GpsLat,GpsLon, Relevance);

                        // adding incident element into array
                        IncidentData.add(incident);
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
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
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

