package com.redfischer.rainy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.redfischer.rainy.GeocodingLocation;
import com.redfischer.rainy.R;
import com.redfischer.rainy.weather.Current;
import com.redfischer.rainy.weather.Day;
import com.redfischer.rainy.weather.Forecast;
import com.redfischer.rainy.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class GeocodingActivity extends Activity {
    Button addressButton;
    TextView addressTV;
    TextView latLongTV;
    TextView mTextView;

    public static final String TAG = GeocodingActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    private Forecast mForecast;
    private ColorPicker mColorPicker = new ColorPicker();
    private int colors = mColorPicker.getColor();

    private double latitude  = 8.537981;
    private double longtitude = -80.782127;
    //LocationActivity appLocationService;
    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView)
    ImageView mIconImageView;
    //@InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    EditText editText;
    @InjectView(R.id.sunSetValue) TextView mSunSetValue;
    @InjectView(R.id.sunRiseValue) TextView mSunRiseValue;
    @InjectView(R.id.windValue) TextView mWindValue;

    GeocodingLocation locationAddress = new GeocodingLocation();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding);

        ButterKnife.inject(this);
        mProgressBar.setVisibility(View.INVISIBLE);

       // addressTV = (TextView) findViewById(R.id.addressTV);
        latLongTV = (TextView) findViewById(R.id.latLongTV);
        //mTextView = (TextView) findViewById(R.id.textViewL);


        addressButton = (Button) findViewById(R.id.addressButton);
        editText = (EditText) findViewById(R.id.addressET);


        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(colors);

        getForecast(latitude, longtitude);

        addressButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {


                String address = editText.getText().toString().toUpperCase();

                if(address.isEmpty()) {

                   latitude  = 8.537981;
                    longtitude = -80.782127;
                    Toast.makeText(GeocodingActivity.this,"PLEASE SPECIFY A LOCATION",Toast.LENGTH_LONG).show();
                }
                else {

                    locationAddress.getAddressFromLocation(address, getApplicationContext(), new GeocoderHandler());


                    // System.out.println("Latitude" + locationAddress.getLatitude(address, getApplicationContext(), new GeocoderHandler()));
                  //  System.out.println("Longituderrg" + locationAddress.getLongtitude());
                   latitude = locationAddress.getLatitude();
                   longtitude = locationAddress.getLongtitude();

                    locationAddress.getLocationFromAddress(latitude, longtitude, getApplicationContext(), new GeocoderHandler());
                   // System.out.println("City: " + locationAddress.getfAdd());

                    getForecast(latitude, longtitude);
                    //mLocationLabel.setText(address);
                }

            }
        });

    }

    private void getForecast(double latitude,double longtitude) {
        // working with weather api
        String apiKey ="23cbb7011b5b4471112040e431fc52c8" ;
        String forcastUrl = "https://api.forecast.io/forecast/" + apiKey +"/" + latitude +","+ longtitude;


        if(isNetworkAvalable()) {
            toggleRefresh();
            //working with ok http
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forcastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                        updateDisplay();

                                }
                            });


                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught", e);
                    }


                }
            });
        }
        else {
            Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();

        }
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {

            mProgressBar.setVisibility(View.VISIBLE);
            addressButton.setVisibility(View.INVISIBLE);

        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            addressButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "%");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        //String gh = locationAddress.getfAdd();

            mLocationLabel.setText(current.getTimeZone());



        if(locationAddress.getfAdd() == null)
        {
            mLocationLabel.setText(current.getTimeZone());
        }
        else {
            mLocationLabel.setText(locationAddress.getfCountry() + "/" + locationAddress.getfAdd());
        }


        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
        mSunRiseValue.setText(current.getFormattedSunRise());
        mSunSetValue.setText(current.getFormattedSunSet());
        mWindValue.setText(current.getWind() + "mph");


    }



    private Forecast parseForecastDetails(String jsonData) throws JSONException
    {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetials(jsonData));

        return forecast;

    }


    private Current getCurrentDetials(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        Log.i(TAG,"From JSON: " + timeZone);

        JSONObject currently = forecast.getJSONObject("currently");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Current current = new Current();

        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timeZone);

        current.setWind(currently.getDouble("windSpeed"));



        JSONObject jsonDay = data.getJSONObject(0);
        current.setSunrise(jsonDay.getLong("sunriseTime"));
        current.setSunset(jsonDay.getLong("sunsetTime"));
        Log.d(TAG, current.getFormattedTime());



        return current;
    }

    private boolean isNetworkAvalable() {

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected())
        {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError()
    {

        AlertDialogFragment dialog =  new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            latLongTV.setText(locationAddress);
        }
    }


}