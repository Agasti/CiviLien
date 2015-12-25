package com.civilien.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class BaseActivity extends AppCompatActivity implements LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    final static int LOCATION_permissionCode = 1;

    private static final Boolean SHOW_DIALOG = true;
    static GoogleApiClient mGoogleApiClient;
    static LocationRequest mLocationRequest;

    static JSONObject User_Data;
    static LatLng myLatLng;
    static ArrayList IncidentArray = new ArrayList();
    static JSONArray IncidentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the location client to start receiving updates
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        // start the location client
        mGoogleApiClient.connect();
    }


    //Handles permission and messages
    @TargetApi(Build.VERSION_CODES.M)
    public void handlePermissions(Context context, final int requestCode) {
        if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(BaseActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(new View(getApplicationContext()), "Please grant the app access to location services in \"" +
                                "Settings > APPS > Configure Apps > App Permissions > Location\"",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                requestCode);
                    }}).show();
                return;
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
        }
    }

    // Permission callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_permissionCode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(BaseActivity.this, "Location access granted!", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(BaseActivity.this, "Location access denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.

        handlePermissions(getApplicationContext(), LOCATION_permissionCode);
        Location mCurrentLocation = LocationServices.FusedLocationApi.
                getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("current location", mCurrentLocation.toString());
            myLatLng= new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }


    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(CONSTANTS.UPDATE_INTERVAL)
                .setFastestInterval(CONSTANTS.FASTEST_INTERVAL);
        // Request location updates
        if (mGoogleApiClient.isConnected()) {
            handlePermissions(getApplicationContext(), LOCATION_permissionCode);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this,
                        CONSTANTS.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Location services", " connection failed with code: " +
                    String.valueOf(connectionResult.getErrorCode()));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handlePermissions(getApplicationContext(), LOCATION_permissionCode);
        Location myCurrentLocation = LocationServices.FusedLocationApi.
                getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (myCurrentLocation != null) {
            // Print current location if not null
            Log.d("current location", myCurrentLocation.toString());
            myLatLng = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
        }
    }
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

    public void createAdapterList (int listView,int Layout){

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
            JSONObject json = request.JSON_Request(CONSTANTS.URL_GET_INCIDENTS);

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
