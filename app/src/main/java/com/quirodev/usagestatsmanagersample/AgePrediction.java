package com.quirodev.usagestatsmanagersample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class AgePrediction extends AppCompatActivity {
    private TextView mWeatherTextView;
    private TextView titleView;
    String[] mobileArray;
    private ImageView image1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ageprediction);
        int count=0;
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        for (String name : MainActivity.map.keySet())
            count++;
        mobileArray = new String[count];
        int i=0;
        for (String name : MainActivity.map.keySet())
            mobileArray[i++]=name;
        //  titleView = (TextView) findViewById(R.id.title1);

        // COMPLETED (3) Create an array of Strings that contain fake weather data
        /*
         * This String array contains dummy weather data. Later in the course, we're going to get
         * real weather data. For now, we want to get something on the screen as quickly as
         * possible, so we'll display this dummy data.
         */
        //titleView.setText("App That have exceeded the time limit");

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.row_layout, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                Log.d(">>>>====----> Detail", "onOptionsItemSelected.android.R.id.home");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(">>>>====----> Detail", "onBackPressed");
        supportFinishAfterTransition();
    }
}