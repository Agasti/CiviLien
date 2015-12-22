package com.civilien.app;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    static LatLng myLatLng;
    static ArrayList<Incident> IncidentData = new ArrayList<>();
}
