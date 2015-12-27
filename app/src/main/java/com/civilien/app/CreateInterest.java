package com.civilien.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateInterest extends BaseActivity {

    Incident incident = null;
    int Success = 0;
    String Category, Type, Username = "", Title, GPSLat, GPSLon;
    EditText Title_box, Category_box, Type_box, Lat_box, Lon_box;
    Spinner Categories, Types;
    static ArrayList<Spinner> spinners = new ArrayList<>();
    HashMap<String, String> values = new HashMap();
    String[] valuesList;
    ArrayList<ArrayAdapter> AdaptersArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_interest);

        Title_box = (EditText) findViewById(R.id.textBox_Title);
//        Category_box = (EditText) findViewById(R.id.textBox_Category);
//        Type_box = (EditText) findViewById(R.id.textBox_Type);
        Lat_box = (EditText) findViewById(R.id.textBox_GPSLat);
        Lon_box = (EditText) findViewById(R.id.textBox_GPSLon);



        valuesList = new String[]{"Category", "Type"};

        Categories = (Spinner) findViewById(R.id.spinner_Category);
        Types = (Spinner) findViewById(R.id.spinner_Type);
        spinners.add(Categories);
        spinners.add(Types);
        Log.d("spinners", spinners.toString());

        setSpinnerlistener(Categories, "Categories", 0);
//        ArrayAdapter<CharSequence> CategorySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.Categories, android.R.layout.simple_spinner_item);
//        CategorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Categories.setAdapter(CategorySpinnerAdapter);
//
//
//        ArrayAdapter<CharSequence> TypeSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.Road, android.R.layout.simple_spinner_item);
//        TypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Types.setAdapter(TypeSpinnerAdapter);



        Button setMyLocation = (Button) findViewById(R.id.btn_myPosition);
        setMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lat_box.setText(Double.toString(myLatLng.latitude));
                Lon_box.setText(Double.toString(myLatLng.longitude));

            }
        });
    }


    public void setSpinnerlistener(final Spinner spinner,String StringResource, final int i) {

        final int index = i;
        Log.d("passed spinner",  spinner.toString());
        int resource = getResources().getIdentifier(StringResource, "array", getPackageName());
        Log.d("resource",Integer.toString(resource));
        ArrayAdapter<CharSequence> SpinnerAdapter = ArrayAdapter.createFromResource(CreateInterest.this, resource, android.R.layout.simple_spinner_item);
        Log.d("Adapter", SpinnerAdapter.toString());
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String string = parent.getItemAtPosition(position).toString();
                Log.w("Item selected!", "");
                Log.d("string", string);
                Log.d("next index", Integer.toString(index + 1));
                values.put(valuesList[index], string);
                Log.w("values", values.toString());
                if(index + 1 < spinners.size()){
                    Spinner next = spinners.get(index + 1);
                    Log.d("next spinner", next.toString());
                    setSpinnerlistener(next, string.replace(" ",""), i + 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_incident, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            Category = values.get(TAGS.CATEGORY);
            Type = values.get(TAGS.TYPE);
            Title = Title_box.getText().toString();
            GPSLat = Lat_box.getText().toString();
            GPSLon = Lon_box.getText().toString();
            try {
                Username = User_Data.getString(TAGS.USERNAME);
                Log.d("Username", Username);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                incident = new Incident(Category, Type, Username, Title, GPSLat, GPSLon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("incident", incident.toString());

            new sendIncidentData().execute(incident.ConvertToJSON());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class sendIncidentData extends AsyncTask<JSONObject, Void, JSONObject> {

        public ProgressDialog pDialog= new ProgressDialog(CreateInterest.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Sending Incident...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject...  args) {
            HTTPHelper request = new HTTPHelper();
            return request.JSON_POST_Request(CONSTANTS.URL_CREATE_INCIDENT, args);
        }

        @Override
        protected void onPostExecute(final JSONObject response) {
            super.onPostExecute(response);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();

                    try {
                        Success = Integer.parseInt(response.get(TAGS.SUCCESS).toString());
                        Toast.makeText(CreateInterest.this, response.get("Message").toString(), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (Success == 1) {
                        finish();
                    }
                }
            });
        }


    }

}


