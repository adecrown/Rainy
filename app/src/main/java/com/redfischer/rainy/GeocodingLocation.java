package com.redfischer.rainy;

/**
 * Created by Adecrown on 24/06/15.
 */
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.redfischer.rainy.weather.Other;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {


    private static final String TAG = "GeocodingLocation";

    private Address address;
    private Address maddress;
    private double Lang;
    private double lat;
    private String fAdd;
    private String fCountry;

    public void getAddressFromLocation(final String locationAddress, final Context context, final Handler handler)
    {
       // getApplicationContext();


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        address = (Address) addressList.get(0);
                        setLongtitude(address.getLongitude());
                        setLatitude(address.getLatitude());

                    }
                } catch (IOException e)
                {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                }
               /*finally
                {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        String resulter = "Address: " + locationAddress +
                                "\n\nLatitude and Longitude :\n" + result;
                        bundle.putString("address", resulter);
                        message.setData(bundle);



                        //mOther.setLatitude();
                        *//* System.out.println(mLatitude); *//*
                      //  mOther.setLongitude(address.getLongitude());

                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }*/


    }


    public void getLocationFromAddress(final double lat,final double longi, final Context context,final Handler handler)
    {
        // getApplicationContext();


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List
                    addressList = geocoder.getFromLocation(lat, longi,1);
                    Log.i(TAG, "From geo: " + address);
            if (addressList != null && addressList.size() > 0) {
                maddress = (Address) addressList.get(0);

                setfCountry(String.valueOf(maddress.getCountryCode()));

                    setfAdd(maddress.getLocality());


               // System.out.println("nnou: " + maddress);
            }
        } catch (IOException e)
        {
            Log.e(TAG, "Unable to connect to Geocoder", e);
        }
               /*finally
                {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        String resulter = "Address: " + locationAddress +
                                "\n\nLatitude and Longitude :\n" + result;
                        bundle.putString("address", resulter);
                        message.setData(bundle);



                        //mOther.setLatitude();
                        *//* System.out.println(mLatitude); *//*
                      //  mOther.setLongitude(address.getLongitude());

                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }*/


    }

    public double getLongtitude() {
        return Lang;
    }

    public void setLongtitude(double lang) {
        Lang = lang;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double lat) {
        this.lat = lat;
    }

    public String getfAdd() {
        return fAdd;
    }

    public void setfAdd(String fAdd) {
        this.fAdd = fAdd;
    }

    public String getfCountry() {
        return fCountry;
    }

    public void setfCountry(String fCountry) {
        this.fCountry = fCountry;
    }
}


