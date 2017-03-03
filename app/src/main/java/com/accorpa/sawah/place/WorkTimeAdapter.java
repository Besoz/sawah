package com.accorpa.sawah.place;

import android.content.Context;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.WorkTime;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by root on 02/02/17.
 */

public class WorkTimeAdapter extends BaseAdapter{


    private Context mContext;
    private LayoutInflater mInflater;

    private WorkTime[] mDataSource;

    public WorkTimeAdapter(Context mContext, WorkTime[] mDataSource) {
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mDataSource = mDataSource;
    }

    @Override
    public int getCount() {
        return mDataSource.length;
    }

    @Override
    public Object getItem(int position) {
        return mDataSource[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        WorkDayView holder;

        WorkTime day = (WorkTime) getItem(position);

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.work_time_item, parent, false);

            holder = new WorkTimeAdapter.WorkDayView();
            holder.day = (TextView) convertView.findViewById(R.id.day);
            holder.time = (TextView) convertView.findViewById(R.id.time);


            convertView.setTag(holder);
        }
        else{
            holder = (WorkTimeAdapter.WorkDayView) convertView.getTag();
        }

        holder.day.setText(DataHandler.getInstance(mContext).getDayMapping(day.getDay()));
        holder.time.setText(day.getTime());


        int dayNo = Utils.getInstance().getTodayDayNumber();


        if (dayNo == day.getDay()){
            holder.day.setTypeface(null, Typeface.BOLD);
            holder.time.setTypeface(null, Typeface.BOLD);
        }


        return convertView;
    }

    public static class WorkDayView{

        public TextView day;
        public TextView time;
    }

}
