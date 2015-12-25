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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class BaseActivity extends AppCompatActivity implements LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static boolean CAN_ACCESS_LOCATION = false;

    final static int GET_LAST_LOCATION = 1;
    final static int REQUEST_LOCATION_UPDATE = 2;
    final static int SET_MY_LOCATION = 3;

    static GoogleMap mMap;
    static GoogleApiClient mGoogleApiClient;
    static LocationRequest mLocationRequest;
    Location mCurrentLocation = null;
    static LatLng myLatLng;

    static JSONObject User_Data = new JSONObject();
    static ArrayList IncidentArray = new ArrayList();
    static JSONArray IncidentData = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the location client to start receiving updates
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        // start the location client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (User_Data.length() != 0) {
            outState.putString("User_Data", User_Data.toString());
        }
        if (IncidentData.length() != 0) {
            outState.putString("IncidentData", IncidentData.toString());
        }
        super.onSaveInstanceState(outState);
    }

    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.

        if (mGoogleApiClient.isConnected()) {
            CallWithPermission(BaseActivity.this, GET_LAST_LOCATION);
        }
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("current location", mCurrentLocation.toString());
            myLatLng= new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(CONSTANTS.UPDATE_INTERVAL)
                .setFastestInterval(CONSTANTS.FASTEST_INTERVAL);
        // Request location updates
        if (CAN_ACCESS_LOCATION && mGoogleApiClient.isConnected()) {
            CallWithPermission(getApplicationContext(), REQUEST_LOCATION_UPDATE);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
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
        Location myCurrentLocation = null;
        if (CAN_ACCESS_LOCATION && mGoogleApiClient.isConnected()) {
            CallWithPermission(getApplicationContext(), GET_LAST_LOCATION);
            myCurrentLocation = LocationServices.FusedLocationApi.
                    getLastLocation(mGoogleApiClient);
        }
        // Note that this can be NULL if last location isn't already known.
        if (myCurrentLocation != null) {
            // Print current location if not null
            Log.d("current location", myCurrentLocation.toString());
            myLatLng = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
        }
    }

    public void getIncidentsData(Boolean showDialog) {

        if (IncidentArray.isEmpty()) {

            DownloadIncidentsData.TaskListener listener = null;
            if(showDialog) {
                listener = new DownloadIncidentsData.TaskListener() {

                    private ProgressDialog pDialog = new ProgressDialog(BaseActivity.this);

                    @Override
                    public void onStarted() {
                        pDialog.setMessage("Getting Incidents data..");
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
            new DownloadIncidentsData(listener).execute();
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

    public static class DownloadIncidentsData extends AsyncTask<Void, Void, Void> {

        public int success;
        public interface TaskListener {
            void onStarted();
            void onFinished();
        }

        private final TaskListener taskListener;

        public DownloadIncidentsData(TaskListener Listener){
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
                    Log.w("IncidentArray", IncidentArray.toString());
                    Log.e("IncidentData_", IncidentData.toString());
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

    //Handles permission and messages
    @TargetApi(Build.VERSION_CODES.M)
    void CallWithPermission(Context context, final int requestCode) {
        if (ActivityCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(BaseActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(findViewById(android.R.id.content), R.string.PERMISSION_RATIONALE_location,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.ACCEPT, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
                        Snackbar.make(findViewById(android.R.id.content), R.string.PATH_permission_location, Snackbar.LENGTH_INDEFINITE).show();
                    }}).show();
                return;
            }
            ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
        }
    }

    // Permission callback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d("REQUEST CODE", Integer.toString(requestCode));
        Log.d("GRANT RESULTS", Arrays.toString(grantResults));
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            Toast.makeText(BaseActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();

            switch (requestCode) {
                case GET_LAST_LOCATION:
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    break;
                case REQUEST_LOCATION_UPDATE:
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    break;
                case SET_MY_LOCATION:
                    mMap.setMyLocationEnabled(true);
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            // Permission Denied
            Toast.makeText(BaseActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }
}
