package com.civilien.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CatActivity extends AppCompatActivity {

    public static String ExtraName;

    TextView CatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);

        CatText = (TextView) findViewById(R.id.catText);

        Intent intent = getIntent();
        String name = intent.getStringExtra(ExtraName);
        CatText.setText(name);
    }

}
