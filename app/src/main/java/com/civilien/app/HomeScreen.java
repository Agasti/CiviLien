package com.civilien.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeScreen extends BaseActivity {

    ImageButton map, browse;
    Button civimates, messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null){
            Log.d("SAVEDSTATE___", savedInstanceState.toString());
            try {
                User_Data = new JSONObject(savedInstanceState.getString("User_Data"));
                Log.w("Loaded User_Data", User_Data.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Loading SharedPref", getPreferences(MODE_PRIVATE).toString());
            // restoring preferences
            String AppData_user = getPreferences(Context.MODE_PRIVATE).getString("User_Data","EMPTY");
//            Log.v("AppData_user", AppData_user);

            if (!AppData_user.equals("EMPTY")) {
                try {
                    User_Data = new JSONObject(AppData_user);
                    Log.w("Loaded User_Data", User_Data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String AppData_incidents = getPreferences(Context.MODE_PRIVATE).getString("IncidentData","EMPTY");
//            Log.w("AppData_incidents", AppData_incidents);

                if (!AppData_incidents.equals("EMPTY")) {
                try {
                    IncidentData = new JSONArray(AppData_incidents);
                    Log.w("Loaded IncidentData", IncidentData.toString());
                    for (int i = 0; i < IncidentData.length(); i++) {
                        IncidentArray.add(new Incident(IncidentData.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        // Login if not already did

//        Log.w("User_Data", User_Data.());
        if  (User_Data.isNull(TAGS.USERNAME)) {
            startActivity(new Intent(HomeScreen.this,
                    Login.class));
            finish();
        }

        // fab create Incident
        FloatingActionButton CreateIncident_fab = (FloatingActionButton) findViewById(R.id.fab);
        CreateIncident_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, CreateInterest.class));
            }
        });

        map = (ImageButton) findViewById(R.id.btn_Map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Map.class));
            }
        });

        browse = (ImageButton) findViewById(R.id.btn_Browse);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Browse.class));
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
    protected void onStop() {

        //TODO: remove comments after refreshing is handled automatically
        saveAppData();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // Disconnecting the client invalidates it.
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
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
