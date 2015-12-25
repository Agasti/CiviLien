package com.civilien.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends BaseActivity {

    Button map, browse, civimates, messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null){
            Log.d("SAVEDSTATE___", savedInstanceState.toString());
            try {
                User_Data = new JSONObject(savedInstanceState.getString("App_state"));
                Log.w("User_Data", User_Data.toString());
                IncidentData = new JSONArray(savedInstanceState.getString("IncidentData"));
                Log.w("IncidentData", IncidentData.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("SharedPref to load", getPreferences(MODE_PRIVATE).toString());
        // restoring preferences
        String AppData_user = getPreferences(Context.MODE_PRIVATE).getString("User_Data","EMPTY");
        Log.w("AppData_user", AppData_user);

        if (!AppData_user.equals("EMPTY")) {
            try {
                User_Data = new JSONObject(AppData_user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String AppData_incidents = getPreferences(Context.MODE_PRIVATE).getString("IncidentData","EMPTY");
        Log.w("AppData_incidents", AppData_incidents);

        if (!AppData_incidents.equals("EMPTY")) {
            try {
                IncidentData = new JSONArray(AppData_incidents);
                Log.w("IncidentData", IncidentData.toString());
                for (int i = 0; i < IncidentData.length(); i++) {
                    IncidentArray.add(new Incident(IncidentData.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Login if not already did
        if  (User_Data == null) {
            startActivity(new Intent(MainActivity.this,
                    LoginActivity.class));
            finish();
        }

        // fab create Incident
        FloatingActionButton CreateIncident_fab = (FloatingActionButton) findViewById(R.id.fab);
        CreateIncident_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateIncident.class));
            }
        });

        map = (Button) findViewById(R.id.mapButton);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        browse = (Button) findViewById(R.id.browseButton);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BrowseActivity.class));
            }
        });

/*        messages = (Button) findViewById(R.id.messagesButton);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {

        //TODO: remove comments after refreshing is handled automatically
//        saveAppData();

        // Disconnecting the client invalidates it.
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void saveAppData(){
        SharedPreferences.Editor ShrPrefEditor = getPreferences(Context.MODE_PRIVATE).edit();
        if (User_Data.length() != 0) {
            ShrPrefEditor.putString("User_Data", User_Data.toString());
        }
        if (IncidentData.length() != 0) {
            ShrPrefEditor.putString("IncidentData", IncidentData.toString());
        }
        Log.d("ShrPrefEditor to save", ShrPrefEditor.toString());
        ShrPrefEditor.apply();
    }

}
