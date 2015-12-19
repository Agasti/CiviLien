package com.civilien.app;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;


public class CreateIncident extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incident);


        Button create = (Button) findViewById(R.id.btn_addIncident);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String IncID, PostDate,Category, Type, User, PostTitle;
                double GpsLat, GpsLon, Relevance;

                Category = ((EditText) findViewById(R.id.textBox_Category)).getText().toString();
                Type = ((EditText) findViewById(R.id.textBox_Type)).getText().toString();
                PostTitle = ((EditText) findViewById(R.id.textBox_Title)).getText().toString();
                User = "Ok";
                GpsLat = 34;
                GpsLon = -6;
                IncID = "1";
                PostDate = "testdate";
                Relevance = 1;

                Incident incident = null;
                try {
                    incident = new Incident(IncID, PostDate, Category, Type, User, PostTitle, GpsLat, GpsLon, Relevance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Snackbar.make(v, incident.toString(), Snackbar.LENGTH_LONG).show();

            }
        });
    }



}
