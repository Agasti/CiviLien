package com.civilien.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

public class MapsActivity extends BaseActivity implements LocationListener
        , GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private static final Boolean SHOW_DIALOG = true;
    private GoogleMap mMap;
    static GoogleApiClient mGoogleApiClient;
    static LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create the location client to start receiving updates
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        // fab create Incident
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, CreateIncident.class);
                startActivity(intent);
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionHandler(MapsActivity.this);
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionHandler(MapsActivity.this);
            return;
        }
        mMap.setMyLocationEnabled(true);

        downloadIncidentData(SHOW_DIALOG);

    }

    @Override
    public void useIncidentData() {
        // Add markers for each incident

        JSONArray array = null;
        try {
            array = new JSONArray(IncidentData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Random r = new Random();
        double lat = 0;
        double lon = 0;
        String MarkerTitle = null;
        int length;
        if (array != null) {
            length = array.length();
        } else{
            Log.d("_DATA ERROR_", "JSONArray array(IncidentData) is empty!");
            length = 0;
        }
        LatLng[] IncidentPosition = new LatLng[length];

        for (int i = 0;  i < length; i++) {

            try {
                lat = Double.parseDouble(array.getJSONObject(i).getString(TAGS.GPS_LAT));
                lon = Double.parseDouble(array.getJSONObject(i).getString(TAGS.GPS_LON));
                MarkerTitle = array.getJSONObject(i).get(TAGS.INC_ID).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lat = lat + ((double) r.nextInt(100) / 10000);
            lon = lon + ((double) r.nextInt(100) / 10000);
            IncidentPosition[i] = new LatLng(lat, lon);
            Log.d("NEXT LAT_LON", Double.toString(lat) + " - " + Double.toString(lon));
            mMap.addMarker(new MarkerOptions().position(IncidentPosition[i]).title(MarkerTitle));
            Log.d("___MARKER " + Integer.toString(i) + " ADDED___", IncidentPosition[i].toString());
        }


    }

    //Handles permission and messages
    final static int RequestCodeAskPermission = 123;

    @TargetApi(Build.VERSION_CODES.M)
    public void permissionHandler(Context context) {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, "Please grant the app access to location services in \"Settings > APPS > Configure Apps > App Permissions > Location\"", Toast.LENGTH_LONG).show();
                return;
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCodeAskPermission);
        }
    }

    // Permission callback
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCodeAskPermission:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MapsActivity.this, "Location access granted!", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(MapsActivity.this, "Location access denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestCodeAskPermission);
            return;
        }
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
    @TargetApi(Build.VERSION_CODES.M)
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(CONSTANTS.UPDATE_INTERVAL)
                .setFastestInterval(CONSTANTS.FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestCodeAskPermission);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestCodeAskPermission);
            return;
        }
        Location myCurrentLocation = LocationServices.FusedLocationApi.
                getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (myCurrentLocation != null) {
            // Print current location if not null
            Log.d("current location", myCurrentLocation.toString());
            myLatLng = new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude());
        }

    }
}
