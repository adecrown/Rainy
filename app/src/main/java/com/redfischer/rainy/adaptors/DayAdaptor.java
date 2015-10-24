package com.redfischer.rainy.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.redfischer.rainy.R;
import com.redfischer.rainy.weather.Current;
import com.redfischer.rainy.weather.Day;
import com.redfischer.rainy.weather.Forecast;

/**
 * Created by Adecrown on 23/06/15.
 */
public class DayAdaptor extends BaseAdapter {

    private Context mContext;
    private Day[] mDays;

    public DayAdaptor(Context mContext,Day[] mDays)
    {
        this.mContext = mContext;
        this.mDays = mDays;
    }


    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);




            convertView.setTag(holder);
        }
        else {
            holder =(ViewHolder) convertView.getTag();
        }
        Day day = mDays[position];
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(day.getTemperatureMax() + "");
        if(position==0)
        {
            holder.dayLabel.setText("Today");
        }
        else if(position == 1){
            holder.dayLabel.setText("Tomorrow");
        }
        else {

            holder.dayLabel.setText(day.getDayOfTheWeek());
        }

        return convertView;
    }

    private static class ViewHolder{
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }

}
