package com.civilien.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

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
            View rootView = inflater.inflate(R.layout.fragment_view_incidents, container, false);

            TextView PostDate_label, Category_label, Type_label, Username_label, Title_label, GPSLat_label, GPSLon_label, Votes_label;
            ImageView Category_icon, Type_icon;

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

            try {
                Incident element = new Incident(IncidentData.getJSONObject(getArguments().getInt(ARG_SECTION_NUMBER)));

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

            return rootView;
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
