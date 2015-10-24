package com.redfischer.rainy.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.redfischer.rainy.R;
import com.redfischer.rainy.adaptors.DayAdaptor;
import com.redfischer.rainy.adaptors.HourAdaptor;
import com.redfischer.rainy.weather.Day;
import com.redfischer.rainy.weather.Hour;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HourlyForcastActivity extends ActionBarActivity {


    private Hour[] mHours;
    @InjectView(R.id.reyclerView) RecyclerView mRecyclerView;

    private ColorPicker mColorPicker = new ColorPicker();
    private int colors = mColorPicker.getColor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_forcast);
        ButterKnife.inject(this);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(colors);


        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelables, parcelables.length, Hour[].class);

       HourAdaptor adaptor = new HourAdaptor(this,mHours);
        mRecyclerView.setAdapter(adaptor);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }


}
