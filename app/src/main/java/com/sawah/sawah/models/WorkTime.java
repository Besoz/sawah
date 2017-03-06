package com.sawah.sawah.models;

import android.icu.text.DateFormatSymbols;

import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.models.Place;
import com.orm.SugarRecord;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by root on 25/02/17.
 */
public class WorkTime extends SugarRecord implements Comparable<WorkTime>{

    private int day;
    private String time;
    private boolean isNow;
    private Place place;


    private static HashMap<String, Integer> hm=new HashMap<String, Integer>();


    public void setTime(String time) {
        this.time = time;
    }

    public void setDay(String day) {

        this.day = Utils.getInstance().mapDay(day);
    }

    public void setNow(boolean now) {
        isNow = now;
    }

    public Place getPlace() {
        return place;
    }

    public WorkTime(Place place, String day, String time, boolean isNow) {
        this.day = Utils.getInstance().mapDay(day);
        this.time = time;
        this.isNow = isNow;
        this.place = place;
    }

    public WorkTime() {
    }

    public int getDay() {
        return day;
    }

    public boolean isNow() {
        return isNow;
    }

    public String getTime() {
        return time;
    }
    public void setPlace(Place place) {
        this.place = place;
    }


    @Override
    public int compareTo(WorkTime o) {

        return this.day - o.getDay();
    }
}
