package com.civilien.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;


public class CreateIncident extends BaseActivity {

    Incident incident = null;
    int Success = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incident);


        Button create = (Button) findViewById(R.id.btn_addIncident);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Category, Type, User, Title, GPSLat, GPSLon, Relevance;

                Category = ((EditText) findViewById(R.id.textBox_Category)).getText().toString();
                Type = ((EditText) findViewById(R.id.textBox_Type)).getText().toString();
                Title = ((EditText) findViewById(R.id.textBox_Title)).getText().toString();
                User = CONSTANTS.USERNAME;
                GPSLat = Double.toString(myLatLng.latitude);
                GPSLon = Double.toString(myLatLng.longitude);

                try {
                    incident = new Incident(Category, Type, User, Title, GPSLat, GPSLon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new sendIncidentData().execute();

            }
        });
    }

    class sendIncidentData extends AsyncTask<Void, Void, JSONObject> {

        public ProgressDialog pDialog= new ProgressDialog(CreateIncident.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Sending Incident...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(Void... args) {
            JSONObject response = new JSONObject();
            HttpURLConnection conn = null;
            try {

                // format incident data into request parameters
                StringBuilder request_params = new StringBuilder();
                int i = 0;
                Iterator<String> keys = incident.keys();
                while (keys.hasNext()){
                    String key = keys.next();
                    if(i != 0){
                        request_params.append("&");
                    }
                    request_params.append(key).append("=").append(URLEncoder.encode((String) incident.get(key), "UTF-8"));
                    i++;
                }
                // Check log cat for request parameters
                Log.d("*", getString(R.string.LogCatBreak));
                Log.d("___REQUEST_PARAMS___", request_params.toString());


                // create connection and send request
                URL url = new URL(CONSTANTS.URL_CREATE_INCIDENT);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestProperty("Content-Length","" + Integer.toString(request_params.toString().getBytes().length));
                conn.setRequestProperty("Content-Language", "en-US");
                conn.setUseCaches(false);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                DataOutputStream writer = new DataOutputStream(conn.getOutputStream());
                writer.writeBytes(request_params.toString());
                writer.flush();
                writer.close();

                conn.connect();

                InputStream InpS = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        InpS, "iso-8859-1"), 8);


                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    line = line + "\n";
                    sb.append(line);
                }
                InpS.close();
                // Check log cat for built string
                Log.d("___RESPONSE STRING___", sb.toString());
                if (sb.length() > 0) {
                    response = new JSONObject(sb.toString());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            // Check log cat for JSON response
//            Log.d("___RESPONSE___", response.toString());
            return response;
        }

        @Override
        protected void onPostExecute(final JSONObject response) {
            super.onPostExecute(response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();

                    try {
                        Success = Integer.parseInt(response.get(TAGS.SUCCESS).toString());
                        Toast.makeText(CreateIncident.this, response.get("Message").toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (Success == 1) {
                        finish();
                    }
                }
            });
        }


    }

}


