package com.civilien.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class CreateIncident extends BaseActivity {

    Incident incident = null;
    int Success = 0;
    String Category, Type, Username = "", Title, GPSLat, GPSLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incident);


        Button setMyLocation = (Button) findViewById(R.id.btn_myPosition);
        setMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GPSLat = Double.toString(myLatLng.latitude);
                GPSLon = Double.toString(myLatLng.longitude);

            }
        });
        Button create = (Button) findViewById(R.id.btn_addIncident);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Category = ((EditText) findViewById(R.id.textBox_Category)).getText().toString();
                Type = ((EditText) findViewById(R.id.textBox_Type)).getText().toString();
                Title = ((EditText) findViewById(R.id.textBox_Title)).getText().toString();
                GPSLat = ((EditText) findViewById(R.id.textBox_GPSLat)).getText().toString();
                GPSLat = ((EditText) findViewById(R.id.textBox_GPSLon)).getText().toString();
                try {
                    Username = User_Data.getString(TAGS.USERNAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    incident = new Incident(Category, Type, Username, Title, GPSLat, GPSLon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new sendIncidentData().execute(incident.ConvertToJSON());

            }
        });
    }

    class sendIncidentData extends AsyncTask<JSONObject, Void, JSONObject> {

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
        protected JSONObject doInBackground(JSONObject...  args) {
            HTTPHelper request = new HTTPHelper();
            return request.JSON_POST_Request(CONSTANTS.URL_CREATE_INCIDENT, args);
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


