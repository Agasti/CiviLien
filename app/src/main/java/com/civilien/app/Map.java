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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Random;

public class Map extends BaseActivity implements OnMapReadyCallback {

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
                Intent intent = new Intent(Map.this, CreateInterest.class);
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

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        mMap.setPadding(0,0,40,200);
        if (!CANNOT_ACCESS_LOCATION) {
            //noinspection ResourceType
            mMap.setMyLocationEnabled(true);
        }

        if (IncidentArray.isEmpty()) {
            getIncidentsData(SHOW_DIALOG);
        } else useIncidentData();
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

            String Category = "";
            String Type = "";
            try {
                lat = Double.parseDouble(array.getJSONObject(i).getString(TAGS.GPS_LAT));
                lon = Double.parseDouble(array.getJSONObject(i).getString(TAGS.GPS_LON));
                Category = array.getJSONObject(i).getString(TAGS.CATEGORY);
                Type = array.getJSONObject(i).getString(TAGS.TYPE);
                MarkerTitle = array.getJSONObject(i).get(TAGS.INC_ID).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lat = lat + ((double) (r.nextInt(100) - 50) / 10000);
            lon = lon + ((double) (r.nextInt(100) - 50)/ 10000);
            IncidentPosition[i] = new LatLng(lat, lon);
//            Log.d("NEXT LAT_LON", Double.toString(lat) + " - " + Double.toString(lon));
            int resource = Map.this.getResources().getIdentifier("marker_"+Type.toLowerCase().replace(" ", ""), "drawable", getPackageName());
            if (resource == 0) {
                resource = Map.this.getResources().getIdentifier("marker_"+Category.toLowerCase().replace(" ", ""), "drawable", getPackageName());
            }
            if (resource != 0) {
                mMap.addMarker(new MarkerOptions().position(IncidentPosition[i]).title(MarkerTitle).icon(BitmapDescriptorFactory.fromResource(resource)));
            }else mMap.addMarker(new MarkerOptions().position(IncidentPosition[i]).title(MarkerTitle));
            Log.d("___MARKER " + Integer.toString(i) + " ADDED___", IncidentPosition[i].toString());

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    Log.d("___MARKER CLICK____", marker.getId() + "  " + marker.getTitle());
                    Intent checkIncident = new Intent(Map.this, ViewInterest.class);
                    checkIncident.putExtra("position", marker.getId().substring(1));
                    startActivity(checkIncident);
                    return true;
                }
            });

        }


    }
}
