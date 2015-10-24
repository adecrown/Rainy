package com.redfischer.rainy.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redfischer.rainy.R;
import com.redfischer.rainy.adaptors.DayAdaptor;
import com.redfischer.rainy.weather.Current;
import com.redfischer.rainy.weather.Day;
import com.redfischer.rainy.weather.Forecast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DailyForcastActivity extends Activity {

    private Day[] mDays;
    @InjectView(android.R.id.list) ListView mListView;
    @InjectView(android.R.id.empty) TextView mEmptyTextView;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    private ColorPicker mColorPicker = new ColorPicker();
    private int colors = mColorPicker.getColor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forcast);
        ButterKnife.inject(this);




        Intent intent = getIntent();

        mLocationLabel.setText(intent.getStringExtra(MainActivity.NEW_FORECAST));

        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables,parcelables.length,Day[].class);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(colors);

            //Day gh =  new Day();
            //TextView viewer = (TextView) findViewById(R.id.locationLabel);
            //viewer.setText(gh.getTimezone());



        DayAdaptor adaptor = new DayAdaptor(this,mDays);
        mListView.setAdapter(adaptor);
        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String highTemp = mDays[position].getTemperatureMax() + "";
                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek, highTemp, conditions);

                Toast.makeText(DailyForcastActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });



    }


}
