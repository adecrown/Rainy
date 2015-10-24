package com.redfischer.rainy.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static final String NEW_FORECAST = "NEW_FORECAST";
    private Forecast mForecast;

    private ColorPicker mColorPicker = new ColorPicker();
    private int colors = mColorPicker.getColor();

    private double latitude ;
    private double longtitude ;
    LocationActivity appLocationService;
    @InjectView(R.id.timeLabel) TextView mTimeLabel;
    @InjectView(R.id.locationLabel) TextView mLocationLabel;
    @InjectView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue) TextView mHumidityValue;
    @InjectView(R.id.precipValue) TextView mPrecipValue;
    @InjectView(R.id.summaryLabel) TextView mSummaryLabel;
    @InjectView(R.id.iconImageView) ImageView mIconImageView;
    @InjectView(R.id.refreshImageView) ImageView mRefreshImageView;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.sunSetValue) TextView mSunSetValue;
    @InjectView(R.id.sunRiseValue) TextView mSunRiseValue;
    @InjectView(R.id.windValue) TextView mWindValue;
    GeocodingLocation locationAddress = new GeocodingLocation();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appLocationService = new LocationActivity(MainActivity.this);



        ButterKnife.inject(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);

        Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);


        if (nwLocation != null) {
            latitude = nwLocation.getLatitude();

            longtitude = nwLocation.getLongitude();
        }
         else if (gpsLocation != null) {
            latitude = gpsLocation.getLatitude();
            longtitude= gpsLocation.getLongitude();

        }
        else
        {
            showSettingsAlert("GPS AND NETWORK");


        }
        //final double latitude = 53.7997;
        //final double longtitude =1.5492;
       // mGetReverseGeoCoding.getAddress(latitude,longtitude);
      //System.out.println(mGetReverseGeoCoding.getCity());


       final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        //relativeLayout.setBackgroundColor(R.drawable.bg_gradient);
        relativeLayout.setBackgroundColor(colors);


        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longtitude);
                relativeLayout.setBackgroundColor(colors);



            }
        });

        locationAddress.getLocationFromAddress(latitude, longtitude, getApplicationContext(), new GeocoderHandler());
        //System.out.println("City: " + locationAddress.getfAdd());

        getForecast(latitude, longtitude);
        relativeLayout.setBackgroundColor(colors);


        Log.d(TAG, "Main UI code is running");

    }





    private void getForecast(double latitude,double longtitude) {
        // working with weather api
        String apiKey ="23cbb7011b5b4471112040e431fc52c8" ;
        String forcastUrl = "https://api.forecast.io/forecast/" + apiKey +"/" + latitude +","+ longtitude;

       // String apiKey ="3308de785dd34e025f4ba19be4287e05" ;
       // String appId = "4ab2dc13";
        //String forcastUrl = "http://api.weatherunlocked.com/api/current/"+ latitude +","+ longtitude + "?app_id={"+appId+"}&app_key={" +apiKey+"}";




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
            mRefreshImageView.setVisibility(View.INVISIBLE);

        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {

        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "%");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
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


        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));

        return forecast;

    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for(int i=0; i< data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();


            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
                day.setTimezone(timeZone);


            days[i] = day;
        }
        return days;

    }

    private Hour[] getHourlyForecast(String jsonData)throws JSONException {

        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for(int i=0; i< data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();


            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timeZone);


            hours[i] = hour;


        }
        return hours;
    }

    private Current getCurrentDetials(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timeZone = forecast.getString("timezone");
        Log.i(TAG,"From JSON: " + timeZone);

        JSONObject currently = forecast.getJSONObject("currently");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        //Day[] days = new Day[0];




        Current current = new Current();

        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timeZone);
       // System.out.println(test.getfAdd());

        current.setWind(currently.getDouble("windSpeed"));


        JSONObject jsonDay = data.getJSONObject(0);
        current.setSunrise(jsonDay.getLong("sunriseTime"));
        current.setSunset(jsonDay.getLong("sunsetTime"));

        Log.d(TAG, "how are you "+current.getFormattedTime());




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

    @OnClick(R.id.dailyButton)
    public  void  startDailyActivity(View view)
    {
        Current current = mForecast.getCurrent();
        String loc;
        if(locationAddress.getfAdd() == null)
        {
            loc = current.getTimeZone();
        }
        else
        {
            loc = locationAddress.getfAdd().toUpperCase();
        }
        Intent intent = new Intent(this, DailyForcastActivity.class);
        intent.putExtra(DAILY_FORECAST,mForecast.getDailyForecast());
        intent.putExtra(NEW_FORECAST,loc);

        startActivity(intent);
    }

@OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view)
{
    Intent intent = new Intent(this, HourlyForcastActivity.class);
    intent.putExtra(HOURLY_FORECAST,mForecast.getHourlyForecast());
    startActivity(intent);
}
    @OnClick(R.id.newLocationButton)
    public  void  startNewLocation(View view)
    {
        Intent intent = new Intent(this,GeocodingActivity.class);
        //intent.putExtra(DAILY_FORECAST,mForecast.getDailyForecast());
        startActivity(intent);
    }


    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
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

            //latLongTV.setText(locationAddress);
        }
    }


}