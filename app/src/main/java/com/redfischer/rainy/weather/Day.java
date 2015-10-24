package com.redfischer.rainy.weather;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.redfischer.rainy.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Adecrown on 23/06/15.
 */
public class Day implements Parcelable{
    private long mTime;
    private String mSummary;
    private double mTemperature;
    private String mIcon;
    private  String mTimezone;



    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMax() {
        int mCelsius = (int)(mTemperature - 32) * 5 / 9;
        return (int)Math.round(mCelsius);
    }

    public void setTemperatureMax(double temperature) {
        mTemperature = temperature;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getIconId(){
        return Forecast.getIconId(mIcon);
    }



    public String getDayOfTheWeek()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));


        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeDouble(mTemperature);
        dest.writeString(mIcon);
        dest.writeString(mTimezone);

    }
    private Day(Parcel in)
    {
        mTime = in.readLong();
        mSummary = in.readString();
        mTemperature = in.readDouble();
        mIcon = in.readString();
        mTimezone = in.readString();

    }
    public  Day()
    {

    }

    public  static final Creator <Day> CREATOR = new Creator<Day>(){

        public  Day createFromParcel(Parcel source)
        {
            return new Day(source);
        }

        public Day[] newArray(int size)
        {
            return new  Day[size];
        }
    };
}