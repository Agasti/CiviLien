package com.civilien.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ViewInterest extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static TextView PostDate_label, Category_label, Type_label, Username_label, Title_label, GPSLat_label, GPSLon_label, Votes_label;
    static Incident element;
    static Context context;
    static View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_interest);

        Intent callerIntent = getIntent();
        int position = Integer.parseInt(callerIntent.getStringExtra("position"));
//        Log.d("ViewIncident_position", Integer.toString(position));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(position);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_incidents, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_view_incidents, container, false);

            ImageView Category_icon, Type_icon;

            context = getContext();

            Votes_label = (TextView) rootView.findViewById(R.id.label_Votes);
            PostDate_label = (TextView) rootView.findViewById(R.id.label_PostDate);
            Username_label = (TextView) rootView.findViewById(R.id.label_Username);
            Title_label = (TextView) rootView.findViewById(R.id.label_Title);
            GPSLat_label = (TextView) rootView.findViewById(R.id.label_GPSLat);
            GPSLon_label = (TextView) rootView.findViewById(R.id.label_GPSLon);
            Category_label = (TextView) rootView.findViewById(R.id.label_Category);
            Type_label = (TextView) rootView.findViewById(R.id.label_Type);
            Category_icon= (ImageView) rootView.findViewById(R.id.imageView1);
            Type_icon= (ImageView) rootView.findViewById(R.id.imageView2);

            String ID = null;
            try {
                element = new Incident(IncidentData.getJSONObject(getArguments().getInt(ARG_SECTION_NUMBER)));

                ID = element.getString(TAGS.INC_ID);
                Votes_label.setText(element.getVotes().toString());
                PostDate_label.setText(element.getPostDate().toString());
                Username_label.setText(element.getUsername().toString());
                Title_label.setText(element.getTitle().toString());
                GPSLat_label.setText(element.getGPSLat().toString());
                GPSLon_label.setText(element.getGPSLon().toString());
                String Category = element.getCategory().toString();
                String Type = element.getType().toString();
                Category_label.setText(Category);
                Type_label.setText(Type);
                GPSLon_label.setText(element.getGPSLon().toString());
                int resource = getContext().getResources().getIdentifier(Category.toLowerCase().replace(" ", ""), "drawable", getContext().getPackageName());
                if (resource != 0) {
                    Category_icon.setImageResource(resource);
                } else {Category_icon.setImageResource(R.drawable.ic_warning_category);}
                resource = getContext().getResources().getIdentifier(Type.toLowerCase().replace(" ", ""),"drawable", getContext().getPackageName());
                if (resource != 0) {
                    Category_icon.setImageResource(resource);
                }else Type_icon.setImageResource(R.drawable.ic_warning_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ImageButton Upvote, Downvote;
            Upvote = (ImageButton) rootView.findViewById(R.id.Upvote_imageButton);
            final String finalID = ID;
            Upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalID != null) {
                        JSONObject vote = new JSONObject();
                        try {
                            vote.put(TAGS.INC_ID, finalID);
                            vote.put(TAGS.USERNAME, User_Data.getString("Username"));
                            vote.put("Value","1");
                            vote.put("Vote", finalID +":"+"1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new sendIncidentData().execute(vote);
                    } else Log.e("ID", finalID);
                }
            });

            Downvote = (ImageButton) rootView.findViewById(R.id.DownVote_imageButton);
            Downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalID != null) {
                        JSONObject vote = new JSONObject();
                        try {
                            vote.put(TAGS.INC_ID, finalID);
                            vote.put(TAGS.USERNAME, User_Data.getString("Username"));
                            vote.put("Value", "-1");
                            vote.put("Vote", TAGS.INC_ID+"_"+finalID +":"+"-1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new sendIncidentData().execute(vote);
                    } else Log.e("ID", finalID);
                }
            });


            return rootView;
        }

        class sendIncidentData extends AsyncTask<JSONObject, Void, JSONObject> {


            public ProgressDialog pDialog= new ProgressDialog(getContext());
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pDialog.setMessage("Voting...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected JSONObject doInBackground(JSONObject...  args) {
                HTTPHelper request = new HTTPHelper();
                return request.JSON_POST_Request(CONSTANTS.URL_VOTE, args);
            }

            @Override
            protected void onPostExecute(final JSONObject response) {
                super.onPostExecute(response);
                try {
                    int success = Integer.parseInt(response.getString(TAGS.SUCCESS));;
                    String Message = response.get("Message").toString();
                    int newVotes = Integer.parseInt(response.get("Result").toString());
                    pDialog.dismiss();

                    if (success == 1) {
                        try {
                            newVotes = Integer.parseInt(element.getString(TAGS.VOTES)) + newVotes;
                            element.setVotes(Integer.toString(newVotes));
                            TextView votes = (TextView) rootView.findViewById(R.id.label_Votes);
                            votes.setText(Integer.toString(newVotes));
                            ViewGroup vg = (ViewGroup) rootView.findViewById(R.id.Fragment_myView);
                            vg.invalidate();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(context,Message,Toast.LENGTH_LONG).show();
                    Log.d("Vote", Message);
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                        }
//                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return IncidentData.length();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                return IncidentData.getJSONObject(position).getString(TAGS.INC_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
