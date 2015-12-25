package com.civilien.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private static final Boolean SHOW_DIALOG = true;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // fab create Incident
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, CreateIncident.class);
                startActivity(intent);
            }
        });

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
    @TargetApi(Build.VERSION_CODES.M)
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CallWithPermission(MapsActivity.this, SET_MY_LOCATION);
        } else mMap.setMyLocationEnabled(true);

        downloadIncidentData(SHOW_DIALOG);
    }

    @Override
    public void useIncidentData() {
        // Add markers for each incident

        JSONArray array = null;
        try {
            array = new JSONArray(IncidentArray.toString());
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
            Log.d("_DATA ERROR_", "JSONArray array(IncidentArray) is empty!");
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
//            Log.d("NEXT LAT_LON", Double.toString(lat) + " - " + Double.toString(lon));
            mMap.addMarker(new MarkerOptions().position(IncidentPosition[i]).title(MarkerTitle));
            Log.d("___MARKER " + Integer.toString(i) + " ADDED___", IncidentPosition[i].toString());

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Log.d("___MARKER CLICK____", marker.getId() + "  " + marker.getTitle());
                    Intent checkIncident = new Intent(MapsActivity.this, viewIncidentsActivity.class);
                    checkIncident.putExtra("position", marker.getId().substring(1));
                    startActivity(checkIncident);
                    return true;
                }
            });

        }


    }
}
