package com.civilien.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class CreateInterest extends BaseActivity {

    Incident incident = null;
    int Success = 0;
    String Category, Type, Username = "", Title, GPSLat, GPSLon;
    EditText Title_box, Category_box, Type_box, Lat_box, Lon_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_interest);

        Title_box = (EditText) findViewById(R.id.textBox_Title);
        Category_box = (EditText) findViewById(R.id.textBox_Category);
        Type_box = (EditText) findViewById(R.id.textBox_Type);
        Lat_box = (EditText) findViewById(R.id.textBox_GPSLat);
        Lon_box = (EditText) findViewById(R.id.textBox_GPSLon);

        Button setMyLocation = (Button) findViewById(R.id.btn_myPosition);
        setMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lat_box.setText(Double.toString(myLatLng.latitude));
                Lon_box.setText(Double.toString(myLatLng.longitude));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_incident, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            Category = Category_box.getText().toString();
            Type = Type_box.getText().toString();
            Title = Title_box.getText().toString();
            GPSLat = Lat_box.getText().toString();
            GPSLat = Lon_box.getText().toString();
            try {
                Username = User_Data.getString(TAGS.USERNAME);
                Log.d("Username", Username);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                incident = new Incident(Category, Type, Username, Title, GPSLat, GPSLon);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new sendIncidentData().execute(incident.ConvertToJSON());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class sendIncidentData extends AsyncTask<JSONObject, Void, JSONObject> {

        public ProgressDialog pDialog= new ProgressDialog(CreateInterest.this);
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
                        Toast.makeText(CreateInterest.this, response.get("Message").toString(), Toast.LENGTH_LONG).show();
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


