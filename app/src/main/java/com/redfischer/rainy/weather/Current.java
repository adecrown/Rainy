package com.redfischer.rainy.weather;

import com.redfischer.rainy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Adecrown on 22/06/15.
 */
public class Current {
    private  String mIcon;
    private  long mTime;
    private  double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private  String mSummary;
    private String mTimeZone;
    private Long mSunset;
    private Long mSunrise;
    private double mWind;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public  int getIconId()
    {
       return Forecast.getIconId(mIcon);

    }

    public long getTime() {
        return mTime;
    }

    public String getFormattedTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public void setTime(long time) {
        mTime = time;
    }


    public int getTemperature() {

        // it converts  Fahrenheit to  Celsius

        int mCelsius = (int)(mTemperature - 32) * 5 / 9;

        return Math.round(mCelsius);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public int getHumidity() {
        double humidity = mHumidity * 100;
        return (int)Math.round(humidity);
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance * 100;

        return (int)Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public Long getSunset() {
        return mSunset;
    }

    public void setSunset(Long sunset) {
        mSunset = sunset;
    }

    public Long getSunrise() {
        return mSunrise;
    }

    public void setSunrise(Long sunrise) {
        mSunrise = sunrise;
    }

    public String getFormattedSunRise()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mma");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getSunrise() * 1000);
        String timeString = formatter.format(dateTime).toLowerCase();
        return timeString;
    }

    public String getFormattedSunSet()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mma");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getSunset() * 1000);
        String timeString = formatter.format(dateTime).toLowerCase();
        return timeString;
    }


    public double getWind() {
        return (int)Math.round(mWind);
    }

    public void setWind(double wind) {
        mWind = wind;
    }
}
